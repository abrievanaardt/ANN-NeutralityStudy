package ac.up.cos700.neutralitystudy.data.util;

/**
 *
 * @author Abrie van Aardt
 */
public class ResultsException extends Exception {

    public ResultsException() {
        super();
    }

    public ResultsException(String msg) {
        super(msg);
    }
    
    @Override
    public String getMessage(){
        return "Something went wrong while attempting to prepare the results.";
    }
}
