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
package fr.cnrs.iees.uit.indexing;

import fr.cnrs.iees.uit.space.Box;
import fr.cnrs.iees.uit.space.Point;

/**
 * As a {@link ExpandingRegionIndexingTree}, but using discrete coordinates internally instead
 * of continuous ones.
 * 
 * @author Jacques Gignoux - 13 oct. 2020
 *
 * @param <T> the type of objects indexed
 */
public class ExpandingLimitedPrecisionIndexingTree<T> extends LimitedPrecisionIndexingTree<T> {

	/**
	 * Constructor using an initial box to start with. This box can be later enlarged to fit
	 * items located outside the initial box. The precision argument is used to scale the
	 * coordinates internally: all coordinate values are multiplied by 1/precision and truncated 
	 * to {@code long}s, so that exact comparison of locations becomes possible. In this system, 
	 * two or more items can have exactly the same location.
	 * 
	 * @param domain the initial domain to start the tree with
	 * @param precision the precision of coordinates - a distance smaller than precision is considered 
	 * equal to zero
	 */
	public ExpandingLimitedPrecisionIndexingTree(Box domain, double precision) {
		super(domain, precision);
	}
	
	@Override
	public void insert(T item, Point at) {
        while (!regionContainsLocator(root.lowerBounds,root.upperBounds,factory.newLocator(at)))
        	root = root.expandRootRegion(at);
        super.insert(item, at);
	}

}
