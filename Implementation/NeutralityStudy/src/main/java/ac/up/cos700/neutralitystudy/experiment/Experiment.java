package ac.up.cos700.neutralitystudy.experiment;

import ac.up.cos700.neutralitystudy.study.util.StudyConfig;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing an abstract experiment. 
 * 
 * @author Abrie van Aardt
 */
public abstract class Experiment {

    public Experiment(StudyConfig _config) {
        config = _config;
        name = getClass().getSimpleName();     
        path = config.path + "\\" + name;
        simulations = new double[config.simulationCount];
        
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
    protected double[] simulations;
    
}
