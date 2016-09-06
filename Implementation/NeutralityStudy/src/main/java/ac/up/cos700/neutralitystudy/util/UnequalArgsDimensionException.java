package ac.up.cos700.neutralitystudy.util;

/**
 *
 * @author Abrie van Aardt
 */
public class UnequalArgsDimensionException extends Exception{
    public UnequalArgsDimensionException(String msg){
        super(msg);
    }
    
    public UnequalArgsDimensionException(){
        super();
    }
    
    @Override
    public String getMessage(){
        return "Number of input arguments must match the dimensionality of the function";
    }
    
    
}
