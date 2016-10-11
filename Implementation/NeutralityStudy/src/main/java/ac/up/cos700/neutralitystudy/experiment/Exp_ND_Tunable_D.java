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
        //another additional row for the averages
        //one more row for the standard deviations
        neutrality = new double[config.entries.get("simulations").intValue() + 3][numDims];

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
        //fill average and std dev of neutralities into the grid
        for (int i = 0; i < numDims; i++) {//for each column
            neutrality[avgNeutralityIndex][i] = avgNeutrality[i];

            //extracting a column of neutrality values - ignore headers and summaries
            double[] neutralityForColumn = new double[neutrality.length - 3];
            for (int j = 1; j <= neutralityForColumn.length; j++) {
                neutralityForColumn[j - 1] = neutrality[j][i];
            }

            neutrality[stdDevNeutralityIndex][i] = calculateSampleStdDev(neutralityForColumn, avgNeutrality[i]);
        }

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
