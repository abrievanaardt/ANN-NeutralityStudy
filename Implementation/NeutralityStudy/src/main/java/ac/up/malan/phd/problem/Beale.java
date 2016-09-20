package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Beale extends RealProblem {

    public Beale(double lowerBound, double upperBound) {
        super(lowerBound, upperBound, 2);
    }

    public Beale() {
        super(-4.5, 4.5, 2);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        double x1 = x[0];
        double x2 = x[1];

        return (1.5 - x1 + x1 * x2) * (1.5 - x1 + x1 * x2) + (2.25 - x1 + x1 * x2 * x2) * (2.25 - x1 + x1 * x2 * x2) + (2.625 - x1 + x1 * x2 * x2 * x2) * (2.625 - x1 + x1 * x2 * x2 * x2);
    }
}
