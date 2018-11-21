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
 * An interface and a factory for <em>n</em>-dimensional points. Immutable.
 * 
 * @author Jacques Gignoux - 07-08-2018 
 *
 */
// Tested OK on version 0.0.1 on 21/11/2018
public interface Point extends Dimensioned, Cloneable {
	
	/** @return i<sup>th</sup> coordinate of the point*/ 
	public abstract double coordinate(int i);
	
	/** for convenience: returns the first coordinate as x */
	public default double x() {
		return Double.NaN;
	}
	
	/** for convenience: returns the second coordinate as y */
	public default double y() {
		return Double.NaN;
	}
	
	/** for convenience: returns the third coordinate as z */
	public default double z() {
		return Double.NaN;
	}
	
	/** instantiate a new Point. NB: only this method should be used to instantiate Points */
	public static Point newPoint(double...x1) {
		switch (x1.length) {
		case 0: return null;
		case 1: return new Point1D(x1[0]);
		case 2: return new Point2D(x1[0],x1[1]);
		case 3: return new Point3D(x1);
		}
		return new PointND(x1);
	}
	
	/** clone a Point */
	public abstract Point clone();

	/** return a new point addition of the coordinates of two Points of same dimension */
	public static Point add(Point A, Point B)  {
		if (A.dim()!=B.dim())
			throw new UitException("Invalid operation: addition of points of different dimensions");
		int dim = A.dim();
		double[] x = new double[dim];
		for (int i=0; i<dim; i++)
			x[i] = A.coordinate(i) + B.coordinate(i);
		return Point.newPoint(x);
	}
	
	/** return a new Point with a scalar added to every coordinate */
	public static Point add(Point A, double scalar) {
		int dim = A.dim();
		double[] x = new double[dim];
		for (int i=0; i<dim; i++)
			x[i] = A.coordinate(i) + scalar;
		return Point.newPoint(x);
	}
}
