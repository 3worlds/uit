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

import java.util.*;

import au.edu.anu.omhtk.collections.QuickListOfLists;
import fr.cnrs.iees.uit.indexing.location.Locator;
import fr.cnrs.iees.uit.space.Point;

/**
 * The difference between this class and RegionIndexingNode is that items may
 * share the same location
 * 
 * @author Jacques Gignoux - 8 oct. 2020
 *
 * @param <T>
 */
class LimitedPrecisionIndexingNode<T> 
		extends IndexingNode<T, LimitedPrecisionIndexingNode<T>> {

	private int mydepth = 0;
	
	/** the storage capacity of leaf nodes - may be made dynamic*/
	protected static int LEAF_MAX_ITEMS = 10;
//	protected static int LEAF_MAX_ITEMS = 2; // for testing only
	
	// region (not as box)
	private long sideLength = 0;
	protected Locator lowerBounds;
	protected Locator upperBounds;

	/** the list of (Locator,item) pairs stored in this node */
	protected Map<T, Locator> items = new HashMap<>();

	private int dim;

	protected LimitedPrecisionIndexingTree<T> tree = null;

	public LimitedPrecisionIndexingNode(LimitedPrecisionIndexingNode<T> parent,
			long sideLength, 
			Locator lower,
			LimitedPrecisionIndexingTree<T> tree,
			int depth) {
		super();
		this.parent = parent;
		this.tree = tree;
		this.sideLength = sideLength;
		lowerBounds = lower;
		upperBounds = Locator.add(lowerBounds,sideLength);
		mydepth = depth;
		dim = tree.dim;
	}
	
	// returns the index of the child node containing the point loc
	private int childIndex(Locator loc) {
		int[] ix = new int[dim];
		int index=0;
		for (int i=0; i<dim; i++) {
			int nn = -1;
			if (loc.coordinate(i)-lowerBounds.coordinate(i)<sideLength/2)
				nn=0;
			else // if (loc.coordinate(i)>=sideLength/2)
				nn=1;
//			else { // debugging only - should never occur
//				System.out.println("RARE BUG occurring!");
//				System.out.println("for point "+loc.toString()+" in region ["+
//					lowerBounds.toString()+upperBounds.toString()+"]");
//			}
//			long prop = (loc.coordinate(i)-lowerBounds.coordinate(i))
//				/(sideLength);
//			int nn = (int) Math.floor(2*prop);
//			// could possibly nn equal 2 ? yes.
//			if (nn==2)
//				nn--;
			ix[i] = nn;
		}
		for (int i=0; i<dim; i++)
			index += ix[i]*(1<<(dim-i-1));
		return index;
	}
	
	// index computation for children - veery tricky - carefully checked, OK.
    private void recurseMin(int depth, int itbase, long[][] result) {
    	int itmax = 1<<(dim-depth-1);
    	long min = lowerBounds.coordinate(depth);
    	for (int i=0; i<itmax; i++)
    		result[i+itbase][depth] = min;
    	if (depth<dim-1)
    		recurseMin(depth+1,itbase,result);
    	long max = (min + upperBounds.coordinate(depth))/2;
    	for (int i=0; i<itmax; i++)
    		result[itmax+i+itbase][depth] = max;
    	if (depth<dim-1)
    		recurseMin(depth+1,itbase+itmax,result);
    }
	
    // create children nodes according to dimension and region
    // moves contained items to them
    
	@SuppressWarnings("unchecked")
	private void makeChildren() {
		// work out the new region mins and maxs with the proper indexing
    	long[][] mins = new long[1<<dim][dim];
    	recurseMin(0,0,mins);
//    	double[][] maxs = new double[1<<dim][dim];
//    	recurseMax(0,0,maxs);
    	// setup the new child nodes with the proper regions
    	children = new LimitedPrecisionIndexingNode[1<<dim];
    	for (int i=0; i<(1<<dim); i++) {
    		Locator lower = tree.factory.newLocator(mins[i]);
//    		Box reg = new BoxImpl(lower, upper);
    		children[i] = new LimitedPrecisionIndexingNode<T>(this,sideLength/2,lower,tree,mydepth+1);
    	}
    	// spread the extant items into the child nodes
    	for (Map.Entry<T,Locator> e:items.entrySet()) {
    		LimitedPrecisionIndexingNode<T> newNode = children[childIndex(e.getValue())];
    		newNode.insert(e.getKey(),e.getValue());
    		tree.itemToNodeMap.put(e.getKey(),newNode); // this will replace the former mapping
    	}
    	// empty the item list now they have been put in the child nodes
    	items.clear();
	}

	// inserts item T in the proper (existing) child node of node 'node' - recursive
	private LimitedPrecisionIndexingNode<T> insertInChild(T item,
			LimitedPrecisionIndexingNode<T> node, 
			Locator loc) {
		while (node.children!=null) {
			int i = node.childIndex(loc);
			node = node.children[i];
		}
		return node.insert(item,loc);
	}
	
	public LimitedPrecisionIndexingNode<T> insert(T item, Locator loc) {
		// do not insert same item twice at the same location
		if (!items.containsKey(item)) {
			// insert the item here or in my children
			// if list of items is full, expand to child nodes
			if ((items.size() >= LEAF_MAX_ITEMS) && (mydepth<=tree.maxDepth))
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

	@Override
	public T item() {
		throw new UnsupportedOperationException();
	}

	// recursive
	private void getAllItems(QuickListOfLists<T> list, LimitedPrecisionIndexingNode<T> node) {
		if (!node.items.isEmpty())
			list.addList(node.items.keySet());
		else if (node.children!=null)
			for (int i=0; i<node.children.length; i++)
				getAllItems(list,node.children[i]);
	}

	@Override
	public Collection<T> items() {
		QuickListOfLists<T> list = new QuickListOfLists<T>();
		getAllItems(list,this);
		return list;
	}

	@Override
	public void clear() {
		items.clear();
	}
	
	// CHECK THIS IN DETAIL!!!
	protected boolean regionOverlaps(Locator lower, Locator upper) {
		for (int i=0; i<dim; i++) {
			if (upperBounds.coordinate(i)<lower.coordinate(i))
				return false;
			if (lowerBounds.coordinate(i)>upper.coordinate(i))
				return false;
		}
		return true;
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
			.append(lowerBounds.toString())
			.append("-")
			.append(upperBounds.toString())
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

	protected LimitedPrecisionIndexingNode<T> expandRootRegion(Point at) {
		// if there is no parent, make one (NB this means I am the root and this parent is going to replace me)
		if (parent==null) {
			// work out the new region boundaries
			long[] newlows = new long[dim];
			for (int i=0; i<dim; i++)
				newlows[i] = lowerBounds.coordinate(i);
			long[] newups = new long[dim];
			for (int i=0; i<dim; i++)
				newups[i] = upperBounds.coordinate(i);
			Locator loc = tree.factory.newLocator(at);
			for (int i=0; i<dim; i++) {
				if (loc.coordinate(i)<lowerBounds.coordinate(i))
					newlows[i] =  lowerBounds.coordinate(i)-sideLength;
				if (loc.coordinate(i)>upperBounds.coordinate(i))
					newups[i] = upperBounds.coordinate(i)+sideLength;
			}
			Locator lower = tree.factory.newLocator(newlows);
			parent = new LimitedPrecisionIndexingNode<T>(null,sideLength*2,lower,tree,mydepth-1);
			parent.makeChildren(); // this creates empty children in the parent
			// place me in my parent's children
			Locator centre = Locator.add(lowerBounds,sideLength/2);
			parent.children[parent.childIndex(centre)] = this;
		}
		return parent;
	}


}
