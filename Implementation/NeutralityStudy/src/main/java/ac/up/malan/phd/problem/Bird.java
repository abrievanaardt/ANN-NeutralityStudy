package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Bird extends RealProblem {

    public Bird() {
        super(-6.285714286, 6.285714286, 2);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        double x1 = x[0];
        double x2 = x[1];

        return Math.sin(x1) * Math.exp((1 - Math.cos(x2)) * (1 - Math.cos(x2))) + Math.cos(x2) * Math.exp((1 - Math.sin(x1)) * (1 - Math.sin(x1))) + (x1 - x2) * (x1 - x2);
    }

}
