package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

/* This problem is described as Niching problem 1 in "Fundamentals
 * of Computational Swarm Intelligence", AP Engelbrecht, p.39
 * MULTIPLE GLOBAL OPTIMA
 */
public class Niching1 extends RealProblem {

    public Niching1() {
        super(0, 2, 1);
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != getDimensionality())
            throw new UnequalArgsDimensionException();
        
        return (Math.pow(Math.sin(5 * Math.PI * x[0]), 6));
    }
}
