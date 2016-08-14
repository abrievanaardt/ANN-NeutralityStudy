package ac.up.cos700.neutralitystudy.function;

import ac.up.cos700.neutralitystudy.function.IFunction;

/**
 * Implementation of Sigmoid in 1D with λ = 1 
 * 
 * @author Abrie van Aardt
 */
public class Sigmoid implements IFunction {

    @Override
    public int getDimensionCount() {
        return 1;
    }

    @Override
    public double evaluate(double... x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
