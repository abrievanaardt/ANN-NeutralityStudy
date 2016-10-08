package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.experiment.Experiment;
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
 * Represents a study. A study is a group of experiments conducted
 * towards a common goal.
 * 
 * @author Abrie van Aardt
 */
public abstract class Study {
    
    //huge todo: also inject the sampler to be used for each experiment, here
    
    protected Study() throws StudyConfigException{
        try {       
            experiments = new ArrayList<>(10);
            config = StudyConfig.fromFile(getClass().getSimpleName());        
            
            //add global configurations
            StudyConfig commonConfig = StudyConfig.fromFile(COMMON_CONFIG_FILE_NAME);
            
            //specific configuration overrides global configuration
            for (Entry<String,Double> entry : commonConfig.entries.entrySet()) {
                config.entries.putIfAbsent(entry.getKey(), entry.getValue());
            }                                      
            
        } catch (FileNotFoundException e){
            throw new StudyConfigException(e.getMessage());
        }
    }
    
    public void run(){
        
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
    
    protected List<Experiment> experiments;
    protected StudyConfig config; 
    protected Sampler sampler;
    private static final String COMMON_CONFIG_FILE_NAME = "Study";
}
