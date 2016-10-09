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
public class Exp_ND_Tunable_D extends Experiment {

    public Exp_ND_Tunable_D(StudyConfig _config, NeutralityMeasure _neutralityMeasure, RealProblem _problem) {
        super(_config, _neutralityMeasure, _problem);

        problem = _problem;        

        stepCount = config.entries.get("stepCount").intValue();
        stepRatio = config.entries.get("stepRatio");
        
        sampler = new ProgressiveRandomWalkSampler(problem, stepCount, stepRatio);

        minDim = config.entries.get("minDim").intValue();
        stepDim = config.entries.get("stepDim").intValue();
        numDims = config.entries.get("numDim").intValue();

        //one additional row for the dimension header
        neutrality = new double[config.entries.get("simulations").intValue() + 1][numDims];

        avgNeutrality = new double[numDims];
        dimValues = new double[numDims];

        //add a row of values to serve as headings: indicating the value of 
        //the parameter at that column. Remember to ignore this row when e.g.
        //plotting.
        for (int i = 0; i < numDims; i++) {
            neutrality[0][i] = minDim + i * stepDim;
        }

    }

    @Override
    protected void runSimulation(int currentSimulation) throws Exception {

        int currentDim = minDim;

        for (int i = 0; i < numDims; i++) {
            dimValues[i] = minDim + i * stepDim;

            problem.setDimensionality(currentDim);
            
            Walk[] walks = sampler.sample();

            neutrality[currentSimulation][i] = neutralityMeasure.measure(walks, config.entries.get("epsilon"));
            avgNeutrality[i] = calculateNewAverage(avgNeutrality[i], neutrality[currentSimulation][i], currentSimulation);

            currentDim += stepDim;
        }

    }

    @Override
    protected void finalise() throws Exception {
        Results.writeToFile(path, name + "_Neutrality", neutrality);

        //graph of neutrality parameter vs neutrality measured
        Results.newGraph(this, path, problem.getName() + " Neutrality vs Dimension", "Dimensions", "Neutrality", "", 2);
        Results.addPlot(this, "", dimValues, avgNeutrality, "linespoints");
        Results.plot(this);

    }

    protected final double[][] neutrality;
    protected final int stepCount;
    protected final double stepRatio;

    protected final int minDim;
    protected final int stepDim;
    protected final int numDims;
    protected final double[] avgNeutrality;
    protected final double[] dimValues;
}
