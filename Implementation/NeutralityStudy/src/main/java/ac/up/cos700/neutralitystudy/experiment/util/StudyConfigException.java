package ac.up.cos700.neutralitystudy.experiment.util;

/**
 *
 * @author Abrie van Aardt
 */
public class StudyConfigException extends Exception {
    public StudyConfigException(){        
        super();
    }
    
    public StudyConfigException(String msg){
        super(msg);        
    }
    
    @Override
    public String getMessage(){
        return "Study.config file is not appropriately configured.";
    }
}
