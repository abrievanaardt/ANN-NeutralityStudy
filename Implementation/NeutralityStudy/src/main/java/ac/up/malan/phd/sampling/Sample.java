package ac.up.malan.phd.sampling;

import java.io.*;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import java.util.*;
import java.util.Collections;
import ac.up.malan.phd.sampling.util.SampleException;

/**
 * This class models a random sample of a problem
 *
 * @author Dr Katherine Malan
 * @author Abrie van Aardt - Made minor adjustments to the code during
 * integration.
 * 
 */
public class Sample {

    // Constants for the different ways of sampling the initial population
    public static final int SAMPLETYPE_DEBUG = 0;
    public static final int SAMPLETYPE_RANDOM = 1;
    public static final int SAMPLETYPE_WALK = 2;

    protected RealProblem problem;  // problem on which the sample is based

    protected int numPoints;   // number of points to use in the sample
    protected Vector[] points;  // array of position vectors (based on a sample)
    protected double[] fitness;  // array of fitness values corresponding to points
    private int sampleType; // type of sample to generate the initial population: one of constants SAMPLETYPE_*

    protected int gBestIndex;  // index of global best position in points array (determined when fitnessPoints() called)

    protected double xMax, xMin; // range of the problem
    protected int dimension; // dimension of the problem
    protected double minFit, maxFit; // calculated in fitnessAll method

    public Sample(RealProblem prob) throws SampleException // assume 1000D points and random sample
    {
        this(prob, SAMPLETYPE_RANDOM, 1000 * prob.getDimensionality());
    }

    /* Constructor: set the problem on which the sample should be done
     * as well as the 
     */
    public Sample(RealProblem prob, int sampleT, int numP) throws SampleException {
        problem = prob;
        sampleType = sampleT;
        numPoints = numP;

        xMax = problem.getUpperBound();
        xMin = problem.getLowerBound();
        dimension = problem.getDimensionality();

        points = new Vector[numPoints];   // create array of position vectors
        for (int p = 0; p < numPoints; p++) {// for all points in the sample	    
            points[p] = new Vector(dimension);   // create the actual Vector for storing the point
        }
        fitness = new double[numPoints];  // create the array of fitness values      

        // Sample the space for the initial positions:
        switch (sampleType) {
            case SAMPLETYPE_DEBUG:
                doSetSample();
                fitnessPoints();      // determine the fitness of all points
                break;
            case SAMPLETYPE_RANDOM:
                doRandomSample();
                fitnessPoints();      // determine the fitness of all points
                break;
            case SAMPLETYPE_WALK:   // do nothing
                break;
            default:
                throw new SampleException("Invalid sample type in Sample.java");                
        }
    }

    /* Creates a Sample object based on prob which matches the sampleToCopy (i.t.o) xValues
     */
    public Sample(RealProblem prob, Sample sampleToCopy) throws SampleException {
        problem = prob;
        numPoints = sampleToCopy.numPoints;
        sampleType = sampleToCopy.sampleType;
        points = new Vector[numPoints]; // create array for storing position vectors
        fitness = new double[numPoints]; // ... and the corresponding fitness values
        dimension = problem.getDimensionality();
        xMax = problem.getUpperBound();
        xMin = problem.getLowerBound();
        if (dimension != sampleToCopy.dimension) {
            System.out.println("ERROR: Dimension of problem and sampleToCopy do not match in Sample.java");
            System.exit(0);
        }
        for (int i = 0; i < numPoints; i++)
            points[i] = sampleToCopy.getPoint(i); // get a clone of the Vector object at the position
        fitnessPoints();
    }

