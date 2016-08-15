package ac.up.cos700.neutralitystudy.neuralnet;

import ac.up.cos700.neutralitystudy.function.util.UnequalArgsDimensionException;

/**
 * Interface to a fully connected feed forward neural network for classification
 *
 * @author Abrie van Aardt
 */
public interface IFFNeuralNet {

    /**
     * Computes class probabilities for the given input pattern (classifies the
     * data)
     *
     * @param inputPattern
     * @return array of class probabilities
     */
    public double[] classify(double... inputPattern);

    /**
     * Return a copy of the weights in the network in order of layer occurrence
     *
     * @return weightVector
     */
    public double[] getWeightVector();

    /**
     * Sets the weightVector to a copy of _weightVector
     *
     * @param _weightVector
     * @throws UnequalArgsDimensionException
     */
    public void setWeightVector(double... _weightVector)
            throws UnequalArgsDimensionException;

    /**
     * Gets the number of weights in the entire network, including weight biases
     *
     * @return network dimensionality
     */
    public int getDimensionality();

    /**
     * Acquires a live reference to the underlying neurons in the network. This
     * facilitates learning algorithms such as {@link BackPropogation} which
     * requires knowledge of the network topology.
     *
     * @return
     */
    public Neuron[][] getNetworkLayers();
}
