package ac.up.cos700.neutralitystudy.experiment;

import ac.up.cos700.neutralitystudy.data.Results;
import ac.up.cos700.neutralitystudy.function.Function;
import ac.up.cos700.neutralitystudy.function.problem.Quantiser;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.sampling.ProgressiveRandomWalkSampler;
import ac.up.cos700.neutralitystudy.study.util.StudyConfig;
import ac.up.malan.phd.sampling.Walk;

/**
 *
 * @author Abrie van Aardt
 */
public class Experiment_1D_Tunable_Quantised extends Experiment {

    //todo: this class can only be used for the quantiser. Some of the functionality
    //has been generalised. Remove this generalisation
    public Experiment_1D_Tunable_Quantised(StudyConfig _config, NeutralityMeasure _neutralityMeasure, RealProblem _problem) {
        super(_config, _neutralityMeasure, _problem);

        problemName = _problem.getClass().getSimpleName();

        if (!_problem.getUnderlyingFunctionName().equals("")) {
            problemName += "_" + _problem.getUnderlyingFunctionName();
        }

        path += "_" + problemName;

        problem = _problem;

        stepCount = config.entries.get("stepCount").intValue();
        stepRatio = config.entries.get("stepRatio");

        minParameter = config.entries.get("minPar");
        stepParameter = config.entries.get("stepPar");
        numParameters = (int) Math.ceil((config.entries.get("maxPar") - minParameter) / (stepParameter)) + 1;

        neutrality = new double[config.simulationCount + 1][numParameters];
        
        avgNeutrality = new double[numParameters];
        qValues = new double[numParameters];

        //add a row of values to serve as headings: indicating the value of 
        //the parameter at that column. Remember to ignore this row when e.g.
        //plotting.
        for (int i = 0; i < numParameters; i++) {
            neutrality[0][i] = minParameter + i * stepParameter;
        }

    }

    @Override
    protected void runSimulation(int currentSimulation) throws Exception {

        double currentQ = minParameter;
       
        for (int i = 0; i < numParameters; i++) {
            qValues[i] = minParameter + i * stepParameter;
            
            RealProblem quantisedProblem = new Quantiser(problem, currentQ, problem.getLowerBound(), problem.getUpperBound());
            sampler = new ProgressiveRandomWalkSampler(quantisedProblem, stepCount, stepRatio);
            Walk[] walks = sampler.sample();
            
            neutrality[currentSimulation][i] = neutralityMeasure.measure(walks);
            avgNeutrality[i] = calculateNewAverage(avgNeutrality[i], neutrality[currentSimulation][i], currentSimulation);
            
            
            Results.newGraph(path, problemName + "_" + "q" + "_" + currentQ, "x", "f(x)", null, 2);
            Results.addPlot(null, quantisedProblem);
            Results.plot();
            
            
            currentQ += stepParameter;
        }

    }

    @Override
    protected void finalise() throws Exception {                
//        //plot a representive sample
//            Walk[] walks = new ProgressiveRandomWalkSampler(problem, 25, 0.1).sample();
//            Results.newGraph(path, problem.getClass().getSimpleName() + " Sampled", "x", "f(x)", null, 2);
//            Results.addPlot(problem.getClass().getSimpleName(), problem);
//            for (int j = 0; j < walks.length; j++) {
//                Results.addPlot("Walk " + (j + 1), walks[j].getPoints(), walks[j].getPointsFitness(), "linespoints");
//            }
//
//            Results.plot();        
        Results.writeToFile(path, "Neutrality", neutrality);
        
        
        Results.newGraph(path, "Quantised " + problemName +  "Neutrality vs Quantum", "Q", "Neutrality", "", 2);
        Results.addPlot("", qValues, avgNeutrality, "lines");
        Results.plot();

    }

    private double calculateNewAverage(double oldAvg, double newValue, int currentTotal) {
        return (oldAvg * (currentTotal - 1) + newValue) / currentTotal;
    }

    private final double[][] neutrality;
    private final int stepCount;
    private final double stepRatio;
    private ProgressiveRandomWalkSampler sampler;

    private final double minParameter;
    private final double stepParameter;
    private final int numParameters;
    private String problemName;
    private final double[] avgNeutrality;
    private final double[] qValues;
}