    /* Creates a single Sample object based on an array of Sample objects */
 /* The type and problem of the new Sample is set to be the same as the first sample in the array */
 /* The method fails if the dimension of the samples is not the same for all samples in the array */
    public Sample(Sample[] sample) throws SampleException {
        int numSamples = sample.length;
        problem = sample[0].problem;
        int totalPoints = 0;
        for (int i = 0; i < numSamples; i++)
            totalPoints += sample[i].numPoints;
        numPoints = totalPoints;
        sampleType = sample[0].sampleType;
        points = new Vector[numPoints]; // create array for storing position vectors
        fitness = new double[numPoints]; // ... and the corresponding fitness values
        xMax = problem.getUpperBound();
        xMin = problem.getLowerBound();
        dimension = problem.getDimensionality();
        // Check that the dimension of all Samples is the same:
        for (int i = 1; i < numSamples; i++) {
            if (dimension != sample[i].dimension) {
                throw new SampleException("Dimensions of array of samples not equal in method Sample::Sample(Sample[])");              
            }
        }
        int index = 0;
        for (int i = 0; i < numSamples; i++) {
            int sizeCurrentSample = sample[i].numPoints;
            for (int j = 0; j < sizeCurrentSample; j++) {
                points[index] = sample[i].getPoint(j);  // get a clone of the Vector object at the position
                index++;
            }
        }
        fitnessPoints();
    }

    public int getNumPoints() {
        return numPoints;
    }

    public int getGBestIndex() {
        return gBestIndex;
    }

    // getMinFitness: gets the minimum fitness value in the sample
    public double getMinFitness() {
        return minFit;
    }

    // getMaxFitness: gets the maximum fitness value in the sample
    public double getMaxFitness() {
        return maxFit;
    }

