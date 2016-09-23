package ac.up.malan.phd.sampling;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import java.util.*;
import ac.up.malan.phd.sampling.util.SampleException;

/**
 * A particular sample that captures information of neighbouring points. A 
 * neighbourhood is defined as the point cloud within a hypercube of some
 * size around the point in question.
 * 
 * @author Dr Katherine Malan
 */
public class Walk extends Sample {

    public static int WALKTYPE_RANDOM = 1;
    public static int WALKTYPE_STRAIGHT = 2;
    public static int WALKTYPE_RANDOM_INCREASING = 3;
    public static int WALKTYPE_STRAIGHT_FULL = 4; // This walk takes constant steps from xMin to xMax
    public static int WALKTYPE_RANDOM_INCREASING_NOWRAP = 5; // This walk starts at xMin, works like random increasing, but stops at xMax
    public static int WALKTYPE_RANDOM_MANHATTAN = 6;  // Increases by constant steps along 1 dimension at a time, at boundaries it switches direction.
    public static int WALKTYPE_RANDOM_PROGRESSIVE = 7;  // Like Manhattan, but random size

    public static int START_POSITION_XMIN = 0;
    public static int START_POSITION_RANDOM = 1;
    public static int START_POSITION_SMALL = 2;   // The starting position is initialised in the bottom left hypervolume
    public static int START_POSITION_SPECIFIED = 3;  // see walkSpecifier

    private double percSmall = 0.5; // used for determining the size of the range for the hypervolume of START_POSITION_SMALL walks
    int startStrategy;  // one of START_POSITION_*
    BinaryFlag walkSpecifier;  // indicates where to start and whether to increase or decrease for each dimension

    int numSteps;   // number of steps in the walk (this is one less than the number of points)
    int walkType; // type of walk: one of the constants WALKTYPE_*
    double stepSize;    // stepSize: the size of the "step" taken. In case of:
    //    WALKTYPE_STRAIGHT: actual step size = stepSize        
    //    WALKTYPE_RANDOM actual step size = random number in range [-stepSize, +stepSize)
    //    WALKTYPE_RANDOM_INCREASING actual step size = random number in range [0, stepSize)
    //    WALKTYPE_RANDOM_PROGRESSIVE actual step size = random number in range [0, stepSize)

    public Walk(RealProblem prob) throws SampleException // assume random walk
    {
        this(prob, WALKTYPE_RANDOM);
    }

    /* Creates a Walk object based on RealProblem prob which matches the walk (i.t.o) xValues
     * as another Walk object called walkToCopy
     */
    public Walk(RealProblem prob, Walk walkToCopy) throws SampleException {
        super(prob, walkToCopy);
        numSteps = walkToCopy.numSteps;
        walkType = walkToCopy.walkType;
    }

    public Walk(RealProblem prob, int walkType) throws SampleException { // assume 100 steps
        this(prob, walkType, 100);
    }

    public Walk(RealProblem prob, int walkT, int numS) throws SampleException {
        // call constructor with some sensible value for step size based on the range
        this(prob, walkT, numS, (prob.getUpperBound() - prob.getLowerBound()) / 100.0, START_POSITION_RANDOM);
    }

    public Walk(RealProblem prob, int walkT, int numS, double stepS) throws SampleException {
        this(prob, walkT, numS, stepS, START_POSITION_RANDOM);
    }

    public Walk(RealProblem prob, int walkT, int numS, double stepS, int sStrategy) throws SampleException {
        this(prob, walkT, numS, stepS, sStrategy, null);  // assume no starting position
    }

