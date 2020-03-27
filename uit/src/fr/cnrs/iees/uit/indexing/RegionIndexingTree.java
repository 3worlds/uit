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

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.rscs.aot.collections.QuickListOfLists;
import fr.cnrs.iees.uit.space.Box;
import fr.cnrs.iees.uit.space.Distance;
import fr.cnrs.iees.uit.space.Point;
import fr.cnrs.iees.uit.space.Sphere;
import fr.cnrs.iees.uit.space.SphereImpl;

/**
 * <p>Implementation of a region-based <em>k</em>-d tree.</p>
 *
 *  <p><strong> NOTE</strong>: This implementation is a re-coding of the QuadTree and Octtree classes
 *  written by <a href="https://dev.solita.fi/2015/08/06/quad-tree.html"><strong>Paavo Toivanen</strong></a>.
 *  Compared to his work, we have</p>
 *  <ul>
 *  <li>generalised the QuadTree and Octree classes to the n-dimensional case into a single class (this one);</li>
 *  <li>rewritten in generic terms using the {@linkplain Point}, {@linkplain Box} and {@linkplain Sphere}
 *  classes the spatial tests used to put newcomers into the proper spatial regions;</li>
 *  <li>implemented a more efficient subclass for the case where the initial root region size is known.</li>
 *  </ul>
 *
 *  <p>Among the features inherited from P. Toivanen's work are</p>
 *  <ul>
 *  <li>the possibility to optimise performance by letting the tree re-balance its leaf node item list capacity;</li>
 *  <li>the storage of the item location within the node, which is useful for very dynamic trees (e.g. for use in
 *  simulation computations).</li>
 *  </ul>

 * @author Jacques Gignoux - 07-08-2018
 *
 * @param <T> type of content to index
 */
// Tested OK (but see warnings on some methods) with version 0.0.1 on 26/11/2018
public abstract class RegionIndexingTree<T> extends AbstractIndexingTree<T,RegionIndexingNode<T>> {

	private boolean DYNAMIC_MAX_OBJECTS = false;
    private double MAX_OBJ_TARGET_EXPONENT = 0.333333; // 0.5 a good general solution
    private int nItems = 0;

    /**
     * Constructor to use only when the initial region is unknown and has to be built from the first
     * item put in.
     * @param dim
     */
    protected RegionIndexingTree(int dim) {
    	super(dim);
    }

    /**
     * Constructor to use only when the initial region in which all points will be contained ('domain') is known
     * @param domain
     */
    protected RegionIndexingTree(Box domain) {
    	super(domain);
    	root = new RegionIndexingNode<T>(null,domain);
    }

    /**
     * <p>Setting this to <strong>true</strong></p> will balance the tree depth and size following P. Tovainen's
     * benchmarking which shows that a relatively general solution exists which enables to keep performance
     * constant when the tree grows. Basically, it adapts the storage capacity of leaf nodes to the tree size
     * so that the average number of searches to find a particular node is kept reasonably low.</p>
     *
     * <p>See the benchmarking details <a href="https://dev.solita.fi/2015/08/06/quad-tree.html"><strong>there</strong></a>.</p>
     *
     * @param o whether optimisation should be enabled (<strong>true</strong>) or not (<strong>false</strong>).
     */
    public void setOptimisation(boolean o) {
    	DYNAMIC_MAX_OBJECTS = o;
    }

	@Override
	public void insert(T item, Point at) {
        if (root.insert(item, at))
        	nItems++;
        if (DYNAMIC_MAX_OBJECTS && nItems % 100 == 0)
            adjustMaxObjects();
	}

    // recursive
    private void collectOverlappingNodes(Box limits, RegionIndexingNode<T> node, List<RegionIndexingNode<T>> nodes) {
    	if (node!=null)
    		if (node.region().overlaps(limits)) {
    			if (node.children!=null)
    				for (int i=0; i<node.children.length; i++)
    					collectOverlappingNodes(limits,node.children[i],nodes);
    			else
    				nodes.add(node);
    		}
    }

    // maybe this should not be public? it's a helper method
	protected Iterable<RegionIndexingNode<T>> getNodesWithin(Box limits) {
		List<RegionIndexingNode<T>> nodes = new ArrayList<RegionIndexingNode<T>>();
		collectOverlappingNodes(limits,root,nodes);
		return nodes;
	}

	// CAUTION. This method is only a helper method. It should not be made public.
	// It will return the node containing the point, but for points on borders
	// it may not be the box in which an item is to be found.
	// So this is safe as long as algos using it are able to jump out if distance to edge = 0
	// Use with caution.
	protected RegionIndexingNode<T> getNearestNode(Point at) {
		if (root==null)
			return null;
		RegionIndexingNode<T> node = root;
		boolean stop = false;
		// infinite loop looking for [1.3259620779235943,1.7358619279229224] within box (0,0)by(4,4)
		// I suspect the items have not been partitioned properly due to either a < instead of a <= or > instead of >=
		while ((!stop) && node.region().contains(at)) {
			if (node.children!=null) {
				for (int i=0; i<node.children.length; i++)
					if (node.children[i].region().contains(at)) {
						node = node.children[i];
						break;
					}
			}
			else
				stop =true;
		}
		return node;
	}


