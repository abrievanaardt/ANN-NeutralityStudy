package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Bohachevsky2 extends RealProblem {

    public Bohachevsky2() {
        super(-100, 100, 2);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        return x[0] * x[0] + 2 * x[1] * x[1] - 0.3 * Math.cos(3 * Math.PI * x[0]) * Math.cos(4 * Math.PI * x[1]) + 0.3;
    }

}
