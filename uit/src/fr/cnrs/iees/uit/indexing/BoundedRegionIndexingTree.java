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

/**
 * <p>
 * A region-based <em>k</em>-d tree where the size of the initial region is
 * known and used to initialise the tree root node.
 * </p>
 *
 * <p>
 * <strong>NOTE</strong>: Compared to {@linkplain ExpandingRegionIndexingTree},
 * this implementation does not make any assumption on the length of the indexed
 * region in all dimensions: 'hyper-rectangular' domains can be used.
 * </p>
 *
 * @author Jacques Gignoux - 06-09-2018
 *
 * @param <T> the type of objects indexed
 */
// tested OK with version 0.0.1 on 26/11/2018
public class BoundedRegionIndexingTree<T> extends RegionIndexingTree<T> {

	/**
	 * This constructor assumes all items will be contained within the box passed as
	 * its argument.
	 * 
	 * @param domain the region indexed by this tree
	 */
	public BoundedRegionIndexingTree(Box domain) {
		super(domain);
		root = new RegionIndexingNode<T>(null, domain, this);
	}

}
