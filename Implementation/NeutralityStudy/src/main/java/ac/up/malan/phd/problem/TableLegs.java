package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

/* This is a simple function that is flat, except for two "legs" at the bounds of the search space.
 */
public class TableLegs extends RealProblem {

    public TableLegs() {
        super(-5, 5, 1);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        if ((x[0] < -4.5) || (x[0] > 4.5))
            return 0;
        else
            return 1;
    }
}
