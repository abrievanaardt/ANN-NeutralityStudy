package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class VassilevSampleProblem extends RealProblem {

    public VassilevSampleProblem() {
        super(0, 5, 1);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        int n = (int) x[0];
        switch (n) {
            case 0:
                return 0;
            case 1:
                return 0.01;
            case 2:
                return 0.05;
            case 3:
                return 0.2;
            case 4:
                return 0.21;
            case 5:
                return 0.9;
        }
        return -1;
    }
}
