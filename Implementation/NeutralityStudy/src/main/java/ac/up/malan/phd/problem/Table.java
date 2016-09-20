package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

/* This is a simple function that is flat, except for one small "table" 
   of magnitude 1 between -0.5 and 0.5 (inclusive)
 */
public class Table extends RealProblem {

    public Table() {
        super(-5, 5, 1);
    }

    public Table(int dim) {
        super(-5, 5, dim);
    }

    public Table(double lowerBound, double upperBound, int dim) {
        super(lowerBound, upperBound, dim);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double tmp = 1;
        for (int i = 0; i < getDimensionality(); i++) {
            if ((x[i] > 2.5) || (x[i] < -2.5))
                return 0;
        }
        return tmp;
    }
}
