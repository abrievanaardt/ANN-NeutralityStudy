package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Spherical extends RealProblem {

    public Spherical() {
        super(-100, 100, 30);
    }

    public Spherical(int dim) {
        super(-100, 100, dim);
    }

    public Spherical(double lowerBound, double upperBound, int dim) {
        super(lowerBound, upperBound, dim);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double tmp = 0;
        for (int i = 0; i < getDimensionality(); i++) {
            tmp += x[i] * x[i];
        }
        return tmp;
    }

}
