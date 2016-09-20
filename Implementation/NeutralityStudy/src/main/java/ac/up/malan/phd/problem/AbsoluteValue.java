package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class AbsoluteValue extends RealProblem {

    public AbsoluteValue() {
        super(-100, 100, 30);
    }

    public AbsoluteValue(int dim) {
        super(-100, 100, dim);
    }

    public AbsoluteValue(double lowerBound, double upperBound, int dim) {
        super(lowerBound, upperBound, dim);
    }

    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();

        double tmp = 0;
        for (int i = 0; i < getDimensionality(); i++) {
            tmp += Math.abs(x[i]);
        }
        return tmp;
    }

}
