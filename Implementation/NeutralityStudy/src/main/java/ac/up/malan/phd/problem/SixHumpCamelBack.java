package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class SixHumpCamelBack extends RealProblem {

    public SixHumpCamelBack(double lowerBound, double upperBound) {
        super(lowerBound, upperBound, 2);
    }

    public SixHumpCamelBack() {
        super(-5, 5, 2);
        setOptimumFitness(-1.0316285);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double x1 = x[0];
        double x2 = x[1];
        return (4 * x1 * x1 - 2.1 * Math.pow(x1, 4.0) + Math.pow(x1, 6.0) / 3.0 + x1 * x2 - 4 * (x2 * x2) + 4 * Math.pow(x2, 4.0));
    }
}
