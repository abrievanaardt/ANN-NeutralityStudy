package ac.up.cos700.neutralitystudy.function;

import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

/**
 * Implements the Sine function for temporarily testing other features.
 *
 * @author Abrie van Aardt
 */
public class Test implements IFunction {

    @Override
    public int getDimensionality() {
        return 2;
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();

        return Math.pow(x[0], 2) + Math.pow(x[1], 2);
    }

    @Override
    public double getLowerBound() {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double getUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

}
