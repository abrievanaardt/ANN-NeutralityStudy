package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

public class ShekelsFoxholes extends RealProblem {

    private double[][] A = new double[2][25];

    public ShekelsFoxholes() {
        super(-65.536, 65.536, 2);
        int index = 0;
        for (int j = -32; j <= 32; j += 16) {
            for (int i = -32; i <= 32; i += 16) {
                A[0][index] = i;
                A[1][index] = j;
                index++;
            }
        }
        setOptimumFitness(0.9980038);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();

        double result = 0.002;
        double resultI = 0.0;
        for (int i = 1; i <= 25; i++) {
            double resultJ = 0.0;
            for (int j = 0; j < 2; j++) {
                resultJ += Math.pow(x[j] - A[j][i - 1], 6);
            }
            resultJ = i + resultJ;
            resultI += 1 / resultJ;
        }
        resultI = 0.002 + resultI;
        result = 1.0 / resultI;
        return result;
    }

}
