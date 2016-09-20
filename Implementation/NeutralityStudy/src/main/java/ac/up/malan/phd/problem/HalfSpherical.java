package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

/* This is a simple parabola function: f(x1,x2) = x1*x1
 * defined only for a positive range (to any dimension)
 */
public class HalfSpherical extends RealProblem {

    public HalfSpherical() {
        super(0, 100, 1);
    }

    public HalfSpherical(int dim) {
        super(0, 100, dim);
    }

    public HalfSpherical(double xMx, int dim) {
        super(0, xMx, dim);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double tmp = 0;
        for (int i = 0; i < getDimensionality(); i++) {
            tmp += x[i] * x[i];
        }
        return tmp;
    }
}
