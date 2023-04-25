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
package fr.cnrs.iees.uit.indexing.location;

import java.util.Objects;

/**
 * An optimised implementation of a Locator in dimension 1.
 * 
 * @author Jacques Gignoux - 10-09-2018 
 *
 */
//Tested OK on version 0.0.1 on 21/11/2018
class Locator1D implements Locator {

	private long x = 0L;
	private LocatorFactory factory;
	
	protected Locator1D(LocatorFactory  locf, long x1) {
		super();
		x=x1;
		factory = locf;
	}
	
	@Override
	public int dim() {
		return 1;
	}

	@Override
	public long coordinate(int i) {
		return x;
	}
	
	@Override
	public String toString() {
		return "["+x+"]";
	}

	@Override 
	public Locator1D clone() {
		return new Locator1D(factory,x);
	}

	@Override
	public LocatorFactory factory() {
		return factory;
	}

	@Override
	public int hashCode() {
		return Objects.hash(factory,x);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Locator1D))
			return false;
		Locator1D other = (Locator1D) obj;
		return factory == other.factory // factories must be the same 
			&& x == other.x;
	}

}
