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
 * <a href="https://en.wikipedia.org/wiki/N-sphere"><em>n</em>-sphere</a>. Basically, it represents a
 * set of points within a constant distance of a central point.
 * Immutable.</p>
 */
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
	
	/** returns the length / surface / volume / hypervolume (according to dimension) of this sphere */
	public abstract double size();

	/**
	 * 
	 * @param p a Point to test for being inside the Sphere
	 * @return {@code true} if the Point is contained in the Sphere
	 */
	public default boolean contains(Point p) {
		return Distance.squaredEuclidianDistance(p,centre()) <= Distance.sqr(radius());
	}

	/**
	 * 
	 * @param b a Box to test for being fully contained in the Sphere
	 * @return {@code true} if the Point is contained in the Sphere
	 */
	public default boolean contains(Box b) {
		return contains(b.upperBounds()) && contains(b.lowerBounds());
	}

	/**
	 * 
	 * @param s another Sphere to test for overlap with this one 
	 * @return {@code true} if the two Spheres overlap
	 */
	public default boolean overlaps(Sphere s) {
		return Distance.euclidianDistance(centre(),s.centre()) < radius()+s.radius();
	}
	
	
	/**
	 * 
	 * @param b a Box to test for overlap with this one 
	 * @return {@code true} if this Sphere overlaps the Box
	 */
	public default boolean overlaps(Box b) {
		// TODO
		return false;
	}
	
	/**
	 * Computes the largest Sphere fully contained in a Box.
	 * 
	 * @param b a Box
	 * @return an instance of a Sphere
	 */
	public static Sphere inSphere(Box b) {
		// TODO
		return null;
	}
	
	/**
	 * Computes the smallest Sphere containing a Box.
	 * 
	 * @param b the Box
	 * @return an instance of a Sphere
	 */
	public static Sphere outSphere(Box b) {
		// TODO
		return null;
	}
	
	
	
}
