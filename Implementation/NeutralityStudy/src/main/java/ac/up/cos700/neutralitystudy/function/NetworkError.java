package ac.up.cos700.neutralitystudy.function;

import ac.up.cos700.neutralitystudy.data.Dataset;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import ac.up.cos700.neutralitystudy.neuralnet.IFFNeuralNet;
import ac.up.cos700.neutralitystudy.neuralnet.metric.DefaultNetworkError;
import ac.up.cos700.neutralitystudy.neuralnet.metric.INetworkError;


/**
 * This class provides functionality to evaluate neural network classification
 * error given a weight vector. The network and accompanying dataset is
 * injected.
 *
 * @author Abrie van Aardt
 */
public class NetworkError implements IFunction{
    private final IFFNeuralNet network;
    private final Dataset dataset;
    private final INetworkError networkError;
    
    public NetworkError(IFFNeuralNet _network, Dataset _dataset, INetworkError _networkError) {
        network = _network.clone();//deep copy
        dataset = _dataset;
        networkError = _networkError;
    }
    
    /**
     * Calculates the number of weights that occur in the network, including 
     * the biases.
     * 
     * @return network weight dimensionality
     */
    @Override
    public int getDimensionality() {
        return network.getDimensionality();
    }

    /**
     * Evaluates the classification error of the network given all the weights
     * that should be used in the network, together with the dataset. 
     * 
     * @param x
     * @return network error 
     * @throws UnequalArgsDimensionException 
     */
    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException{
        if (x.length != network.getDimensionality())
            throw new UnequalArgsDimensionException();
        
        network.setWeightVector(x);
        return networkError.measure(network, dataset);      
    }
    
    
}
