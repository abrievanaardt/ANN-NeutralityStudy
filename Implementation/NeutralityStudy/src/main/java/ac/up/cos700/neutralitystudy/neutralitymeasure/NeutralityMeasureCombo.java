package ac.up.cos700.neutralitystudy.neutralitymeasure;

import ac.up.malan.phd.sampling.Walk;

/**
 * A combination of neutrality two neutrality measures
 *
 * @author Abrie van Aardt
 */
public class NeutralityMeasureCombo extends NeutralityMeasure {

    /**
     * Instantiates a neutrality measure that is a combination of _m1 and _m2
     *
     * @param _m1 first neutrality measure
     * @param _m2 second neutrality measure
     * @param _mixRatio a value between 0 and 1 indicating influence of _m1 over
     * _m2
     */
    public NeutralityMeasureCombo(NeutralityMeasure _m1, NeutralityMeasure _m2, double _mixRatio) {
        super();
        mixRatio = _mixRatio;
        m1 = _m1;
        m2 = _m2;
        name = "Combo_" + m1.getMeasureName() + "_" + m2.getMeasureName();
    }

    @Override
    public double measure(Walk[] samples, double epsilon) {

        double weightedMeasure
                = mixRatio * (m1.measure(samples, epsilon))
                + (1 - mixRatio) * (m2.measure(samples, epsilon));

        return weightedMeasure;
    }

    private final double mixRatio;
    private final NeutralityMeasure m1;
    private final NeutralityMeasure m2;
}
