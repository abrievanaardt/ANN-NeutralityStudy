package ac.up.cos700.neutralitystudy.function.problem;

import ac.up.malan.phd.problem.*;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Step2 extends RealProblem {

    /**
     * Creates a new instance of Step
     */
    public Step2() {
        super(-10.0, 10.0, 30);
    }

    public Step2(int dim) {
        super(-10, 10, dim);
    }

    public Step2(double lowerBound, double upperBound, int dim) {
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
