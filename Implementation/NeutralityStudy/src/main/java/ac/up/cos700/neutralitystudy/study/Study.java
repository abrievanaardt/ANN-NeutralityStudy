package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.experiment.Experiment;
import ac.up.cos700.neutralitystudy.study.util.StudyConfig;
import ac.up.cos700.neutralitystudy.study.util.StudyConfigException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a study. A study is a group of experiments conducted
 * towards a common goal.
 * 
 * @author Abrie van Aardt
 */
public abstract class Study {
    protected Study() throws StudyConfigException{
        try {       
            experiments = new ArrayList<>(10);
            config = StudyConfig.fromFile(getClass().getSimpleName());            
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
}
