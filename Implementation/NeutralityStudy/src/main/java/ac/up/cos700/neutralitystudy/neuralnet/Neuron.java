package ac.up.cos700.neutralitystudy.neuralnet;

import ac.up.cos700.neutralitystudy.function.IFunction;
import ac.up.cos700.neutralitystudy.neuralnet.util.*;

/**
 *
 * @author Abrie van Aardt
 */
public class Neuron {

    public double feed(double... inputVector) throws UnequalInputWeightException {
        if (inputVector.length != weightVector.length - 1) {
            throw new UnequalInputWeightException();
        }

        double netInput = netInputFunction.evaluate(inputVector);

        return activationFunction.
                evaluate(netInput - weightVector[weightVector.length - 1]);
    }

    private double[] weightVector;//the last position in the array is used to store the bias
    private IFunction activationFunction;
    private IFunction netInputFunction;

}
