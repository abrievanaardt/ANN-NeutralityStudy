package ac.up.cos700.neutralitystudy.experiment;

import ac.up.cos700.neutralitystudy.data.Dataset;
import ac.up.cos700.neutralitystudy.data.Pattern;
import ac.up.cos700.neutralitystudy.data.util.IncorrectFileFormatException;
import ac.up.cos700.neutralitystudy.data.util.TrainingTestingTuple;
import ac.up.cos700.neutralitystudy.function.Identity;
import ac.up.cos700.neutralitystudy.function.Sigmoid;
import ac.up.cos700.neutralitystudy.function.util.NotAFunctionException;
import ac.up.cos700.neutralitystudy.function.util.UnequalArgsDimensionException;
import ac.up.cos700.neutralitystudy.neuralnet.IFFNeuralNet;
import ac.up.cos700.neutralitystudy.neuralnet.training.BackPropogation;
import ac.up.cos700.neutralitystudy.neuralnet.util.FFNeuralNetBuilder;
import ac.up.cos700.neutralitystudy.neuralnet.util.UnequalInputWeightException;
import ac.up.cos700.neutralitystudy.neuralnet.util.ZeroNeuronException;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Abrie van Aardt
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            setupLogging();

            Dataset dataset = Dataset.fromFile("ac/up/cos700/neutralitystudy/data/cancer.nsds");

            TrainingTestingTuple datasets = dataset.split(0.8);

            IFFNeuralNet network = new FFNeuralNetBuilder()
                    .addLayer(dataset.getInputCount(), Identity.class)
                    .addLayer(6, Sigmoid.class)
                    .addLayer(dataset.getTargetCount(), Sigmoid.class)
                    .build();

            new BackPropogation(0.05, 0.001).train(network, datasets.training);

            Iterator<Pattern> testIt = datasets.testing.iterator();
            while (testIt.hasNext()) {
                Pattern p = testIt.next();
                double[] outputs = network.classify(p.getInputs());
                System.out.println("===== Pattern ====");
                for (int i = 0; i < outputs.length; i++) {
                    System.out.print(String.format("%.3f", outputs[i]) + "\t");
                }
                System.out.println();
                for (int i = 0; i < outputs.length; i++) {
                    System.out.print(String.format("%.3f", p.getTargets()[i]) + "\t");
                }
                System.out.println();
                System.out.println();
                System.out.println();
            }

        }
        catch (IOException | IncorrectFileFormatException | NotAFunctionException | ZeroNeuronException | UnequalInputWeightException | UnequalArgsDimensionException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void setupLogging() throws IOException {
        Logger.getLogger(Test.class.getName()).setLevel(Level.CONFIG);
        Logger logger = Logger.getLogger("");
        FileHandler fileHandler = new FileHandler("study-log-%u.txt", true);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.setLevel(Level.ALL);
        logger.getHandlers()[0].setLevel(Level.CONFIG);
        logger.getHandlers()[1].setLevel(Level.ALL);
    }

}