	// CAUTION: not resistant to multiple items at the same location !
	@Override
	public T getNearestItem(Point at) {
		// find box enclosing the point
		RegionIndexingNode<T> node = getNearestNode(at);
		double dist2 = Double.MAX_VALUE;
//		Point pt = null;
		T theItem = null;
		// find the item closest to the point
		for (T item: node.items.keySet()) {
			double d = Distance.squaredEuclidianDistance(node.items.get(item), at);
			if (d<dist2) {
				theItem = item;
				dist2 = d;
			}
		}
		// if the distance of the item to the point is larger than the distance
		// of the point to the box edges, the item may be in the enclosing box
		double dist = Math.sqrt(dist2);
		if (dist > Distance.distanceToClosestEdge(at, node.region())) {
			// search the sphere for items at distance dist from point at
			// excluding those in node (already tested)
			Sphere s = new SphereImpl(at,dist);
			Box b = Box.boundingBox(s);
			Iterable<RegionIndexingNode<T>> list = getNodesWithin(b);
			T item = null;
			for (RegionIndexingNode<T> n:list)
				if (n!=node)
					for (T it: n.items.keySet()) {
						double d = Distance.squaredEuclidianDistance(n.items.get(it), at);
						if (d<dist2) {
							dist2 = d;
							item = it;
						}
				}
			if (item!=null)
				return item;
		}
		return theItem;
	}

	// helper methods for remove() (below)
	//
	// recurse parents until the point is not on a border anymore
	private RegionIndexingNode<T> findContainingParent(RegionIndexingNode<T> node, Point at) {
		if (node!=null)
			if (node.region().isPointOnBorder(at))
				return findContainingParent(node.parent,at);
		return node;
	}
	// remove children when they are all empty to adjust tree structure to item content
	private void shrinkNode(RegionIndexingNode<T> node) {
		boolean shrink = true;
		if (node.children!=null) {
			for (RegionIndexingNode<T> c:node.children)
				shrink = shrink && (c.children==null) && (c.items.isEmpty()) ;
			if (shrink) {
				node.children = null;
				if (node.items.isEmpty())
					if (node.parent!=null)
						shrinkNode(node.parent);
			}
		}
	}
	// recurse children to find the proper node
	private boolean removeFromChild(T item, RegionIndexingNode<T> node, Point at) {
		if (node.items.containsKey(item)) {
			if (node.items.remove(item)!=null) {
//				if (node.items.remove(item,at)) { // this was badly wrong - didnt owrk at all!
				nItems--;
				if (node.items.isEmpty())
					shrinkNode(node.parent);
				return true; // end recursion
			}
		}
		else if (node.children!=null) {
			for (RegionIndexingNode<T> n:node.children)
				if (n.region().contains(at))
					if (removeFromChild(item,n,at))
						return true;
		}
		// all other cases: item must be outside tree.
		return false; // end recursion
	}

	@Override
	public boolean remove(T item, Point at) {
		RegionIndexingNode<T> n = getNearestNode(at);
		// this is ok in most cases
		if (n.items.containsKey(item)) {
			if (n.items.remove(item)!=null) {
//			if (n.items.remove(item,at)) { // this was badly wrong - didnt owrk at all!
				nItems--;
				if (n.items.isEmpty())
					shrinkNode(n.parent);
				return true;
			}
			else
				return false;
		}
		// this applies to nodes on the border of the returned 'nearest' node
		else {
			n = findContainingParent(n,at);
			return removeFromChild(item,n.parent,at);
		}
	}

	@Override
	public int size() {
		return nItems;
	}

    private void adjustMaxObjects() {
        RegionIndexingNode.LEAF_MAX_ITEMS = Math.max(7,(int)Math.pow(nItems, MAX_OBJ_TARGET_EXPONENT));
    }

    @Override
	public Iterable<T> getItemsWithin(Box limits) {
    	// get all nodes overlapping limits (including children)
		Iterable<RegionIndexingNode<T>> blist = getNodesWithin(limits);
		List<T> extraItems = new ArrayList<>();
		QuickListOfLists<T> result = new QuickListOfLists<T>();
		// search node list for items
		for (RegionIndexingNode<T> n:blist)
			if (!n.items.isEmpty()) // we dont care about empty nodes
				// node region fully contained in limits: insert all items
				if (limits.contains(n.region()))
					result.addList(n.items.keySet());
				// node region not fully contained in limits: check all items
				else for (T item:n.items.keySet())
					if (limits.contains(n.items.get(item)))
						extraItems.add(item);
		result.addList(extraItems);
		return result;
	}

    // works exactly as above
	@Override
	public Iterable<T> getItemsWithin(Sphere limits) {
		Iterable<RegionIndexingNode<T>> blist = getNodesWithin(Box.boundingBox(limits));
		List<T> extraItems = new ArrayList<>();
		QuickListOfLists<T> result = new QuickListOfLists<T>();
		for (RegionIndexingNode<T> n:blist)
			if (!n.items.isEmpty())
				if (limits.contains(n.region()))
					result.addList(n.items.keySet());
				else for (T item:n.items.keySet())
					if (limits.contains(n.items.get(item)))
						extraItems.add(item);
		result.addList(extraItems);
		return result;
	}

	// recursive - called by toString();
	private String nodeToString(RegionIndexingNode<T> node, int depth, boolean s) {
		StringBuilder sb = new StringBuilder();
		if (node!=null) {
			String indent = "";
			for (int i=0; i<depth; i++)
				indent += "--";
			if (s) sb.append(indent).append(node.toShortString());
			else sb.append(indent).append(node.toString());
			if (node.children!=null)
				for (int i=0; i<node.children.length; i++)
					sb.append(nodeToString(node.children[i],depth+1,s));
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("RegionIndexingTree\n");
		sb.append(nodeToString(root,0,false));
		return sb.toString();
	}

	/**
	 *
	 * @return a 'short' description of this tree
	 */
	public String toShortString() {
		StringBuilder sb = new StringBuilder();
		sb.append("RegionIndexingTree\n");
		if (root!=null) {
			sb.append("region = ");
			sb.append(root.region().toString());
			sb.append('\n');
		}
		sb.append(nodeToString(root,0,true));
		return sb.toString();
	}

}








