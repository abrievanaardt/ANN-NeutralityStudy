package ac.up.cos700.neutralitystudy.function;

import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

/**
 * This function discretises the output of any {@link IFunction}. This can be
 * used to introduce neutrality into a particular function as proposed by
 *
 * A. Owen and I. Harvey, "Adapting particle swarm optimisation for fitness
 * landscapes with neutrality" in Swarm Intelligence (SIS'07) IEEE Symposium on,
 * IEEE, 2007, pp. 258 - 265.
 *
 * @author Abrie van Aardt
 */
public class Quantiser implements IFunction {

    /**
     * Initialises the Quantiser.
     *
     * @param _function the function to quantise
     * @param _q the quantum size defining the amount of neutrality
     */
    public Quantiser(IFunction _function, double _q) {
        function = _function;
        q = _q;
    }

    @Override
    public int getDimensionality() {
        return function.getDimensionality();
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != function.getDimensionality())
            throw new UnequalArgsDimensionException();

        double rawOutput = function.evaluate(x);

        return rawOutput - (rawOutput % q);
    }

    @Override
    public double getLowerBound() {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double getUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

    private final IFunction function;
    private final double q;

}