    /* Constructor: set the problem on which the random walk should be done
     * as well as the length (number of steps) in the random walk, the type of walk, the 
     * step size, the starting strategy and the walkSpecifier
     */
    public Walk(RealProblem prob, int walkT, int numS, double stepS, int sStrategy, BinaryFlag wS) throws SampleException {
        super(prob, Sample.SAMPLETYPE_WALK, numS + 1); // numPoints is 1 more than the number of steps
        //System.out.println("After calling super, numPoints = " + numPoints);
        numSteps = numS;
        walkType = walkT;
        stepSize = stepS;
        startStrategy = sStrategy;
        if (wS != null) {
            try {
                walkSpecifier = (BinaryFlag) wS.clone();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            walkSpecifier = null;
        if (walkType == WALKTYPE_STRAIGHT_FULL) {
            // check if the current settings will almost fill the space:
            double coverage = numPoints * stepSize;
            double actualRange = xMax - xMin;
            // if the coverage is within the bounds of the range and if adding another step will 
            // go over the range, then fine. If it is not true, then adjust the stepSize to something appropriate
            if (!((coverage <= actualRange) && ((coverage + stepSize) > actualRange))) {
                System.out.println("Warning: Parameters specified for WALKTYPE_STRAIGHT_FULL do not fill the range");
                stepSize = (xMax - xMin) / numSteps;
                // check if stepSize will wrap around:
                double val = xMin;
                for (int i = 0; i < numSteps; i++) {
                    val = val + stepSize;
                }
                if (val > xMax) {
                    stepSize *= 0.9999999999;    // this is a hack to avoid the problem of rounding errors and
                    // wrapping around to the beginning again
                }
                System.out.println("Adjusting stepSize to " + stepSize + " to fill the range");
            }
        }
        if ((walkType == WALKTYPE_STRAIGHT) || (walkType == WALKTYPE_STRAIGHT_FULL))
            doStraightWalk();
        else
            doRandomWalk(); // default
        fitnessPoints();  // compute all the fitness values
    }

    public int getLength() {
        return numSteps;
    }

    public double getStepSize() {
        return stepSize;
    }

    // getIndexOfMin: determines the index in the walk that has the (first) minimum fitness value
    public int getIndexOfMin() {
        return getGBestIndex();
    }

    // getXAtMin: determines the (first) position on the walk that has the minimum fitness value.
    // returns the Vector at that position.
    public Vector getXAtMin() {
        return getGBest();
    }

    // getX: gets a clone of the x vector at position i in the walk
    public Vector getX(int i) {
        return getPoint(i);
    }

    /* Initialise the array of position vectors (walk) using a random walk
     * Note: the max step size is dependent on the range (domain) of the problem
     */
    public void doRandomWalk() {
        // intialise the random number generator:
        Random randGenerator = new Random();

        Vector tempX = new Vector(dimension); // temporary position vector for the random walk

        // INITIALISE THE STARTING POSITION
        if (startStrategy == START_POSITION_XMIN) {
            for (int i = 0; i < dimension; i++)
                tempX.setReal(i, xMin);
        }
        else if (startStrategy == START_POSITION_RANDOM) {
            // assign tempX a random value within the bounds of the problem (starting position):
            for (int i = 0; i < dimension; i++)
                tempX.setReal(i, randGenerator.nextDouble() * (xMax - xMin) + xMin);
        }
        else if (startStrategy == START_POSITION_SMALL) {
            double xRange = xMax - xMin;
            double smallRange = percSmall * xRange;
            for (int i = 0; i < dimension; i++)
                tempX.setReal(i, randGenerator.nextDouble() * smallRange + xMin);
        }
        else if (startStrategy == START_POSITION_SPECIFIED) {
            // use the walkSpecifier object to determine where to start:

            // for every dimension, generate a random number in the starting zone hypervolume:
            for (int i = 0; i < dimension; i++) {
                // generate a random offset within 50% of the range:
                double offset = randGenerator.nextDouble() * ((xMax - xMin) * 0.5);
                if (walkSpecifier.isSet(i))
                    tempX.setReal(i, xMax - offset);
                else
                    tempX.setReal(i, xMin + offset);
            }
            // generate a random dimension to move the starting position to the edge of the search space:
            int randDim = (int) Math.floor(randGenerator.nextDouble() * dimension);
            if (walkSpecifier.isSet(randDim))  // this means that this dimension should be at maximum
                tempX.setReal(randDim, xMax);
            else
                tempX.setReal(randDim, xMin);
        }

        // DO THE WALK:
        int count = 0; // for counting the number of steps taken
        double smallRandomChange, newValue = 0;
        while (count < numPoints) {

            if (walkType == WALKTYPE_RANDOM_MANHATTAN) {
                // save the previous point:
                for (int i = 0; i < dimension; i++) {
                    points[count].setReal(i, tempX.getReal(i));
                }
                // generate a random dimension within the dimension of the problem:
                int randDim = (int) Math.floor(randGenerator.nextDouble() * dimension);
                int inc = 1;
                //    if(count%2 == 0) {
                if (walkSpecifier.isSet(randDim))
                    inc = -1;
                else
                    inc = 1;
                newValue = tempX.getReal(randDim) + (inc * stepSize);  // decrease or increase by stepSize in the random dimension
                if ((newValue > xMax) || (newValue < xMin)) { // outside the bounds of the problem
                    newValue = tempX.getReal(randDim) - (inc * stepSize);
                    walkSpecifier.flipBit(randDim);    // change the direction		               
                }
                //  }
                /* else {
		int randBit = (int)Math.floor(randGenerator.nextDouble()*2);
		if (randBit == 0) inc = 1;
		else inc = -1;
		newValue = tempX.getReal(randDim) + (inc * stepSize);
		if ((newValue > xMax) || (newValue < xMin)) 
		   newValue = tempX.getReal(randDim) - (inc * stepSize);
	    }  */
                tempX.setReal(randDim, newValue); // change tempX to new value
            }

            else if (walkType == WALKTYPE_RANDOM_PROGRESSIVE) {
                for (int i = 0; i < dimension; i++) {
                    // save the previous component:
                    points[count].setReal(i, tempX.getReal(i));

                    // for all steps: progress in set direction
                    smallRandomChange = (randGenerator.nextDouble() * stepSize); // a positive value
                    if (walkSpecifier.isSet(i))
                        smallRandomChange = -smallRandomChange;
                    newValue = tempX.getReal(i) + smallRandomChange;
                    if (newValue < xMin) { // out of bounds -- too small:
                        // calculate by how much it goes over the boundary:
                        double diff = xMin - newValue;
                        newValue = xMin + diff;  // bounce back from the boundary
                        walkSpecifier.flipBit(i);  // change direction bias for that dimension
                    }
                    else if (newValue > xMax) {  // out of bounds -- too big:
                        double diff = newValue - xMax;
                        newValue = xMax - diff;  // bounce back from the boundary
                        walkSpecifier.flipBit(i);  // change direction bias for that dimension
                    }
                    // previous strategy: change direction in both cases (too big or too small):
                    // newValue = tempX.getReal(i) - smallRandomChange;
                    // walkSpecifier.flipBit(i);    // change the direction
                    tempX.setReal(i, newValue);
                }
            }

            else {  // WALKTYPE_RANDOM_*
                for (int i = 0; i < dimension; i++) { // for every part of the vector
                    points[count].setReal(i, tempX.getReal(i)); // save the last position

                    if (walkType == WALKTYPE_RANDOM) {
                        boolean outsideBounds = true; // assume out of bounds to start
                        while (outsideBounds) {  // while the new value is outside the bounds   			
                            // generate a small random step in either direction
                            smallRandomChange = (randGenerator.nextDouble() * stepSize * 2) - stepSize;
                            newValue = tempX.getReal(i) + smallRandomChange; // change tempXi by small random amount
                            if ((newValue >= xMin) && (newValue <= xMax)) { // check if new value would be in bounds
                                outsideBounds = false;
                                tempX.setReal(i, newValue); // update tempX with the step
                            } // end of if		    
                        } // end of while outside bounds
                    }
                    else {       // WALKTYPE_RANDOM_INCREASING  or WALKTYPE_RANDOM_INCREASING_NOWRAP    
                        smallRandomChange = (randGenerator.nextDouble() * stepSize); // a positive value
                        newValue = tempX.getReal(i) + smallRandomChange; // change tempXi by small positive amount
                        if ((newValue > xMax) || (newValue < xMin)) { // outside the bounds
                            if (walkType == WALKTYPE_RANDOM_INCREASING_NOWRAP) {
                                // save the rest of the previous position:
                                for (int j = i + 1; j < dimension; j++)
                                    points[count].setReal(j, tempX.getReal(j));
                                stopWalk(count); // stop the walk at the current number of steps
                                return;
                            }
                            else {
                                newValue = xMin;  // wrap to minimum value
                            }
                        }
                        tempX.setReal(i, newValue); // change tempXi to new value
                    }
                } // end of for each part of the vector
            }
            count++;
        }
    }

    private void stopWalk(int positionsCreated) {
        // System.out.println("Inside stopWalk: " + positionsCreated);
        numPoints = positionsCreated + 1;  // including the initial position
        numSteps = numPoints - 1;
    }

    public void doStraightWalk() { // simple walk from xMin in increments of numSteps
        double val = xMin; // start with the minimum value
        for (int i = 0; i < numPoints; i++) {
            for (int j = 0; j < dimension; j++) {
                points[i].setReal(j, val);
            }
            val = val + stepSize;
            if (val > xMax) {
                System.out.println("WRAPPING AROUND: val: " + val + "\t xMax: " + xMax);
                val = xMin;  // simply wrap around
            }
        }
    }

    public void printWalk() {
        System.out.print("Walk ");
        printSample();
    }

    /* Calculate and return the normalised average absolute difference between all points in the 
     * walk and their corresponding points on the opposite side of the fitness axis (on all dimensions)
     */
    public double getAvgSymmetry() throws SampleException {
        double totalAbsDiff = 0;
        // minFitness and maxFitness is used to store the min and max fitness of the points on 
        // the walk as well as their symmetrically opposite points (these may fall outside the range)
        double minFitness = minFit; // set minFitness to be the minFit of the points in the walk
        double maxFitness = maxFit; // set maxFitness to be the maxFit of the points in the walk

        double fitness1, fitness2, absDiff;
        Vector oppositeX = new Vector(dimension); // vector for storing the opposite point

        for (int i = 0; i < numPoints; i++) { // for all points in the walk
            fitness1 = fitness[i];  // fitness value of the current point in the walk

            // Determine the opposite point on all axes:
            for (int d = 0; d < dimension; d++)  // for every dimension of the point
                oppositeX.setReal(d, -(points[i].getReal(d))); // opposite point
            //     System.out.println("Point:" + walk[i] + "\t\tOpposite point:" + oppositeX);
            try {
                fitness2 = problem.evaluate(oppositeX);
            }
            catch (UnequalArgsDimensionException ex) {
                throw new SampleException(ex.getMessage());
            }
            if (fitness2 > maxFitness)
                maxFitness = fitness2;
            if (fitness2 < minFitness)
                minFitness = fitness2;
            //     System.out.println("fitness1: " + fitness1 + "\t\tfitness2: " + fitness2);
            absDiff = Math.abs(fitness1 - fitness2);
            totalAbsDiff += absDiff;
        }
        //   System.out.println("totalAbsDiff:  " + totalAbsDiff);
        double avgDiff = totalAbsDiff / numPoints;
        double fitnessRange = maxFitness - minFitness;
        return 1 - (avgDiff / fitnessRange);
    }

    public static void main(String[] args) {
        int dim = 2;
        BinaryFlag bf = new BinaryFlag(dim);
//        RealProblem p = new Spherical(-100, 100, dim);
        int walkNum = 1;
        Walk w;
        int maxNumStepsInWalk = 50;
        double stepSize = 10;
        for (int i = 0; i < 4; i++) {
//            w = new Walk(p, WALKTYPE_RANDOM_PROGRESSIVE, maxNumStepsInWalk, stepSize, START_POSITION_SPECIFIED, bf);
            try {
                //     w.printPointsToFile("progressiveRandom" + (int)stepSize + "_" + walkNum + ".dat");
//                w.printPointsToFile("progressiveRandom" + (int) stepSize + "_" + walkNum + ".dat");
                //      w.printWalk();
            }
            catch (Exception e) {
            }
            walkNum++;
            bf.next();
        }
    }

}
