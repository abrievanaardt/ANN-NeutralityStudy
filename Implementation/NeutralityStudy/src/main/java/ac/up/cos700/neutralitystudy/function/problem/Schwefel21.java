package ac.up.cos700.neutralitystudy.function.problem;

import ac.up.malan.phd.problem.*;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Schwefel21 extends RealProblem {

    /**
     * Creates a new instance of Step
     */
    public Schwefel21() {
        super(-0.1, 0.1, 30);
    }

    public Schwefel21(int dim) {
        super(-0.1, 0.1, dim);
    }

    public Schwefel21(double lowerBound, double upperBound, int dim) {
        super(lowerBound, upperBound, dim);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double maximum = Double.MIN_VALUE;
        
        for (double d : x) {
            maximum = Math.abs(d) > maximum ? d : maximum;
        }
        
        return maximum;
    }

}
