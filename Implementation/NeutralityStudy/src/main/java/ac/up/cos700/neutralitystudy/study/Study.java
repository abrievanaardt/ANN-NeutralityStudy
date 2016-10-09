package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.experiment.Exp_1D_Tunable_Q;
import ac.up.cos700.neutralitystudy.experiment.Experiment;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure1;
import ac.up.cos700.neutralitystudy.sampling.ProgressiveRandomWalkSampler;
import ac.up.cos700.neutralitystudy.sampling.Sampler;
import ac.up.cos700.neutralitystudy.study.util.StudyConfig;
import ac.up.cos700.neutralitystudy.study.util.StudyConfigException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a study. A study is a group of experiments conducted towards a
 * common goal.
 *
 * @author Abrie van Aardt
 */
public abstract class Study extends Thread {

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
    public Study setup(NeutralityMeasure nm) {
        neutralityMeasure = nm;
        config.name = this.getClass().getSimpleName() + "_" + neutralityMeasure.getMeasureName();
        config.path = "Studies\\" + getStudyName();

        return this;
    }

    @Override
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

            experiment.run();
        }
    }

    public String getStudyName() {
        return config.name;
    }

    public StudyConfig config;
    protected List<Experiment> experiments;

    protected RealProblem[] problems;
    protected NeutralityMeasure neutralityMeasure;
    protected Sampler sampler;
    private static final String COMMON_CONFIG_FILE_NAME = "Study";
}
