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
package fr.cnrs.iees.uit.space;

/**
 * <p>Various ways of computing an <a href="https://en.wikipedia.org/wiki/Euclidean_distance">Euclidian distance</a> 
 * (static methods).</p>
 * 
 * <p>NOTE: given the effect of rounding errors on number comparisons, we implemented a discrete
 * version of this using {@code long}s (cf. {@link fr.cnrs.iees.uit.indexing.location.IntDistance IntDistance}).</p>
 * 
 * @author Jacques Gignoux - 07-08-2018 
 *
 */
// tested OK with version 0.0.1 on 21/11/2018
public class Distance {
	
	// to prevent any instantiation
	private Distance() {}
	
	/** utility: square 
	 * 
	 * @param a the number to square
	 * @return a²
	 */
	public static double sqr(double a) {
		return a*a;
	}

	/** utility: square 
	 * 
	 * @param a the number to square
	 * @return a²
	 */
	public static long sqr(long a) {
		return a*a;
	}

	/**
	 * utility: sum of squares
	 * 
	 * @param a the numbers to square and sum
	 * @return sum of a²'s
	 */
	public static double sumOfSquares(double... a) {
		double sum = 0.0;
		for (int i=0; i<a.length; i++)
			sum += a[i]*a[i];
		return sum;
	}

	/**
	 * utility: sum of squares
	 * 
	 * @param a the numbers to square and sum
	 * @return sum of a²'s
	 */
	public static double sumOfSquares(long... a) {
		long sum = 0L;
		for (int i=0; i<a.length; i++)
			sum += a[i]*a[i];
		return sum;
	}
	
	/** quick computation of distance in one dimension
	 * 
	 * @param x1 coordinate of first point
	 * @param x2 coordinate of second point
	 * @return the distance between the points
	 */
	public static double distance1D(double x1,double x2) {
		return Math.abs(x1-x2);
	}

	/** squared euclidian distance in 1D 
	 * 
	 * @param x1 coordinate of first point
	 * @param x2 coordinate of second point
	 * @return the square of the distance between the points
	 */
	public static double squaredEuclidianDistance(double x1, double x2) {
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
	public static double squaredEuclidianDistance(double x1, double y1, double x2, double y2) {
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
	public static double squaredEuclidianDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
		return sqr(x1-x2)+sqr(y1-y2)+sqr(z1-z2);
	}

	/** squared euclidian distance in n dimensions using {@link Point} 
	 * 
	 * @param p1 the first point
	 * @param p2 the second point 
	 * @return the square of the distance between the points
	 */
	public static double squaredEuclidianDistance(Point p1, Point p2) {
		if (p1.dim()!=p2.dim())
			throw new IllegalArgumentException("squaredEuclidianDistance: Arguments of different dimensions");
		double dist = 0.0;
		for (int i=0; i<p1.dim(); i++)
			dist += sqr(p1.coordinate(i)-p2.coordinate(i));
		return dist;
	}

	/** euclidian distance in 1D. (Note: same as {@link Distance#distance1D distance1D(...)})
	 * 
	 * @param x1 coordinate of first point
	 * @param x2 coordinate of second point
	 * @return the distance between the points
	 */
	public static double euclidianDistance(double x1, double x2) {
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
	public static double euclidianDistance(double x1, double y1, double x2, double y2) {
		return Math.hypot(x2-x1,y2-y1);
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
	public static double euclidianDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt(squaredEuclidianDistance(x1,y1,z1,x2,y2,z2));
	}

	/** euclidian distance in  n dimensions using {@link Point}
	 * 
	 * @param p1 the first point
	 * @param p2 the second point 
	 * @return the distance between the points
	 */
	public static double euclidianDistance(Point p1, Point p2) {
		return Math.sqrt(squaredEuclidianDistance(p1,p2));
	}

	// helper for distanceToClosestEdge(Point,Box) - recursive
	private static double distanceToClosestEdge(boolean in, int dim, double dist, Point p, Box b) {
		if (in) { // coming from in
			if ((p.coordinate(dim)<b.lowerBound(dim))||(p.coordinate(dim)>b.upperBound(dim))) {
				// now out
				double d = Math.min(Distance.distance1D(p.coordinate(dim),b.lowerBound(dim)), 
						Distance.distance1D(p.coordinate(dim),b.upperBound(dim)));
				dist = d; 
				in = false;
			}
			else { 
				// now in
				double d = Math.min(Distance.distance1D(p.coordinate(dim),b.lowerBound(dim)), 
						Distance.distance1D(p.coordinate(dim),b.upperBound(dim)));
				dist = Math.min(d, dist);
				in = true;
			}
		}
		else { // coming from out
			if ((p.coordinate(dim)<b.lowerBound(dim))||(p.coordinate(dim)>b.upperBound(dim))) {
				// now out
				double d = Math.min(Distance.distance1D(p.coordinate(dim),b.lowerBound(dim)), 
						Distance.distance1D(p.coordinate(dim),b.upperBound(dim)));
				dist = Math.sqrt(dist*dist+d*d);
				in = false;
			}
			else { 
				// now in: dist = previous dist, no change
				in = true;
			}
		}
		if (dim==p.dim()-1)
			return dist;
		else 
			return distanceToClosestEdge(in,dim+1,dist,p,b);
	}
	
	/**
	 * <p>Computes the distance of a {@link Point} to the closest edge of a {@link Box}.</p>
	 * <p>This is not as trivial as it seems. Each edge is a finite segment in its dimension, so that if
	 * the distance can be computed as a perpendicular distance (the intuitive way) only if a
	 * perpendicular line can be drawn between that point and the segment that represents the 
	 * box edge. In other words,</p>
	 * 
	 * <img src="{@docRoot}/../doc/images/distance-to-closest-edge.svg" width="200" 
	 	alt="the space classes"/>
	 * <ol> 
	 * <li>If the point is inside the box, then it returns the
	 * <strong>perpendicular distance</strong> to the closest edge.</li>
	 * <li>If the point is outside the box but a perpendicular distance can be drawn to
	 * one or more edges (meaning that in some dimensions the point falls within the box side width):
	 *   <ul>
	 *   <li>"Inner" distances are ignored.</li>
	 *   <li>The returned distance is the <strong>square root of the sum of the squared perpendicular 
	 * "outer" distances</strong>.</li>
	 *   <em>NB: this complexity arises in dimensions &gt;2</em>
	 *   </ul>
	 * </li> 
	 * <li>
	 * If the point is outside the box so that all its components in all dimensions are
	 * also outside the box, the returned distance is <strong>square root of the sum of the squared perpendicular 
	 * distances to all edges.</strong>.
	 * </li>
	 * </ol>
	 * 
	 * @param p the point
	 * @param b the box
	 * @return the distance
	 */
	public static double distanceToClosestEdge(Point p, Box b) {
		if (p.dim()!=b.dim())
			throw new IllegalArgumentException("distanceToClosestEdge: Arguments of different dimensions");
		double dist = Math.min(Distance.distance1D(p.coordinate(0),b.lowerBound(0)), 
				Distance.distance1D(p.coordinate(0),b.upperBound(0)));
		if ((p.coordinate(0)<b.lowerBound(0))||(p.coordinate(0)>b.upperBound(0)))
			return distanceToClosestEdge(false,1,dist,p,b);
		else
			return distanceToClosestEdge(true,1,dist,p,b);
	}
	
}
