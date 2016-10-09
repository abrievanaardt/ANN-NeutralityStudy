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
public class Exp_1D_Tunable_S extends Experiment {

    public Exp_1D_Tunable_S(StudyConfig _config, NeutralityMeasure _neutralityMeasure, RealProblem _problem) {
        super(_config, _neutralityMeasure, _problem);

        problem = _problem;
        
        avgNumTraversals = config.entries.get("avgNumTraversals").intValue();

        minS = config.entries.get("minS");
        stepS = config.entries.get("stepS");
        numSs = config.entries.get("numS").intValue();
        
        stepCount = (int)((1/minS)*2*avgNumTraversals);//allows for n-time traversal on average
        stepRatio = minS;        

        //one additional row for the step size values
        neutrality = new double[config.entries.get("simulations").intValue() + 1][numSs];

        avgNeutrality = new double[numSs];
        sValues = new double[numSs];

        //add a row of values to serve as headings: indicating the value of 
        //the parameter at that column. Remember to ignore this row when e.g.
        //plotting.
        for (int i = 0; i < numSs; i++) {
            neutrality[0][i] = minS + i * stepS;
        }

    }

    @Override
    protected void runSimulation(int currentSimulation) throws Exception {

        double currentS = minS;

        for (int i = 0; i < numSs; i++) {
            sValues[i] = minS + i * stepS;

            
            
            stepCount = (int)((1 / currentS) * 2 * avgNumTraversals);//allows for n-time traversal on average
            stepRatio = currentS;   
            
            
            //todo: inversion of control (hand over to builder)
            sampler = new ProgressiveRandomWalkSampler(problem, stepCount, stepRatio);
            Walk[] walks = sampler.sample();

            neutrality[currentSimulation][i] = neutralityMeasure.measure(walks, config.entries.get("epsilon"));
            avgNeutrality[i] = calculateNewAverage(avgNeutrality[i], neutrality[currentSimulation][i], currentSimulation);

            currentS += stepS;
        }

    }

    @Override
    protected void finalise() throws Exception {
        Results.writeToFile(path, name + "_Neutrality", neutrality);

        NumberFormat decFormat = new DecimalFormat("#0.000");

        //plot 3 examples
        for (int i = 0; i <= numSs; i += (int)(numSs/2)) {
            
            double currentS = minS + (i == 0 ? 0 : i -1) * stepS;

            stepCount = (int) ((1 / currentS) * 2 * avgNumTraversals);//allows for n-time traversal on average
            stepRatio = currentS;

            //todo: inversion of control (hand over to builder)
            //todo: remove state
            sampler = new ProgressiveRandomWalkSampler(problem, stepCount, stepRatio);
            Walk[] walks = sampler.sample();

//            //graph of problem
//            Results.newGraph(this, path, problem.getName() + " " + "maxStepSize" + " = " + decFormat.format(currentS), "x", "f(x)", null, 2);
//            Results.addPlot(this, null, problem);
//            Results.plot(this);

            //graph of problem - showing sample
            Results.newGraph(this, path, problem.getName() + " " + "maxStepSize" + " = " + decFormat.format(currentS) + " Sampled", "x", "f(x)", null, 2);
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

    protected final double[][] neutrality;
    protected int stepCount;
    protected double stepRatio;
    protected final int avgNumTraversals;
    protected final double minS;
    protected final double stepS;
    protected final int numSs;
    protected final double[] avgNeutrality;
    protected final double[] sValues;
}
