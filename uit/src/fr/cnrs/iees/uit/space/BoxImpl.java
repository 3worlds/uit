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
 * <p>An straightforward implementation of {@linkplain Box}.</p>
 * 
 * @author Jacques Gignoux - 07-08-2018 
 *
 */
public class BoxImpl implements Box {
	
	private Point upper = null;
	private Point lower = null;
	
	public BoxImpl(Point lower, Point upper) {
		super();
		this.upper = upper;
		this.lower = lower;
		if (lower.dim()!=upper.dim())
			throw new UitException("Error creating Box: upper and lower bounds must have the same dimension");
		for (int i=0; i<upper.dim(); i++)
			if (upper.coordinate(i)<lower.coordinate(i))
				throw new UitException("Error creating Box: upper bouds smaller than lower bounds");
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
	public boolean equals(Object other) {
		if (Box.class.isAssignableFrom(other.getClass())) {
			Box b = (Box) other;
			return (upper.equals(b.upperBounds()) && lower.equals(b.lowerBounds()));
		}
		return false;
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
	
}
