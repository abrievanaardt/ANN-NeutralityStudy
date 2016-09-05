package ac.up.cos700.neutralitystudy.experiment;

import ac.up.cos700.neutralitystudy.data.Dataset;
import ac.up.cos700.neutralitystudy.data.Pattern;
import ac.up.cos700.neutralitystudy.data.Results;
import ac.up.cos700.neutralitystudy.data.util.IncorrectFileFormatException;
import ac.up.cos700.neutralitystudy.data.util.StudyLogFormatter;
import ac.up.cos700.neutralitystudy.data.util.TrainingTestingTuple;
import ac.up.cos700.neutralitystudy.function.Identity;
import ac.up.cos700.neutralitystudy.function.Sigmoid;
import ac.up.cos700.neutralitystudy.function.util.NotAFunctionException;
import ac.up.cos700.neutralitystudy.function.util.UnequalArgsDimensionException;
import ac.up.cos700.neutralitystudy.neuralnet.IFFNeuralNet;
import ac.up.cos700.neutralitystudy.neuralnet.metric.ClassificationAccuracy;
import ac.up.cos700.neutralitystudy.neuralnet.metric.DefaultNetworkError;
import ac.up.cos700.neutralitystudy.neuralnet.training.BackPropagation;
import ac.up.cos700.neutralitystudy.neuralnet.util.FFNeuralNetBuilder;
import ac.up.cos700.neutralitystudy.neuralnet.util.ThresholdOutOfBoundsException;
import ac.up.cos700.neutralitystudy.neuralnet.util.UnequalInputWeightException;
import ac.up.cos700.neutralitystudy.neuralnet.util.ZeroNeuronException;
import java.io.IOException;
import java.util.Iterator;
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
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            setupLogging();
        }
        catch (IOException e) {

        }

        Logger
                .getLogger(Study.class.getName())
                .log(Level.INFO, "Configuring experiment.");

        expUsingAllInputs();
        
    }

    private static void expUsingAllInputs() {

        Logger
                .getLogger(Study.class.getName())
                .log(Level.INFO, "============ Using All Inputs ============");

        String expName = "Exp_All_Inputs";
        StudyConfig config;
        BackPropagation backPropagation;

        try {
            config = StudyConfig.fromFile(expName);

            double[] trainingErrorHistory = new double[config.maxEpoch];
            double[] validationErrorHistory = new double[config.maxEpoch];

            Logger
                    .getLogger(Study.class.getName())
                    .log(Level.INFO, "Doing {0} simulation(s)", config.simulations);

            for (int i = 1; i <= config.simulations; i++) {

                Logger
                        .getLogger(Study.class.getName())
                        .log(Level.INFO, "Starting simulation {0}.", i);

                TrainingTestingTuple trainingValidationSets = Dataset
                        .fromFile("ac/up/cos711/digitrecognitionstudy/data/train")
                        .split(0.8);

                Dataset trainingset = trainingValidationSets.training;
                Dataset validationset = trainingValidationSets.testing;
                Dataset generalisationset = Dataset.fromFile("ac/up/cos711/digitrecognitionstudy/data/t10k");

                IFFNeuralNet network = new FFNeuralNetBuilder()
                        .addLayer(trainingset.getInputCount(), Identity.class)
                        .addLayer(config.hiddenUnits, Sigmoid.class)
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
                double generalisationError = new DefaultNetworkError().measure(network, generalisationset);
                //todo: classificationAccuracy is measured on the generalisation set, check correctness
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
                }

                Logger.getLogger(Study.class.getName()).log(Level.INFO,
                        "NN classification accuracy is {0}%", classificationAccuracy);
            }

            //get average errors for all simulations
            for (int j = 0; j < trainingErrorHistory.length; j++) {
                trainingErrorHistory[j] /= config.simulations;
                validationErrorHistory[j] /= config.simulations;
            }
            
            Results.writeToFile(expName, "E_t_vs_Epoch", trainingErrorHistory);
            Results.writeToFile(expName, "E_v_vs_Epoch", validationErrorHistory);

        }
        catch (IOException | IncorrectFileFormatException | NotAFunctionException | ZeroNeuronException | UnequalInputWeightException | UnequalArgsDimensionException | ThresholdOutOfBoundsException ex) {
            Logger.getLogger(Study.class.getName()).log(Level.SEVERE, "", ex);
        }
    }

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
