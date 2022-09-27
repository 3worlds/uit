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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import fr.cnrs.iees.uit.space.Point;

/**
 * {@linkplain IndexingNode} used in <em>point-based</em> 
 * {@link fr.cnrs.iees.uit.indexing.IndexingTree IndexingTree}s.
 *
 * @author Jacques Gignoux - 10-09-2018
 *
 * @param <T> the type of object stored in this node
 */
class PointIndexingNode<T> extends IndexingNode<T,PointIndexingNode<T>> {

	protected Point location;
	private T item;

	@SuppressWarnings("unchecked")
	protected PointIndexingNode(PointIndexingNode<T> parent) {
		super();
		this.parent = parent;
		children = new PointIndexingNode[1<<location.dim()]; // #children = 2^dim
	}

	public PointIndexingNode<T> insert(T item, Point loc) {
		location = loc;
		this.item = item;
		return this;
	}

	public T item() {
		return item;
	}

	@Override
	public Collection<T> items() {
		List<T> l = new LinkedList<T>();
		l.add(item);
		return Collections.unmodifiableCollection(l);
	}

	@Override
	public void clear() {
		item=null;

	}

}
