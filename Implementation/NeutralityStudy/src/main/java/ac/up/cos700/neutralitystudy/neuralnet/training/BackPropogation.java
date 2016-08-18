package ac.up.cos700.neutralitystudy.neuralnet.training;

import ac.up.cos700.neutralitystudy.neuralnet.IFFNeuralNet;
import ac.up.cos700.neutralitystudy.neuralnet.Neuron;
import java.util.Random;

/**
 *
 * @author Abrie van Aardt
 */
public class BackPropogation implements IFFNeuralNetTrainer {

    @Override
    public void train(IFFNeuralNet network) {
        initialise(network);
        double networkError;
        
        do{
            
        } while (networkError <= errorDelta);
        
        
    }

    private void initialise(IFFNeuralNet network) {
        Neuron[][] layers = network.getNetworkLayers();
        for (int i = 0; i < layers.length; i++) {
            for (int j = 0; j < layers[i].length; j++) {
                int fanin = layers[i][j].getWeightCount();
                double range = 1.0 / Math.sqrt(fanin);
                for (int k = 0; k < fanin; k++) {
                    layers[i][j].setWeight(k,
                            rand.nextDouble() * 2 * range - range);
                }
            }
        }
    }

    private Random rand = new Random(System.nanoTime());
    private double errorDelta = 0.0001;
}
