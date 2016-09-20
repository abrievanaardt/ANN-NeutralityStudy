package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

/* This is a simple function that in most cases will pull
   an individual towards the centre, which is not the global
   minimum
 */
public class HoleInMountain extends RealProblem {

    public HoleInMountain() {
        super(0, 11, 1);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        double inVal = x[0];
        if (inVal < 5)
            return inVal + 1;
        else if (inVal > 6)
            return (-inVal) + 12;
        else
            return 0;
    }
}
