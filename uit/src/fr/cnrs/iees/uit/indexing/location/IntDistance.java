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

import fr.cnrs.iees.uit.UitException;

/**
 * <p>Distance computations for Locators, ie based on {@code long}s.</p>
 * <p>NOTE: Methods returning  a <em>squared</em> distances always return an exact value
 * (as squares of {@code long}s), whereas methods returning <em>distances</em> always return a 
 * value truncated to the nearest long (as root squares of  {@code long}s).</p>
 * 
 * 
 * @author Jacques Gignoux - 8 oct. 2020
 *
 */
public class IntDistance {
	// to prevent any instantiation
	private IntDistance() {}
	
	/** utility: square
	 * 
	 * @param a the number to square
	 * @return a²
	 */
	public static long sqr(long a) {
		return a*a;
	}
	
	/** quick computation of distance in one dimension
	 * 
	 * @param x1 coordinate of first point
	 * @param x2 coordinate of second point
	 * @return the distance between the points
	 */
	public static long distance1D(long x1,long x2) {
		return Math.abs(x1-x2);
	}

	/** squared euclidian distance in 1D 
	 * 
	 * @param x1 coordinate of first point
	 * @param x2 coordinate of second point
	 * @return the square of the distance between the points
	 */
	public static long squaredEuclidianDistance(long x1, long x2) {
		return sqr(distance1D(x1,x2));
	}
	
	/** squared euclidian distance in 2D 
	 * 
	 * @param x1 first coordinate of first point
	 * @param y1 second coordinate of first point
	 * @param x2 first coordinate of second point
	 * @param y2 second coordinate of second point
	 * @return the square of the distance between the points
	 */
	public static long squaredEuclidianDistance(long x1, long y1, long x2, long y2) {
		return sqr(x1-x2)+sqr(y1-y2);
	}

	/** squared euclidian distance in 3D 
	 * 
	 * @param x1 first coordinate of first point
	 * @param y1 second coordinate of first point
	 * @param z1 third coordinate of first point
	 * @param x2 first coordinate of second point
	 * @param y2 second coordinate of second point
	 * @param z2 third coordinate of second point
	 * @return the square of the distance between the points
	 */
	public static long squaredEuclidianDistance(long x1, long y1, long z1, long x2, long y2, long z2) {
		return sqr(x1-x2)+sqr(y1-y2)+sqr(z1-z2);
	}

	/** squared euclidian distance in n dimensions using {@link Locator} 
	 * 
	 * @param p1 the first locator
	 * @param p2 the second locator
	 * @return the square of the distance between the locators
	 */
	public static long squaredEuclidianDistance(Locator p1, Locator p2) {
		if (p1.dim()!=p2.dim())
			throw new UitException("squaredEuclidianDistance: Arguments of different dimensions");
		long dist = 0L;
		for (int i=0; i<p1.dim(); i++)
			dist += sqr(p1.coordinate(i)-p2.coordinate(i));
		return dist;
	}

	/** euclidian distance in 1D. (Note: same as {@link IntDistance#distance1D distance1D(...)})
	 * 
	 * @param x1 coordinate of first point
	 * @param x2 coordinate of second point
	 * @return the distance between the points
	 */
	public static long euclidianDistance(long x1, long x2) {
		return distance1D(x1,x2);
	}
	
	/** euclidian distance in 2D 
	 * 
	 * @param x1 first coordinate of first point
	 * @param y1 second coordinate of first point
	 * @param x2 first coordinate of second point
	 * @param y2 second coordinate of second point
	 * @return the distance between the points
	 */
	public static long euclidianDistance(long x1, long y1, long x2, long y2) {
		return Math.round(Math.hypot(x2-x1,y2-y1)); // no inner overflow error
	}

	/** euclidian distance in 3D 
	 * 
	 * @param x1 first coordinate of first point
	 * @param y1 second coordinate of first point
	 * @param z1 third coordinate of first point
	 * @param x2 first coordinate of second point
	 * @param y2 second coordinate of second point
	 * @param z2 third coordinate of second point
	 * @return the distance between the points
	 */
	public static long euclidianDistance(long x1, long y1, long z1, long x2, long y2, long z2) {
		return Math.round(Math.sqrt(squaredEuclidianDistance(x1,y1,z1,x2,y2,z2)));
	}

	/** euclidian distance in  n dimensions using {@link Locator}
	 * 
	 * @param p1 the first locator
	 * @param p2 the second locator
	 * @return the distance between the locator
	 */
	public static long euclidianDistance(Locator p1, Locator p2) {
		return Math.round(Math.sqrt(squaredEuclidianDistance(p1,p2)));
	}	

}
