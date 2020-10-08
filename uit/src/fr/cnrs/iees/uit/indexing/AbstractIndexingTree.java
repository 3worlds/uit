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

import fr.cnrs.iees.uit.IndexingTree;
import fr.cnrs.iees.uit.space.Box;

/**
 * <p>An abstract ancestor for 'generalised QuadTrees'. By <em>generalised</em> we mean indexing trees
 * of any dimension (strictly speaking,  <a href="https://en.wikipedia.org/wiki/K-d_tree"><em>k</em>-d trees</a>),
 * e.g. a binary tree for dim=1, a quadtree for dim=2, and an octree for dim=3.</p>
 * 
 * <p>The implementation proposed is generic, i.e. the tree dimension is inferred from the dimension of the {@linkplain Point}s
 * used to build the tree.</p> 
 * 
 * <p>Implementations are provided for <a href="https://en.wikipedia.org/wiki/Quadtree#Point_quadtree">point-based</a> and 
 * <a href="https://en.wikipedia.org/wiki/Quadtree#Region_quadtree">region-based</a> trees.</p>  
 * 
 * @author Jacques Gignoux - 29-08-2018 
 *
 * @param <T> the type of objects indexed
 * @param <N> the type of {@linkplain IndexingNode node} used to build this tree
 */
// Tested OK with version 0.0.1 on 24/11/2018
public abstract class AbstractIndexingTree<T,N extends IndexingNode<T,N>> implements IndexingTree<T,N> {
	
	/** the tree dimension */
	protected int dim = 0;
	/** the tree root node */
	protected N root = null;
	
	/**
	 * <p>Constructor to build an {@linkplain IndexingTree} with <em>no</em> initial spatial domain (the
	 * spatial domain is worked out from the first points inserted in the tree following the procedure
	 * developed by <a href="https://dev.solita.fi/2015/08/06/quad-tree.html"><strong>Paavo Toivanen</strong></a>).</p>
	 * 
	 * @param dim the dimension of the space to index
	 */
	protected AbstractIndexingTree(int dim) {
		super();
		this.dim = dim;
	}
	
	/**
	 * <p>Constructor to build an {@linkplain IndexingTree} with an initial spatial domain. All points are going
	 * to be located within this domain, which is used to create the root node of the IndexingTree in region-based trees.
	 * @param domain
	 */
	protected AbstractIndexingTree(Box domain) {
		this(domain.dim());
	}
		
	@Override
	public final int dim() {
		return dim;
	}

	@Override
	public final N root() {
		return root;
	}
	
	@Override
	public Iterable<T> getAllItems() {
		return root.items();
	}
		
}