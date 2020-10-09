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

import java.util.Collection;

/**
 * <p>This class is both a wrapper used to store items in the tree and the node  type used to build
 * the {@linkplain IndexingTree}.</p>
 *
 * @author Jacques Gignoux - 07-08-2018
 *
 * @param <T> the type of objects indexed, ie stored in these nodes
 */
public abstract class IndexingNode<T,N> {

	/** The parent node, i.e. the node immediately above in the tree.*/
	protected N parent = null;

	/**
	 * <p>The children nodes, i.e. the nodes immediately below in the tree, used to partition space.
	 * In every dimension, space is partitioned into two sub-spaces, so that there are 2<sup><em>n</em></sup>
	 * children if dimension = <em>n</em>.</p>
	 */
	protected N[] children = null;

//	/**
//	 * Store an item into this node. ---> moved to descendants
//	 *
//	 * @param item the item to store
//	 * @param loc the location (in space) where it should go
//	 * @return the node where the item was inserted, null if not inserted
//	 */
//	public abstract N insert(T item, Point loc);

	/**
	 * Will throw an Exception if more than one item is present in the node.
	 * @return the only or first item contained in this node.
	 */
	public abstract T item();

	/**
	 * CAUTION: this returns <em>all</em> items starting at this node, including all those of its children nodes.
	 * @return the list of items contained in the sub-tree starting at this node.
	 */
	public abstract Collection<T> items();

	/** removes all items contained in this node */
	public abstract void clear();

}