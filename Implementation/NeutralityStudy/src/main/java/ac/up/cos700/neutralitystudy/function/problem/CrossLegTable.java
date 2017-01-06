package ac.up.cos700.neutralitystudy.function.problem;

import ac.up.cos700.neutralitystudy.function.Function;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import static java.lang.Math.*;
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
public class CrossLegTable extends RealProblem {

    public CrossLegTable() {
        super(-10, 10, 2);
    }

    /**
     * Initialises the Quantiser.
     *
     * @param _function the function to quantise
     * @param _q the quantum size defining the amount of neutrality
     */
    public CrossLegTable(Function _function, double _q, double lowerBound, double upperBound) {
        super(lowerBound, upperBound, _function.getDimensionality());
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
       return 1-1.0 / pow(abs(exp(abs(100.0 - sqrt(x[0]*x[0] + x[1]*x[1])/PI)) * sin(x[0]) * sin(x[1])) + 1, 0.1);
    }

    @Override
    public String getName() {
        return "CrossLegTable";
    }

}
