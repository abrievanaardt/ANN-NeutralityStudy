package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.experiment.Experiment;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.sampling.Sampler;
import ac.up.cos700.neutralitystudy.study.util.StudyConfig;
import ac.up.cos700.neutralitystudy.study.util.StudyConfigException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a study. A study is a group of experiments conducted towards a
 * common goal.
 *
 * @author Abrie van Aardt
 */
public abstract class Study{

    //huge todo: also inject the sampler to be used for each experiment, here
    protected Study() throws StudyConfigException {
        try {
            experiments = new ArrayList<>(10);
            config = StudyConfig.fromFile(getClass().getSimpleName());

            //global configurations
            StudyConfig commonConfig = StudyConfig.fromFile(COMMON_CONFIG_FILE_NAME);

            //specific configuration overrides global configuration
            for (Entry<String, Double> entry : commonConfig.entries.entrySet()) {
                config.entries.putIfAbsent(entry.getKey(), entry.getValue());
            }

        }
        catch (FileNotFoundException e) {
            throw new StudyConfigException(e.getMessage());
        }
    }

    /**
     * This method must be called only once before a study is started.
     *
     * @param nm the neutrality measure to be used during the study
     * @return a handle to the current study for chained calls
     */
    public Study setup(NeutralityMeasure nm, double... otherParamters) {
        neutralityMeasure = nm;
        config.name = this.getClass().getSimpleName() + "_" + neutralityMeasure.getMeasureName();
        config.path = "Studies\\" + getStudyName();

        return this;
    } 
    public void run() {

        Logger
                .getLogger(Study.class.getName())
                .log(Level.INFO, "...");

        Logger
                .getLogger(Study.class.getName())
                .log(Level.INFO, "...");

        Logger
                .getLogger(Study.class.getName())
                .log(Level.INFO, "Doing study: {0}", config.name);

        for (Experiment experiment : experiments) {
            EXPERIMENT_EXECUTOR.submit(experiment);
        }       
        
    }

    public String getStudyName() {
        return config.name;
    }
    
    public static void awaitStudies() throws InterruptedException{
        EXPERIMENT_EXECUTOR.awaitTermination(5, TimeUnit.DAYS);
    }

    public StudyConfig config;
    protected List<Experiment> experiments;

    protected RealProblem[] problems;
    protected NeutralityMeasure neutralityMeasure;
    protected Sampler sampler;
    private static final String COMMON_CONFIG_FILE_NAME = "Study";
    private static final ExecutorService EXPERIMENT_EXECUTOR = 
            Executors.newCachedThreadPool();
}
