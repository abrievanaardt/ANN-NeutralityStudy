package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

// Not getting correct results on this problem:
public class Giunta extends RealProblem {

    public Giunta() {
        super(-1, 1, 2);
    }

    public Giunta(double lowerBound, double upperBound) {
        super(lowerBound, upperBound, 2);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double tmp = 0;
        for (int i = 0; i < 2; i++) {
            tmp += Math.sin((16.0 / 15.0 * x[i]) - 1)
                    + Math.pow(Math.sin((16.0 / 15.0 * x[i]) - 1), 2)
                    + 1.0 / 50.0 * Math.sin(4 * (((16.0 / 15.0) * x[i]) - 1));
        }
        return 0.6 + tmp;
    }    

}
