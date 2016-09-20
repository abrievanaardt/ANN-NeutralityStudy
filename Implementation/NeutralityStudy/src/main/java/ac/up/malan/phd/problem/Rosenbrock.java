package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Rosenbrock extends RealProblem {

    public Rosenbrock() {
        super(-2.048, 2.048, 30);
    }

    public Rosenbrock(int dim) {
        super(-2.048, 2.048, dim);
    }

    public Rosenbrock(double lowerBound, double upperBound, int dim) {
        super(lowerBound, upperBound, dim);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double tmp = 0.0;
        for (int i = 0; i < getDimensionality() - 1; ++i) {
            double a = x[i];
            double b = x[i + 1];
            tmp += ((100 * (b - a * a) * (b - a * a)) + ((a - 1.0) * (a - 1.0)));
        }
        return tmp;
    }
}
