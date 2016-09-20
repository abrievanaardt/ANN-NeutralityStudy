package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Griewank extends RealProblem {

    public Griewank() {
        super(-600, 600, 30);
    }

    public Griewank(int dim) {
        super(-600, 600, dim);
    }

    public Griewank(double lowerBound, double upperBound, int dim) {
        super(lowerBound, upperBound, dim);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double sumsq = 0;
        double prod = 1;
        for (int i = 0; i < getDimensionality(); i++) {
            sumsq += x[i] * x[i];
            prod *= (Math.cos(x[i] / Math.sqrt(i + 1)));
        }
        return 1 + sumsq * (1.0 / 4000.0) - prod;
    }

}
