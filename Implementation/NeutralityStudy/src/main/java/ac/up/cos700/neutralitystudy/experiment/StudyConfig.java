package ac.up.cos700.neutralitystudy.experiment;

import ac.up.cos700.neutralitystudy.experiment.util.StudyConfigException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Class representing the configuration for an experiment.
 *
 * @author Abrie van Aardt
 */
public class StudyConfig {

    // NN Configuration
    public double acceptableTrainingError;
    public double learningRate;
    public int binSize;
    public double classificationRigor;
    public int maxEpoch;
    
    // General Experiment Configuration
    public int simulations;
    
    

    public static StudyConfig fromFile(String expName) throws FileNotFoundException, StudyConfigException {
        StudyConfig config = new StudyConfig();
        Scanner fileScanner = new Scanner(new File("study.config"));
        fileScanner.useDelimiter("\\r|\\r\\n|\\n|\\s=\\s");

        if (fileScanner.findWithinHorizon(expName, 1000) == null)
            throw new StudyConfigException("Could not find an entry for this experiment.");
        
        fileScanner.next();              

        while (fileScanner.hasNext()) {
            String next = fileScanner.next();            

            //skip the first line (the name of the experiment)
            if (next.length() > 0 && next.charAt(0) == '/') {
                break;
            }

            switch (next) {
                case "acceptableTrainingError":
                    config.acceptableTrainingError = fileScanner.nextDouble();
                    break;
                case "learningRate":
                    config.learningRate = fileScanner.nextDouble();
                    break;
                case "binSize":
                    config.binSize = fileScanner.nextInt();
                    break;
                case "classificationRigor":
                    config.classificationRigor = fileScanner.nextDouble();
                    break;
                case "maxEpoch":
                    config.maxEpoch = fileScanner.nextInt();
                    break;
                case "simulations":
                    config.simulations = fileScanner.nextInt();
                    break;
            }
        }

        return config;
    }
}
