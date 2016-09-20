package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Salomon extends RealProblem {

    public Salomon() {
        super(-100, 100, 30);
    }

    public Salomon(int dim) {
        super(-100, 100, dim);
    }

    public Salomon(double lowerBound, double upperBound, int dim) {
        super(lowerBound, upperBound, dim);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double sumSquares = 0.0;
        for (int i = 0; i < getDimensionality(); i++) {
            sumSquares += x[i] * x[i];
        }
        return -(Math.cos(2 * Math.PI * Math.sqrt(sumSquares))) + (0.1 * Math.sqrt(sumSquares)) + 1;
    }

}
