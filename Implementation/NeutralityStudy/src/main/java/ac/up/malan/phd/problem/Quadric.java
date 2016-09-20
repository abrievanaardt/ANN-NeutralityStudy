package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Quadric extends RealProblem {

    public Quadric() {
        super(-100, 100, 30);
    }

    public Quadric(int dim) {
        super(-100, 100, dim);
    }

    public Quadric(double lowerBound, double upperBound, int dim) {
        super(lowerBound, upperBound, dim);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double sumsq = 0.0;
        double sum;
        for (int i = 0; i < getDimensionality(); ++i) {
            sum = 0;
            for (int j = 0; j <= i; ++j) {
                sum += x[j];
            }
            sumsq += sum * sum;
        }
        return sumsq;
    }

}
