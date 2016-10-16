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
public class Exp_ND_Tunable_Dom extends Experiment {

    public Exp_ND_Tunable_Dom(StudyConfig _config, NeutralityMeasure _neutralityMeasure, RealProblem _problem) {
        super(_config, _neutralityMeasure, _problem);

        problem = _problem;

        stepCount = config.entries.get("stepCount").intValue();
        stepRatio = config.entries.get("stepRatio");

        sampler = new ProgressiveRandomWalkSampler(problem, stepCount, stepRatio);

        numDoms = config.entries.get("numDom").intValue();

        //one additional row for the dimension header
        //another additional row for the averages
        //one more row for the standard deviations
        neutrality = new double[config.entries.get("simulations").intValue() + 3][numDoms];
        
        avgNeutralityIndex = config.entries.get("simulations").intValue() + 1;
        stdDevNeutralityIndex = avgNeutralityIndex + 1;

        avgNeutrality = new double[numDoms];
        domValues = new double[numDoms];
        
        //add a row of values to serve as headings: indicating the value of 
        //the parameter at that column. Remember to ignore this row when e.g.
        //plotting.
        for (int i = 0; i < numDoms; i++) {
            neutrality[0][i] = config.entries.get("dom" + i);
        }

    }

    @Override
    protected void runSimulation(int currentSimulation) throws Exception {

        for (int i = 0; i < numDoms; i++) {
            domValues[i] = neutrality[0][i];

            problem.setLowerBound(-domValues[i]);
            problem.setUpperBound(domValues[i]);

            Walk[] walks = sampler.sample();

            neutrality[currentSimulation][i] = neutralityMeasure.measure(walks, config.entries.get("epsilon"));
            avgNeutrality[i] = calculateNewAverage(avgNeutrality[i], neutrality[currentSimulation][i], currentSimulation);

        }

    }

    @Override
    protected void finalise() throws Exception {
        //fill average and std dev of neutralities into the grid
        for (int i = 0; i < numDoms; i++) {//for each column
            neutrality[avgNeutralityIndex][i] = avgNeutrality[i];

            //extracting a column of neutrality values - ignore headers and summaries
            double[] neutralityForColumn = new double[neutrality.length - 3];
            for (int j = 1; j <= neutralityForColumn.length; j++) {
                neutralityForColumn[j - 1] = neutrality[j][i];
            }

            neutrality[stdDevNeutralityIndex][i] = calculateSampleStdDev(neutralityForColumn, avgNeutrality[i]);
        }

        Results.writeToFile(path, name + "_Neutrality", neutrality);

        //graph of absolute domain vs neutrality measured
        Results.newGraph(this, path, problem.getName() + " Neutrality vs Domain", "Domain", "Neutrality", "", 2);
        Results.addPlot(this, "", domValues, avgNeutrality, "linespoints");
        Results.plot(this);

    }

    protected final double[][] neutrality;
    protected final int stepCount;
    protected final double stepRatio;
//    protected final double minDom;
//    protected final int factorDom;
    protected final int numDoms;
    protected final double[] avgNeutrality;
    protected final double[] domValues;
}
