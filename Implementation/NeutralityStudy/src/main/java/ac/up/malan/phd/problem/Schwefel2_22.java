package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Schwefel2_22 extends RealProblem {

    public Schwefel2_22() {
        super(-10, 10, 30);
    }

    public Schwefel2_22(int dim) {
        super(-10, 10, dim);
    }

    public Schwefel2_22(double lowerBound, double upperBound, int dim) {
        super(lowerBound, upperBound, dim);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double sum = 0;
        double product = 0;
        for (int i = 0; i < getDimensionality(); ++i) {
            sum += Math.abs(x[i]);
            if (i == 0)
                product = Math.abs(x[i]);
            else
                product *= Math.abs(x[i]);
        }

        return sum + product;
    }

}
