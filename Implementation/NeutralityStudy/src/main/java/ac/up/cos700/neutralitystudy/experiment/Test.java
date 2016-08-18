/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ac.up.cos700.neutralitystudy.experiment;

import ac.up.cos700.neutralitystudy.data.Dataset;
import ac.up.cos700.neutralitystudy.data.Pattern;
import ac.up.cos700.neutralitystudy.data.util.TrainingTestingTuple;
import ac.up.cos700.neutralitystudy.function.Identity;
import ac.up.cos700.neutralitystudy.function.Sigmoid;
import ac.up.cos700.neutralitystudy.neuralnet.IFFNeuralNet;
import ac.up.cos700.neutralitystudy.neuralnet.util.FFNeuralNetBuilder;
import java.util.Iterator;
import javafx.util.Pair;

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
                .addLayer(30, Identity.class)
                .addLayer(6, Sigmoid.class)
                .addLayer(1, Sigmoid.class)
                .build();
        
        double[] weights = new double[network.getDimensionality()];
        for (int i = 0; i < network.getDimensionality(); i++) {
            weights[i] = Math.random();
        }
        
        network.setWeightVector(weights);
        
        Dataset dataset = Dataset.fromFile("ac/up/cos700/neutralitystudy/data/cancer.nsds");
        
        Iterator<Pattern> patternIterator = dataset.iterator();
        
//        while(patternIterator.hasNext()){
//            Pattern p = patternIterator.next();
//                    
//            for (Double x : p.getInputs()) {
//                System.out.print(x + " ");
//            }
//            System.out.println();          
//        }
        

        while(patternIterator.hasNext()){
            double[] classificaionResult = network.classify(patternIterator.next().getInputs());
        
            for (double d : classificaionResult) {
                System.out.println(d + ", ");
            }
        }
        
    }

}
