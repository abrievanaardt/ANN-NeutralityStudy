package ac.up.cos700.neutralitystudy.sampling;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.malan.phd.sampling.BinaryFlag;
import ac.up.malan.phd.sampling.Walk;
import ac.up.malan.phd.sampling.util.SampleException;

/**
 * This class makes use of a progressive random walk to sample the landscape of
 * a given n-dimensional problem. A number of steps are performed with random
 * step sizes within a defined subset of the domain. The total number of walks
 * are equal to twice the problem dimension and starting zones are selected according
 * to the formula:
 * <pre>
 * (2^n)/n
 * </pre>
 * where n is the dimension of the problem.
 *
 * @author Abrie van Aardt
 */
public class ProgressiveRandomWalkSampler implements Sampler{

    public ProgressiveRandomWalkSampler(RealProblem _problem, int _stepCount, double _stepRatio) {
        problem = _problem;
        //todo: extract magic number to config file
        walkCount = 2*problem.getDimensionality();
        stepSize = (problem.getUpperBound() - problem.getLowerBound()) * _stepRatio;
        stepCount = _stepCount;
        startingZoneDelta = (int) Math.ceil(Math.pow(2, problem.getDimensionality()) / (double) walkCount);
    }

    @Override
    public Walk[] sample() throws SampleException {     

        Walk[] walks = new Walk[walkCount];
        BinaryFlag startingZone;
        String startingBitMask;
        Walk w;

        for (int i = 0; i < walkCount; i++) {
            startingBitMask = Integer.toBinaryString((i * startingZoneDelta) % (int) Math.pow(2, problem.getDimensionality()));
            startingZone = new BinaryFlag(problem.getDimensionality());

            //ensure that startingBitMask is the same length as startingZone, for correct indexing
            while (startingBitMask.length() != problem.getDimensionality()){
                startingBitMask = "0" + startingBitMask;
            }
            
            for (int j = 0; j < startingBitMask.length(); j++) {
                if (startingBitMask.charAt(j) == '1')
                    startingZone.flipBit(j);
            }

            w = new Walk(problem, Walk.WALKTYPE_RANDOM_PROGRESSIVE, stepCount, stepSize, Walk.START_POSITION_SPECIFIED, startingZone);
            
            walks[i] = w;
        }
        return walks;
    }

    private final int walkCount;
    private final double stepSize;
    private final int stepCount;
    private final RealProblem problem;
    private final int startingZoneDelta;
}
