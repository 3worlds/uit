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

import java.util.Arrays;

/**
 * An implementation of Point in dimension <em>n</em>.
 * @author Jacques Gignoux - 07-08-2018 
 *
 */
//Tested OK on version 0.0.1 on 21/11/2018
class PointND implements Point {
	
	private double[] x = null;
		
	protected PointND(double...x1) {
		super();
		x = x1;
	}
	
	@Override
	public int dim() {
		return x.length;
	}
	
	@Override
	public double coordinate(int i) {
		return x[i];
	}
	
	@Override
	public double x() {
		return x[0];
	}
	
	@Override
	public double y() {
		return x[1];
	}
	
	@Override
	public double z() {
		return x[2];
	}

	@Override
	public String toString() {
		String s="[";
		for (int i=0; i<x.length; i++)
			if (i<x.length-1) 
				s += x[i]+",";
			else 
				s += x[i]+"]";
		return s;
	}
	
	@Override
	public PointND clone() {
		return new PointND(x);
	}
	
	@Override
	public double[] asArray() {		
		return x.clone();
	}
	
	public static Point valueOf(String s) {
		return Point.valueOf(s);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(x);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PointND))
			return false;
		PointND other = (PointND) obj;
		return Arrays.equals(x, other.x);
	}

}
