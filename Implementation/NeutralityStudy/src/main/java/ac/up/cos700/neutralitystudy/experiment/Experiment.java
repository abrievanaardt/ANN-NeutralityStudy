package ac.up.cos700.neutralitystudy.experiment;

import ac.up.cos700.neutralitystudy.data.Graph;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.sampling.ProgressiveRandomWalkSampler;
import ac.up.cos700.neutralitystudy.study.util.StudyConfig;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing an abstract experiment that provides hooks for
 * specific experiments to perform their desired tasks.
 *
 * @author Abrie van Aardt
 */
public abstract class Experiment extends Thread{

    //huge todo: inject the sampler to be used, or have a builder create the
    //desired one. Right now, progressiveWalk is hard-coded. Would have to 
    //pass the builder since experiments like Quantiser have to create sampler more 
    //than once.
    public Experiment(StudyConfig _config, NeutralityMeasure _neutralityMeasure, RealProblem _problem) {
        config = _config;
        problem = _problem;
        neutralityMeasure = _neutralityMeasure;
        name = this.getClass().getSimpleName() + "_" + problem.getName() + "_" + neutralityMeasure.getMeasureName();
        path = config.path + "\\" + name;
        simulations = new double[config.entries.get("simulations").intValue()];

        //by default the second last value in the column (no headers)
        avgNeutralityIndex = config.entries.get("simulations").intValue();

        //by default the last value in the column (no headers)
        stdDevNeutralityIndex = avgNeutralityIndex + 1;

        for (int i = 0; i < simulations.length; i++) {
            simulations[i] = i + 1;
        }
    }

    @Override
    public void run() {

        Logger
                .getLogger(Experiment.class.getName())
                .log(Level.INFO, "...");

        Logger
                .getLogger(Experiment.class.getName())
                .log(Level.INFO, "Running experiment: {0}", name);

        Logger
                .getLogger(Experiment.class.getName())
                .log(Level.INFO, "Doing {0} simulation(s)", config.entries.get("simulations"));

        Logger
                .getLogger(getClass().getName())
                .log(Level.FINER, "Problem: {0}, dim: {1}, xMin: {2}, xMax: {3}, minFitness: {4}", new Object[]{
            problem.getName(),
            problem.getDimensionality(),
            problem.getLowerBound(),
            problem.getUpperBound(),
            problem.getOptimumFitness()
        });

        try {

            for (int i = 1; i <= config.entries.get("simulations"); i++) {

                Logger
                        .getLogger(Experiment.class.getName())
                        .log(Level.INFO, name + " - Simulation {0}", i);

                runSimulation(i);

            }

            finalise();

        }
        catch (Exception e) {
            Logger.getLogger(Experiment.class.getName()).log(Level.SEVERE, "", e);
        }
    }

    /**
     * Calculates the running average assuming indexing starts at
     *
     * @param oldAvg
     * @param newValue
     * @param currentTotal
     * @return new average
     */
    protected double calculateNewAverage(double oldAvg, double newValue, int currentTotal) {
        return (oldAvg * (currentTotal - 1) + newValue) / currentTotal;
    }

    /**
     * Calculates the sample standard deviation of the array of values
     * 
     * @param values
     * @param avg
     * @return sample standard deviation
     */
    protected double calculateSampleStdDev(double[] values, double avg) {
        double stdDev = Arrays.stream(values).map(i -> Math.pow(i - avg, 2)).sum() / (values.length-1);
        return Math.sqrt(stdDev);
    }

    protected abstract void runSimulation(int currentSimulation)
            throws Exception;

    protected abstract void finalise() throws Exception;

    public String getExpName() {
        return name;
    }

    public Graph graph;
    protected StudyConfig config;
    protected String name;
    protected String path;
    protected NeutralityMeasure neutralityMeasure;
    protected RealProblem problem;
    protected ProgressiveRandomWalkSampler sampler;
    protected double[] simulations;
    protected int stdDevNeutralityIndex;
    protected int avgNeutralityIndex;
}
