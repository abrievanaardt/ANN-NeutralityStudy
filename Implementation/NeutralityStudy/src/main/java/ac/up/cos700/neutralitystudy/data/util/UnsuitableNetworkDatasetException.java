package ac.up.cos700.neutralitystudy.data.util;

/**
 *
 * @author Abrie van Aardt
 */
public class UnsuitableNetworkDatasetException extends Exception {
    @Override
    public String getMessage(){
        return "The format of the dataset is not suited for this network topology";
    }
}
