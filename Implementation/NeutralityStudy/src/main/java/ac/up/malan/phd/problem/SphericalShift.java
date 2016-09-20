package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException; 
import ac.up.malan.phd.sampling.Vector;

/* Implements the function: f(x) = x*x - 2*shift*x + shift*shift 
   where shift is a horizontal shift in the minimum of the spherical function, without a change in shape
 */
public class SphericalShift extends Spherical {

    Vector shift;   // This shifts the minimum of the Spherical function to this position
    
    public SphericalShift(double lowerBound, double upperBound, int dim, Vector shift) throws CloneNotSupportedException {
        super(lowerBound, upperBound, dim);
        this.shift = (Vector)shift.clone();
    }
    
    @Override
public double evaluate(double... x) throws UnequalArgsDimensionException {
if (x.length != getDimensionality())
throw new UnequalArgsDimensionException();
        double tmp = 0, shiftI;
        for (int i = 0; i < getDimensionality(); i++) {
            shiftI = shift.getReal(i);
            tmp += x[i] * x[i] - 2 * shiftI * x[i] + shiftI*shiftI;
        }
        return tmp;
    }
    
}
