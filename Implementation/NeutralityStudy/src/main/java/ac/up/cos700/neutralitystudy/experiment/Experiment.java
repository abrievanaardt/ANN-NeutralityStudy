package ac.up.cos700.neutralitystudy.experiment;

import ac.up.cos700.neutralitystudy.data.Graph;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.sampling.ProgressiveRandomWalkSampler;
import ac.up.cos700.neutralitystudy.study.util.StudyConfig;
import ac.up.malan.phd.sampling.Walk;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing an abstract experiment.
 *
 * @author Abrie van Aardt
 */
public abstract class Experiment{

    //huge todo: inject the sampler to be used, or have a builder create the
    //desired one. Right now, progressiveWalk is hard-coded. Would have to 
    //pass the builder since experiments like Quantiser have to create sampler more 
    //than once.
    public Experiment(StudyConfig _config, NeutralityMeasure _neutralityMeasure, RealProblem _problem) {
        config = _config;        
        problem = _problem;
        name = this.getClass().getSimpleName() + "_" + problem.getName();
        path = config.path + "\\" + name;
        simulations = new double[config.entries.get("simulations").intValue()];
        neutralityMeasure = _neutralityMeasure;
        

        for (int i = 0; i < simulations.length; i++) {
            simulations[i] = i + 1;
        }
    }

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
                        .log(Level.INFO, "Starting simulation {0}", i);

                runSimulation(i);

            }

            finalise();

        }
        catch (Exception e) {
            Logger.getLogger(Experiment.class.getName()).log(Level.SEVERE, "", e);
        }
    }

    protected double calculateNewAverage(double oldAvg, double newValue, int currentTotal) {
        return (oldAvg * (currentTotal - 1) + newValue) / currentTotal;
    }

    protected abstract void runSimulation(int currentSimulation)
            throws Exception;

    protected abstract void finalise() throws Exception;
    
    public String getName(){
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
}
