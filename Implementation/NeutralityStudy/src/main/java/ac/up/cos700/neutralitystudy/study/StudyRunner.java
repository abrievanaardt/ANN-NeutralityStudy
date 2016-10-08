package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.study.util.StudyConfig;
import ac.up.cos700.neutralitystudy.data.Dataset;
import ac.up.cos700.neutralitystudy.data.Results;
import ac.up.cos700.neutralitystudy.data.util.IncorrectFileFormatException;
import ac.up.cos700.neutralitystudy.data.util.ResultsException;
import ac.up.cos700.neutralitystudy.data.util.StudyLogFormatter;
import ac.up.cos700.neutralitystudy.study.util.StudyConfigException;
import ac.up.cos700.neutralitystudy.function.Identity;
import ac.up.cos700.neutralitystudy.function.problem.*;
import ac.up.cos700.neutralitystudy.function.Sigmoid;
import ac.up.cos700.neutralitystudy.function.TestFunction;
import ac.up.cos700.neutralitystudy.function.util.NotAFunctionException;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import ac.up.cos700.neutralitystudy.neuralnet.IFFNeuralNet;
import ac.up.cos700.neutralitystudy.neuralnet.metric.ClassificationAccuracy;
import ac.up.cos700.neutralitystudy.neuralnet.metric.DefaultNetworkError;
import ac.up.cos700.neutralitystudy.neuralnet.training.BackPropagation;
import ac.up.cos700.neutralitystudy.neuralnet.util.FFNeuralNetBuilder;
import ac.up.cos700.neutralitystudy.neuralnet.util.ThresholdOutOfBoundsException;
import ac.up.cos700.neutralitystudy.neuralnet.util.ZeroNeuronException;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure1;
import ac.up.malan.phd.problem.*;
import ac.up.cos700.neutralitystudy.sampling.ProgressiveRandomWalkSampler;
import ac.up.malan.phd.sampling.Walk;
import ac.up.malan.phd.sampling.util.SampleException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is where my experiment is configured.
 *
 * @author Abrie van Aardt
 */
public class StudyRunner {

