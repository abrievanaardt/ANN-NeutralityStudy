package ac.up.cos700.neutralitystudy.function.problem;

import ac.up.cos700.neutralitystudy.function.Function;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ac.up.malan.phd.sampling.Vector;

/**
 * This class represents a function, bounded in all dimensions by a hypercube
 * and with a known optimum value. This is intended to be treated as a landscape
 * that can be searched by an optimisation algorithm. Where problems do not have
 * known optima, this is indicated with Double.NaN and the corresponding vector
 * will be null.
 *
 * @author Abrie van Aardt
 * @author Dr Katherine Malan
 */
public abstract class RealProblem extends Function {
    
    public RealProblem(double xmin, double xmax, int dim) {
        this(xmin, xmax, dim, 0);
    }

    public RealProblem(double xmin, double xmax, int dim, double fmin) {
        lowerBound = xmin;
        upperBound = xmax;
        dimensionality = dim;
        optimumFitness = fmin;        
    }

    public double evaluate(Vector x) throws UnequalArgsDimensionException {
        return this.evaluate(x.toArray());
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }
    
    public void setLowerBound(double _lowerBound){
        lowerBound = _lowerBound;
    }
    
    public void setUpperBound(double _upperBound){
        upperBound = _upperBound;
    }

    public double getOptimumFitness() {
        return optimumFitness;
    }

    public void setOptimumFitness(double d) {
        optimumFitness = d;
    }

    public Vector getOptimumX() {
        return optimumX;
    }

    public double[] getOptimumXArray() {
        return optimumX.toArray();
    }

    public void setOptimumX(Vector v) {
        try {
            optimumX = (Vector) v.clone();
        }
        catch (CloneNotSupportedException e) {
            Logger.getLogger(RealProblem.class.getName()).log(Level.SEVERE, "Could not clone vector", e);
        }
    }

    public void setOptimumXArray(double[] _x) {
        optimumX = new Vector(_x);
    }

    public boolean isInRange(Vector x) {
        for (int i = 0; i < dimensionality; i++) {
            if ((x.getReal(i) < lowerBound) || (x.getReal(i) > upperBound))
                return false;
        }
        return true;
    }

    // return the euclidean distance between the minimum and maximum points in multidimensional space
    public double getTrueRange() throws UnequalArgsDimensionException {
        double eucDistance;
        Vector min = new Vector(dimensionality);
        Vector max = new Vector(dimensionality);
        for (int i = 0; i < dimensionality; i++) {
            min.setReal(i, lowerBound);
            max.setReal(i, upperBound);
        }
        eucDistance = min.getEuclideanDistance(max);

        return eucDistance;
    }

    protected double lowerBound = -10; // lowerBound & upperBound refer to the domain of the problem
    protected double upperBound = 10;
    protected double optimumFitness = 0;// optimum fitness value
    protected Vector optimumX;
}
