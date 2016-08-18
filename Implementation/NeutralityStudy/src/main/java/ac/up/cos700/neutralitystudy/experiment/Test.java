/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ac.up.cos700.neutralitystudy.experiment;

import ac.up.cos700.neutralitystudy.function.Identity;
import ac.up.cos700.neutralitystudy.function.Sigmoid;
import ac.up.cos700.neutralitystudy.neuralnet.IFFNeuralNet;
import ac.up.cos700.neutralitystudy.neuralnet.util.FFNeuralNetBuilder;

/**
 *
 * @author Abrie van Aardt
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        IFFNeuralNet network = new FFNeuralNetBuilder()
                .addLayer(3, Identity.class)
                .addLayer(6, Sigmoid.class)
                .addLayer(2, Sigmoid.class)
                .build();
        
        double[] weights = new double[network.getDimensionality()];
        for (int i = 0; i < network.getDimensionality(); i++) {
            weights[i] = Math.random();
        }
        
        network.setWeightVector(weights);

        double[] classificaionResult = network.classify(new double[]{0.256,0.8975,0.98755});
        
        for (double d : classificaionResult) {
            System.out.println(d + ", ");
        }
    }

}
