package ac.up.cos700.neutralitystudy.function.problem;

import ac.up.malan.phd.problem.*;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Cube extends RealProblem {

    /**
     * Creates a new instance of Step
     */
    public Cube() {
        super(-1.0, 1.0, 2);
    }

    public Cube(double lowerBound, double upperBound, int dim) {
        super(lowerBound, upperBound, dim);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        return 100 * Math.pow(x[1] - Math.pow(x[0],3), 2) + Math.pow(1 - x[0],2);
    }

}
