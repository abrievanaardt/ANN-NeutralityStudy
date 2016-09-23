package ac.up.cos700.neutralitystudy.neutralitymeasure;

import ac.up.malan.phd.sampling.Walk;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the first, basic neutrality measure considered for this study. The
 * measure makes use of a progressive random walk sample and analyses 3-point
 * objects within a number of walks. It combines 2 internal measures:
 * <ol>
 * <li>
 * The proportion of 3-point objects that are neutral
 * </li>
 * <li>
 * The longest sequence of 3-point objects that are neutral (proportional to the
 * total number of objects)
 * </li>
 * </ol>
 *
 * Each internal measure contributes 50% towards the total measure.
 *
 * @author Abrie van Aardt
 */
public class NeutralityMeasure1 extends NeutralityMeasure {

    public NeutralityMeasure1(double epsilon) {
        super(epsilon);
    }

    @Override
    public double measure(Walk[] samples) {

        double totalForSamples = 0;

        for (int i = 0; i < samples.length; i++) {
            double ratioNeutralObjects;
            double ratioNeutralLongest;

            double[][] objects = samples[i].getPointFitnessObjects();//now contains 3-point structures

            ratioNeutralObjects = calculateRatioNeutralObjects(objects);
            ratioNeutralLongest = calculateRatioNeutralLongest(objects);

            //each measure contributes 50%
            totalForSamples += (ratioNeutralObjects + ratioNeutralLongest) / 2;
        }

        //the average neutrality across all sample walks provided
        double average = totalForSamples / samples.length;

        Logger
                .getLogger(getClass().getName())
                .log(Level.FINER, "Neutrality Measured: {0}", average);

        
        return average;
    }

    /**
     * Implements internal measure 1
     *
     * @param objects sequence of 3-point objects
     * @return ratio of neutral objects
     */
    private double calculateRatioNeutralObjects(double[][] objects) {
        int neutralCount = 0;

        for (int i = 0; i < objects.length; i++) {
            if (isNeutral(objects[i]))
                ++neutralCount;
        }

        return ((double) neutralCount) / ((double) objects.length);
    }

    /**
     * Implements internal measure 2
     *
     * @param objects sequence of 3-point objects
     * @return ratio of longest neutral chain
     */
    private double calculateRatioNeutralLongest(double[][] objects) {
        int neutralCount = 0;
        int longestCount = 0;

        for (int i = 0; i < objects.length; i++) {
            if (isNeutral(objects[i])){
                ++neutralCount;
                //update longest sequence of neutral objects
                longestCount = neutralCount > longestCount ? neutralCount : longestCount;
            }
            else {
                
                neutralCount = 0;
            }
        }

        return ((double) longestCount) / ((double) objects.length);
    }

    /**
     * Method assumes that points only contains 3 points.
     *
     * @param points
     * @return whether the 3 points are neutral or not
     */
    private boolean isNeutral(double[] points) {
        return (Math.abs(points[0] - points[1]) < epsilon
                && Math.abs(points[1] - points[2]) < epsilon);
    }

}
