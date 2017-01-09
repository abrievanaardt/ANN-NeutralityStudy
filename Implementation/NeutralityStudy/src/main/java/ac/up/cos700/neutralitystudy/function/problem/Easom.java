package ac.up.cos700.neutralitystudy.function.problem;

import ac.up.malan.phd.problem.*;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Easom extends RealProblem {

    /**
     * Creates a new instance of Step
     */
    public Easom() {
        super(-10.0, 10.0, 2);
    }

    public Easom(double lowerBound, double upperBound, int dim) {
        super(lowerBound, upperBound, dim);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();

        //System.out.println(1 - (Math.cos(x[0]) * Math.cos(x[1]) * Math.exp(-Math.pow(x[0] - Math.PI, 2) - Math.pow(x[1] - Math.PI, 2))));
        
        return 1 - (Math.cos(x[0]) * Math.cos(x[1]) * Math.exp(-Math.pow(x[0] - Math.PI, 2) - Math.pow(x[1] - Math.PI, 2)));
    }

}
