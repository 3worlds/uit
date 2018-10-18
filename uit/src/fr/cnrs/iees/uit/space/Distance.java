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
 * Various ways of computing a distance
 * @author Jacques Gignoux - 07-08-2018 
 *
 */
public class Distance {
	
	
	/** utility: square */
	public static double sqr(double a) {
		return a*a;
	}
	
	/** quick computation of distance in one dimension */
	public static double distance1D(double x1,double x2) {
		return Math.abs(x1-x2);
	}

	/** squared euclidian distance in 1D */
	public static double squaredEuclidianDistance(double x1, double x2) {
		return sqr(distance1D(x1,x2));
	}
	
	/** squared euclidian distance in 2D */
	public static double squaredEuclidianDistance(double x1, double y1, double x2, double y2) {
		return sqr(x1-x2)+sqr(y1-y2);
	}

	/** squared euclidian distance in 3D */
	public static double squaredEuclidianDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
		return sqr(x1-x2)+sqr(y1-y2)+sqr(z1-z2);
	}

	/** squared euclidian distance in nD using Point */
	public static double squaredEuclidianDistance(Point p1, Point p2) {
		double dist = 0.0;
		for (int i=0; i<p1.dim(); i++)
			dist += sqr(p1.coordinate(i)-p2.coordinate(i));
		return dist;
	}

	/** euclidian distance in 1D */
	public static double euclidianDistance(double x1, double x2) {
		return distance1D(x1,x2);
	}
	
	/** euclidian distance in 2D */
	public static double euclidianDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(squaredEuclidianDistance(x1,y1,x2,y2));
	}

	/** euclidian distance in 3D */
	public static double euclidianDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt(squaredEuclidianDistance(x1,y1,z1,x2,y2,z2));
	}

	/** euclidian distance in nD using Point */
	public static double euclidianDistance(Point p1, Point p2) {
		return Math.sqrt(squaredEuclidianDistance(p1,p2));
	}

	/**  distance of a point to the closest edge in a box 
	 *  CAUTION: no check on dimension of p and b - must be the same */
	public static double distanceToClosestEdge(Point p, Box b) {
		double dist = Double.MAX_VALUE;
		for (int i=0; i<p.dim(); i++) {
			dist = Math.min(dist, distance1D(p.coordinate(i),b.lowerBound(i)));
			dist = Math.min(dist, distance1D(p.coordinate(i),b.upperBound(i)));
		}
		return dist;
	}
	
}
