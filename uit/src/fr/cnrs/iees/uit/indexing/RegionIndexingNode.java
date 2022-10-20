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
import java.util.HashMap;
import java.util.Map;

import au.edu.anu.omhtk.collections.QuickListOfLists;
import fr.cnrs.iees.uit.space.Box;
import fr.cnrs.iees.uit.space.Point;

/**
 * <p>{@linkplain IndexingNode} used in region-based {@link fr.cnrs.iees.uit.indexing.IndexingTree IndexingTree}</p>
 *
 *  <p><strong> NOTE</strong>: This implementation is a re-coding of the inner Quad class in
 *  the QuadTree class
 *  written by <a href="https://dev.solita.fi/2015/08/06/quad-tree.html"><strong>Paavo Toivanen</strong></a>.
 *
 *  @see {@linkplain RegionIndexingTree} for more detail on the code adaptation.
 *
 * @author Jacques Gignoux - 30-08-2018
 *
 * @param <T> the type of object stored in this tree
 */
class RegionIndexingNode<T> extends IndexingNode<T,RegionIndexingNode<T>> {

	/** the storage capacity of leaf nodes - may be made dynamic*/
	protected static int LEAF_MAX_ITEMS = 10;
//	protected static int LEAF_MAX_ITEMS = 2; // for testing only

	private Box region;

	/** the list of (Point,item) pairs stored in this node */
	protected Map<T, Point> items = new HashMap<>();

	private int dim;

	protected RegionIndexingTree<T> tree = null;

	/**
	 *
	 * @param parent a parent node (if null, this is the root node of the tree)
	 * @param region the portion of space represented by this node
	 */
	public RegionIndexingNode(RegionIndexingNode<T> parent, Box region, RegionIndexingTree<T> tree) {
		super();
		this.parent = parent;
		this.region = region;
		dim = region.dim();
		this.tree = tree;
	}

	/**
	 *
	 * @return the portion of space represented by this node
	 */
	protected Box region() {
		return region;
	}

	/**
	 * <p>Computes a new region containing the point passed as an argument. Recurses if necessary.</p>
	 * <p>This method is only used in the {@linkplain ExpandingRegionIndexingTree}.</p>
	 * @param loc the Point to include in the tree region
	 * @return the new root node for the calling tree
	 */
	protected RegionIndexingNode<T> expandRootRegion(Point loc) {
		// if the region is null, enlarge it using the new point as the limit so that the item falls in the region
		if (region.size()==0.0) {
			region = Box.boundingCube(region.lowerBounds(), loc);
			return this;
		}
		// if there is no parent, make one (NB this means I am the root and this parent is going to replace me)
		else {
			if (parent==null) {
				// work out the new region boundaries
				double[] newlows = new double[dim];
				for (int i=0; i<dim; i++)
					newlows[i] = region.lowerBound(i);
				double[] newups = new double[dim];
				for (int i=0; i<dim; i++)
					newups[i] = region.upperBound(i);
				for (int i=0; i<dim; i++) {
					if (loc.coordinate(i)<region.lowerBound(i))
						newlows[i] =  region.lowerBound(i)-region.sideLength(i);
					if (loc.coordinate(i)>region.upperBound(i))
						newups[i] = region.upperBound(i)+region.sideLength(i);
				}
//				parent = new RegionIndexingNode<T>(null,
//					new BoxImpl(Point.newPoint(newlows),Point.newPoint(newups)),tree);
				parent = new RegionIndexingNode<T>(null,
					Box.boundingBox(Point.newPoint(newlows),Point.newPoint(newups)),tree);
				parent.makeChildren(); // this creates empty children in the parent
				// place me in my parent's children
				parent.children[parent.childIndex(region.centre())] = this;
			}
			return parent;
		}
	}

	// recursive
	
	public RegionIndexingNode<T> insert(T item, Point loc) {
		// do not insert same item twice at the same location
		if (!items.containsKey(item)) {
			// insert the item here or in my children
			// if list of items is full, expand to child nodes
			if (items.size() >= LEAF_MAX_ITEMS)
				makeChildren();
			// if there are child nodes, put the item in the proper child
			if (children!=null)
				return insertInChild(item, this,loc);
			// otherwise, put it in this list
			else {
				items.put(item,loc);
				return this;
			}
		}
		else
			return null;
	}

	// returns the index of the child node containing the point loc
	private int childIndex(Point loc) {
		int[] ix = new int[dim];
		int index=0;
		for (int i=0; i<dim; i++) {
			double prop = (loc.coordinate(i)-region.lowerBound(i))
					/(region.upperBound(i)-region.lowerBound(i));
			int nn = (int) Math.floor(2*prop);
			// could possibly nn equal 2 ? yes.
			if (nn==2)
				nn--;
			ix[i] = nn;
		}
		for (int i=0; i<dim; i++)
			index += ix[i]*(1<<(dim-i-1));

		// debug
		// this is a very rarely occurring bug: index out of range
		if ((index<0)||(index>(1<<dim))) {
			System.out.println("RARE BUG occurring!");
			System.out.println("for point "+loc.toString()+" in region "+region.toString());
		}
		// debug

		return index;
	}

