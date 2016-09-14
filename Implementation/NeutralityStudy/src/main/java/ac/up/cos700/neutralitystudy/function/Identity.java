package ac.up.cos700.neutralitystudy.function;

import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

/**
 * Implementation of the identity function y = x
 *
 * @author Abrie van Aardt
 */
public class Identity implements IFunction {

    @Override
    public int getDimensionality() {
        return 1;
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != 1)
            throw new UnequalArgsDimensionException();
        return x[0];
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
