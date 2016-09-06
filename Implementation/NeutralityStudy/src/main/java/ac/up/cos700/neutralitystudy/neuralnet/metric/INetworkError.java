package ac.up.cos700.neutralitystudy.neuralnet.metric;

import ac.up.cos700.neutralitystudy.data.Dataset;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import ac.up.cos700.neutralitystudy.neuralnet.IFFNeuralNet;

/**
 *
 * @author Abrie van Aardt
 */
public interface INetworkError {
    public double measure(IFFNeuralNet network, Dataset testingSet) throws UnequalArgsDimensionException;
}