	// inserts item T in the proper (existing) child node of node 'node' - recursive
	private RegionIndexingNode<T> insertInChild(T item, RegionIndexingNode<T> node, Point loc) {
		while (node.children!=null) {
			int i = node.childIndex(loc);
			node = node.children[i];
		}
		return node.insert(item,loc);
	}

    // index computation for children - veery tricky - carefully checked, OK.
    private void recurseMin(int depth, int itbase, double[][] result) {
    	int itmax = 1<<(dim-depth-1);
    	double min = region.lowerBound(depth);
    	for (int i=0; i<itmax; i++)
    		result[i+itbase][depth] = min;
    	if (depth<dim-1)
    		recurseMin(depth+1,itbase,result);
    	double max = (min + region.upperBound(depth))/2;
    	for (int i=0; i<itmax; i++)
    		result[itmax+i+itbase][depth] = max;
    	if (depth<dim-1)
    		recurseMin(depth+1,itbase+itmax,result);
    }
    private void recurseMax(int depth, int itbase, double[][] result) {
    	int itmax = 1<<(dim-depth-1);
    	double min = (region.lowerBound(depth)+region.upperBound(depth))/2;
    	for (int i=0; i<itmax; i++)
    		result[i+itbase][depth] = min;
    	if (depth<dim-1)
    		recurseMax(depth+1,itbase,result);
    	double max = region.upperBound(depth);
    	for (int i=0; i<itmax; i++)
    		result[itmax+i+itbase][depth] = max;
    	if (depth<dim-1)
    		recurseMax(depth+1,itbase+itmax,result);
    }

    // create children nodes according to dimension and region
    // moves contained items to them
	@SuppressWarnings("unchecked")
	private void makeChildren() {
		// work out the new region mins and maxs with the proper indexing
    	double[][] mins = new double[1<<dim][dim];
    	recurseMin(0,0,mins);
    	double[][] maxs = new double[1<<dim][dim];
    	recurseMax(0,0,maxs);
    	// setup the new child nodes with the proper regions
    	children = new RegionIndexingNode[1<<dim];
    	for (int i=0; i<(1<<dim); i++) {
    		Point lower = Point.newPoint(mins[i]);
    		Point upper = Point.newPoint(maxs[i]);
//    		Box reg = new BoxImpl(lower, upper);
    		Box reg = Box.boundingBox(lower, upper);
    		children[i] = new RegionIndexingNode<T>(this,reg,tree);
    	}
    	// spread the extant items into the child nodes
    	for (Map.Entry<T,Point> e:items.entrySet()) {
    		RegionIndexingNode<T> newNode = children[childIndex(e.getValue())];
    		newNode.insert(e.getKey(),e.getValue());
    		tree.itemToNodeMap.put(e.getKey(),newNode); // this will replace the former mapping
    	}
    	// empty the item list now they have been put in the child nodes
    	items.clear();
	}

	@Override
	public T item() {
		if (items.isEmpty())
			return null;
		if (items.size()==1)
			return items.keySet().iterator().next();
		throw new UnsupportedOperationException("Item list contains more than one item - use items() to get them.");
	}

	// recursive
	private void getAllItems(QuickListOfLists<T> list, RegionIndexingNode<T> node) {
		if (!node.items.isEmpty())
			list.addList(node.items.keySet());
		else if (node.children!=null)
			for (int i=0; i<node.children.length; i++)
				getAllItems(list,node.children[i]);
	}

	@Override
	public final Collection<T> items() {
		QuickListOfLists<T> list = new QuickListOfLists<T>();
		getAllItems(list,this);
		return list;
	}

	@Override
	public final void clear() {
		items.clear();
	}

	// for debugging
	/**
	 *
	 * @return a short String description of this node
	 */
	protected String toShortString() {
		StringBuilder sb=new StringBuilder();
		sb.append("items={");
		char sep = ',';
		for (T item:items.keySet()) {
			sb.append(item.toString())
				.append(sep);
		}
		if (sb.charAt(sb.length()-1)==sep)
			sb.deleteCharAt(sb.length()-1);
		sb.append("}\n");
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("region=[")
			.append(region.lowerBounds().toString())
			.append("-")
			.append(region.upperBounds().toString())
			.append("], ");
		sb.append("items={");
		char sep = ',';
		for (T item:items.keySet()) {
			sb.append(item.toString())
				.append("@")
				.append(items.get(item).toString())
				.append(sep);
		}
		if (sb.charAt(sb.length()-1)==sep)
			sb.deleteCharAt(sb.length()-1);
		sb.append("}\n");
		return sb.toString();
	}

}
