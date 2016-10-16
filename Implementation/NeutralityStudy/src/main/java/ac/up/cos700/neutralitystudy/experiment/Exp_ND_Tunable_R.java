package ac.up.cos700.neutralitystudy.experiment;

import ac.up.cos700.neutralitystudy.data.Results;
import ac.up.cos700.neutralitystudy.function.problem.PlateStacker;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.sampling.ProgressiveRandomWalkSampler;
import ac.up.cos700.neutralitystudy.study.util.StudyConfig;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import ac.up.malan.phd.sampling.Walk;

/**
 *
 * @author Abrie van Aardt
 */
public class Exp_ND_Tunable_R extends Experiment {

    public Exp_ND_Tunable_R(StudyConfig _config, NeutralityMeasure _neutralityMeasure) 
            throws UnequalArgsDimensionException{
        
        //PlateStacker dimensionality is twice the number of circles used (placement in 2D)
        super(_config, _neutralityMeasure, new PlateStacker(_config.entries.get("numPlates").intValue()*2));

        stepCount = config.entries.get("stepCount").intValue();
        stepRatio = config.entries.get("stepRatio");

        minR = config.entries.get("minR");
        stepR = config.entries.get("stepR");
        numRs = config.entries.get("numR").intValue();

        //one additional row for the q-values
        //another additional row for averages
        //another row for standard deviations
        neutrality = new double[config.entries.get("simulations").intValue() + 3][numRs];

        avgNeutralityIndex = config.entries.get("simulations").intValue() + 1;
        stdDevNeutralityIndex = avgNeutralityIndex + 1;

        avgNeutrality = new double[numRs];
        rValues = new double[numRs];

        //add a row of values to serve as headings: indicating the value of 
        //the parameter at that column. Remember to ignore this row when e.g.
        //plotting.
        for (int i = 0; i < numRs; i++) {
            neutrality[0][i] = minR + i * stepR;
        }

    }

    @Override
    protected void runSimulation(int currentSimulation) throws Exception {

        double currentR = minR;

        for (int i = 0; i < numRs; i++) {
            rValues[i] = minR + i * stepR;

            ((PlateStacker)problem).setPlateRadius(currentR);

            //todo: inversion of control (hand over to builder)
            sampler = new ProgressiveRandomWalkSampler(problem, stepCount, stepRatio);
            Walk[] walks = sampler.sample();

            neutrality[currentSimulation][i] = neutralityMeasure.measure(walks, config.entries.get("epsilon"));
            avgNeutrality[i] = calculateNewAverage(avgNeutrality[i], neutrality[currentSimulation][i], currentSimulation);

            currentR += stepR;
        }

    }

    @Override
    protected void finalise() throws Exception {

        //fill average and std dev of neutralities into the grid
        for (int i = 0; i < numRs; i++) {//for each column
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
        Results.newGraph(this, path, problem.getName() + " Neutrality vs Plate Radius", "Radius", "Neutrality", "", 2);
        Results.addPlot(this, "", rValues, avgNeutrality, "linespoints");
        Results.plot(this);

    }

    protected final double[][] neutrality;
    protected final int stepCount;
    protected final double stepRatio;

    protected final double minR;
    protected final double stepR;
    protected final int numRs;
    protected final double[] avgNeutrality;
    protected final double[] rValues;
}
