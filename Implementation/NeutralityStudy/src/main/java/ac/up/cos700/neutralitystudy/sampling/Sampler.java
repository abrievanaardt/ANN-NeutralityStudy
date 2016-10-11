package ac.up.cos700.neutralitystudy.sampling;

import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.malan.phd.sampling.Walk;
import ac.up.malan.phd.sampling.util.SampleException;

/**
 *
 * @author Abrie van Aardt
 */
public interface Sampler {

    public Walk[] sample() throws SampleException;

    static public Sampler makeSampler(Type typeOfSampler, RealProblem _problem, int stepCount, double stepRatio) {
        switch (typeOfSampler) {
            case PROGRESSIVE_RANDOM_WALK:
                return new ProgressiveRandomWalkSampler(_problem, stepCount, stepRatio);
            default:
                return null;
        }
    }

    public enum Type {
        PROGRESSIVE_RANDOM_WALK
    };
}
