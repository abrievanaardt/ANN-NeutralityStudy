package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Rastrigin extends RealProblem {

    public Rastrigin() {
        super(-5.12, 5.12, 30);
    }

    public Rastrigin(int dim) {
        super(-5.12, 5.12, dim);
    }

    public Rastrigin(double lowerBound, double upperBound, int dim) {
        super(lowerBound, upperBound, dim);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double tmp = 0;
        for (int i = 0; i < getDimensionality(); ++i) {
            tmp += x[i] * x[i] - 10.0 * Math.cos(2 * Math.PI * x[i]);
        }
        return 10 * getDimensionality() + tmp;
    }
}
