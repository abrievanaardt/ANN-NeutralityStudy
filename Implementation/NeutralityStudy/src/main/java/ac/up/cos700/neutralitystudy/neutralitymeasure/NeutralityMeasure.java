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

    /**
     * Measures the amount of neutrality present in the sample. The algorithm
     * specifics of this measurement are left to implementing classes. It is
     * however expected that this method should always return a value in the
     * range [0,1], where 0 indicates no neutrality and 1 indicates a landscape
     * that is completely flat.
     *
     * @param sample the walks performed on a particular problem
     * @param epsilon distance within which points are considered to be flat
     * @return a measure between 0 and 1 inclusive
     */
    public abstract double measure(Walk[] sample, double epsilon);

    /**
     * Method assumes that points only contains 3 points. All 3 points have to
     * be equal in fitness, with an acceptable error of 'epsilon'
     *
     * @param points
     * @return whether the 3 points are neutral or not
     */
    protected boolean isNeutral(double[] points, double epsilon) {
        return (Math.abs(points[0] - points[1]) <= epsilon
                && Math.abs(points[1] - points[2]) <= epsilon
                && Math.abs(points[0] - points[2]) <= epsilon);
    }
    
    public String getMeasureName(){
        return name;
    }
    
    protected String name;
}
