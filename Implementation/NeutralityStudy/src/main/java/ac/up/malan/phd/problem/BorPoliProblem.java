package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class BorPoliProblem extends RealProblem {

    public BorPoliProblem() {
        super(0, 7, 1);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();

        int n = (int) x[0];
        switch (n) {
            case 0:
                return 6;
            case 1:
                return 5;
            case 2:
                return 5;
            case 3:
                return 3;
            case 4:
                return 2;
            case 5:
                return 1;
            case 6:
                return 2;
            case 7:
                return 7;
        }
        return -1;
    }
}
