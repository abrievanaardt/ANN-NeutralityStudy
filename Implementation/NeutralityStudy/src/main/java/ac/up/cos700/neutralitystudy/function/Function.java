package ac.up.cos700.neutralitystudy.function;

import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

/**
 * This serves as the services contract for all benchmark functions, including
 * statically defined functions such as {@link Alpine}, as well as functions
 * with configurable neutrality (e.g. stacking plates) and ultimately
 * {@link NetworkError}, for calculation of NN classification error.
 *
 * @author Abrie van Aardt
 */
public abstract class Function {

    public int getDimensionality() {
        return dimensionality;
    }

    public abstract double evaluate(double... x) throws UnequalArgsDimensionException;

    protected int dimensionality;
}