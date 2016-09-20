package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

// This function is the multidimensional E-F102 from 96WHI00 
public class Rana extends RealProblem {

    public Rana() {
        super(-512, 512, 2);
    }

    public Rana(int dim) {
        super(-512, 512, dim);
    }

    public Rana(double lowerBound, double upperBound, int dim) {
        super(lowerBound, upperBound, dim);
    }

    private double f102(double x, double y) {
        double term1 = x * Math.sin(Math.sqrt(Math.abs(y + 1 - x)))
                * Math.cos(Math.sqrt(Math.abs(x + y + 1)));
        double term2 = (y + 1) * Math.cos(Math.sqrt(Math.abs(y + 1 - x)))
                * Math.sin(Math.sqrt(Math.abs(x + y + 1)));
        return term1 + term2;
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        int d = getDimensionality();
        double sumTotal = 0.0;
        for (int i = 0; i < d; ++i) {
            int index1 = i;
            int index2 = (i + 1) % (d);
            sumTotal += f102(x[index1], x[index2]);
        }
        return sumTotal;
    }
}
