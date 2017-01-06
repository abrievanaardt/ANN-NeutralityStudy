package ac.up.cos700.neutralitystudy.neutralitymeasure;

import ac.up.malan.phd.sampling.Walk;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the first, basic neutrality measure considered for this study. The
 * measure makes use of a progressive random walk sample and analyses 3-point
 * objects within a number of walks.
 * <br><br> 
 * This measure calculates the proportion of 3-point objects that are neutral.
 *
 * @author Abrie van Aardt
 */
public class NeutralityMeasure1 extends NeutralityMeasure {

    public NeutralityMeasure1(){
        name = "M1";
    }
    
    @Override
    public double measure(Walk[] samples, double epsilon) {

        double totalForSamples = 0;
        
        double minFitness = Double.MAX_VALUE;
        double maxFitness  = Double.MIN_VALUE;
        
        for (int i = 0; i < samples.length; i++) {
            double temp = samples[i].getMinFitness();
            minFitness = temp < minFitness ? temp : minFitness;
            temp = samples[i].getMaxFitness();
            maxFitness = temp > maxFitness ? temp : maxFitness;            
        }

        for (int i = 0; i < samples.length; i++) {
            double ratioNeutralObjects;            

            double[][] objects = samples[i].getPointFitnessObjects();//now contains 3-point structures

            //Epsilon is adjusted to be proportional to problem range (as calculated from the sample)
            ratioNeutralObjects = calculateRatioNeutralObjects(objects, epsilon * (maxFitness - minFitness));            
            totalForSamples += ratioNeutralObjects;
        }

        //the average neutrality across all sample walks provided
        double average = totalForSamples / samples.length;

        Logger
                .getLogger(getClass().getName())
                .log(Level.FINER, "Neutrality Measured: {0}", average);

        return average;
    }

    /**
     * Implements the measure alluded to.
     *
     * @param objects sequence of 3-point objects
     * @return ratio of neutral objects
     */
    private double calculateRatioNeutralObjects(double[][] objects, double epsilon) {
        int neutralCount = 0;

        for (int i = 0; i < objects.length; i++) {
            if (isNeutral(objects[i], epsilon))
                ++neutralCount;
        }

        return ((double) neutralCount) / ((double) objects.length);
    }    

}
