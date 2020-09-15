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

import fr.cnrs.iees.uit.UitException;
import fr.cnrs.iees.uit.space.Box;
import fr.cnrs.iees.uit.space.BoxImpl;
import fr.cnrs.iees.uit.space.Point;

/**
 * <p>A region-based <em>k</em>-d tree where the size of the initial region is unknown.</p>
 *
 * <p>The root node region size (which must contain all the points of the tree) is worked out
 * from the points inserted in the tree. A smart algorithm by
 * <a href="https://dev.solita.fi/2015/08/06/quad-tree.html"><strong>Paavo Toivanen</strong></a>)
 * enables the tree to grow 'upwards' by enlarging the root node region (and consequently re-rooting the tree)
 * as required to enclose the newly arriving points.</p>
 *
 * <p><strong>NOTE</strong>: a possibly strong restriction of this implementation is that the indexed space
 * <em>must have the same length in all dimensions</em>, i.e. it is a hypercube. This is the best assumption we can do
 * without additional information. Use {@linkplain BoundedRegionIndexingTree} if you need to relax this
 * assumption in your particular application.</p>
 *
 * @author Jacques Gignoux - 10-09-2018
 *
 * @param <T> the type of objects indexed
 */
public class ExpandingRegionIndexingTree<T> extends RegionIndexingTree<T> {

	// instantiate a completely empty expanding tree
    public ExpandingRegionIndexingTree(int dim) {
    	super(dim);
    }

    // instantiate an empty indexing tree with a region to start with
    public ExpandingRegionIndexingTree(Box domain) {
    	super(domain.dim());
    	Box reg = Box.boundingCube(domain.lowerBounds(),domain.upperBounds());
    	root = new RegionIndexingNode<T>(null,reg,this);
    }
    
	@Override
	public void insert(T item, Point at) {
        if (root == null) {
        	Box reg = new BoxImpl(at.clone(),at.clone());
            root = new RegionIndexingNode<T>(null,reg,this);
        }
        while (!root.region().contains(at))
        	root = root.expandRootRegion(at);
        super.insert(item, at);
        if (root.parent!=null)
        	root = root.parent;
        if (root.parent!=null)
        	throw new UitException("CRITICAL - Problem in indexing tree expansion");
	}

}
