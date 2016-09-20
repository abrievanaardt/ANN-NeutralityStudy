package ac.up.cos700.neutralitystudy.function;

import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

/**
 * Implements the Sine function for temporarily testing other features.
 *
 * @author Abrie van Aardt
 */
public class TestFunction extends Function {

    public TestFunction(){
        dimensionality = 2;
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();

        return Math.pow(x[0], 2) + Math.pow(x[1], 2);
    }    

}
