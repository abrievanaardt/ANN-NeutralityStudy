package ac.up.cos700.neutralitystudy.function.problem;

import ac.up.cos700.neutralitystudy.function.Distance;
import ac.up.cos700.neutralitystudy.function.Function;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the Stacking Plates optimisation scheme proposed by
 * <pre>
 * A. Owen and I. Harvey, "Adapting particle swarm optimisation for fitness
 * landscapes with neutrality" in Swarm Intelligence (SIS'07) IEEE Symposium
 * on, IEEE, 2007, pp. 258 - 265.
 * </pre>
 *
 * todo: The fitness scoring mechanism can easily be adapted from a binary score
 * to one based on the inverse distance, which could lead to 'near' neutral
 * regions that will be useful to test a neutrality measure. This should be
 * investigated further.
 *
 * @author Abrie van Aardt
 */
public class PlateStacker extends RealProblem {

    /**
     * Initialises the PlateStacker.
     *
     * @param _plateRadius the radius of individual plates
     * @param lowerBound
     * @param upperBound
     * @param _dimensionality
     */
    public PlateStacker(double _plateRadius, double lowerBound, double upperBound, int _dimensionality)
            throws UnequalArgsDimensionException {

        super(lowerBound, upperBound, _dimensionality, 0);

        if (_dimensionality % 2 != 0 || _dimensionality < 4)
            throw new UnequalArgsDimensionException("Sequence represents circle placement in 2D - Dimension has to be a multiple of 2 and at least 4");

        plateRadius = _plateRadius;

        optimumX = null;//many optima, not important  

//        Logger
//                .getLogger(getClass().getName())
//                .log(Level.FINER, "Using plate radius: {0}", _plateRadius);
    }
    
    /**
     * Initialises the PlateStacker bounded by the unit square.
     * 
     * @param _plateRadius
     * @param _dimensionality
     * @throws UnequalArgsDimensionException 
     */
    public PlateStacker(double _plateRadius, int _dimensionality) 
            throws UnequalArgsDimensionException{
        this(_plateRadius, 0, 1, _dimensionality);
    }
    
    public PlateStacker(int _dimensionality) 
            throws UnequalArgsDimensionException{
        this(0, 0, 1, _dimensionality);//define radius with call
    }
    
    public void setPlateRadius(double _plateRadius){
        plateRadius = _plateRadius;
    }

    @Override
    public double evaluate(double... x) throws UnequalArgsDimensionException {
        if (dimensionality != x.length)
            throw new UnequalArgsDimensionException();

        double xLeft;
        double yLeft;
        double xRight;
        double yRight;
        double distance;
        double score = 0;

        //determine how many plates (circles) overlap
        //award a point for each non-overlap
        for (int i = 0; i < x.length; i = i + 2) {
            xLeft = x[i];
            yLeft = x[i + 1];

            for (int j = 2; j < x.length; j = j + 2) {
                xRight = x[j];
                yRight = x[j + 1];

                distance = new Distance().evaluate(xLeft, yLeft, xRight, yRight);

                if (distance >= 2 * plateRadius) {
                    ++score;
                }
            }
        }

        return score;
    }

    private double plateRadius;

}
