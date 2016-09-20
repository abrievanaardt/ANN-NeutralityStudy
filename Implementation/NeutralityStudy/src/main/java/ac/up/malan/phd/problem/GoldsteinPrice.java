package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class GoldsteinPrice extends RealProblem {

    public GoldsteinPrice(double lowerBound, double upperBound) {
        super(lowerBound, upperBound, 2);
    }

    public GoldsteinPrice() {
        super(-2, 2, 2);
        setOptimumFitness(3.0);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double part1 = 1 + (x[0] + x[1] + 1.0) * (x[0] + x[1] + 1.0) * (19.0 - 14.0 * x[0] + 3 * x[0] * x[0] - 14 * x[1] + 6 * x[0] * x[1] + 3 * x[1] * x[1]);
        double part2 = 30 + (2 * x[0] - 3 * x[1]) * (2 * x[0] - 3 * x[1]) * (18 - 32 * x[0] + 12 * x[0] * x[0] + 48 * x[1] - 36 * x[0] * x[1] + 27 * x[1] * x[1]);
        return part1 * part2;
    }

}
