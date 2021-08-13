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

import fr.cnrs.iees.uit.space.Box;
import fr.cnrs.iees.uit.space.Dimensioned;
import fr.cnrs.iees.uit.space.Point;
import fr.cnrs.iees.uit.space.Sphere;
import fr.ens.biologie.generic.Sizeable;

/**
 * <p>The base interface for an indexing tree.</p>
 * <p>Classes implementing this interface are meant to index objects in space using a tree structure, like binary trees,
 * quad-trees and octrees. This interface is the root of the hierarchy.  </p>
 *
 * @author Jacques Gignoux - 07-08-2018
 *
 * @param <T> the type of objects indexed
 * @param <N> the type of {@linkplain IndexingNode node} used to build this tree
 */
public interface IndexingTree<T,N extends IndexingNode<T,N>> extends Dimensioned, Sizeable {

	/**
	 * @return the tree root node
	 */
	public abstract N root();

	/**
	 * <p>Insert an object in the tree at a given location</p>
	 * @param item the object to insert
	 * @param at the location where to insert it.
	 */
	public abstract void insert(T item, Point at);

	/**
	 * <p>Get the item closest to location.</p> 
	 * <p>CAUTION: will pick the first item encountered if two or more items are
	 * found exactly at the same distance of the argument.</p>
	 * @param at the location
	 * @return the item closest to this location
	 */
	public abstract T getNearestItem(Point at);

	/**
	 * <p>Get the items closest to location.</p>
	 * <p>Will pick all the items found at exactly the same distance of the argument.</p>
	 * @param at the location
	 * @return the list of items closest to this location
	 */
	public default Collection<T> getNearestItems(Point at) {
		return null;
	}

	/**
	 * Remove item and adapts the tree structure to the removal if needed.
	 * 
	 * @param item the item to remove
	 * @return true if item successfully removed
	 */
	public abstract boolean remove(T item);

	/**
	 * Get all items within a {@linkplain Box} contained in the tree space.
	 * 
	 * @param limits the Box in which to search for items
	 * @return the list of items contained in the Box
	 */
	public abstract Collection<T> getItemsWithin(Box limits);

	/**
	 * Get all items within a {@linkplain Sphere spherical region} contained in the tree space.
	 * 
	 * @param limits the Sphere in which to search for items
	 * @return the list of items contained in the Sphere
	 */
	public abstract Collection<T> getItemsWithin(Sphere limits);

	/**
	 * Get all items of this tree in a flat list
	 * @return the list of all items contained in the tree
	 */
	public abstract Collection<T> getAllItems();
	
	/**
	 * empties this tree
	 */
	public abstract void clear();
	
}
