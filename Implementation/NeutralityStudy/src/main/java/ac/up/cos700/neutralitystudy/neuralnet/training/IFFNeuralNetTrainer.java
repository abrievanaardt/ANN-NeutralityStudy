package ac.up.cos700.neutralitystudy.neuralnet.training;

import ac.up.cos700.neutralitystudy.neuralnet.IFFNeuralNet;

/**
 * Interface to the functionality to train a fully connected feed forward
 * neural network. Search/training algorithms intended to be used to search
 * weight space for minimal error must implement this interface.
 * 
 * @author Abrie van Aardt
 */
public interface IFFNeuralNetTrainer{
    public void train(IFFNeuralNet network);
}
