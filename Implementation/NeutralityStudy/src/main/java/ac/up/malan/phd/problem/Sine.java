package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

/* This is a simple sine function
 */
public class Sine extends RealProblem {

    public Sine() {
        super(0, 30, 1);
        setOptimumFitness(-1);
    }

    public Sine(double xMn, double xMx, int dim) {
        super(xMn, xMx, dim);
        setOptimumFitness(-1);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double tmp = 0;
        for (int i = 0; i < getDimensionality(); ++i)
            tmp += Math.sin(x[i]);
        return tmp;
    }
}
