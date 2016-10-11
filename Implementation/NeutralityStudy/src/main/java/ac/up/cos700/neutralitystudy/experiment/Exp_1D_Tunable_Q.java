package ac.up.cos700.neutralitystudy.experiment;

import ac.up.cos700.neutralitystudy.data.Results;
import ac.up.cos700.neutralitystudy.function.problem.Quantiser;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.sampling.ProgressiveRandomWalkSampler;
import ac.up.cos700.neutralitystudy.study.util.StudyConfig;
import ac.up.malan.phd.sampling.Walk;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author Abrie van Aardt
 */
public class Exp_1D_Tunable_Q extends Experiment {

    public Exp_1D_Tunable_Q(StudyConfig _config, NeutralityMeasure _neutralityMeasure, RealProblem _problem) {
        super(_config, _neutralityMeasure, _problem);

        problem = _problem;

        stepCount = config.entries.get("stepCount").intValue();
        stepRatio = config.entries.get("stepRatio");

        minQ = config.entries.get("minQ");
        stepQ = config.entries.get("stepQ");
        numQs = config.entries.get("numQ").intValue();

        //one additional row for the q-values
        //another additional row for averages
        //another row for standard deviations
        neutrality = new double[config.entries.get("simulations").intValue() + 3][numQs];

        avgNeutralityIndex = config.entries.get("simulations").intValue() + 1;
        stdDevNeutralityIndex = avgNeutralityIndex + 1;

        avgNeutrality = new double[numQs];
        qValues = new double[numQs];

        //add a row of values to serve as headings: indicating the value of 
        //the parameter at that column. Remember to ignore this row when e.g.
        //plotting.
        for (int i = 0; i < numQs; i++) {
            neutrality[0][i] = minQ + i * stepQ;
        }

    }

    @Override
    protected void runSimulation(int currentSimulation) throws Exception {

        double currentQ = minQ;

        for (int i = 0; i < numQs; i++) {
            qValues[i] = minQ + i * stepQ;

            RealProblem quantisedProblem = new Quantiser(problem, currentQ, problem.getLowerBound(), problem.getUpperBound());

            //todo: inversion of control (hand over to builder)
            sampler = new ProgressiveRandomWalkSampler(quantisedProblem, stepCount, stepRatio);
            Walk[] walks = sampler.sample();

            neutrality[currentSimulation][i] = neutralityMeasure.measure(walks, config.entries.get("epsilon"));
            avgNeutrality[i] = calculateNewAverage(avgNeutrality[i], neutrality[currentSimulation][i], currentSimulation);

            currentQ += stepQ;
        }

    }

    @Override
    protected void finalise() throws Exception {

        //fill average and std dev of neutralities into the grid
        for (int i = 0; i < numQs; i++) {//for each column
            neutrality[avgNeutralityIndex][i] = avgNeutrality[i];

            //extracting a column of neutrality values - ignore headers and summaries
            double[] neutralityForColumn = new double[neutrality.length - 3];
            for (int j = 1; j <= neutralityForColumn.length; j++) {
                neutralityForColumn[j - 1] = neutrality[j][i];
            }

            neutrality[stdDevNeutralityIndex][i] = calculateSampleStdDev(neutralityForColumn, avgNeutrality[i]);
        }

        Results.writeToFile(path, name + "_Neutrality", neutrality);

        NumberFormat decFormat = new DecimalFormat("#0.000");

        //plot 3 examples
        for (int i = 0; i <= numQs; i += (int) (numQs / 2)) {

            double currentQ = minQ + (i == 0 ? 0 : i - 1) * stepQ;

            RealProblem quantisedProblem = new Quantiser(problem, currentQ, problem.getLowerBound(), problem.getUpperBound());

            //todo: inversion of control (hand over to builder)
            //todo: remove state
            sampler = new ProgressiveRandomWalkSampler(quantisedProblem, stepCount, stepRatio);
            Walk[] walks = sampler.sample();

//            //graph of quantised problem
//            Results.newGraph(this, path, quantisedProblem.getExpName() + " " + "quantum" + " = " + decFormat.format(currentQ), "x", "f(x)", null, 2);
//            Results.addPlot(this, null, quantisedProblem);
//            Results.plot(this);
            //graph of quantised problem - showing sample
            Results.newGraph(this, path, quantisedProblem.getName() + " " + "quantum" + " = " + decFormat.format(currentQ) + " Sampled", "x", "f(x)", null, 2);
            Results.addPlot(this, null, quantisedProblem);
            for (int j = 0; j < walks.length; j++) {
                Results.addPlot(this, "Walk " + (j + 1), walks[j].getPoints(), walks[j].getPointsFitness(), "linespoints");
            }
            Results.plot(this);
        }

        //graph of neutrality parameter vs neutrality measured
        Results.newGraph(this, path, "Quantised " + problem.getName() + " Neutrality vs Quantum", "Quantum", "Neutrality", "", 2);
        Results.addPlot(this, "", qValues, avgNeutrality, "lines smooth sbezier");
        Results.addPlot(this, "", qValues, avgNeutrality, "points pointtype 7 pointsize 0.4");
        Results.plot(this);

    }

    protected final double[][] neutrality;
    protected final int stepCount;
    protected final double stepRatio;

    protected final double minQ;
    protected final double stepQ;
    protected final int numQs;
    protected final double[] avgNeutrality;
    protected final double[] qValues;
}
