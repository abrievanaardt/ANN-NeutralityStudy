package ac.up.cos700.neutralitystudy.sampling;

import ac.up.malan.phd.sampling.Walk;
import ac.up.malan.phd.sampling.util.SampleException;

/**
 *
 * @author Abrie van Aardt
 */
public abstract class Sampler {
    abstract public Walk[] sample() throws SampleException;
}
