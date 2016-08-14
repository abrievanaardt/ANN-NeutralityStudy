package ac.up.cos700.neutralitystudy.function;

/**
 * This serves as the services contract for all benchmark functions, including
 * statically defined functions such as Alpine, as well as functions with
 * configurable neutrality (e.g. stacking plates) and ultimately MSE, for 
 * calculation of network error.
 * 
 * @author Abrie van Aardt 
 */
public interface IFunction {
    public int getDimensionCount();
    public double evaluate(double... x);        
}
