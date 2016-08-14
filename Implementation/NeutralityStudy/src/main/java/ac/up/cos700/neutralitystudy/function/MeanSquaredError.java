package ac.up.cos700.neutralitystudy.function;

import ac.up.cos700.neutralitystudy.neuralnet.FFNeuralNet;

/**
 * This class provides functionality to evaluate neural network classification
 * error given a weight vector. The network and accompanying dataset is
 * injected.
 *
 * @author Abrie van Aardt
 */
public class MeanSquaredError implements IFunction {

    private final FFNeuralNet network;

    public MeanSquaredError(FFNeuralNet _network) {
        network = _network;
    }

    @Override
    public int getDimensionCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double evaluate(double... x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
