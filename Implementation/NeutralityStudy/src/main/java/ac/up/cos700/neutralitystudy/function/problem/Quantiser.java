package ac.up.cos700.neutralitystudy.function.problem;

import ac.up.cos700.neutralitystudy.function.Function;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This function discretises the output of any {@link Function}. This can be
 * used to introduce neutrality into a particular function as proposed by
 *
 * A. Owen and I. Harvey, "Adapting particle swarm optimisation for fitness
 * landscapes with neutrality" in Swarm Intelligence (SIS'07) IEEE Symposium on,
 * IEEE, 2007, pp. 258 - 265.
 *
 * @author Abrie van Aardt
 */
public class Quantiser extends RealProblem {

    /**
     * Initialises the Quantiser.
     *
     * @param _function the function to quantise
     * @param _q the quantum size defining the amount of neutrality
     */
    public Quantiser(Function _function, double _q, double lowerBound, double upperBound) {

        super(lowerBound, upperBound, _function.getDimensionality());

        function = _function;
        q = _q;

        Logger
                .getLogger(getClass().getName())
                .log(Level.FINER, "Quantising: {0}, quantum: {1}", new Object[]{
            _function.getClass().getSimpleName(),
            _q
        });
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();

        double rawOutput = function.evaluate(x);

        return rawOutput - (rawOutput % q);
    }

    @Override
    public String toString() {
        return "Quantised " + function.getClass().getSimpleName();
    }

    private final Function function;
    private final double q;
}
