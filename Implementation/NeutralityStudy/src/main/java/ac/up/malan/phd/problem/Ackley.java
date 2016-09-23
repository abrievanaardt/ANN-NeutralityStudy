package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Ackley extends RealProblem {

    public Ackley() {
        super(-32.768, 32.768, 30);
    }

    public Ackley(int dim) {
        super(-32.768, 32.768, dim);
    }

    public Ackley(double lowerBound, double upperBound, int dim) {
        super(lowerBound, upperBound, dim);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();

        double sumsq = 0.0;
        double sumcos = 0.0;
        for (int i = 0; i < getDimensionality(); ++i) {
            sumsq += x[i] * x[i];
            sumcos += Math.cos(2 * Math.PI * x[i]);
        }
        return -20.0 * Math.exp(-0.2 * Math.sqrt(sumsq / getDimensionality())) - Math.exp(sumcos / getDimensionality()) + 20 + Math.E;
    }
}
