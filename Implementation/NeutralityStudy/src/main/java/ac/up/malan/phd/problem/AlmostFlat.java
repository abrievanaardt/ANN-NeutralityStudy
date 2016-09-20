package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;


/* This is a simple function that is flat, except for one small "table" 
   of magnitude 1 between -0.5 and 0.5 (inclusive)
 */
public class AlmostFlat extends RealProblem {

    public AlmostFlat() {
        super(-5, 5, 1);
    }

    public AlmostFlat(int dim) {
        super(-5, 5, dim);
    }

    public AlmostFlat(double lowerBound, double upperBound, int dim) {
        super(lowerBound, upperBound, dim);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        double tmp = 1;
        for (int i = 0; i < getDimensionality(); i++) {
            if ((x[i] > 0.5) || (x[i] < -0.5))
                return 0;
        }
        return tmp;
    }
}
