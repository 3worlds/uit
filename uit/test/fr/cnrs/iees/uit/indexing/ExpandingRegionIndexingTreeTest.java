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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.uit.space.Point;

class ExpandingRegionIndexingTreeTest {

	private ExpandingRegionIndexingTree<Integer> tree, tree2;
	
	@BeforeEach
	private void init() {
		// extreme (4D, not square) testing
		tree = new ExpandingRegionIndexingTree<>(4);
		tree.setOptimisation(true);
		// a simpler (2D) case
		tree2 = new ExpandingRegionIndexingTree<>(2);
		tree2.setOptimisation(true);
	}

	@Test
	void testInsert() {
		Point p = Point.newPoint(4,4,3,5); 	tree.insert(1, p);
		// tree root is set only when a point is inserted
		assertNotNull(tree.root);
		// region is now equal to point
		assertEquals(tree.root.region().toString(),"[[4.0,4.0,3.0,5.0],[4.0,4.0,3.0,5.0]]");
		p = Point.newPoint(4,4,3,7); 		tree.insert(2, p);
		// region expands to include new point
		// means the region side length in all dimensions is set to the maximal
		// distance between two point coordinates (here, 2)
		assertEquals(tree.root.region().toString(),"[[4.0,4.0,3.0,5.0],[6.0,6.0,5.0,7.0]]");
		// from here region no longer expands as all new points fall within it
		p = Point.newPoint(4,4,5,5); 		tree.insert(3, p);
		p = Point.newPoint(4,4,5,7); 		tree.insert(4, p);
		p = Point.newPoint(4,6,3,5); 		tree.insert(5, p);
		p = Point.newPoint(4,6,3,7); 		tree.insert(6, p);
		p = Point.newPoint(4,6,5,5); 		tree.insert(7, p);
		p = Point.newPoint(4,6,5,7); 		tree.insert(8, p);
		p = Point.newPoint(6,4,3,5); 		tree.insert(9, p);
		p = Point.newPoint(6,4,3,7); 		tree.insert(10, p);
		// this is the max number of items that can be contained in a single node
		assertEquals(tree.toShortString(),"ExpandingRegionIndexingTree\n" + 
				"region = [[4.0,4.0,3.0,5.0],[6.0,6.0,5.0,7.0]]\n" + 
				"items={1,2,3,4,5,6,7,8,9,10}\n" + 
				"");
		p = Point.newPoint(6,4,5,5); 		tree.insert(11, p);
		p = Point.newPoint(6,4,5,7); 		tree.insert(12, p);
		p = Point.newPoint(6,6,3,5); 		tree.insert(13, p);
		p = Point.newPoint(6,6,3,7); 		tree.insert(14, p);
		p = Point.newPoint(6,6,5,5); 		tree.insert(15, p);
		p = Point.newPoint(6,6,5,7); 		tree.insert(16, p);
		// region has not expanded
		assertEquals(tree.root.region().toString(),"[[4.0,4.0,3.0,5.0],[6.0,6.0,5.0,7.0]]");
		// now there must be only one item in each node
		assertEquals(tree.toShortString(),"ExpandingRegionIndexingTree\n" + 
				"region = [[4.0,4.0,3.0,5.0],[6.0,6.0,5.0,7.0]]\n" + 
				"items={}\n" + 
				"--items={1}\n" + 
				"--items={2}\n" + 
				"--items={3}\n" + 
				"--items={4}\n" + 
				"--items={5}\n" + 
				"--items={6}\n" + 
				"--items={7}\n" + 
				"--items={8}\n" + 
				"--items={9}\n" + 
				"--items={10}\n" + 
				"--items={11}\n" + 
				"--items={12}\n" + 
				"--items={13}\n" + 
				"--items={14}\n" + 
				"--items={15}\n" + 
				"--items={16}\n");
		// this point is out of the Box and should cause region expansion
		p = Point.newPoint(7,8,9,10);	tree.insert(17, p);
		// region is now increased by 1 cell in dims 1 and 2 and 2 cells in dims 3 and 4
		assertEquals(tree.root.region().toString(),"[[4.0,4.0,3.0,5.0],[8.0,8.0,11.0,13.0]]");
		// tree now has a depth of 3
		// adding a large number (one million) of items
		int N = 1000000;
		for (int i=tree.size(); i<=N; i++) {
			p = Point.newPoint(Math.random()*tree.root.region().sideLength(0)+tree.root.region().lowerBound(0), 
					Math.random()*tree.root.region().sideLength(1)+tree.root.region().lowerBound(1), 
					Math.random()*tree.root.region().sideLength(2)+tree.root.region().lowerBound(2), 
					Math.random()*tree.root.region().sideLength(3)+tree.root.region().lowerBound(3));
			tree.insert(i,p);
		}
		// nobody was lost!
		assertEquals(tree.size(),N);
		// region didnt expand
		assertEquals(tree.root.region().toString(),"[[4.0,4.0,3.0,5.0],[8.0,8.0,11.0,13.0]]");
		// one more point just outside region
		p = Point.newPoint(3.9,4.1,3.5,5.6);	tree.insert(N+1, p);
		// region expanded by 4 (sidelength) in dim 1, other dimensions unchanged
		assertEquals(tree.root.region().toString(),"[[0.0,4.0,3.0,5.0],[8.0,8.0,11.0,13.0]]");
	}
	
	@Test
	void testRemove() {
		for (int i=0; i<1000000; i++)
			tree2.insert(i, Point.newPoint(Math.random()*16,Math.random()*16));
		assertEquals(tree2.size(),1000000);
		// region is now 
		System.out.println(tree2.root.region());
		for (int i=0; i<500000; i++)
			tree2.remove(i);
		assertEquals(tree2.size(),500000);
		// region didnt shrink (same as before)
		System.out.println(tree2.root.region());
		for (int i=500000; i<1000000; i++)
			tree2.remove(i);
		assertEquals(tree2.size(),0);
		// region never shrinks
		System.out.println(tree2.root.region());
	}

	@Test
	void testExpandingRegionIndexingTree() {
		assertNotNull(tree);
		assertNull(tree.root);
		assertEquals(tree.dim,4);
		assertNotNull(tree2);
		assertNull(tree2.root);
		assertEquals(tree2.dim,2);
		System.out.println(tree.toShortString());
		System.out.println(tree2.toShortString());
	}
	
}
