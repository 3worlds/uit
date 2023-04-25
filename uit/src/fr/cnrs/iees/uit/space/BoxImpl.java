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

import java.util.Objects;

/**
 * <p>A straightforward implementation of {@link Box}.</p>
 * 
 * @author Jacques Gignoux - 07-08-2018 
 *
 */
//Tested OK with version 0.0.1 on 23/11/2018
class BoxImpl implements Box {
	
	private Point upper = null;
	private Point lower = null;
	// hash code stored for faster tests
	private int hash = 0;
	
	/**
	 * This constructor is unsafe because the Points passed as arguments must satisfy the condition that
	 * every coordinate of the lower Point is strictly lower than the matching coordinate
	 * of the upper Point. Hence the protected visibility. To build a box with any points, use the static methods
	 * Box.boundingBox() and Box.boundingCube() instead.
	 * @param lower
	 * @param upper
	 */
	protected BoxImpl(Point lower, Point upper, boolean check) {
		super();
		this.upper = upper;
		this.lower = lower;
		if (check) {
			if (lower.dim()!=upper.dim())
				throw new IllegalArgumentException("Error creating Box: upper and lower bounds must have the same dimension");
			for (int i=0; i<upper.dim(); i++)
				if (upper.coordinate(i)<lower.coordinate(i))
					throw new IllegalArgumentException("Error creating Box: upper bouds smaller than lower bounds");
		}
	}
	
	/**
	 * WARNING: This constructor should never be used - use the static factory methods of {@link Box} instead.
	 * @param lower
	 * @param upper
	 */
	protected BoxImpl(Point lower, Point upper) {
		this(lower,upper,true);
	}
	
	
	@Override
	public final int dim() {
		return upper.dim();
	}

	@Override
	public Point lowerBounds() {
		return lower;
	}

	@Override
	public Point upperBounds() {
		return upper;
	}

	@Override
	public double lowerBound(int i) {
		return lower.coordinate(i);
	}

	@Override
	public double upperBound(int i) {
		return upper.coordinate(i);
	}

	@Override
	public String toString() {
		return "["+lower.toString()+","+upper.toString()+"]";
	}

	@Override
	public double size() {
		if (lower.equals(upper))
			return 0.0;
		double size = 1.0;
		for (int i=0; i<upper.dim(); i++)
			size *= (upper.coordinate(i)-lower.coordinate(i));
		return size;
	}
	
	public static Box valueOf(String s) {
		return Box.valueOf(s);
	}

	@Override
	public int hashCode() {
		if (hash==0)
			hash = Objects.hash(lower,upper);;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Box))
			return false;
		Box other = (Box) obj;
		return Objects.equals(lower, other.lowerBounds()) 
			&& Objects.equals(upper, other.upperBounds());
	}
	
}
