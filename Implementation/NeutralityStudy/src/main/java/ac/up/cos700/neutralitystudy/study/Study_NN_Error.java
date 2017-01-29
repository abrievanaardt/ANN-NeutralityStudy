package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.data.Dataset;
import ac.up.cos700.neutralitystudy.data.util.IncorrectFileFormatException;
import ac.up.cos700.neutralitystudy.function.Identity;
import ac.up.cos700.neutralitystudy.function.Sigmoid;
import ac.up.cos700.neutralitystudy.function.Tanh;
import ac.up.cos700.neutralitystudy.function.problem.NetworkError;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.function.util.NotAFunctionException;
import ac.up.cos700.neutralitystudy.neuralnet.IFFNeuralNet;
import ac.up.cos700.neutralitystudy.neuralnet.metric.DefaultNetworkError;
import ac.up.cos700.neutralitystudy.neuralnet.util.FFNeuralNetBuilder;
import ac.up.cos700.neutralitystudy.neuralnet.util.ZeroNeuronException;
import ac.up.cos700.neutralitystudy.study.util.StudyConfigException;
import java.io.FileNotFoundException;

/**
 *
 * @author Abrie van Aardt
 */
public class Study_NN_Error extends Study {

    public Study_NN_Error() throws StudyConfigException {
        super();

        double absoluteDomain = config.entries.get("absDomain");

        try {

            String[] datasetNames = new String[]{
                "cancer",
                  "xor",
//                  "and",
                "diabetes",
                "glass",
                "heart",
                "iris"
            };

            problems = new RealProblem[datasetNames.length];

            for (int i = 0; i < problems.length; i++) {
                Dataset dataset = Dataset
                        .fromFile(PATH_PREFIX
                                + Tanh.class.getSimpleName().toLowerCase()
                                + "/" + datasetNames[i]                                
                                + EXT);

                dataset.setDatasetName(datasetNames[i]);

                IFFNeuralNet network = new FFNeuralNetBuilder()
                        .addLayer(dataset.getInputCount(), Identity.class)
                        .addLayer(dataset.getHiddenCount(), Sigmoid.class)
                        .addLayer(dataset.getTargetCount(), Sigmoid.class)
                        .build();

                problems[i] = new NetworkError(network, dataset, new DefaultNetworkError(), -absoluteDomain, absoluteDomain);
            }
        }
        catch (FileNotFoundException | IncorrectFileFormatException | NotAFunctionException | ZeroNeuronException e) {
            throw new StudyConfigException(e.getMessage());
        }
    }

    private static final String PATH_PREFIX = "ac/up/cos700/neutralitystudy/data/";
    private static final String EXT = ".nsds";

}