    public Vector getGBest() {
        Vector retVector = null;
        try {
            retVector = (Vector) points[gBestIndex].clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return retVector;
    }

    public double getXMin() {
        return xMin;
    }

    public double getXMax() {
        return xMax;
    }

    public int getDimension() {
        return dimension;
    }

    // returns the position vector at position i in the sample
    public Vector getPoint(int i) {
        Vector retVector = null;
        try {
            retVector = (Vector) points[i].clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return retVector;
    }

    // returns the fitness of the point at position i in the sample
    public double getFitness(int i) {
        return fitness[i];
    }

    // Initialise the array of position vectors in a set way for debugging
    // (simply starts at the min of the range and adds one until points used up)
    public void doSetSample() {
        int num = (int) (problem.getLowerBound());
        for (int i = 0; i < numPoints; i++) {
            for (int j = 0; j < dimension; j++) {
                points[i].setReal(j, num);
            }
            num++;
        }
    }

    /* Initialise the array of position vectors using the random sampling technique
     */
    public void doRandomSample() {
        // intialise the random number generator:
        Random randGenerator = new Random();
        for (int p = 0; p < numPoints; p++) {// for all points in the sample	    
            for (int i = 0; i < dimension; i++)   // for every dimension in the search space
                points[p].setReal(i, randGenerator.nextDouble() * (xMax - xMin) + xMin);
        }
    }

    /* computes the fitness of all the position vectors in the sample */
    public void fitnessPoints() throws SampleException {
        try {
            fitness[0] = problem.evaluate(points[0]);
            maxFit = fitness[0];
            minFit = fitness[0];
            int bestIndex = 0;
            for (int i = 1; i < numPoints; i++) {
                fitness[i] = problem.evaluate(points[i]);
                if (fitness[i] < minFit) {
                    bestIndex = i;
                    minFit = fitness[i];
                }
                if (fitness[i] > maxFit) {
                    maxFit = fitness[i];
                }
            }
            gBestIndex = bestIndex;
        }
        catch (UnequalArgsDimensionException ex) {
            throw new SampleException(ex.getMessage());
        }
    }

    public void printSample() {
        System.out.println("Sample:");
        for (int i = 0; i < numPoints; i++) {
            System.out.println(points[i] + "  " + "Fitness: " + fitness[i]);
        }
    }

    // This method converts the points in the sample to be in the domain of [0,1] in all dimensions
    // Note that the fitness values are calculated in the constructor, so the normalised points 
    // correspond to the previously calculated fitness values.
    public void normalisePoints() {
        double range = xMax - xMin;
        for (int i = 0; i < numPoints; i++) {
            for (int d = 0; d < dimension; d++) {
                double num = points[i].getReal(d);
                num = (num - xMin) / range;
                points[i].setReal(d, num);
            }
        }
    }

    /* getDispersion: calculate the dispersion of the given percentage of the best points in the sample
     */
    public double getDispersion(double percentage) throws SampleException {
        double dispersion = 0;
        int bestN = (int) (percentage * numPoints);
        normalisePoints();
        LinkedList<Index> list = new LinkedList();
        for (int i = 0; i < numPoints; i++)
            list.add(new Index(i));
        Collections.sort(list);
        double distanceTotal = 0;
        double distance = 0;
        int numDistances = 0;
        for (int i = 0; i < bestN - 1; i++) {
            for (int j = i + 1; j < bestN; j++) {
                numDistances++;
                Index one = list.get(i);
                Index two = list.get(j);
                try {
                    distance = points[one.index].getEuclideanDistance(points[two.index]);
                }
                catch (UnequalArgsDimensionException e) {
                    throw new SampleException(e.getMessage());
                }
                distanceTotal += distance;
        
            }
        }
        double averagePairwiseDistance = distanceTotal / numDistances;
        
        return averagePairwiseDistance;
    }

    public double getFullDispersion() {
        double dispersion = -1.0;  // indicates that the dispersion is not known
        switch (dimension) {
            case 1:
                return 0.33;
            case 2:
                return 0.52;
            case 3:
                return 0.66;
            case 4:
                return 0.78;
            case 5:
                return 0.88;
            case 6:
                return 0.97;
            case 7:
                return 1.05;
            case 8:
                return 1.13;
            case 9:
                return 1.20;
            case 10:
                return 1.27;
            case 15:
                return 1.56;
            case 20:
                return 1.81;
            case 30:
                return 2.22;
        }
        System.out.println("Warning: dispersion not known for full sample, see: Sample::getFullDispersion");
        return dispersion;
    }

    public double calculateMeasure() throws SampleException {
        return getDispersionMetric(0.1);   // the best 10% of the points
    }

    /* getDispersionMetric: calculate the dispersion metric of the current sample based on 
      * the dispersion of the given percentage of the best points in the sample
     */
    public double getDispersionMetric(double percentage) throws SampleException {
        double dispersion = getDispersion(percentage);
        return dispersion - getFullDispersion();
    }

    class Index implements Comparable {

        int index;  // index into points and fitness array for the position

        public Index(int i) {
            index = i;
        }

        public int compareTo(Object o) {
            Index other = (Index) o;
            if (fitness[index] > fitness[other.index])
                return 1;
            else if (fitness[index] < fitness[other.index])
                return -1;
            else
                return 0;
        }

        public String toString() {
            return (points[index] + " " + fitness[index]);
        }
    }

    public void printSampleToFile(String outFile) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(outFile));
        for (int i = 0; i < numPoints; i++) {
            out.println(points[i] + " " + "Fitness: " + fitness[i]);
        }
        out.close();
    }

