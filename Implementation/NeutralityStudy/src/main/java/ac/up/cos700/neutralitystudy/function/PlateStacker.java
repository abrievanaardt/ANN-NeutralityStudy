package ac.up.cos700.neutralitystudy.function;

import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;

/**
 * Implements the Stacking Plates optimisation scheme proposed by
 * <pre>
 * 
 * A. Owen and I. Harvey, "Adapting particle swarm optimisation for fitness
 * landscapes with neutrality" in Swarm Intelligence (SIS'07) IEEE Symposium
 * on, IEEE, 2007, pp. 258 - 265.
 * </pre>
 * todo: The fitness scoring mechanism can easily be adapted from a binary score
 * to one based on the inverse distance, which could lead to 'near' neutral
 * regions that will be useful to test a neutrality measure. This should be
 * investigated further.
 *
 * @author Abrie van Aardt
 */
public class PlateStacker implements IFunction {

    /**
     * Initialises the PlateStacker.
     *
     * @param _plateRadius the radius of individual plates
     */
    public PlateStacker(double _plateRadius) {
        plateRadius = _plateRadius;
    }

    @Override
    public int getDimensionality() {
        return -1;//signal variable dimensionality
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (x.length % 2 != 0)
            throw new UnequalArgsDimensionException("Sequence represents circle placement in 2D - Dimension has to be a multiple of 2");

        double xLeft;
        double yLeft;
        double xRight;
        double yRight;
        double distance;
        double score = 0;

        //determine how many plates (circles) overlap
        //award a point for each overlap
        for (int i = 0; i < x.length; i = i + 2) {
            xLeft = x[i];
            yLeft = x[i + 1];

            for (int j = 0; j < x.length; j = j + 2) {
                xRight = x[j];
                yRight = x[j + 1];

                distance = new Distance().evaluate(xLeft, yLeft, xRight, yRight);

                if (distance < 2 * plateRadius){
                    ++score;
                }                
            }
        }
        
        return score;
    }

    private final double plateRadius;

}
