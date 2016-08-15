package ac.up.cos700.neutralitystudy.neuralnet;

import ac.up.cos700.neutralitystudy.function.util.UnequalArgsDimensionException;
import ac.up.cos700.neutralitystudy.neuralnet.util.FFNeuralNetConfig;

/**
 *
 * @author Abrie van Aardt
 */
public class FFNeuralNet implements IFFNeuralNet {

    private Neuron[][] layers;

    public FFNeuralNet(FFNeuralNetConfig config) {
        layers = new Neuron[config.layers.size()][];

        for (int i = 0; i < layers.length; i++) {
            Neuron[] neurons = new Neuron[config.layers.get(i).neuronCount];
            for (int j = 0; j < neurons.length; j++) {
                layers[i][j] = new Neuron();
                layers[i][j].setActivationFunction(config.layers.get(i).activationFunction);
                layers[i][j].setWeightCount(config.layers.get(i).weightCountPerNeuron);
            }
        }
    }

    @Override
    public double[] classify(double... inputPattern) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getDimensionality() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double[] getWeightVector() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setWeightVector(double... _weightVector) throws UnequalArgsDimensionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Neuron[][] getNetworkLayers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
