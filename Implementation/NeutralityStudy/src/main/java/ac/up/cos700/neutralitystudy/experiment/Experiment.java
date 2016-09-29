package ac.up.cos700.neutralitystudy.experiment;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.study.util.StudyConfig;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing an abstract experiment. 
 * 
 * @author Abrie van Aardt
 */
public abstract class Experiment {
    
    //huge todo: inject the sampler to be used, don't create it here

    public Experiment(StudyConfig _config, NeutralityMeasure _neutralityMeasure, RealProblem _problem) {
        config = _config;
        name = getClass().getSimpleName();     
        path = config.path + "\\" + name;
        simulations = new double[config.simulationCount];
        neutralityMeasure = _neutralityMeasure;
        problem = _problem;
        
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
                .log(Level.INFO, "Doing {0} simulation(s)", config.simulationCount);
        
        Logger
                .getLogger(getClass().getName())
                .log(Level.FINER, "Problem: {0}, dim: {1}, xMin: {2}, xMax: {3}, minFitness: {4}", new Object[]{
            problem.getClass().getSimpleName(),
            problem.getDimensionality(),
            problem.getLowerBound(),
            problem.getUpperBound(),
            problem.getOptimumFitness()
        });

        try {

            for (int i = 1; i <= config.simulationCount; i++) {

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

    protected abstract void runSimulation(int currentSimulation)
            throws Exception;

    protected abstract void finalise() throws Exception;

    protected StudyConfig config;
    protected String name;
    protected String path;
    protected NeutralityMeasure neutralityMeasure;
    protected RealProblem problem;
    //todo: add the sampler here
    protected double[] simulations;
    
}
