/**************************************************************************
 *  UIT - a Universal Indexing Tree                                       *
 *                                                                        *
 *  Copyright 2018: Jacques Gignoux & Ian D. Davies                       *
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  UIT is a generalisation and re-implementation of QuadTree and Octree  *
 *  implementations by Paavo Toivanen as downloaded on 27/8/2018 on       *
 *  <https://dev.solita.fi/2015/08/06/quad-tree.html>                     *
 *                                                                        *
 **************************************************************************                                       
 *  This file is part of UIT (Universal Indexing Tree).                   *
 *                                                                        *
 *  UIT is free software: you can redistribute it and/or modify           *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  UIT is distributed in the hope that it will be useful,                *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *                         
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with UIT.  If not, see <https://www.gnu.org/licenses/gpl.html>. *
 *                                                                        *
 **************************************************************************/
package fr.cnrs.iees.uit.indexing.location;

import fr.cnrs.iees.uit.space.Box;
import fr.cnrs.iees.uit.space.Dimensioned;
import fr.cnrs.iees.uit.space.Distance;
import fr.cnrs.iees.uit.space.Point;

/**
 * A factory for {@link Locator}s. Since locators use limited precision coordinates, a 
 * same precision must be used to generate comparable locator coordinates.
 * 
 * @author Jacques Gignoux - 8 oct. 2020
 *
 */
public class LocatorFactory implements Dimensioned {
	
	// grain of the locators
	private double precision;
	// dimension of this factory
	private int dim = 0;
	
	Box limits;
	
	/**
	 * <p>This constructor checks that precision*maxDistance is smaller than {@code Long.MAX_VALUE},
	 * to prevent any arithmetic overflow. maxDistance is computed as the diagonal length of the 
	 * Box passed as argument. Precision is re-adjusted if needed.</p>
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
		this.limits = limits;
		this.precision = precision; 
		dim = limits.dim();
	}
	
	/** 
	 * <p>converts a double coordinate into a long locator coordinate</p>
	 * 
	 * <p>Locator coordinates are computed (1) using the lower bound value of the factory limits
	 * as the zero for every dimension, and (2) so that a long value of 1 equals the precision
	 * on the original coordinate scale.</p>
	 * 
	 * @param coord the continuous, {@code double} coordinate
	 * @param dim the index of the dimension of the coordinate 
	 * @return the locator coordinate as {@code long}
	 */
	public long convert(double coord, int dim) {
		return Math.round((coord-limits.lowerBound(dim))/precision);
	}
	
	/**
	 * Converts a point to a locator with given precision (means all coordinates of P are truncated
	 * to 1/precision and transformed into longs).
	 * 
	 * @param P the point to convert
	 * @return the Locator matching P
	 */
	public Locator newLocator(Point P) {
		if (P.dim()!=dim)
			throw new IllegalArgumentException("Invalid operation: argument must have the same dimension as factory");
		long[] L = new long[dim];
		for (int i=0; i<P.dim(); i++)
			L[i] = convert(P.coordinate(i),i);
		return newLocator(L);
	}
	
	/**
	 * instantiate a new locator with long coordinates
	 * 
	 * @param x1 coordinate values
	 * @return a new {@code Locator} instance
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
	 * @param L the locator
	 * @return the point
	 */
	public Point toPoint(Locator L) {
		double[] P = new double[L.dim()];
		for (int i=0; i<L.dim(); i++)
			P[i] = L.coordinate(i)*precision;
		return Point.newPoint(P);
	}
	
	/**
	 * Accessor to precision of coordinates
	 * 
	 * @return the precision
	 */
	public double precision() {
		return precision;
	}
	
	@Override
	public int dim() {
		return dim;
	}
	
}
