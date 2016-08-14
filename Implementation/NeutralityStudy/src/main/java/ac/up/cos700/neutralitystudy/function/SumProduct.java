package ac.up.cos700.neutralitystudy.function;

import java.util.Arrays;

/**
 *
 * @author Abrie van Aardt
 */
public class SumProduct implements IFunction {

    public SumProduct(double[] _weightVector) {
        weightVector = Arrays.copyOf(_weightVector, _weightVector.length);
    }

    public void setWeightVector(double[] _weightVector) {
        weightVector = Arrays.copyOf(_weightVector, _weightVector.length);
    }

    @Override
    public int getDimensionCount() {
        return -1;
    }

    @Override
    public double evaluate(double... x) {
        double sumProduct = 0;

        //ignores the last element in weightVector, the bias
        for (int i = 0; i < x.length; i++) {
            sumProduct += x[i] * weightVector[i];
        }

        return sumProduct;
    }

    private double[] weightVector;
}
