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
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure1;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure2;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure3;
import ac.up.cos700.neutralitystudy.sampling.ProgressiveRandomWalkSampler;
import ac.up.malan.phd.sampling.Walk;
import ac.up.malan.phd.sampling.util.SampleException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;

/**
 * This is where my experiments are configured.
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
           
            
            //======================== Measure 1 =============================
//            new Study_1D_Simple().setup(new NeutralityMeasure1()).run();
//            new Study_1D_Tunable_Q().setup(new NeutralityMeasure1()).run();    
//            new Study_1D_Tunable_S().setup(new NeutralityMeasure1()).run();    
//            
//            new Study_2D_Simple().setup(new NeutralityMeasure1()).run();
//            new Study_2D_Tunable_Q().setup(new NeutralityMeasure1()).run();            
//            new Study_2D_Tunable_S().setup(new NeutralityMeasure1()).run();            
//            
//            new Study_ND_Tunable_D().setup(new NeutralityMeasure1()).run();

            new Study_NN_Error_Simple().setup(new NeutralityMeasure1()).run();

            

            //======================== Measure 2 =============================
//            new Study_1D_Simple().setup(new NeutralityMeasure2()).run();
//            new Study_1D_Tunable_Q().setup(new NeutralityMeasure2()).run();    
//            new Study_1D_Tunable_S().setup(new NeutralityMeasure2()).run();    
//            
//            new Study_2D_Simple().setup(new NeutralityMeasure2()).run();
//            new Study_2D_Tunable_Q().setup(new NeutralityMeasure2()).run();            
//            new Study_2D_Tunable_S().setup(new NeutralityMeasure2()).run();            
//            
//            new Study_ND_Tunable_D().setup(new NeutralityMeasure2()).run();

            new Study_NN_Error_Simple().setup(new NeutralityMeasure2()).run();
            
            

            //======================== Measure 3 =============================            
//            new Study_1D_Simple().setup(new NeutralityMeasure3()).run();
//            new Study_1D_Tunable_Q().setup(new NeutralityMeasure3()).run();    
//            new Study_1D_Tunable_S().setup(new NeutralityMeasure3()).run();    
//            
//            new Study_2D_Simple().setup(new NeutralityMeasure3()).run();
//            new Study_2D_Tunable_Q().setup(new NeutralityMeasure3()).run();            
//            new Study_2D_Tunable_S().setup(new NeutralityMeasure3()).run();            
//            
//            new Study_ND_Tunable_D().setup(new NeutralityMeasure3()).run();

            new Study_NN_Error_Simple().setup(new NeutralityMeasure3()).run();
            
            Study.awaitStudies();
        
        }
        catch (IOException | StudyConfigException | InterruptedException e) {
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

//            Results.newGraph(expPath, "Jagged", "x", "y", "z", problem.getDimensionality() + 1);
//            Results.addPlot("Test", problem);
//
//            for (int i = 0; i < walks.length; i++) {
//                Results.addPlot("Walk " + (i + 1), walks[i].getPoints(), walks[i].getPointsFitness(), "linespoints");
//            }
//
//            Results.plot();
        }
        catch (SampleException | ResultsException | FileNotFoundException | StudyConfigException | IncorrectFileFormatException | NotAFunctionException | ZeroNeuronException | UnequalArgsDimensionException | ThresholdOutOfBoundsException e) {
            Logger.getLogger(StudyRunner.class.getName()).log(Level.SEVERE, "", e);
        }
    }

    //todo: this should ideally be done with a config file or by specifying a class
    private static void setupLogging() throws IOException {
        Formatter logFormatter = new StudyLogFormatter();
        Logger.getLogger(StudyRunner.class.getName()).setLevel(Level.CONFIG);
        Logger.getLogger(ExecutorService.class.getName()).setLevel(Level.OFF);
        Logger logger = Logger.getLogger("");
        FileHandler logFileHandler = new FileHandler("log\\study.log", true);
        logFileHandler.setFormatter(logFormatter);
        logger.addHandler(logFileHandler);
        logger.setLevel(Level.ALL);
        logger.getHandlers()[0].setFormatter(logFormatter);
        logger.getHandlers()[0].setLevel(Level.ALL);//console output
        logger.getHandlers()[1].setLevel(Level.CONFIG);//normal log file
    }

}
