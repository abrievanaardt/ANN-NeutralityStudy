package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class CarromTable extends RealProblem {

    public CarromTable() {
        super(-10, 10, 2);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double x1 = x[0];
        double x2 = x[1];
        double absExp = Math.abs(1 - (Math.pow(x1 * x1 + x2 * x2, 0.5) / Math.PI));
        double innerBracket = Math.cos(x1) * Math.cos(x2) * Math.exp(absExp);
        double square = innerBracket * innerBracket;
        return -(square) / 30.0;
    }

}
