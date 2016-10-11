package ac.up.cos700.neutralitystudy.sampling;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.malan.phd.sampling.BinaryFlag;
import ac.up.malan.phd.sampling.Walk;
import ac.up.malan.phd.sampling.util.SampleException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class makes use of a progressive random walk to sample the landscape of
 * a given n-dimensional problem. A number of steps are performed with random
 * step sizes within a defined subset of the domain. The total number of walks
 * are equal to twice the problem dimension and starting zones are selected according
 * to the formula:
 * <pre>
 * (2^n)/n
 * </pre>
 * where is the number of walks performed.
 *
 * @author Abrie van Aardt
 */
public class ProgressiveRandomWalkSampler implements Sampler{

    public ProgressiveRandomWalkSampler(RealProblem _problem, int _stepCount, double _stepRatio) {
        problem = _problem;
        walkCount = 2*problem.getDimensionality();
        stepSize = (problem.getUpperBound() - problem.getLowerBound()) * _stepRatio;
        stepCount = _stepCount;
        startingZoneDelta = (int) (Math.pow(2, walkCount) / walkCount);
    }

    @Override
    public Walk[] sample() throws SampleException {     

        Walk[] walks = new Walk[walkCount];
        BinaryFlag startingZone;
        String startingBitMask;
        Walk w;

        for (int i = 0; i < walkCount; i++) {
            startingBitMask = Integer.toBinaryString(i * startingZoneDelta);
            startingZone = new BinaryFlag(problem.getDimensionality());

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
