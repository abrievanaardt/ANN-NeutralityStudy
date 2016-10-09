package ac.up.cos700.neutralitystudy.neutralitymeasure;

import ac.up.malan.phd.sampling.Walk;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the third, basic neutrality measure considered for this study. The
 * measure makes use of a progressive random walk sample and analyses 3-point
 * objectsFitness within a number of walks.
 * <br><br>
 * This measure calculates the ratio of the longest neutral sequence to the
 * diagonal of the unit hypercube, in Euclidean space.
 *
 * @author Abrie van Aardt
 */
public class NeutralityMeasure3 extends NeutralityMeasure {

    public NeutralityMeasure3() {
        name = "M3";
    }

    @Override
    public double measure(Walk[] samples, double epsilon) {

        double totalForSamples = 0;

        for (int i = 0; i < samples.length; i++) {
            double ratioNeutralLongest;

            //scale points within the unit hypercube
            samples[i].normalisePoints();

            double[][] objectsFitness = samples[i].getPointFitnessObjects();//now contains fitness of 3-point structures
            double[][][] objects = samples[i].getPointObjects();//now contains 3-point structures

            ratioNeutralLongest = calculateEuclRatioNeutralLongest(objectsFitness, objects, epsilon);

            totalForSamples += ratioNeutralLongest;
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
     * The measure is normalised by multiplying the distance with 1/√D. It is
     * assumed that the sample was scaled to fit within the unit hypercube prior
     * to calling this method.
     *
     * @param objectsFitness sequence of 3-point objects
     * @param objects sequence of fitness values for 3-point objects
     * @return ratio of longest neutral chain in Euclidean space
     */
    private double calculateEuclRatioNeutralLongest(double[][] objectsFitness, double[][][] objects, double epsilon) {
        int maxStreak = 0;
        int streak = 0;

        double[] firstPoint = new double[0];
        double[] lastPoint = new double[0];

        double[] bestFirstPoint = new double[0];
        double[] bestLastPoint = new double[0];

        for (int i = 0; i < objectsFitness.length; i++) {
            if (isNeutral(objectsFitness[i], epsilon)) {
                if (streak == 0) {
                    firstPoint = objects[i][0];
                }
                lastPoint = objects[i][0];
                ++streak;
            }
            else if (streak > 0) {
                if (streak > maxStreak) {
                    bestLastPoint = lastPoint;
                    bestFirstPoint = firstPoint;
                    maxStreak = streak;
                }
                streak = 0;
            }
        }

        //record the last streak not covered by the loop
        if (streak > maxStreak) {
            bestLastPoint = lastPoint;
            bestFirstPoint = firstPoint;
            maxStreak = streak;
        }

        double distance = euclideanDistance(bestFirstPoint, bestLastPoint);

        return distance / Math.sqrt(objects[0][0].length);
    }

    private double euclideanDistance(double[] point1, double[] point2) {
        double dist = 0;

        for (int i = 0; i < point1.length; i++) {
            dist += Math.pow(point1[i] - point2[i], 2);
        }

        return Math.sqrt(dist);
    }

}
