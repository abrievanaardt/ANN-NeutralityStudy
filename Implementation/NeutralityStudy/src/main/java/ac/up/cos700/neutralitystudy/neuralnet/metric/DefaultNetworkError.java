package ac.up.cos700.neutralitystudy.neuralnet.metric;

import ac.up.cos700.neutralitystudy.data.Dataset;
import ac.up.cos700.neutralitystudy.data.Pattern;
import ac.up.cos700.neutralitystudy.function.IFunction;
import ac.up.cos700.neutralitystudy.function.SquaredError;
import ac.up.cos700.neutralitystudy.function.util.UnequalArgsDimensionException;
import ac.up.cos700.neutralitystudy.neuralnet.IFFNeuralNet;
import ac.up.cos700.neutralitystudy.neuralnet.util.UnequalInputWeightException;
import ac.up.cos700.neutralitystudy.neuralnet.metric.INetworkError;
import java.util.Iterator;

/**
 * Network error that is used for Validation and Generalisation Tests. This
 * class implements the Mean{@link SquaredError} function, with respect to
 * {@link Sigmoid}, over the total patterns in the dataset.
 *
 * @author Abrie van Aardt
 */
public class DefaultNetworkError implements ac.up.cos700.neutralitystudy.neuralnet.metric.INetworkError {

    @Override
    public double measure(IFFNeuralNet network, Dataset testingSet)
            throws UnequalInputWeightException, UnequalArgsDimensionException {
        double error = 0;

        Iterator<Pattern> patterns = testingSet.iterator();
        double[] outputs = new double[1];
        double[] targets = new double[1];
        
        while (patterns.hasNext()) {

            Pattern p = patterns.next();
            outputs = network.classify(p.getInputs());
            targets = p.getTargets();
            error += errorForPattern(targets, outputs);      
        }

        error /= (testingSet.size() * outputs.length);
        
        return error;
    }

    public static double errorForPattern(double[] targets, double[] outputs) throws UnequalArgsDimensionException {
        double sum = 0.0;
        for (int i = 0; i < outputs.length; i++) {
            sum += outputError.evaluate(targets[i], outputs[i]);
        }
        return sum;
    }    
    
    private static final IFunction outputError = new SquaredError();

}