    /**
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            setupLogging();
            
            //Run Studies/Experiments
            //Measure1 - proportion of neutral objects
            new Study_M1_1D_Simple().run();
            new Study_M1_1D_Tunable_Q().run();            
            
            new Study_M1_2D_Simple().run();
            new Study_M1_2D_Tunable_Q().run();
            
            //n-dimensional
            
            //Measure2 - proportion of longest neutral sequence
            new Study_M2_1D_Simple().run();
            new Study_M2_1D_Tunable_Q().run();            
            
            new Study_M2_2D_Simple().run();
            new Study_M2_2D_Tunable_Q().run();
            
            //n-dimensional
            
            //Measure3 - proportion of longest neutral sequence in 
            //euclidean space
            //todo
            
            
            //also study measures on NNs
        }
        catch (IOException | StudyConfigException e) {
            Logger.getLogger(StudyRunner.class.getName()).log(Level.SEVERE, "", e);
        }       

    }
    
    private static void exp1(String studyName) {
        String expName = "Quantised";
        String expPath = studyName + "\\" + expName;
        StudyConfig config;

        try {
            config = StudyConfig.fromFile(expName);

            double parameter;
            int parameterCount = 200;
            double[] neutralityMeasures = new double[parameterCount];
            double[] qValues = new double[parameterCount];

            Logger
                    .getLogger(StudyRunner.class.getName())
                    .log(Level.INFO, "Doing {0} simulation(s)", config.entries.get("simulations"));

            for (int i = 1; i <= config.entries.get("simulations"); i++) {

                Logger
                        .getLogger(StudyRunner.class.getName())
                        .log(Level.INFO, "Starting simulation {0}.", i);

                parameter = 0.1;

                for (int j = 0; j < parameterCount; j++) {
                    RealProblem problem = new PlateStacker(parameter, -5, 5, 4);
                    ProgressiveRandomWalkSampler sampler = new ProgressiveRandomWalkSampler(problem, 1000, 0.1);
                    Walk[] walks = sampler.sample();
                    NeutralityMeasure neutrality = new NeutralityMeasure1(0.02);
                    neutralityMeasures[j] = calculateNewAverage(neutralityMeasures[j], neutrality.measure(walks), i);
                    qValues[j] = parameter;
                    parameter += 0.1;
                }
            }

            Results.newGraph(expPath, "Neutrality vs Quantum", "Q", "Neutrality", "", 2);
            Results.addPlot("", qValues, neutralityMeasures, "lines");
            Results.addPlot("Flat", new Flat());
//
//                for (int j = 0; j < walks.length; j++) {
//                    Results.addPlot("Walk " + (j + 1), walks[j].getPoints(), walks[j].getPointsFitness(), "linespoints");
//                }
//
                Results.plot();

        }
        catch (UnequalArgsDimensionException | SampleException | ResultsException | FileNotFoundException | StudyConfigException e) {
            Logger.getLogger(StudyRunner.class.getName()).log(Level.SEVERE, "", e);
        }
    }

    //todo: move this to an experiment class
    private static void NN_Test(String studyName) {

        String expName = "NNTest";
        String expPath = studyName + "\\" + expName;
        StudyConfig config;
        BackPropagation backPropagation;

        try {
            config = StudyConfig.fromFile(expName);

            double[] trainingErrorHistory = new double[config.entries.get("maxEpoch").intValue()];
            double[] validationErrorHistory = new double[config.entries.get("maxEpoch").intValue()];
            double[] trainingAccHistory = new double[config.entries.get("maxEpoch").intValue()];
            double[] validationAccHistory = new double[config.entries.get("maxEpoch").intValue()];

            Logger
                    .getLogger(StudyRunner.class.getName())
                    .log(Level.INFO, "Doing {0} simulation(s)", config.entries.get("simulations"));

            for (int i = 1; i <= config.entries.get("simulations"); i++) {

                Logger
                        .getLogger(StudyRunner.class.getName())
                        .log(Level.INFO, "Starting simulation {0}.", i);

                List<Dataset> datasets = Dataset
                        .fromFile("ac/up/cos700/neutralitystudy/data/iris.nsds")
                        .shuffle()
                        .split(0.6, 0.2, 0.2);

                Dataset trainingset = datasets.get(0);
                Dataset validationset = datasets.get(1);
                Dataset generalisationset = datasets.get(2);

                IFFNeuralNet network = new FFNeuralNetBuilder()
                        .addLayer(trainingset.getInputCount(), Identity.class)
                        .addLayer(trainingset.getHiddenCount(), Sigmoid.class)
                        .addLayer(trainingset.getTargetCount(), Sigmoid.class)
                        .build();

                backPropagation = new BackPropagation(
                        config.entries.get("acceptableTrainingError"),
                        config.entries.get("learningRate"),
                        config.entries.get("binSize").intValue(),
                        config.entries.get("classificationRigor"),
                        config.entries.get("maxEpoch)").intValue());

                backPropagation.train(network, trainingset, validationset);

                //consolidate results
                double trainingError = backPropagation.getTrainingError();
                double validationError = backPropagation.getValidationError();
                double[] tempTrainingErrorHistory = backPropagation.getTrainingErrorHistory();
                double[] tempValidationErrorHistory = backPropagation.getValidationErorrHistory();
                double[] tempTrainingAccHistory = backPropagation.getTrainingAccHistory();
                double[] tempValidationAccHistory = backPropagation.getValidationAccHistory();

                double generalisationError = new DefaultNetworkError().measure(network, generalisationset);
                double classificationAccuracy = new ClassificationAccuracy(config.entries.get("classificationRigor")).measure(network, generalisationset);

                //send results to disk
                Results.writeToFile(expPath, "E_t", trainingError);
                Results.writeToFile(expPath, "E_v", validationError);
                Results.writeToFile(expPath, "E_g", generalisationError);
                Results.writeToFile(expPath, "A_c", classificationAccuracy);
                Results.writeToFile(expPath, "Weights", network.getWeightVector());

                //add training errors for this simulation
                for (int j = 0; j < trainingErrorHistory.length; j++) {
                    trainingErrorHistory[j] += tempTrainingErrorHistory[j];
                    validationErrorHistory[j] += tempValidationErrorHistory[j];
                    trainingAccHistory[j] += tempTrainingAccHistory[j];
                    validationAccHistory[j] += tempValidationAccHistory[j];
                }

                Logger.getLogger(StudyRunner.class.getName()).log(Level.INFO,
                        "NN classification accuracy is {0}%", classificationAccuracy);
            }

            //get average errors for all simulationCount
            for (int j = 0; j < trainingErrorHistory.length; j++) {
                trainingErrorHistory[j] /= config.entries.get("simulations");
                validationErrorHistory[j] /= config.entries.get("simulations");
                trainingAccHistory[j] /= config.entries.get("simulations");
                validationAccHistory[j] /= config.entries.get("simulations");
            }

            Results.writeToFile(expPath, "E_vs_Epoch", trainingErrorHistory);
            Results.writeToFile(expPath, "E_vs_Epoch", validationErrorHistory);
            Results.writeToFile(expPath, "A_vs_Epoch", trainingAccHistory);
            Results.writeToFile(expPath, "A_vs_Epoch", validationAccHistory);

            RealProblem problem = new Quantiser(new TestFunction(), 30, -10, 10);

            ProgressiveRandomWalkSampler sampler = new ProgressiveRandomWalkSampler(problem, 1000, 0.1);
            Walk[] walks = sampler.sample();

            Results.newGraph(expPath, "Jagged", "x", "y", "z", problem.getDimensionality() + 1);
            Results.addPlot("Test", problem);

            for (int i = 0; i < walks.length; i++) {
                Results.addPlot("Walk " + (i + 1), walks[i].getPoints(), walks[i].getPointsFitness(), "linespoints");
            }

            Results.plot();
        }
        catch (SampleException | ResultsException | FileNotFoundException | StudyConfigException | IncorrectFileFormatException | NotAFunctionException | ZeroNeuronException | UnequalArgsDimensionException | ThresholdOutOfBoundsException e) {
            Logger.getLogger(StudyRunner.class.getName()).log(Level.SEVERE, "", e);
        }
    }

    private static double calculateNewAverage(double oldAvg, double newValue, int currentTotal) {
        return (oldAvg * (currentTotal - 1) + newValue) / currentTotal;
    }

    //todo: this should ideally be done with a config file or by specifying a class
    private static void setupLogging() throws IOException {
        Formatter logFormatter = new StudyLogFormatter();
        Logger.getLogger(StudyRunner.class.getName()).setLevel(Level.CONFIG);
        Logger logger = Logger.getLogger("");
        FileHandler logFileHandler = new FileHandler("study.log", true);
        logFileHandler.setFormatter(logFormatter);
        logger.addHandler(logFileHandler);
        logger.setLevel(Level.ALL);
        logger.getHandlers()[0].setFormatter(logFormatter);
        logger.getHandlers()[0].setLevel(Level.ALL);//console output
        logger.getHandlers()[1].setLevel(Level.CONFIG);//normal log file
    }

}
