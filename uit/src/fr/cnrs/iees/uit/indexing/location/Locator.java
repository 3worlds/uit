package fr.cnrs.iees.uit.indexing.location;

import fr.cnrs.iees.uit.UitException;
import fr.cnrs.iees.uit.space.Dimensioned;
import fr.cnrs.iees.uit.space.Point;

/**
 * <p>The class used to define locations in a Quadtree. It uses integer (long) coordinates
 * so that identical locations can be tested without ambiguity. Immutable.</p>
 * <p>This class should not be used outside of uit and should not be saved to files. It should always
 * be constructed from Points.</p>
 * <p>Caution: locators should only be added if they have the same dimension</p>
 * 
 * @author Jacques Gignoux - 8 oct. 2020
 *
 */
// Tested OK on 8/10/2020
public interface Locator extends Dimensioned, Cloneable {
	
	/** @return i<sup>th</sup> coordinate of the locator */
	public abstract long coordinate(int i);

	/** clone a Locator */
	public abstract Locator clone();

	/** return a new Locator addition of the coordinates of two Locators of same dimension */
	public static Locator add(Locator A, Locator B)  {
		if (A.factory()!=B.factory())
			throw new UitException("Invalid operation: addition of locators of different origins");
		long[] x = new long[A.dim()];
		for (int i=0; i<x.length; i++)
			x[i] += A.coordinate(i)+B.coordinate(i);
		return A.factory().newLocator(x);
	}
	
	/** return a new Locator with a scalar added to every coordinate */
	public static Locator add(Locator A, long scalar) {
		long[] x = new long[A.dim()];
		for (int i=0; i<x.length; i++)
			x[i] = A.coordinate(i)+scalar;
		return A.factory().newLocator(x);
	}
	
	/** return a new Point with a scalar added to coordinate i */
	public static Locator add(Locator A, long scalar, int i) {
		long[] x = new long[A.dim()];
		for (int j=0; j<x.length; j++) {
			x[j] = A.coordinate(j);
			if (j==i)
				x[j] += scalar;
		}
		return A.factory().newLocator(x);
	}

	public LocatorFactory factory();
	
	public default Point toPoint() {
		return factory().toPoint(this);
	}
	
}
