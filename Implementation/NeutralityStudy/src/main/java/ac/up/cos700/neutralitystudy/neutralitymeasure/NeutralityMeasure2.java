package ac.up.cos700.neutralitystudy.neutralitymeasure;

import ac.up.malan.phd.sampling.Walk;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the second, basic neutrality measure considered for this study. The
 * measure makes use of a progressive random walk sample and analyses 3-point
 * objects within a number of walks.
 * <br><br>
 * This measure calculates the longest sequence of 3-point objects that are
 * neutral (proportional to the total number of objects).
 *
 * @author Abrie van Aardt
 */
public class NeutralityMeasure2 extends NeutralityMeasure {

    public NeutralityMeasure2(double epsilon) {
        super(epsilon);
    }

    @Override
    public double measure(Walk[] samples) {

        double totalForSamples = 0;

        for (int i = 0; i < samples.length; i++) {
            double ratioNeutralLongest;

            double[][] objects = samples[i].getPointFitnessObjects();//now contains 3-point structures

            ratioNeutralLongest = calculateRatioNeutralLongest(objects);
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
     * @param objects sequence of 3-point objects
     * @return ratio of longest neutral chain
     */
    private double calculateRatioNeutralLongest(double[][] objects) {

        int maxStreak = 0;
        int streak = 0;

        for (int i = 0; i < objects.length; i++) {
            if (isNeutral(objects[i])) {
                ++streak;
            }
            else if (streak >= maxStreak) {
                //todo: have optimised this, check for correctness
                maxStreak = streak > maxStreak ? streak : maxStreak;
                streak = 0;
            }
        }

        //record the last streak not covered by the loop
        if (streak >= maxStreak)
           maxStreak = streak;
       
        return maxStreak / (double) objects.length;
    }

    /**
     * Implements the measure alluded to. Weighting the result according
     * the number of 'longest' neutral regions found
     * <br>
     * This method currently calculates the max length in a roundabout way, but
     * this provides a platform for future measures.
     *
     * @param objects sequence of 3-point objects
     * @return ratio of longest neutral chain
     */
    private double calculateRatioNeutralLongestWeighted(double[][] objects) {

        HashMap<Integer, Integer> map = new HashMap<>();

        int maxStreak = 0;
        int streak = 0;

        for (int i = 0; i < objects.length; i++) {
            if (isNeutral(objects[i])) {
                ++streak;
            }
            else if (streak >= maxStreak) {
                map.put(streak, streak + map.getOrDefault(streak, 0));
                maxStreak = streak > maxStreak ? streak : maxStreak;
                streak = 0;
            }
        }

        //record the last streak not covered by the loop
        if (streak >= maxStreak)
            map.put(streak, streak + map.getOrDefault(streak, 0));

        int longestSequence = 0;
        int nLongestSequences = 0;
        if (map.size() > 0) {

            //get the max length
            longestSequence = map.keySet().stream()
                    .mapToInt(i -> i)
                    .max().getAsInt();

            //now weight the length (ie take into account there might be more 
            //than one "longest" sequence)
            nLongestSequences = map.get(longestSequence);
        }

//        try {
//
//            Results.newGraph("Distribution", "Number of 3-Point Objects vs Neutral Sequence Length", "Neutral Sequence Length", "Total Number of 3-Point Objects", null, 2);
//            Results.addPlot("", map.keySet().stream().mapToDouble(i -> i).toArray(), map.values().stream().mapToDouble(i -> i).toArray(), "boxes");
//            Results.plot();
//        }
//        catch (Exception e) {
//
//        }       
        return nLongestSequences / (double) objects.length;
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
