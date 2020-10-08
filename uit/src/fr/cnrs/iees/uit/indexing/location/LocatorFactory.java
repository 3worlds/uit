package fr.cnrs.iees.uit.indexing.location;

import fr.cnrs.iees.uit.UitException;
import fr.cnrs.iees.uit.space.Box;
import fr.cnrs.iees.uit.space.Dimensioned;
import fr.cnrs.iees.uit.space.Distance;
import fr.cnrs.iees.uit.space.Point;

/**
 * 
 * @author Jacques Gignoux - 8 oct. 2020
 *
 */
public class LocatorFactory implements Dimensioned {
	
	// grain of the locators
	private int grain;
	// dimension of this factory
	private int dim = 0;
	
	/**
	 * This constructor checks that precision*maxDistance is smaller than the longest possible Long,
	 * to prevent any arithmetic overflow. maxDistance is computed as the diagonal length of the 
	 * Box passed as argument
	 * 
	 * @param precision the precision at which coordinates are truncated (eg 0.001 or 10.0)
	 * @param limits the size of the space this factory is going to work with
	 */
	public LocatorFactory(double precision, Box limits) {
		super();
		double maxDistance = Distance.squaredEuclidianDistance(limits.lowerBounds(), limits.upperBounds());
		double p = precision;
		if (maxDistance/p>Long.MAX_VALUE) {
			while (maxDistance/p>Long.MAX_VALUE)
				p = p*10.0;
			// issue warning "Space too large for required precision - precision adjusted to ..."
		}
		grain = (int) Math.round(1.0/p);
		dim = limits.dim();
	}
	
	/**
	 * Converts a point to a locator with given precision (means all coordinates of P are truncated
	 * to 1/precision and transformed into longs).
	 * 
	 * @param P the point to convert
	 * @param precision the precision to use
	 * @return the Locator matching P
	 */
	public Locator newLocator(Point P) {
		if (P.dim()!=dim)
			throw new UitException("Invalid operation: argument must have the same dimension as factory");
		long[] L = new long[dim];
		for (int i=0; i<P.dim(); i++)
			L[i] = Math.round(P.coordinate(i)*grain);
		return newLocator(L);
	}
	
	/**
	 * instantiate a new locator with long coordinates
	 * 
	 * @param x1
	 * @return
	 */
	public Locator newLocator(long...x1) {
		switch (x1.length) {
			case 0: return null;
			case 1: return new Locator1D(this,x1[0]);
		}
		return new LocatorND(this,x1);
	}

	/**
	 * Converts a Locator to a Point
	 * 
	 * @param L
	 * @param precision
	 * @return
	 */
	public Point toPoint(Locator L) {
		double[] P = new double[L.dim()];
		for (int i=0; i<L.dim(); i++)
			P[i] = L.coordinate(i)*1.0/grain;
		return Point.newPoint(P);
	}
	
	public int grain() {
		return grain;
	}
	
	public double precision() {
		return 1.0/grain;
	}

	@Override
	public int dim() {
		return dim;
	}
	
}
