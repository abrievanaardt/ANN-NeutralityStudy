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
    
}
