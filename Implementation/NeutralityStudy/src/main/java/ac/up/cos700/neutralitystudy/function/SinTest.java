package ac.up.cos700.neutralitystudy.function;

import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

/**
 * Implements the Sine function for temporarily testing other features. 
 * 
 * @author Abrie van Aardt
 */
public class SinTest implements IFunction{

    @Override
    public int getDimensionality() {
        return 1;
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        return Math.sin(x[0]);
    }

}
