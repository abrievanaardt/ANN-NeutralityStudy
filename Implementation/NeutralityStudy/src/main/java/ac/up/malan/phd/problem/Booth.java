package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Booth extends RealProblem {

    public Booth() {
        super(-10, 10, 2);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double x1 = x[0];
        double x2 = x[1];

        return (x1 + 2 * x2 - 7) * (x1 + 2 * x2 - 7) + (2 * x1 + x2 - 5) * (2 * x1 + x2 - 5);
    }
}
