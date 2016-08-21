package ac.up.cos700.neutralitystudy.data;

import ac.up.cos700.neutralitystudy.data.util.IncorrectFileFormatException;
import ac.up.cos700.neutralitystudy.data.util.TrainingTestingTuple;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing a dataset for neural network training. This class is also
 * used by functions deriving from {@link NetworkError} for the purpose of
 * providing sample data to quantify network classification error.
 * <br><br>
 * Files are required to be in the following format:
 * <br>
 * <pre>
 * i=input_count
 * t=target_count
 * [pattern1]
 * ...
 * [patternN]</pre>
 * <br>
 * where every value in [patternK] is separated by a white space.
 *
 * @author Abrie van Aardt
 */
public class Dataset implements Iterable {

    //TODO: check the scale of the data, [root(3), root(3)] for sigmoid
    public static Dataset fromFile(String resourceName)
            throws FileNotFoundException, IncorrectFileFormatException {

        Scanner fileScanner;
        Dataset dataset = new Dataset();
        ClassLoader classLoader = Dataset.class.getClassLoader();
        fileScanner = new Scanner(
                new File(classLoader.getResource(resourceName).getFile()));
        fileScanner.useDelimiter("\\n|\\r\\n|\\r|\\s");

        //try to parse the number of inputs and targets from the file
        try {
            if (fileScanner.findInLine("i") == null) {
                throw new IncorrectFileFormatException();
            }

            dataset.inputCount = Integer.parseInt(fileScanner.findInLine("\\d+"));
            fileScanner.nextLine();

            if (fileScanner.findInLine("t") == null) {
                throw new IncorrectFileFormatException();
            }

            dataset.targetCount = Integer.parseInt(fileScanner.findInLine("\\d+"));
            fileScanner.nextLine();

        }
        catch (NumberFormatException e) {
            throw new IncorrectFileFormatException();
        }

        //demarshal the data patterns
        double[] inputs = new double[dataset.inputCount];
        double[] targets = new double[dataset.targetCount];

        while (fileScanner.hasNextDouble()) {
            for (int i = 0; i < inputs.length; i++) {
                inputs[i] = fileScanner.nextDouble();
            }

            for (int i = 0; i < targets.length; i++) {
                targets[i] = fileScanner.nextDouble();
            }

            Pattern p = new Pattern();
            p.setInputs(inputs);
            p.setTargets(targets);

            dataset.data.add(p);
        }

        Logger logger = Logger.getLogger(Dataset.class.getName());
        logger.log(Level.INFO, "Loaded {3} pattern(s) with {1} input(s) "
                + "and {2} class(es) from dataset: {0}.", new Object[]{
                    resourceName.substring(resourceName.lastIndexOf('/') + 1),
                    dataset.inputCount,
                    dataset.targetCount,
                    dataset.size()
                });

        return dataset;
    }

    @Override
    public Iterator<Pattern> iterator() {
        return data.iterator();
    }

    /**
     * Splits the dataset into a training and testing set. This method provides
     * respective views for the training and testing dataset. Both views are
     * backed by the same underlying dataset. trainingRatio is the portion of
     * the dataset that will be dedicated to training patterns.
     *
     * @param trainingRatio
     * @return TrainingTestingTuple
     */
    public TrainingTestingTuple split(double trainingRatio) {
        Dataset training = new Dataset();
        Dataset testing = new Dataset();

        training.inputCount = inputCount;
        training.targetCount = targetCount;
        testing.inputCount = inputCount;
        testing.targetCount = targetCount;

        int trainingUpperIndex = (int) (trainingRatio * data.size());

        training.data = data.subList(0, trainingUpperIndex);
        testing.data = data.subList(trainingUpperIndex, data.size());

        logger.log(Level.INFO, "Using "
                + String.format("%.2f", trainingRatio * 100)
                + "% of the patterns for training and the remainder for "
                + "testing generalisation.");

        return new TrainingTestingTuple(training, testing);
    }

    public Dataset shuffle() {
        Collections.shuffle(data, random);
        return this;
    }

    public int size() {
        return data.size();
    }

    public int getInputCount() {
        return inputCount;
    }

    public int getTargetCount() {
        return targetCount;
    }

    private List<Pattern> data = new ArrayList<>();
    private int inputCount;
    private int targetCount;
    private Random random = new Random(System.nanoTime());
    private Logger logger = Logger.getLogger(getClass().getName());
}
