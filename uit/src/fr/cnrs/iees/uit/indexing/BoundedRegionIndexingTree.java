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
 * <p>A region-based <em>k</em>-d tree where the size of the initial region is known and used
 * to initialise the tree root node.</p>
 * 
 * <p><strong>NOTE</strong>: Compared to {@linkplain ExpandingRegionIndexingTree}, this implementation does not
 * make any assumption on the length of the indexed region in all dimensions. 'hyper-rectangular' domains can
 * be used.</p>
 * 
 * @author Jacques Gignoux - 06-09-2018 
 *
 * @param <T> the type of objects indexed
 */
public class BoundedRegionIndexingTree<T> extends RegionIndexingTree<T> {


	public BoundedRegionIndexingTree(Box domain) {
    	super(domain);
    	root = new RegionIndexingNode<T>(null,domain);
    }
	
	// TESTING
	// OK works.
//    public static void main(String[] args) {
//    	Box limits = Box.boundingBox(Point.newPoint(0,0,0,0),Point.newPoint(10,10,8,12));
//    	RegionIndexingTree<Integer> q = new BoundedRegionIndexingTree<>(limits);
//    	q.setOptimisation(true);
//    	Map<Point,Integer> l = new HashMap<>();
//    	for (int i=1; i<100000; i++) {
//    		Point p = Point.newPoint(Math.random()*10, Math.random()*10, Math.random()*8, Math.random()*12);
//    		q.insert(i,p);
//    		l.put(p,i);
//    	}
//    	System.out.println(q.toShortString());
//    	
//    	for (Integer i:q.root.items())
//    		System.out.println(i+", ");
//    	Point theP = Point.newPoint(5,5,4,6);
//    	
//    	System.out.println(q.getNearestNode(theP));
//    	System.out.println(q.getNearestItem(theP));
//    	double dist = Double.MAX_VALUE;
//    	for (Point p:l.keySet()) {
//    		double d = Distance.euclidianDistance(p, theP); 
//    		if (d<dist) {
//    			dist = d;
//    			System.out.println(l.get(p));
//    		}
//    	}
//    	
//    	for (int i=1; i<1000; i++) {
//    		Point p = Point.newPoint(Math.random()*10, Math.random()*10, Math.random()*8, Math.random()*12);
//    		System.out.println(q.getNearestItem(p));
//    	}
//    	
//    	System.out.println("test over");
//    }

}
