package ac.up.cos700.neutralitystudy.neutralitymeasure;

import ac.up.malan.phd.sampling.Walk;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A combination of neutrality measure 1 and 2
 *
 * @author Abrie van Aardt
 */
public class NeutralityMeasureCombo extends NeutralityMeasure {

    public NeutralityMeasureCombo(NeutralityMeasure _m1, NeutralityMeasure _m2, double _mixRatio) {
        super(0.0);
        mixRatio = _mixRatio;
        m1 = _m1;
        m2 = _m2;
    }

    @Override
    public double measure(Walk[] samples) {

        double weightedMeasure
                = mixRatio * (m1.measure(samples))
                + (1 - mixRatio) * (m2.measure(samples));

        return weightedMeasure;
    }

    private final double mixRatio;
    private final NeutralityMeasure m1;
    private final NeutralityMeasure m2;
}
