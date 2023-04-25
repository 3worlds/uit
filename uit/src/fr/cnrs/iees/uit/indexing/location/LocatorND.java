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

import java.util.Arrays;
import java.util.Objects;

/**
 * An implementation of Locator for <em>n</em> dimensions.
 * 
 * @author Jacques Gignoux - 07-08-2018
 *
 */
class LocatorND implements Locator {
	
	private long[] x = null;
	private LocatorFactory factory;
		
	protected LocatorND(LocatorFactory locf, long...x1) {
		super();
		x = x1;
		factory = locf;
	}
	
	@Override
	public int dim() {
		return x.length;
	}
	
	@Override
	public long coordinate(int i) {
		return x[i];
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
	public LocatorND clone() {
		return new LocatorND(factory,x);
	}
	
	@Override
	public LocatorFactory factory() {
		return factory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(x);
		result = prime * result + Objects.hash(factory);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof LocatorND))
			return false;
		LocatorND other = (LocatorND) obj;
		return factory == other.factory 
			&& Arrays.equals(x, other.x);
	}

}
