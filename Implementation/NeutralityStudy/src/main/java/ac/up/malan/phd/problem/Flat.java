package ac.up.malan.phd.problem;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException; 

public class Flat extends RealProblem {
    
    public Flat() {
        super(-10,10,1);  
	setOptimumFitness(1);
    }
  
    @Override
public double evaluate(double... x) throws UnequalArgsDimensionException {
if (x.length != getDimensionality())
throw new UnequalArgsDimensionException();
	    return 1;
    }
}