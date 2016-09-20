package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class Quartic extends RealProblem {

    public Quartic() {
        super(-1.28, 1.28, 30);
    }

    public Quartic(int dim) {
        super(-1.28, 1.28, dim);
    }

    public Quartic(double lowerBound, double upperBound, int dim) {
        super(lowerBound, upperBound, dim);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double tmp = 0;
        for (int i = 0; i < getDimensionality(); i++) {
            tmp += (i + 1) * Math.pow(x[i], 4);
        }
        return tmp;
    }  

}
