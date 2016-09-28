package ac.up.cos700.neutralitystudy.neutralitymeasure;

import ac.up.cos700.neutralitystudy.data.Results;
import ac.up.malan.phd.sampling.Walk;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalInt;
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
            ratioNeutralLongest = calculateRatioNeutralLongestWeighted(objects);

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
    private double calculateRatioNeutralLongestWeighted(double[][] objects) {

        HashMap<Integer, Integer> map = new HashMap<>();

        int streak = 0;

        for (int i = 0; i < objects.length; i++) {
            if (isNeutral(objects[i])) {
                ++streak;
            }
            else if (streak > 0) {
                map.put(streak, streak + map.getOrDefault(streak, 0));
                streak = 0;
            }
        }
        
        //record the last streak not covered by the loop
        if (streak > 0)
            map.put(streak, streak + map.getOrDefault(streak, 0));
        
        double avg = distributionAvg(map);
        double stdDev = distributionStdDev(map, avg);

        //now want the total number of neutral 3-point objects within 1
        //standard deviation
        int minLength = (int) avg - (int) Math.floor(stdDev);
        int maxLength = (int) avg + (int) Math.ceil(stdDev);

        double objectsWithin1StdDev = 0;

        for (int i = minLength; i <= maxLength; i++) {
            objectsWithin1StdDev += map.getOrDefault(i, 0);
        }

        //get the fraction of these particular non-zero length neutral objects
        //over the total number of objects in the walk
        double result = objectsWithin1StdDev / (double) objects.length;

//        try {
//
//            Results.newGraph("Distribution", "Number of 3-Point Objects vs Neutral Sequence Length", "Neutral Sequence Length", "Total Number of 3-Point Objects", null, 2);
//            Results.addPlot("", map.keySet().stream().mapToDouble(i -> i).toArray(), map.values().stream().mapToDouble(i -> i).toArray(), "boxes");
//            Results.plot();
//        }
//        catch (Exception e) {
//
//        }       

        return result;
    }

    /**
     * Implements internal measure 2
     *
     * @param objects sequence of 3-point objects
     * @return ratio of longest neutral chain
     */
    private double calculateRatioNeutralLongest(double[][] objects) {

        HashMap<Integer, Integer> neutralSequenceOfLength = new HashMap<>();

        int neutralCount = 0;
        int longestCount = 0;

        for (int i = 0; i < objects.length; i++) {
            if (isNeutral(objects[i])) {

                ++neutralCount;
                //update longest sequence of neutral objects
                longestCount = neutralCount > longestCount ? neutralCount : longestCount;
            }
            else if (neutralCount != 0) {
                neutralCount = 0;
            }
        }

        return ((double) longestCount) / ((double) objects.length);
    }

    private double distributionStdDev(Map<Integer, Integer> map, double avg) {
        Iterator<Entry<Integer, Integer>> iter = map.entrySet().iterator();
        double totalNonZeroObjectCount = map.values().stream().mapToInt(i -> i).sum();
        double stdDev = 0;

        while (iter.hasNext()) {
            Entry<Integer, Integer> entry = iter.next();

            //for each object with the current score
            for (int i = 0; i < entry.getValue(); i++) {
                stdDev += Math.pow(entry.getKey() - avg, 2);
            }
        }

        stdDev = Math.sqrt(stdDev / (totalNonZeroObjectCount - 1));
        
        return stdDev;
    }

    private double distributionAvg(Map<Integer, Integer> map) {
        //map now contains a frequency distribution (where the score is
        //assigned as the length of the sequence the object belongs to)
        //excluding 0 length sequences.
        //
        //Determine the sample std dev of the distribution      
        double totalNonZeroObjectCount = map.values().stream().mapToInt(i -> i).sum();

        //calc sample average
        Iterator<Entry<Integer, Integer>> iter = map.entrySet().iterator();
        double avg = 0;

        while (iter.hasNext()) {
            Entry<Integer, Integer> entry = iter.next();
            avg += entry.getKey() * entry.getValue();
        }

        avg /= totalNonZeroObjectCount;

        return avg;
    }

    /**
     * Method assumes that points only contains 3 points. All 3 points have to
     * be equal in fitness, with an acceptable error of 'epsilon'
     *
     * @param points
     * @return whether the 3 points are neutral or not
     */
    private boolean isNeutral(double[] points) {
        return (Math.abs(points[0] - points[1]) < epsilon
                && Math.abs(points[1] - points[2]) < epsilon
                && Math.abs(points[0] - points[2]) < epsilon);
    }

}
