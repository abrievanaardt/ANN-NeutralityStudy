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
 * This experiment only differs from 1D_Tunable_Q in the way plots are drawn.
 *
 * @author Abrie van Aardt
 */
public class Exp_2D_Tunable_Q extends Exp_1D_Tunable_Q {

    public Exp_2D_Tunable_Q(StudyConfig _config, NeutralityMeasure _neutralityMeasure, RealProblem _problem) {
        super(_config, _neutralityMeasure, _problem);
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
        for (int i = 0; i <= numQs; i += (numQs / 2)) {

            double currentQ = minQ + (i == 0 ? 0 : i - 1) * stepQ;

            RealProblem quantisedProblem = new Quantiser(problem, currentQ, problem.getLowerBound(), problem.getUpperBound());

            //todo: inversion of control (hand over to builder)
            sampler = new ProgressiveRandomWalkSampler(quantisedProblem, stepCount, stepRatio);
            Walk[] walks = sampler.sample();

            //graph of quantised problem
            Results.newGraph(this, path, quantisedProblem.getName()+ " " + "quantum" + " = " + decFormat.format(currentQ), "x1", "x2", "f(x)", 3);
            Results.addPlot(this, null, quantisedProblem);
            Results.plot(this);
            
            //graph of quantised problem - showing sample
            Results.newGraph(this, path, quantisedProblem.getName() + " " + "quantum" + " = " + decFormat.format(currentQ) + " Sampled", "x1", "x2", "f(x)", 3);
            Results.addPlot(this, null, quantisedProblem);
            for (int j = 0; j < walks.length; j++) {
                Results.addPlot(this, "Walk " + (j + 1), walks[j].getPoints(), walks[j].getPointsFitness(), "linespoints");
            }
            Results.plot(this);
        }

        //graph of neutrality parameter vs neutrality measured
        Results.newGraph(this, path, "Quantised " + problem.getName() + " Neutrality vs Quantum", "Quantum", "Neutrality", "", 2);
        Results.addPlot(this, "", qValues, avgNeutrality, "linespoints");        
        Results.plot(this);
    }

}
