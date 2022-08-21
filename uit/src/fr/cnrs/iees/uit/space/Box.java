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

import fr.cnrs.iees.uit.UitException;

/**
 * <p>A rectangular box in a <em>n</em>-dimensional space, aligned on coordinate axes.</p>
 *
 * <p>This was designed to manipulate the concept of a finite block of space independent of
 * the space dimension. If dim=1, this will represent a segment, if dim=2 a rectangle, if dim=3
 * a rectangular cuboid, etc.</p>
 *
 * <p>The assumption of alignment with the coordinate axes allows important optimisations and
 * should not be released</p>
 *
 * <p>Immutable.</p>
 *
 * @author Jacques Gignoux - 07-08-2018
 *
 */
// Tested OK with version 0.0.1 on 23/11/2018
public interface Box extends Dimensioned {

	/**
	 * <p>In two dimensions, this would be the coordinates of the lower left corner of the box.</p>
	 *
	 * @return  the lower bounds of this Box.
	 */
	public abstract Point lowerBounds();

	/**
	 * <p>In two dimensions, this would be the coordinates of the upper right corner of the box.</p>
	 *
	 * @return  the upper bounds of this Box.
	 */
	public abstract Point upperBounds();

	/**
	 * Access to the coordinates of the lower bound of the box.
	 *
	 * @param i the index of the coordinate requested
	 * @return the i<sup>th</sup> coordinate of the lower bound of this box
	 */
	public abstract double lowerBound(int i);

	/**
	 * Access to the coordinates of the upper bound of the box.
	 *
	 * @param i the index of the coordinate requested
	 * @return the i<sup>th</sup> coordinate of the upper bound of this box
	 */
	public abstract double upperBound(int i);

	/**
	 * <p>Test for strict overlapping, i.e. boxes with a common edge are considered not
	 * overlapping. In <a href="https://en.wikipedia.org/wiki/Topology">topological</a> terms, 
	 * returns {@code true} if the closed sets represented by
	 * the boxes overlap.</p>
	 * @param other a box to test for overlap with this one
	 * @return {@code true} if this Box overlaps the one passed as argument
	 */
	public default boolean overlaps(Box other) {
		for (int i=0; i<dim(); i++) {
			if (upperBound(i)<=other.lowerBound(i))
				return false;
			if (lowerBound(i)>=other.upperBound(i))
				return false;
		}
		return true;
	}

	/**
	 * <p>Tests for wide containement, i.e. a point lying on the box edges
	 * is considered inside the box. In <a href="https://en.wikipedia.org/wiki/Topology">topological</a>
	 * terms, returns {@code true} if the point
	 * is in the open set represented by this box.</p>Instantiate
	 * @param p a point to test for falling into this box
	 * @return {@code true} if this box contains the point passed as argument
	 */
	public default boolean contains(Point p) {
		for (int i=0; i<dim(); i++)
			if ((p.coordinate(i)>upperBound(i)) || (p.coordinate(i)<lowerBound(i)))
				return false;
		return true;
	}

	/**
	 * Tests if a point is on the border of a box
	 * @param p a point to test for falling on the edge of the box
	 * @return {@code true} if the point is on the edge
	 */
	public default boolean isPointOnBorder(Point p) {
		if (contains(p)) {
			for (int i=0; i<dim(); i++)
				if ((p.coordinate(i)==upperBound(i)) || (p.coordinate(i)==lowerBound(i)))
					return true;
		}
		return false;
	}

	/**
	 * Test if a box is fully contained in this one (wide containment, cf. 
	 * {@link Box#contains(Point) contains(Point)}).
	 * 
	 * @param b a Box to test for fully being contained into this one
	 * @return {@code true} if this box contains the box passed as argument
	 */
	public default boolean contains(Box b) {
		return contains(b.lowerBounds()) && contains(b.upperBounds());
	}

	/**
	 *	Compute the centroid of this box.
	 *
	 * @return the centroid of this box.
	 */
	public default Point centre() {
		int dim = lowerBounds().dim();
		double[] c = new double[dim];
		for (int i=0; i<dim; i++)
			c[i] = (lowerBound(i)+upperBound(i))/2;
		return Point.newPoint(c);
	}

	/** 
	 * Compute the length / surface / volume / hypervolume (according to dimension) of this box.
	 * 
	 * @return the box size relevant to its space  
	 * */
	public abstract double size();

