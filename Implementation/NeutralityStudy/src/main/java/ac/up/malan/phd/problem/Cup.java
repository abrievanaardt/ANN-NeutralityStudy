package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

/* This is a simple parabola function: f(x1,x2) = x1*x1 + x2*x2
 */
public class Cup extends RealProblem {

    public Cup() {
        super(-5, 5, 2);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        return (x[0] * x[0] + x[1] * x[1]);
    }
}
