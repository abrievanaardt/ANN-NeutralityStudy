package ac.up.cos700.neutralitystudy.neutralitymeasure;

import ac.up.malan.phd.sampling.Walk;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This serves as an interface to which all neutrality measures must conform.
 * 
 * @author Abrie van Aardt
 */
public abstract class NeutralityMeasure {
    
    public NeutralityMeasure(double _epsilon){
        epsilon = _epsilon;
        
        Logger
                .getLogger(getClass().getName())
                .log(Level.FINER, "Using neutrality measure: {0}", getClass().getSimpleName());
        
    }
    
    /**
     * Measures the amount of neutrality present in the sample. The algorithm
     * specifics of this measurement are left to implementing classes. It is
     * however expected that this method should always return a value in the
     * range [0,1], where 0 indicates no neutrality and 1 indicates a
     * landscape that is completely flat.
     * 
     * @param sample the walks performed on a particular problem
     * @param epsilon distance within which points are considered to be flat
     * @return a measure between 0 and 1 inclusive
     */
    public abstract double measure(Walk[] sample);
    
    protected double epsilon;
}
