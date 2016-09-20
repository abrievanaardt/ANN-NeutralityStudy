package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Schwefel2_26 extends RealProblem {

    public Schwefel2_26(int dim) throws UnequalArgsDimensionException {
        super(-500, 500, dim);
        setOpt();
    }

    public Schwefel2_26(double lowerBound, double upperBound, int dim) throws UnequalArgsDimensionException {
        super(lowerBound, upperBound, dim);
        setOpt();
    }

    public void setOpt() throws UnequalArgsDimensionException {
        double[] v = new double[dimensionality];
        for (int i = 0; i < dimensionality; i++)
            v[i] = 420.9687465395405;
        setOptimumXArray(v);
        setOptimumFitness(evaluate(v));
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();

        double sum = 0;
        for (int i = 0; i < getDimensionality(); ++i) {
            sum += x[i] * Math.sin(Math.sqrt(Math.abs(x[i])));
        }

        return -sum;
    }

}
