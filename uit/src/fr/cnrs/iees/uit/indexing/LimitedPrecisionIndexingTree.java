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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import au.edu.anu.rscs.aot.collections.QuickListOfLists;
import fr.cnrs.iees.uit.indexing.location.IntDistance;
import fr.cnrs.iees.uit.indexing.location.Locator;
import fr.cnrs.iees.uit.indexing.location.LocatorFactory;
import fr.cnrs.iees.uit.space.Box;
import fr.cnrs.iees.uit.space.Distance;
import fr.cnrs.iees.uit.space.Point;
import fr.cnrs.iees.uit.space.Sphere;

/**
 * As a {@link BoundedRegionIndexingTree}, but using discrete coordinates internally instead
 * of continuous ones.
 * 
 * @author Jacques Gignoux - 8 oct. 2020
 *
 */
public class LimitedPrecisionIndexingTree<T> 
		extends AbstractIndexingTree<T,LimitedPrecisionIndexingNode<T>> {

	// stupid optimisation
	static private final long sqrtMax = Math.round(Math.sqrt(Long.MAX_VALUE));
	
	/** The factory for locator, making sure they all have the same precision */
	protected LocatorFactory factory = null;
	
	/** Tree optimisation parameters */
	private boolean DYNAMIC_MAX_OBJECTS = false;
    private double MAX_OBJ_TARGET_EXPONENT = 0.333333; // 0.5 a good general solution
    
   // reverse mapping of items to nodes to facilitate removal of items without knowing their location
    protected Map<T,LimitedPrecisionIndexingNode<T>> itemToNodeMap = new HashMap<>();
    
    /** maximal depth of tree (limited by precision*maxSide)*/
    protected int maxDepth = 0;
    private long maxSideLength = 0;

	/**
	 * Constructor from a box. All items indexed by this tree will stay inside this box.
	 * The precision argument is used to scale the
	 * coordinates internally: all coordinate values are multiplied by 1/precision and truncated 
	 * to {@code long}s, so that exact comparison of locations becomes possible. In this system, 
	 * two or more items can have exactly the same location.
	 * 
	 * @param domain the initial domain to start the tree with
	 * @param precision the precision of coordinates - a distance smaller than precision is considered 
	 * equal to zero
	 */
	public LimitedPrecisionIndexingTree(Box domain, double precision) {
		super(domain);
		factory = new LocatorFactory(precision,domain);
		double maxSide = 0.0;
		for (int i=0; i<dim; i++)
			maxSide = Math.max(maxSide,domain.sideLength(i));
		long maxS = Math.round(maxSide/factory.precision());
		// smallest power of 2 that is larger than maxS
		while (maxSideLength < maxS) {
			maxDepth++;
			maxSideLength = 1<<maxDepth;
		}
		long[] init = new long[dim];
		Arrays.fill(init,0L);
		Locator lowerBounds = factory.newLocator(init);
		root = new LimitedPrecisionIndexingNode<T>(null,maxSideLength,lowerBounds,this,1);
	}

    /**
     * <p>Setting this to <strong>true</strong> will balance the tree depth and size following P. Tovainen's
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
	
    private void adjustMaxObjects() {
    	LimitedPrecisionIndexingNode.LEAF_MAX_ITEMS = Math.max(7,
        	(int)Math.pow(itemToNodeMap.size(), 
        	MAX_OBJ_TARGET_EXPONENT));
    }
    
	@Override
	public void insert(T item, Point at) {
		Locator atloc = factory.newLocator(at);
		LimitedPrecisionIndexingNode<T> node = root.insert(item, atloc);
        if (node!=null)
        	itemToNodeMap.put(item,node);
        if (DYNAMIC_MAX_OBJECTS && itemToNodeMap.size() % 100 == 0)
            adjustMaxObjects();
	}

	// remove children when they are all empty to adjust tree structure to item content
	private void shrinkNode(LimitedPrecisionIndexingNode<T> node) {
		boolean shrink = true;
		if (node.children!=null) {
			for (LimitedPrecisionIndexingNode<T> c:node.children)
				shrink = shrink && (c.children==null) && (c.items.isEmpty()) ;
			if (shrink) {
				for (LimitedPrecisionIndexingNode<T> c:node.children)
					c.tree = null;
				node.children = null;
				if (node.items.isEmpty())
					if (node.parent!=null)
						shrinkNode(node.parent);
			}
		}
	}
	
	@Override
	public boolean remove(T item) {
		if (itemToNodeMap.containsKey(item)) {
			// remove the item in its node list
			LimitedPrecisionIndexingNode<T> n = itemToNodeMap.get(item);
			n.items.remove(item);
			if (n.items.isEmpty())
				if (n.parent!=null)
					shrinkNode(n.parent);
			// remove it from list here.
			itemToNodeMap.remove(item);
			return true;
		}
		return false;
	}
	
	@Override
	public T getNearestItem(Point at) {
		throw new UnsupportedOperationException();
	}
	
	// CHECK THIS CAREFULLY with points on edges!
	// CAUTION. This method is only a helper method. It should not be made public.
	// It will return the node containing the point, but for points on borders
	// it may not be the box in which an item is to be found.
	// So this is safe as long as algos using it are able to jump out if distance to edge = 0
	// Use with caution.
	protected LimitedPrecisionIndexingNode<T> getNearestNode(Locator at) {
		if (root==null)
			return null;
		LimitedPrecisionIndexingNode<T> node = root;
		boolean stop = false;
//		while ((!stop) && node.region().contains(at)) {
		while ((!stop) && regionContainsLocator(node.lowerBounds,node.upperBounds,at)) {
			if (node.children!=null) {
				for (int i=0; i<node.children.length; i++)
//					if (node.children[i].region().contains(at)) {
					if (regionContainsLocator(node.children[i].lowerBounds,
							node.children[i].upperBounds,at)) {
						node = node.children[i];
						break;
					}
			}
			else
				stop =true;
		}
		return node;
	}

	@Override
	public Collection<T> getNearestItems(Point at) {
		return getNearestItems(at,1);
	}
	
	/**
	 * <p>Returns nearest items up to rank, e.g. if rank = 3 return the nearest items, 
	 * the 2nd nearest items and the 3rd nearest item as a flat list.</p>
	 * 
	 * @param at the location which neighbours are searched
	 * @param rank the rank of the  neighbour wanted (1 = neares neighbour, 2 = second nearest, etc.)
	 * @return the rank<sup>th</sup> nearest item to the point argument
	 */
	// QUESTION: should we also return the distance ? It may be useful in many cases...
	public Collection<T> getNearestItems(Point at, int rank) {
		Locator atloc = factory.newLocator(at);
		// find box enclosing the point
		LimitedPrecisionIndexingNode<T> node = getNearestNode(atloc);
		long dist2 = Long.MAX_VALUE;
		SortedMap<Long,List<T>> foundItems = new TreeMap<>();
		// find the item closest to the point
		for (T item: node.items.keySet()) {
			long d = IntDistance.squaredEuclidianDistance(node.items.get(item),atloc);
			if (foundItems.get(d)==null)
				foundItems.put(d, new LinkedList<T>());
			foundItems.get(d).add(item);
//			if (d<dist2) {
//				theItem = item;
//				dist2 = d;
//			}
		} 
		if (!foundItems.isEmpty())
			dist2 = foundItems.firstKey(); // the shortest squared distance found
		// if the distance of the item to the point is larger than the distance
		// of the point to the box edges, the item may be in the enclosing box
		double dist = Math.sqrt(dist2);
		// this is dirty code (going back to Box)
		Box reg = Box.boundingBox(factory.toPoint(node.lowerBounds),factory.toPoint(node.upperBounds));
		if (dist > Distance.distanceToClosestEdge(at,reg)) {
			// search the sphere for items at distance dist from point at
			// excluding those in node (already tested)
//			Sphere s = new SphereImpl(at,dist);
			Sphere s = Sphere.newSphere(at,dist);
			Box b = Box.boundingBox(s);
			Locator lows = factory.newLocator(b.lowerBounds());
			Locator ups = factory.newLocator(b.upperBounds());
			Collection<LimitedPrecisionIndexingNode<T>> list = getNodesWithin(lows,ups);
			for (LimitedPrecisionIndexingNode<T> n:list)
				if (n!=node)
					for (T it: n.items.keySet()) {
						long d = IntDistance.squaredEuclidianDistance(n.items.get(it),atloc);
						if (foundItems.get(d)==null)
							foundItems.put(d, new LinkedList<T>());
						foundItems.get(d).add(it);
			}
		}
		if (rank==1)
			return foundItems.get(foundItems.firstKey());
		else {
			int r = 1;
			List<T> result = new ArrayList<>();
			for (long key: foundItems.keySet()) {
				result.addAll(foundItems.get(key));
				if (r==rank)
					return Collections.unmodifiableCollection(result);
				r++;
			}
		}
		return null;
	}

    // recursive
    private void collectOverlappingNodes(Locator lower, Locator upper, 
    		LimitedPrecisionIndexingNode<T> node, 
    		List<LimitedPrecisionIndexingNode<T>> nodes) {
    	if (node!=null)
    		if (node.regionOverlaps(lower,upper)) {
    			if (node.children!=null)
    				for (int i=0; i<node.children.length; i++)
    					collectOverlappingNodes(lower,upper,node.children[i],nodes);
    			else
    				nodes.add(node);
    		}
    }
    
	protected Collection<LimitedPrecisionIndexingNode<T>> getNodesWithin(Locator lower, Locator upper) {
		List<LimitedPrecisionIndexingNode<T>> nodes = new ArrayList<LimitedPrecisionIndexingNode<T>>();
		collectOverlappingNodes(lower,upper,root,nodes);
		return nodes;
	}
	
	protected boolean regionContainsLocator(Locator regionLows, Locator regionUps, Locator loc) {
		for (int i=0; i<dim; i++)
			if ((loc.coordinate(i)>regionUps.coordinate(i)) || 
				(loc.coordinate(i)<regionLows.coordinate(i)) )
				return false;
		return true;
	}
	
	private boolean regionContainsBox(Locator regionLows, Locator regionUps,
			Locator boxLows, Locator boxUps) {
		return regionContainsLocator(regionLows,regionUps,boxLows) && 
			regionContainsLocator(regionLows,regionUps,boxUps);
	}

	// CHECK THIS !!
	private boolean sphereContainsPoint(Locator centre, long radius, Locator loc) {
		long rsq = 0;
		for (int i=0; i<dim; i++) {
			long x = loc.coordinate(i)-centre.coordinate(i);
			rsq += x*x;
		}
		// stupid optimisation
		if (radius<sqrtMax)
			return (rsq<=radius*radius);
		else
			return (Math.sqrt(rsq)<=radius);
	}

	@Override
	public Collection<T> getItemsWithin(Box limits) {
    	// get all nodes overlapping limits (including children)
		Locator lows = factory.newLocator(limits.lowerBounds());
		Locator ups = factory.newLocator(limits.upperBounds());
		Collection<LimitedPrecisionIndexingNode<T>> blist = getNodesWithin(lows,ups);
		List<T> extraItems = new ArrayList<>();
		QuickListOfLists<T> result = new QuickListOfLists<T>();
		// search node list for items
		for (LimitedPrecisionIndexingNode<T> n:blist)
			if (!n.items.isEmpty()) // we dont care about empty nodes
				// node region fully contained in limits: insert all items
//				if (limits.contains(n.region()))
				if (regionContainsBox(lows,ups,n.lowerBounds,n.upperBounds))
					result.addList(n.items.keySet());
				// node region not fully contained in limits: check all items
				else for (T item:n.items.keySet())
//					if (limits.contains(n.items.get(item)))
					if (regionContainsLocator(lows,ups,n.items.get(item)))
						extraItems.add(item);
		result.addList(extraItems);
		return result;
	}

	@Override
	public Collection<T> getItemsWithin(Sphere limits) {
		Box blim = Box.boundingBox(limits);
		Locator lows = factory.newLocator(blim.lowerBounds());
		Locator ups = factory.newLocator(blim.upperBounds());
		Locator centre = factory.newLocator(limits.centre());
		long radius = Math.round(limits.radius()/factory.precision());
		Collection<LimitedPrecisionIndexingNode<T>> blist = getNodesWithin(lows,ups);
		List<T> extraItems = new ArrayList<>();
		QuickListOfLists<T> result = new QuickListOfLists<T>();
		for (LimitedPrecisionIndexingNode<T> n:blist)
			if (!n.items.isEmpty())
				if (regionContainsBox(lows,ups,n.lowerBounds,n.upperBounds))
					result.addList(n.items.keySet());
				else for (T item:n.items.keySet())
//					if (limits.contains(n.items.get(item)))
					if (sphereContainsPoint(centre,radius,n.items.get(item)))
						extraItems.add(item);
		result.addList(extraItems);
		return result;
	}

	@Override
	public int size() {
		return itemToNodeMap.size();
	}

	// recursive - called by toString();
	private String nodeToString(LimitedPrecisionIndexingNode<T> node, int depth, boolean s) {
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
		sb.append(getClass().getSimpleName()+"\n");
		sb.append(nodeToString(root,0,false));
		return sb.toString();
	}

	/**
	 *
	 * @return a 'short' description of this tree
	 */
	public String toShortString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()+"\n");
		if (root!=null) {
			sb.append("region = [");
			sb.append(root.lowerBounds.toString());
			sb.append(root.upperBounds.toString());
			sb.append("]\n");
		}
		sb.append(nodeToString(root,0,true));
		return sb.toString();
	}

	@Override
	public void clear() {
		itemToNodeMap.clear();
		root.items.clear();
		root.children = null;
	}

}
