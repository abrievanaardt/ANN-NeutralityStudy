package ac.up.cos700.neutralitystudy.experiment;

import ac.up.cos700.neutralitystudy.data.Results;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.sampling.ProgressiveRandomWalkSampler;
import ac.up.cos700.neutralitystudy.study.util.StudyConfig;
import ac.up.malan.phd.sampling.Walk;

/**
 *
 * @author Abrie van Aardt
 */
public class Experiment_2D_Simple extends Experiment {

    public Experiment_2D_Simple(StudyConfig _config, NeutralityMeasure _neutralityMeasure, RealProblem _problem) {
        super(_config, _neutralityMeasure, _problem);
        
        path += "_"+ _problem.getClass().getSimpleName();
        neutrality = new double[config.simulationCount];
        problem = _problem;
        sampler = new ProgressiveRandomWalkSampler(problem, stepCount, stepRatio);
    }

    @Override
    protected void runSimulation(int currentSimulation) throws Exception {        
        Walk[] walks = sampler.sample();        
        neutrality[currentSimulation - 1] = neutralityMeasure.measure(walks);
    }

    @Override
    protected void finalise() throws Exception {
        Results.writeToFile(path, "Neutrality", simulations, neutrality);
        Results.newGraph(path, problem.getClass().getSimpleName(), "x", "f(x)", null, 2);
        Results.addPlot(null, problem);
        Results.plot();
                
        //plot a representive sample
        Walk[] walks = sampler.sample();        
        Results.newGraph(path, problem.getClass().getSimpleName() + " Sampled", "x", "f(x)", null, 2);
        Results.addPlot(problem.getClass().getSimpleName(), problem);
        for (int i = 0; i < walks.length; i++) {
            Results.addPlot("Walk " + (i + 1), walks[i].getPoints(), walks[i].getPointsFitness(), "linespoints");
        }     
        
        Results.plot();        
        
    }

    private final double[] neutrality;
    private final int stepCount = config.entries.get("stepCount").intValue();
    private final double stepRatio = config.entries.get("stepRatio");    
    private final ProgressiveRandomWalkSampler sampler;

}
