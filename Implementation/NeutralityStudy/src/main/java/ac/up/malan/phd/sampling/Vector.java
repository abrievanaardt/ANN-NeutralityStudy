package ac.up.malan.phd.sampling;

import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import java.util.Arrays;
import java.util.Random;

/* Position vector */
public class Vector implements Cloneable{
    
        private int dimension = 1; // dimension of vector respresenting the position
        private double[] x;  // array of doubles for the position vector
            
        // Create new Vector of zero values
        public Vector(int dim) {
            dimension = dim;
            x = new double[dimension];
            setZero();
        }
        
        public Vector(double[] _x) {
            dimension = _x.length;
            x = Arrays.copyOf(_x, x.length);            
        }
      
        public Object clone() throws CloneNotSupportedException {
            Vector vectorCopy = (Vector)super.clone();
            vectorCopy.dimension = this.dimension;
            vectorCopy.x = new double[dimension];
            System.arraycopy(this.x, 0, vectorCopy.x, 0, dimension);
            return vectorCopy;
        }
	
	public int getDimension() {
		return dimension;
	}
      
        // Set all component values to 0 (useful when Vector is used as a velocity)
        public void setZero () {
          for (int i=0; i<dimension; i++)
            x[i] = 0;
        }
      
        /* change the x value at position i in the vector to val */
        public void setReal(int i, double val) {
            if ((i < dimension) && (i >= 0))
            {
                x[i] = val;
            }
            else
                System.out.println("Invalid index according to dimension in Vector::setReal");
                    
        }
        
        /* get the value at position i in the vector */
        public double getReal(int i) {
            if ((i < dimension) && (i >= 0))
            {
                return x[i];
            }
            else
            {
                System.out.println("Invalid index according to dimension in Vector::getReal");
                return 0;
            }
            
        }
        
        /* string representation of the position vector */
        public String toString() {
            String s = "[ ";
            for (int i=0; i<dimension-1; i++)
                s = s + x[i] + " , ";
            s = s + x[dimension-1] + " ]";
            return s;
        }
	
	public double getEuclideanDistance(Vector x2) throws UnequalArgsDimensionException{
	    int dim2 = x2.getDimension();
	    if (dimension != dim2) 
		throw new UnequalArgsDimensionException();
	    double sumOfSquares = 0;
            for(int i=0; i<dim2; i++) {		
		sumOfSquares += ((x[i] - x2.x[i])*(x[i] - x2.x[i]));
	    }
	    return Math.sqrt(sumOfSquares);  
        }
	
	// getNewVectorWithNoise: adds random Gaussian noise with the given std. dev
	// to the current Vector point in all dimensions. Returns a new Vector 
	// object that has noise added.
	public Vector getNewVectorWithNoise() { return getNewVectorWithNoise(1);}
	
	public Vector getNewVectorWithNoise(double stdDev) {
		Vector noisyVector = new Vector(dimension);
		Random generator = new Random();
		double rand;
		for (int i=0; i<dimension; i++) {
			rand = generator.nextGaussian();
			rand *= stdDev;
			noisyVector.x[i] = x[i] + rand;
		}
		return noisyVector;
	}
        
        public double[] toArray(){
            return Arrays.copyOf(x, dimension);
        }        
        
}