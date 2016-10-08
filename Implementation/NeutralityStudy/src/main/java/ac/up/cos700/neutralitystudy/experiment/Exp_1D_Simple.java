package ac.up.cos700.neutralitystudy.experiment;

import ac.up.cos700.neutralitystudy.data.Results;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.sampling.ProgressiveRandomWalkSampler;
import ac.up.cos700.neutralitystudy.study.util.StudyConfig;

/**
 *
 * @author Abrie van Aardt
 */
public class Exp_1D_Simple extends Experiment {

    public Exp_1D_Simple(StudyConfig _config, NeutralityMeasure _neutralityMeasure, RealProblem _problem) {
        super(_config, _neutralityMeasure, _problem);        
        neutrality = new double[config.entries.get("simulations").intValue()];        
        sampler = new ProgressiveRandomWalkSampler(problem, stepCount, stepRatio);
    }

    @Override
    protected void runSimulation(int currentSimulation) throws Exception {        
        previousWalks = sampler.sample();        
        neutrality[currentSimulation - 1] = neutralityMeasure.measure(previousWalks);
    }

    @Override
    protected void finalise() throws Exception {        
        Results.writeToFile(path, name + "_Neutrality", simulations, neutrality);
        
        //graph of problem
        Results.newGraph(path, problem.getName(), "x", "f(x)", null, 2);
        Results.addPlot(null, problem);
        Results.plot();
                
        //graph of problem - showing sample           
        Results.newGraph(path, problem.getName() + " Sampled", "x", "f(x)", null, 2);
        Results.addPlot(problem.getName(), problem);
        for (int i = 0; i < previousWalks.length; i++) {
            Results.addPlot("Walk " + (i + 1), previousWalks[i].getPoints(), previousWalks[i].getPointsFitness(), "linespoints");
        }     
        
        Results.plot();        
        
    }

    protected final double[] neutrality;
    protected final int stepCount = config.entries.get("stepCount").intValue();
    protected final double stepRatio = config.entries.get("stepRatio");     

 
    

}
