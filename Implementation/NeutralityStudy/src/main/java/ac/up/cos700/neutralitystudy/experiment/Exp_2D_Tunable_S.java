package ac.up.cos700.neutralitystudy.experiment;

import ac.up.cos700.neutralitystudy.data.Results;
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
public class Exp_2D_Tunable_S extends Exp_1D_Tunable_S {

    public Exp_2D_Tunable_S(StudyConfig _config, NeutralityMeasure _neutralityMeasure, RealProblem _problem) {
        super(_config, _neutralityMeasure, _problem);
    }

    @Override
    protected void finalise() throws Exception {
        //fill average and std dev of neutralities into the grid
        for (int i = 0; i < numSs; i++) {//for each column
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
        for (int i = 0; i <= numSs; i += (int) (numSs / 2)) {

            double currentS = minS + (i == 0 ? 0 : i - 1) * stepS;

            stepCount = (int) ((1 / currentS) * 2 * avgNumTraversals);//allows for n-time traversal on average
            stepRatio = currentS;

            //todo: inversion of control (hand over to builder)
            //todo: remove state
            sampler = new ProgressiveRandomWalkSampler(problem, stepCount, stepRatio);
            Walk[] walks = sampler.sample();

            //graph of problem
            Results.newGraph(this, path, problem.getName() + " " + "maxStepSize" + " = " + decFormat.format(currentS), "x1", "x2", "f(x)", 3);
            Results.addPlot(this, null, problem);
            Results.plot(this);
            
            //graph of problem - showing sample
            Results.newGraph(this, path, problem.getName() + " " + "maxStepSize" + " = " + decFormat.format(currentS) + " Sampled", "x1", "x2", "f(x)", 3);
            Results.addPlot(this, null, problem);
            for (int j = 0; j < walks.length; j++) {
                Results.addPlot(this, "Walk " + (j + 1), walks[j].getPoints(), walks[j].getPointsFitness(), "linespoints");
            }
            Results.plot(this);
        }

        //graph of neutrality parameter vs neutrality measured
        Results.newGraph(this, path, problem.getName() + " Neutrality vs Max Step Size", "Max Step Size", "Neutrality", "", 2);
        Results.addPlot(this, "", sValues, avgNeutrality, "linespoints");
        Results.plot(this);

    }
}
