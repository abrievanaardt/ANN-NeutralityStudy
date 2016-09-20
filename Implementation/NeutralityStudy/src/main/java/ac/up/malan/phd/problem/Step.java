package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Step extends RealProblem {

    /**
     * Creates a new instance of Step
     */
    public Step() {
        super(-100.0, 100.0, 30);
    }

    public Step(int dim) {
        super(-100, 100, dim);
    }

    public Step(double lowerBound, double upperBound, int dim) {
        super(lowerBound, upperBound, dim);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double sum = 0.0;
        for (int i = 0; i < getDimensionality(); ++i) {
            sum += Math.floor(x[i] + 0.5) * Math.floor(x[i] + 0.5);
        }
        return sum;
    }

}
