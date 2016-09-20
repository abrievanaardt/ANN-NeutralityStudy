package ac.up.cos700.neutralitystudy.experiment;

import ac.up.malan.phd.problem.Table;
import ac.up.cos700.neutralitystudy.data.Dataset;
import ac.up.cos700.neutralitystudy.data.Results;
import ac.up.cos700.neutralitystudy.data.util.IncorrectFileFormatException;
import ac.up.cos700.neutralitystudy.data.util.ResultsException;
import ac.up.cos700.neutralitystudy.data.util.StudyLogFormatter;
import ac.up.cos700.neutralitystudy.experiment.util.StudyConfigException;
import ac.up.cos700.neutralitystudy.function.Identity;
import ac.up.cos700.neutralitystudy.function.problem.*;
import ac.up.cos700.neutralitystudy.function.Sigmoid;
import ac.up.cos700.neutralitystudy.function.*;
import ac.up.cos700.neutralitystudy.function.util.NotAFunctionException;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import ac.up.cos700.neutralitystudy.neuralnet.IFFNeuralNet;
import ac.up.cos700.neutralitystudy.neuralnet.metric.ClassificationAccuracy;
import ac.up.cos700.neutralitystudy.neuralnet.metric.DefaultNetworkError;
import ac.up.cos700.neutralitystudy.neuralnet.training.BackPropagation;
import ac.up.cos700.neutralitystudy.neuralnet.util.FFNeuralNetBuilder;
import ac.up.cos700.neutralitystudy.neuralnet.util.ThresholdOutOfBoundsException;
import ac.up.cos700.neutralitystudy.neuralnet.util.ZeroNeuronException;
import ac.up.malan.phd.problem.*;
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
public class Study {

    /**
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            setupLogging();
        }
        catch (IOException e) {
            Logger.getLogger(Study.class.getName()).log(Level.SEVERE, "", e);
        }

        Logger
                .getLogger(Study.class.getName())
                .log(Level.INFO, "Configuring experiment.");

        test();

    }

    private static void test() {

        String expName = "Test";
        StudyConfig config;
        BackPropagation backPropagation;

        try {
            config = StudyConfig.fromFile(expName);

            double[] trainingErrorHistory = new double[config.maxEpoch];
            double[] validationErrorHistory = new double[config.maxEpoch];
            double[] trainingAccHistory = new double[config.maxEpoch];
            double[] validationAccHistory = new double[config.maxEpoch];

            Logger
                    .getLogger(Study.class.getName())
                    .log(Level.INFO, "Doing {0} simulation(s)", config.simulations);

            for (int i = 1; i <= config.simulations; i++) {

                Logger
                        .getLogger(Study.class.getName())
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
                        config.acceptableTrainingError,
                        config.learningRate,
                        config.binSize,
                        config.classificationRigor,
                        config.maxEpoch);

                backPropagation.train(network, trainingset, validationset);

                //consolidate results
                double trainingError = backPropagation.getTrainingError();
                double validationError = backPropagation.getValidationError();
                double[] tempTrainingErrorHistory = backPropagation.getTrainingErrorHistory();
                double[] tempValidationErrorHistory = backPropagation.getValidationErorrHistory();
                double[] tempTrainingAccHistory = backPropagation.getTrainingAccHistory();
                double[] tempValidationAccHistory = backPropagation.getValidationAccHistory();

                double generalisationError = new DefaultNetworkError().measure(network, generalisationset);
                double classificationAccuracy = new ClassificationAccuracy(config.classificationRigor).measure(network, generalisationset);

                //send results to disk
                Results.writeToFile(expName, "E_t", trainingError);
                Results.writeToFile(expName, "E_v", validationError);
                Results.writeToFile(expName, "E_g", generalisationError);
                Results.writeToFile(expName, "A_c", classificationAccuracy);
                Results.writeToFile(expName, "Weights", network.getWeightVector());

                //add training errors for this simulation
                for (int j = 0; j < trainingErrorHistory.length; j++) {
                    trainingErrorHistory[j] += tempTrainingErrorHistory[j];
                    validationErrorHistory[j] += tempValidationErrorHistory[j];
                    trainingAccHistory[j] += tempTrainingAccHistory[j];
                    validationAccHistory[j] += tempValidationAccHistory[j];
                }

                Logger.getLogger(Study.class.getName()).log(Level.INFO,
                        "NN classification accuracy is {0}%", classificationAccuracy);
            }

            //get average errors for all simulations
            for (int j = 0; j < trainingErrorHistory.length; j++) {
                trainingErrorHistory[j] /= config.simulations;
                validationErrorHistory[j] /= config.simulations;
                trainingAccHistory[j] /= config.simulations;
                validationAccHistory[j] /= config.simulations;
            }

            Results.writeToFile(expName, "E_vs_Epoch", trainingErrorHistory);
            Results.writeToFile(expName, "E_vs_Epoch", validationErrorHistory);
            Results.writeToFile(expName, "A_vs_Epoch", trainingAccHistory);
            Results.writeToFile(expName, "A_vs_Epoch", validationAccHistory);
            
            Results.newGraph(expName, "Jagged", "x", "y", "z", 3);
//            Results.addPlot("Quantised", new Quantiser(new TestFunction(), 6, -5, +5));
            Results.addPlot("Table", new Griewank(-10, 10, 2));
            Results.plot();
        }
        catch (ResultsException | FileNotFoundException | StudyConfigException | IncorrectFileFormatException | NotAFunctionException | ZeroNeuronException | UnequalArgsDimensionException | ThresholdOutOfBoundsException e) {
            Logger.getLogger(Study.class.getName()).log(Level.SEVERE, "", e);
        }
    }

    //todo: this should ideally be done with a config file or by specifying a class
    private static void setupLogging() throws IOException {
        Formatter logFormatter = new StudyLogFormatter();
        Logger.getLogger(Study.class.getName()).setLevel(Level.CONFIG);
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