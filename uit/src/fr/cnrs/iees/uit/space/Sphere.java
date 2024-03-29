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
 * <p>A generalised sphere in <em>n</em>-dimensional space (also called
 * <a href="https://en.wikipedia.org/wiki/N-sphere"><em>n</em>-sphere</a>). Basically, it represents a
 * set of points within a constant distance of a central point.
 * Immutable.</p>
 */
//Tested OK on version 0.0.1 on 21/11/2018
// but with one unimplemented method (overlaps(Box))
public interface Sphere extends Dimensioned {

	/**
	 *
	 * @return the centre of the sphere
	 */
	public abstract Point centre();

	/**
	 *
	 * @return the radius of the sphere
	 */
	public abstract double radius();

	/** 
	 * <p>Compute the length / surface / volume / hypervolume (according to dimension) of this sphere.</p>
	 * <p><strong>WARNING</strong>: works for dimension &lt;9 only.</p>
	 * 
	 * @return the size of the sphere for dimension &lt; 9 - throws an Exception for dimension &gt; 8
	 */
	public abstract double size();

	/**
	 * Test for wide containement, i.e. a point lying on the perimeter
	 * is considered inside the sphere.
	 *
	 * @param p a point to test for falling inside the sphere
	 * @return {@code true} if the point is contained in the sphere
	 */
	public default boolean contains(Point p) {
		return Distance.squaredEuclidianDistance(p,centre()) <= Distance.sqr(radius());
	}

	/**
	 * Test if a box is fully contained in this sphere (wide containment, cf. 
	 * {@link Sphere#contains(Point) contains(Point)}).	 *
	 * @param b a box to test for being fully contained in the sphere
	 * @return {@code true} if the point is contained in the sphere
	 */
	public default boolean contains(Box b) {
		return contains(b.upperBounds()) && contains(b.lowerBounds());
	}

	/**
	 * Test for overlapping with another sphere. This is not strict overlapping, i.e. two 
	 * tangent spheres (sharing only one point of their perimeter) will be considered overlapping.
	 *
	 * @param s another Sphere to test for overlap with this one
	 * @return {@code true} if the two Spheres overlap
	 */
	public default boolean overlaps(Sphere s) {
		return Distance.euclidianDistance(centre(),s.centre()) < radius()+s.radius();
	}


	/**
	 * <p>Test for overlapping with a box.</p>
	 * <p><strong>WARNING</strong>: this method is unfinished! do not use.</p>
	 *
	 * @param b a Box to test for overlap with this one
	 * @return {@code true} if this Sphere overlaps the Box
	 */
	public default boolean overlaps(Box b) {
		if (b.dim()!=dim())
			throw new IllegalArgumentException("overlaps: Arguments of different dimensions");
		// if the centre is within the box, there is overlap
		if (b.contains(centre()))
			return true;
		// if the centre is off the border from more than radius in any coordinate
		// axis direction (= perpendicular distance), then there is no overlap
		for (int i=0; i<dim(); i++)
			if (Distance.distance1D(centre().coordinate(i), b.lowerBound(i))>radius() ||
				Distance.distance1D(centre().coordinate(i), b.upperBound(i))>radius())
				return false;
		// this is the hard case where the centre is not within the box and the
		// perpendicular distance in all directions is smaller than the radius - there may
		// be overlap
		// TODO - check that any of the corners is inside the sphere. Pb: we do not have
		// all corners, only the two extremes.
		return false;
	}

	/**
	 * Computes the largest Sphere fully contained in a Box.
	 *
	 * @param b a Box
	 * @return an instance of a Sphere
	 */
	public static Sphere inSphere(Box b) {
		double smallSide = b.sideLength(0);
		for (int i=1; i<b.dim(); i++)
			smallSide = Math.min(smallSide, b.sideLength(i));
		return new SphereImpl(b.centre(),smallSide/2);
	}

	/**
	 * Computes the smallest Sphere containing a Box.
	 *
	 * @param b the Box
	 * @return an instance of a Sphere
	 */
	public static Sphere outSphere(Box b) {
		double radius = Distance.euclidianDistance(b.centre(), b.lowerBounds());
		return new SphereImpl(b.centre(),radius);
	}

	/**
	 * Build a {@code Sphere} instance from a point and a radius. 
	 * 
	 * @param centre the point to use as the centre
	 * @param radius the radius
	 * @return the new {@code Sphere} instance
	 */
	public static Sphere newSphere(Point centre, double radius) {
		return new SphereImpl(centre,radius);
	}

	/**
	 * Builds a {@code Sphere} instance from a {@link String} - assumes the String has been produced 
	 * previously with a call to {@code toString()} method of a {@code Sphere} implementation 
	 * 
	 * @param s the string to parse
	 * @return the new {@code Sphere} instance
	 */
	public static Sphere valueOf(String s) {
		if (s.trim().isEmpty())
			return null;
		// remove '[' and ']'
		s = s.trim().substring(1,s.trim().length()-1);
		// get the centre and radius
		String scentre = s.substring(0,s.indexOf("],"));
		String sradius = s.substring(s.indexOf("],")+2);
		Point centre = Point.valueOf(scentre+"]");
		double radius = Double.valueOf(sradius);
		// make the sphere
		return newSphere(centre,radius);
	}


}
