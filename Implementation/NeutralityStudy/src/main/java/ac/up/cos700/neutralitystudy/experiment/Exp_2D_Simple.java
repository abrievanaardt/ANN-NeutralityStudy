package ac.up.cos700.neutralitystudy.experiment;

import ac.up.cos700.neutralitystudy.data.Results;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.study.util.StudyConfig;
import ac.up.malan.phd.sampling.Walk;
import java.util.Arrays;

/**
 *
 * This experiment only differs from 1D_Simple in the way plots are drawn.
 * 
 * @author Abrie van Aardt
 */
public class Exp_2D_Simple extends Exp_1D_Simple {

    public Exp_2D_Simple(StudyConfig _config, NeutralityMeasure _neutralityMeasure, RealProblem _problem) {
        super(_config, _neutralityMeasure, _problem);        
    }

    @Override
    protected void finalise() throws Exception {
        //obtain std dev of neutrality
        neutrality[stdDevNeutralityIndex] = calculateSampleStdDev(Arrays.copyOfRange(neutrality, 0, neutrality.length - 2), neutrality[avgNeutralityIndex]);
        
        Results.writeToFile(path, name + "_Neutrality", neutrality);
        
        //graph of problem
        Results.newGraph(this, path, problem.getName(), "x1", "x2", "f(x)", 3);
        Results.addPlot(this, null, problem);
        Results.plot(this);
                
        //graph of problem - showing sample
        Walk[] walks = sampler.sample();        
        Results.newGraph(this, path, problem.getName() + " Sampled", "x1", "x2", "f(x)", 3);
        Results.addPlot(this, problem.getName(), problem);
        for (int i = 0; i < walks.length; i++) {
            Results.addPlot(this, "Walk " + (i + 1), walks[i].getPoints(), walks[i].getPointsFitness(), "linespoints");
        }     
        
        Results.plot(this);        
        
    }
}
