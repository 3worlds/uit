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
 * An optimised implementation of a Point in dimension 1.
 * 
 * @author Jacques Gignoux - 10-09-2018 
 *
 */
public class Point1D implements Point {

	private double x = 0.0;
	
	protected Point1D(double x1) {
		super();
		x=x1;
	}
	
	@Override
	public int dim() {
		return 1;
	}

	@Override
	public double coordinate(int i) {
		return x;
	}
	
	@Override
	public double x() {
		return x;
	}

	@Override
	public String toString() {
		return "["+x+"]";
	}

	@Override 
	public Point1D clone() {
		return new Point1D(x);
	}
	
	@Override
	public boolean equals(Object other) {
		if (Point1D.class.isAssignableFrom(other.getClass()))
			return (x==((Point1D)other).x);
		return false;
	}
	
}
