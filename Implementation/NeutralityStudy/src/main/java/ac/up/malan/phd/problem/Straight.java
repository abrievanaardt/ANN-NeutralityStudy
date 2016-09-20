package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

/* This is a simple straight surface function: f(x1,x2) = x1 + x2 (to any dimension)
 */
public class Straight extends RealProblem {

    public Straight() throws UnequalArgsDimensionException {
        super(-5, 5, 1);
        setOpt();
    }

    public Straight(int dim) throws UnequalArgsDimensionException {
        super(-5, 5, dim);
        setOpt();
    }

    public Straight(double lowerBound, double upperBound, int dim) throws UnequalArgsDimensionException {
        super(lowerBound, upperBound, dim);
        setOpt();
    }

    private void setOpt() throws UnequalArgsDimensionException {
        double[] v = new double[dimensionality];
        for (int i = 0; i < dimensionality; i++)
            v[i] = lowerBound;
        setOptimumXArray(v);
        setOptimumFitness(evaluate(v));
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double tmp = 0;
        for (int i = 0; i < getDimensionality(); i++) {
            double value = x[i];
            tmp += value;
        }
        return tmp;
    }
}
