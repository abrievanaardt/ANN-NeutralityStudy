package ac.up.cos700.neutralitystudy.function.problem;

import ac.up.cos700.neutralitystudy.data.Dataset;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import ac.up.cos700.neutralitystudy.neuralnet.IFFNeuralNet;
import ac.up.cos700.neutralitystudy.neuralnet.metric.INetworkError;

/**
 * This class provides functionality to evaluate neural network classification
 * error given a weight vector. The network and accompanying dataset is
 * injected.
 *
 * @author Abrie van Aardt
 */
public class NetworkError extends RealProblem {

    public NetworkError(IFFNeuralNet _network, Dataset _dataset, INetworkError _networkError, double xmin, double xmax) {        
        super(xmin, xmax, _network.getDimensionality());
        //todo: check if this actually works
        network = _network.clone();//deep copy
        dataset = _dataset;
        networkError = _networkError;        
    }

    /**
     * Evaluates the error of the network (loss function) given all the weights
     * that should be used in the network, together with the dataset.
     *
     * @param x the weight vector
     * @return network error
     * @throws UnequalArgsDimensionException
     */
    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length != network.getDimensionality())
            throw new UnequalArgsDimensionException();

        network.setWeightVector(x);
        return networkError.measure(network, dataset);
    }
    
    @Override
    public String getName(){
        return super.getName() + "_" + dataset.getDatasetName();
    }

    private final IFFNeuralNet network;
    private final Dataset dataset;
    private final INetworkError networkError;

}