    // This is "clean" output: just values separated by spaces
    public void printPointsToFile(String outFile) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(outFile));
        for (int i = 0; i < numPoints; i++) {
            for (int d = 0; d < dimension; d++)
                out.print(points[i].getReal(d) + " ");
            out.println();
        }
        out.close();
    }

    // getCoverage: This function determines the percentage of partitions (boxes for a 2D space) 
    // that are visited by the sample
    // parameter divisor: the number of partitions in each dimension
    public double getCoverage(int divisor) {
        int numPartitions = (int) Math.pow(divisor, dimension);  // number of partitions in total	    
        boolean visited[] = new boolean[numPartitions];  // array for keeping track of which partitions are visited
        for (int i = 0; i < numPartitions; i++)
            visited[i] = false;   // none visited to start off with	 
        double partitionLength = (xMax - xMin) / divisor;
        for (int i = 0; i < numPoints; i++) { // for each point in the sample:		
            int index = 0;  // determine which partition it falls into 
            for (int d = 0; d < dimension; d++) {
                double offsetFromXmin = points[i].getReal(d) - xMin;
                int partitionForDim = (int) (offsetFromXmin / partitionLength);
                if (partitionForDim == divisor)  // at the boundary of the last partition, so put into last partition
                    partitionForDim--;
                index += partitionForDim * (int) Math.pow(divisor, d);
            }
            visited[index] = true;  // save that it has been visited
        }
        // calculate the proportion of partitions visited:	
        long numPartitionsVisited = 0;
        for (int i = 0; i < numPartitions; i++) {
            if (visited[i])
                numPartitionsVisited++;
        }
        return (double) numPartitionsVisited / (double) numPartitions;
    }

    public double getHistogramDev(int divisor, String fileName) throws IOException {
        PrintWriter histogramOut = null;
        if (fileName != null) {
            histogramOut = new PrintWriter(new FileWriter(fileName));
        }
        int numPartitions = (int) Math.pow(divisor, dimension);
        int numPointsInPartition[] = new int[numPartitions];  // array for keeping track of the number of points in each partition
        for (int i = 0; i < numPartitions; i++)
            numPointsInPartition[i] = 0;   // no points to start off with	 
        double partitionLength = (xMax - xMin) / divisor;
        int avgNumPointsInPartition = numPoints / numPartitions;
        if (avgNumPointsInPartition * numPartitions != numPoints) {
            System.out.println("Aborting because numPoints don't fit neatly into partitions in Sample::getHistogramDev");
            System.exit(0);
        }
        for (int i = 0; i < numPoints; i++) {
            int index = 0;  // determine which partition it falls into 
            for (int d = 0; d < dimension; d++) {
                double offsetFromXmin = points[i].getReal(d) - xMin;
                int partitionForDim = (int) (offsetFromXmin / partitionLength);
                if (partitionForDim == divisor)  // at the boundary of the last partition, so put into last partition
                    partitionForDim--;
                index += partitionForDim * (int) Math.pow(divisor, d);
            }
            numPointsInPartition[index]++;  // increment the number of points in that partition
        }

        // calculate the std deviation & return
        double sumOfSquares = 0;
        for (int i = 0; i < numPartitions; i++) {
            if (histogramOut != null) {
                histogramOut.println(numPointsInPartition[i]);
            }
            sumOfSquares += Math.pow((numPointsInPartition[i] - avgNumPointsInPartition), 2);
        }
        if (histogramOut != null)
            histogramOut.close();
        double stdDev = Math.sqrt(sumOfSquares / (numPartitions - 1));
        return stdDev;
    }

    public double[][] getPoints() {
        double[][] tempPoints = new double[points.length][points[0].getDimension()];

        for (int i = 0; i < tempPoints.length; i++) {

            for (int j = 0; j < tempPoints[i].length; j++) {
                tempPoints[i][j] = points[i].getReal(j);
            }
        }

        return tempPoints;
    }

    public double[] getPointsFitness() {
        return Arrays.copyOf(fitness, fitness.length);
    }

    /**
     * This method assembles the points into overlapping
     * 3-point objects that will be used to calculate neutrality. 
     * A 3-point object consists of a point, together
     * with its 2 neighbouring points.
     * 
     * @return 3-point objects 
     */
    public double[][] getPointFitnessObjects() {
        double[] pointsFitness = getPointsFitness();
        double[][] objects = new double[pointsFitness.length-2][3];

        //from second to second last points - since 2 neighbours are included
        for (int i = 1; i < pointsFitness.length - 1; i++) {
            objects[i-1][0] = pointsFitness[i-1];
            objects[i-1][1] = pointsFitness[i];
            objects[i-1][2] = pointsFitness[i+1];            
        }
        
        return objects;        
    }

}