	/**
	 * Build the smallest box containing two points. Use this method to build a {@code Box} 
	 * without checking if the {@code Point}s suitably contain lower and upper values only.
	 * Of course the points must be of the same dimension, otherwise an Exception is thrown.
	 *
	 * @param A a Point
	 * @param B another Point
	 * @return the smallest Box containing points A and B.
	 */
	public static Box boundingBox(Point A, Point B) {
		if (A.dim()!=B.dim())
			throw new UitException("Invalid operation: boundingBox() on points of different dimensions");
		int dim = A.dim();
		double[] ups = new double[dim];
		double[] lows = new double[dim];
		for (int i=0; i<dim; i++) {
			lows[i] = Math.min(A.coordinate(i), B.coordinate(i));
			ups[i] = Math.max(A.coordinate(i), B.coordinate(i));
		}
		return new BoxImpl(Point.newPoint(lows),Point.newPoint(ups));
	}

	/**
	 * Test if a {@code Sphere} is fully contained in this box (wide containment, cf. 
	 * {@link Box#contains(Point) contains(Point)}).
	 *
	 * @param s a Sphere to test for fully being contained into this one
	 * @return {@code true} if this box contains the sphere passed as argument
	 */
	public default boolean contains(Sphere s) {
		for (int i=0; i<dim(); i++)
			if ((s.centre().coordinate(i)-s.radius()<lowerBound(i)) ||
				(s.centre().coordinate(i)+s.radius()>upperBound(i)))
				return false;
		return true;
	}

	/**
	 * Build the smallest Box containing a Sphere.
	 *
	 * @param s the Sphere to wrap into a Box
	 * @return the smallest Box containing Sphere s
	 */
	public static Box boundingBox(Sphere s) {
		return boundingBox(Point.add(s.centre(),-s.radius()),Point.add(s.centre(),s.radius()));
	}

	/**
	 * Build the smallest <em>(hyper)cubic</em> Box containing a Sphere. By <em>cubic</em> we mean a Box
	 * having the same side length in all dimensions.
	 * <p>Note: yes, this is returning the same result as {@link Box#boundingBox(Sphere) boundingBox(Sphere)}.
	 * Thank you for your careful reading.</p>
	 *
	 * @param s the Sphere to wrap into a Box
	 * @return the smallest Box containing Sphere s
	 */
	public static Box boundingCube(Sphere s) {
		return boundingBox(s);
	}

	/**
	 * Build the smallest <em>(hyper)cubic</em> Box containing two points. By <em>cubic</em> we mean a Box
	 * having the same side length in all dimensions.
	 *
	 * @param A a Point
	 * @param B another Point
	 * @return the smallest cubic Box containing points A and B.
	 */
	public static Box boundingCube(Point A, Point B) {
		Box b = boundingBox(A,B);
		double maxSide = 0.0;
		for (int i=0; i<b.dim(); i++)
			maxSide = Math.max(maxSide, b.upperBound(i)-b.lowerBound(i));
		return new BoxImpl(b.lowerBounds(),Point.add(b.lowerBounds(),maxSide));
	}

	/**
	 * Access to the length of any side of the box.
	 *
	 * @param i the index of the requested side length
	 * @return the length of the side of the box in i<sup>th</sup> dimension
	 */
	public default double sideLength(int i) {
		return (upperBound(i)-lowerBound(i));
	}

	/**
	 * Test if this box is (hyper)cubic, i.e. if all its side lengths are equal.
	 * A hypercube is a generalisation of a cube to n&gt;3 dimensions. 
	 * 
	 * <p>NB: as you noticed it, this method always returns {@code true}
	 * in dimension 1.</p>
	 * 
	 * @return {@code true} if this Box is a square, cube or hypercube.
	 */
	public default boolean isCube() {
		for (int i=1; i<dim(); i++)
			if (sideLength(i)!=sideLength(i-1))
				return false;
		return true;
	}

	/**
	 * Builds a {@code Box} instance from a {@link String} - assumes the String has been produced 
	 * previously with a call to {@code toString()} method of a {@code Box} implementation 
	 * 
	 * @param s the string to parse
	 * @return the new {@code Box} instance
	 */
	public static Box valueOf(String s) {
		if (s.trim().isEmpty())
			return null;
		// remove '[' and ']'
		s = s.trim().substring(1,s.trim().length()-1);
		// get the two points
		String slower = s.substring(0,s.indexOf("],["));
		String supper = s.substring(s.indexOf("],[")+3);
		Point lower = Point.valueOf(slower+"]");
		Point upper = Point.valueOf("["+supper);
		// make the box
		return boundingBox(lower,upper);
	}
}
