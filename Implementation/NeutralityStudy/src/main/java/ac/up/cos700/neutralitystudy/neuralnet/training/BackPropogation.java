package ac.up.cos700.neutralitystudy.neuralnet.training;

import ac.up.cos700.neutralitystudy.neuralnet.IFFNeuralNet;
import java.util.Random;

/**
 *
 * @author Abrie van Aardt
 */
public class BackPropogation implements IFFNeuralNetTrainer {

    @Override
    public void train(IFFNeuralNet network) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void initialise(IFFNeuralNet network){
        
    }
    
    private Random seed = new Random(System.nanoTime());

}
