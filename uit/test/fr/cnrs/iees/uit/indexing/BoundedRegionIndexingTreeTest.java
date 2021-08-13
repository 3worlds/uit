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

import fr.cnrs.iees.uit.space.Box;
import fr.cnrs.iees.uit.space.Point;
import fr.cnrs.iees.uit.space.Sphere;

class BoundedRegionIndexingTreeTest {

	private Box limits;
	private RegionIndexingTree<Integer> tree, tree2;

	@BeforeEach
	private void init() {
		limits = Box.boundingBox(Point.newPoint(0,0,0,0),Point.newPoint(10,10,8,12));
		// extreme (4D, not square) testing
		tree = new BoundedRegionIndexingTree<>(limits);
		tree.setOptimisation(true);
		// a simpler (2D) case
		Box b = Box.boundingBox(Point.newPoint(0,0),Point.newPoint(16,16));
		tree2 = new BoundedRegionIndexingTree<>(b);
	}

	@Test
	void testBoundedRegionIndexingTree() {
		assertNotNull(tree);
		assertNotNull(tree.root);
		assertEquals(tree.dim,limits.dim());
	}

	// copied from testInsert for other tests. The tree is like this:
	//
	//	RegionIndexingTree
	//	region = [[0.0,0.0,0.0,0.0],[10.0,10.0,8.0,12.0]]
	//	items={}
	//	--items={1,18}
	//	--items={2,22,27}
	//	--items={3}
	//	--items={}
	//	----items={}
	//	----items={}
	//	----items={}
	//	----items={32,33,34,21,37,28,29,30}
	//	----items={}
	//	----items={}
	//	----items={}
	//	----items={}
	//	----items={}
	//	----items={}
	//	----items={}
	//	----items={35,36,38,31}
	//	----items={4}
	//	----items={}
	//	----items={}
	//	----items={}
	//	--items={5}
	//	--items={6}
	//	--items={7,24}
	//	--items={20,8,25,26}
	//	--items={9}
	//	--items={10}
	//	--items={11}
	//	--items={12}
	//	--items={13}
	//	--items={23,14}
	//	--items={15}
	//	--items={16,17,19}
	//
	private void fillTree() {
		Point p = Point.newPoint(4,4,3,5); 	tree.insert(1, p);
		p = Point.newPoint(4,4,3,7); 		tree.insert(2, p);
		p = Point.newPoint(4,4,5,5); 		tree.insert(3, p);
		p = Point.newPoint(4,4,5,7); 		tree.insert(4, p);
		p = Point.newPoint(4,6,3,5); 		tree.insert(5, p);
		p = Point.newPoint(4,6,3,7); 		tree.insert(6, p);
		p = Point.newPoint(4,6,5,5); 		tree.insert(7, p);
		p = Point.newPoint(4,6,5,7); 		tree.insert(8, p);
		p = Point.newPoint(6,4,3,5); 		tree.insert(9, p);
		p = Point.newPoint(6,4,3,7); 		tree.insert(10, p);
		p = Point.newPoint(6,4,5,5); 		tree.insert(11, p);
		p = Point.newPoint(6,4,5,7); 		tree.insert(12, p);
		p = Point.newPoint(6,6,3,5); 		tree.insert(13, p);
		p = Point.newPoint(6,6,3,7); 		tree.insert(14, p);
		p = Point.newPoint(6,6,5,5); 		tree.insert(15, p);
		p = Point.newPoint(6,6,5,7); 		tree.insert(16, p);
		p = Point.newPoint(6,4,5,5); 		tree.insert(11, p);
		p = Point.newPoint(6,4,5,7); 		tree.insert(12, p);
		p = Point.newPoint(6,6,3,5); 		tree.insert(13, p);
		p = Point.newPoint(6,6,3,7); 		tree.insert(14, p);
		p = Point.newPoint(6,6,5,5); 		tree.insert(15, p);
		p = Point.newPoint(6,6,5,7); 		tree.insert(16, p);
		p = Point.newPoint(5,5,4,6); 		tree.insert(17, p);
		p = Point.newPoint(0,0,0,0); 		tree.insert(18, p);
		p = Point.newPoint(10,10,8,12); 	tree.insert(19, p);
		p = Point.newPoint(0,10,8,12); 		tree.insert(20, p);
		p = Point.newPoint(0,0,8,12); 		tree.insert(21, p);
		p = Point.newPoint(0,0,0,12); 		tree.insert(22, p);
		p = Point.newPoint(10,10,0,12); 	tree.insert(23, p);
		p = Point.newPoint(0,10,8,0); 		tree.insert(24, p);
		p = Point.newPoint(0,5,5,12); 		tree.insert(25, p);
		p = Point.newPoint(0,5,8,6); 		tree.insert(26, p);
		p = Point.newPoint(0,0,0,6); 		tree.insert(27, p);
		p = Point.newPoint(1,1,6,11); 		tree.insert(28, p);
		p = Point.newPoint(1,1,7,10); 		tree.insert(29, p);
		p = Point.newPoint(1,2,6,9); 		tree.insert(30, p);
		p = Point.newPoint(3,2,6,10); 		tree.insert(31, p);
		p = Point.newPoint(2,1,6,11); 		tree.insert(32, p);
		p = Point.newPoint(2,1,7,10); 		tree.insert(33, p);
		p = Point.newPoint(2,2,6,9); 		tree.insert(34, p);
		p = Point.newPoint(4,2,6,10); 		tree.insert(35, p);
		p = Point.newPoint(4,2,6,10); 		tree.insert(36, p);
		p = Point.newPoint(2,2,6,9); 		tree.insert(37, p); // same as 34
		p = Point.newPoint(4,2,6,10); 		tree.insert(38, p); // same as 35
	}

	private void fillTree2() {
		Point p = Point.newPoint(4,4);	tree2.insert(1, p);
		p = Point.newPoint(12,4);		tree2.insert(2, p);
		p = Point.newPoint(4,12);		tree2.insert(3, p);
		p = Point.newPoint(12,12);		tree2.insert(4, p);
		p = Point.newPoint(10,14);		tree2.insert(5, p);
		p = Point.newPoint(14,14);		tree2.insert(6, p);
		p = Point.newPoint(14,10);		tree2.insert(7, p);
		p = Point.newPoint(10,10);		tree2.insert(8, p);
		p = Point.newPoint(9,13);		tree2.insert(9, p);
		p = Point.newPoint(13,13);		tree2.insert(10, p);
		p = Point.newPoint(9,9);		tree2.insert(11, p);
		p = Point.newPoint(13,9);		tree2.insert(12, p);
		p = Point.newPoint(8.5,11.5);	tree2.insert(13, p);
		p = Point.newPoint(9.5,11.5);	tree2.insert(14, p);
		p = Point.newPoint(8.5,10.5);	tree2.insert(15, p);
		p = Point.newPoint(9.5,10.5);	tree2.insert(16, p);
		p = Point.newPoint(10.5,9.5);	tree2.insert(17, p);
		p = Point.newPoint(11.5,9.5);	tree2.insert(18, p);
		p = Point.newPoint(10.5,8.5);	tree2.insert(19, p);
		p = Point.newPoint(11.5,9.5);	tree2.insert(20, p);
		p = Point.newPoint(11.5,9.6);	tree2.insert(21, p);
		p = Point.newPoint(11.5,9.5);	tree2.insert(22, p);
//		System.out.println(tree2.toString());
	}

	@Test
	void testInsert() {
		tree.setOptimisation(false); // will use a default of max. 10 items per node
		// all these points fall in the 16 quadrants of this 4-dim tree
		Point p = Point.newPoint(4,4,3,5); 	tree.insert(1, p);
		p = Point.newPoint(4,4,3,7); 		tree.insert(2, p);
		p = Point.newPoint(4,4,5,5); 		tree.insert(3, p);
		p = Point.newPoint(4,4,5,7); 		tree.insert(4, p);
		p = Point.newPoint(4,6,3,5); 		tree.insert(5, p);
		p = Point.newPoint(4,6,3,7); 		tree.insert(6, p);
		p = Point.newPoint(4,6,5,5); 		tree.insert(7, p);
		p = Point.newPoint(4,6,5,7); 		tree.insert(8, p);
		p = Point.newPoint(6,4,3,5); 		tree.insert(9, p);
		p = Point.newPoint(6,4,3,7); 		tree.insert(10, p);
		// this is the max number of items that can be contained in a single node
		assertEquals(tree.toShortString(),"BoundedRegionIndexingTree\n" +
				"region = [[0.0,0.0,0.0,0.0],[10.0,10.0,8.0,12.0]]\n" +
				"items={1,2,3,4,5,6,7,8,9,10}\n");
		p = Point.newPoint(6,4,5,5); 		tree.insert(11, p);
		p = Point.newPoint(6,4,5,7); 		tree.insert(12, p);
		p = Point.newPoint(6,6,3,5); 		tree.insert(13, p);
		p = Point.newPoint(6,6,3,7); 		tree.insert(14, p);
		p = Point.newPoint(6,6,5,5); 		tree.insert(15, p);
		p = Point.newPoint(6,6,5,7); 		tree.insert(16, p);
		// now there must be only one item in each node
		assertEquals(tree.toShortString(),"BoundedRegionIndexingTree\n" +
				"region = [[0.0,0.0,0.0,0.0],[10.0,10.0,8.0,12.0]]\n" +
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
		// this point is right in the middle of the box and should be inserted in one node only
		p = Point.newPoint(5,5,4,6); 		tree.insert(17, p);
		// this point is on the lower border
		p = Point.newPoint(0,0,0,0); 		tree.insert(18, p);
		// this point is on the upper border
		p = Point.newPoint(10,10,8,12); 	tree.insert(19, p);
		// other points on the borders
		p = Point.newPoint(0,10,8,12); 		tree.insert(20, p);
		p = Point.newPoint(0,0,8,12); 		tree.insert(21, p);
		p = Point.newPoint(0,0,0,12); 		tree.insert(22, p);
		p = Point.newPoint(10,10,0,12); 	tree.insert(23, p);
		p = Point.newPoint(0,10,8,0); 		tree.insert(24, p);
		p = Point.newPoint(0,5,5,12); 		tree.insert(25, p);
		p = Point.newPoint(0,5,8,6); 		tree.insert(26, p);
		p = Point.newPoint(0,0,0,6); 		tree.insert(27, p);
		assertEquals(tree.toShortString(),"BoundedRegionIndexingTree\n" +
				"region = [[0.0,0.0,0.0,0.0],[10.0,10.0,8.0,12.0]]\n" +
				"items={}\n" +
				"--items={1,18}\n" +
				"--items={2,22,27}\n" +
				"--items={3}\n" +
				"--items={4,21}\n" +
				"--items={5}\n" +
				"--items={6}\n" +
				"--items={7,24}\n" +
				"--items={20,8,25,26}\n" +
				"--items={9}\n" +
				"--items={10}\n" +
				"--items={11}\n" +
				"--items={12}\n" +
				"--items={13}\n" +
				"--items={23,14}\n" +
				"--items={15}\n" +
				"--items={16,17,19}\n");
		// adding more points in one of the quadrants to see it split in 16
		p = Point.newPoint(1,1,6,11); 		tree.insert(28, p);
		p = Point.newPoint(1,1,7,10); 		tree.insert(29, p);
		p = Point.newPoint(1,2,6,9); 		tree.insert(30, p);
		p = Point.newPoint(3,2,6,10); 		tree.insert(31, p);
		p = Point.newPoint(2,1,6,11); 		tree.insert(32, p);
		p = Point.newPoint(2,1,7,10); 		tree.insert(33, p);
		p = Point.newPoint(2,2,6,9); 		tree.insert(34, p);
		p = Point.newPoint(4,2,6,10); 		tree.insert(35, p);
		assertEquals(tree.toShortString(),"BoundedRegionIndexingTree\n" +
				"region = [[0.0,0.0,0.0,0.0],[10.0,10.0,8.0,12.0]]\n" +
				"items={}\n" +
				"--items={1,18}\n" +
				"--items={2,22,27}\n" +
				"--items={3}\n" +
				"--items={32,33,34,35,4,21,28,29,30,31}\n" +
				"--items={5}\n" +
				"--items={6}\n" +
				"--items={7,24}\n" +
				"--items={20,8,25,26}\n" +
				"--items={9}\n" +
				"--items={10}\n" +
				"--items={11}\n" +
				"--items={12}\n" +
				"--items={13}\n" +
				"--items={23,14}\n" +
				"--items={15}\n" +
				"--items={16,17,19}\n");
		p = Point.newPoint(4,2,6,10); 		tree.insert(36, p);
		assertEquals(tree.toShortString(),"BoundedRegionIndexingTree\n" +
				"region = [[0.0,0.0,0.0,0.0],[10.0,10.0,8.0,12.0]]\n" +
				"items={}\n" +
				"--items={1,18}\n" +
				"--items={2,22,27}\n" +
				"--items={3}\n" +
				"--items={}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={32,33,34,21,28,29,30}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={35,36,31}\n" +
				"----items={4}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"--items={5}\n" +
				"--items={6}\n" +
				"--items={7,24}\n" +
				"--items={20,8,25,26}\n" +
				"--items={9}\n" +
				"--items={10}\n" +
				"--items={11}\n" +
				"--items={12}\n" +
				"--items={13}\n" +
				"--items={23,14}\n" +
				"--items={15}\n" +
				"--items={16,17,19}\n");
		// adding items at already existing locations
		p = Point.newPoint(2,2,6,9); 		tree.insert(37, p); // same as 34
		p = Point.newPoint(4,2,6,10); 		tree.insert(38, p); // same as 35
		// we've added 38 points so far
		assertEquals(tree.size(),38);
		// adding an item for the second time (same id)
		p = Point.newPoint(4,2,6,10); 		tree.insert(38, p); // 38 is already in there
		// the item is not inserted
		assertEquals(tree.size(),38);
		// adding a large number (one million) of items
		int N = 1000000;
		for (int i=39; i<=N; i++) {
			p = Point.newPoint(Math.random()*10, Math.random()*10, Math.random()*8, Math.random()*12);
			tree.insert(i,p);
		}
		// nobody was lost!
		assertEquals(tree.size(),N);
		// this to view the tree now - caution, it's huge
//		System.out.println(tree.toShortString());
 	}

	@Test
	void testGetNodesWithin() {
		tree.setOptimisation(false); // will use a default of max. 10 items per node
		fillTree();
		// this is the first Quadrant
		Box b = Box.boundingBox(Point.newPoint(0,0,0,0), Point.newPoint(5,5,4,6));
		Iterable<RegionIndexingNode<Integer>> nodes = tree.getNodesWithin(b);
		// there should be only one node in this list
		assertEquals(showList("testGetNodesWithin",nodes),1);
		// this is another (small) region contained within first quadrant, first quadrant is returned
		b = Box.boundingBox(Point.newPoint(0,0,0,0), Point.newPoint(0.1,0.1,0.1,0.1));
		nodes = tree.getNodesWithin(b);
		assertEquals(showList("testGetNodesWithin",nodes),1);
		// this is a large region containing 2 quadrants
		b = Box.boundingBox(Point.newPoint(0,0,0,0), Point.newPoint(5,5,4,12));
		nodes = tree.getNodesWithin(b);
		assertEquals(showList("testGetNodesWithin",nodes),2);
		// this is a small region overlapping all quadrants, all nodes are returned
		b = Box.boundingBox(Point.newPoint(4,4,3,5), Point.newPoint(6,6,5,7));
		nodes = tree.getNodesWithin(b);
		assertEquals(showList("testGetNodesWithin",nodes),16);
	}

	@Test
	void testGetNearestNode() {
		fillTree2();
		Point p = Point.newPoint(12,4);
		assertEquals(tree2.getNearestNode(p).toString(),"region=[[8.0,0.0]-[16.0,8.0]], items={2@[12.0,4.0]}\n");
		p = Point.newPoint(4,12);
		assertEquals(tree2.getNearestNode(p).toString(),"region=[[0.0,8.0]-[8.0,16.0]], items={3@[4.0,12.0]}\n");
		p = Point.newPoint(4,13);
		assertEquals(tree2.getNearestNode(p).toString(),"region=[[0.0,8.0]-[8.0,16.0]], items={3@[4.0,12.0]}\n");
		p = Point.newPoint(12,12);
		// here, the region returned correctly contains point (12,12), but not point 4 which was the target.
		assertEquals(tree2.getNearestNode(p).toString(),"region=[[10.0,10.0]-[12.0,12.0]], items={8@[10.0,10.0]}\n");
		p = Point.newPoint(10.5,9.5);
		assertEquals(tree2.getNearestNode(p).toString(),"region=[[10.0,8.0]-[12.0,10.0]], items={17@[10.5,9.5],18@[11.5,9.5],19@[10.5,8.5],20@[11.5,9.5],21@[11.5,9.6],22@[11.5,9.5]}\n");
	}

	@Test
	void testGetAllItems() {
		fillTree();
		assertEquals(showList("testGetAllItems",tree.getAllItems()),38);
	}

	@Test
	void testGetNearestItem() {
		fillTree();
		Point p = Point.newPoint(5,5,4,6); // 17
		assertEquals(tree.getNearestItem(p),Integer.valueOf(17));
		p = Point.newPoint(1,2,6,9); // 30
		assertEquals(tree.getNearestItem(p),Integer.valueOf(30));
		p = Point.newPoint(1,2,6,9.01); // very close to 30
		assertEquals(tree.getNearestItem(p),Integer.valueOf(30));
		p = Point.newPoint(6,6,3,6); // halfway between 13 and 14
		// returns 13 but should rather return both items...
		assertEquals(tree.getNearestItem(p),Integer.valueOf(13));
	}

	@Test
	void testRemove() {
//		fillTree2();
//		// this is easy
//		assertTrue(tree2.remove(2, Point.newPoint(12,4)));
//		// this is less easy
//		assertTrue(tree2.remove(4, Point.newPoint(12,12)));
//		// remove everything else
//		assertTrue(tree2.remove(1, Point.newPoint(4,4)));
//		assertTrue(tree2.remove(3, Point.newPoint(4,12)));
//		assertTrue(tree2.remove(5, Point.newPoint(10,14)));
//		assertTrue(tree2.remove(6, Point.newPoint(14,14)));
//		assertTrue(tree2.remove(7, Point.newPoint(14,10)));
//		assertTrue(tree2.remove(8, Point.newPoint(10,10)));
//		assertTrue(tree2.remove(9, Point.newPoint(9,13)));
//		assertTrue(tree2.remove(10, Point.newPoint(13,13)));
//		assertEquals(tree2.size(),12);
////		System.out.println(tree2.toShortString());
//		assertTrue(tree2.remove(11, Point.newPoint(9,9)));
//		assertTrue(tree2.remove(12, Point.newPoint(13,9)));
//		assertTrue(tree2.remove(13, Point.newPoint(8.5,11.5)));
//		assertTrue(tree2.remove(14, Point.newPoint(9.5,11.5)));
//		assertTrue(tree2.remove(15, Point.newPoint(8.5,10.5)));
//		assertTrue(tree2.remove(16, Point.newPoint(9.5,10.5)));
//		assertTrue(tree2.remove(17, Point.newPoint(10.5,9.5)));
//		assertTrue(tree2.remove(18, Point.newPoint(11.5,9.5)));
//		assertTrue(tree2.remove(19, Point.newPoint(10.5,8.5)));
//		assertTrue(tree2.remove(20, Point.newPoint(11.5,9.5)));
//		assertEquals(tree2.size(),2);
////		System.out.println(tree2.toShortString());
//		assertTrue(tree2.remove(21, Point.newPoint(11.5,9.6)));
//		assertTrue(tree2.remove(22, Point.newPoint(11.5,9.5)));
//		assertEquals(tree2.size(),0);
////		System.out.println(tree2.toShortString());
//		// removal on borders
//		fillTree2();
//		tree2.insert(23, Point.newPoint(0,5));
//		tree2.insert(24, Point.newPoint(0,0));
//		tree2.insert(25, Point.newPoint(8,5));
//		tree2.insert(26, Point.newPoint(8,8));
//		tree2.insert(27, Point.newPoint(16,16));
//		tree2.insert(28, Point.newPoint(16,0));
//		assertTrue(tree2.remove(23, Point.newPoint(0,5)));
//		assertTrue(tree2.remove(24, Point.newPoint(0,-0.1)));
//		assertTrue(tree2.remove(25, Point.newPoint(8,5)));
//		assertTrue(tree2.remove(26, Point.newPoint(8,8)));
//		assertTrue(tree2.remove(27, Point.newPoint(16,16)));
//		assertTrue(tree2.remove(28, Point.newPoint(16,0)));
//		assertEquals(tree2.size(),22);
	}

	@Test
	void testRemove2() {
		fillTree2();
		assertTrue(tree2.remove(2));
		assertTrue(tree2.remove(3));
		assertTrue(tree2.remove(1));
		assertTrue(tree2.remove(4));
		assertTrue(tree2.remove(5));
		assertTrue(tree2.remove(6));
		assertTrue(tree2.remove(7));
		assertTrue(tree2.remove(8));
		assertTrue(tree2.remove(9));
		assertTrue(tree2.remove(10));
		assertTrue(tree2.remove(11));
		assertTrue(tree2.remove(12));
		assertTrue(tree2.remove(13));
		assertTrue(tree2.remove(14));
		assertTrue(tree2.remove(15));
		assertTrue(tree2.remove(16));
		assertTrue(tree2.remove(17));
		assertTrue(tree2.remove(18));
		assertTrue(tree2.remove(19));
		assertTrue(tree2.remove(20));
		assertTrue(tree2.remove(21));
		assertTrue(tree2.remove(22));
		assertEquals(tree2.size(),0);
	}

	@Test
	void testRemove3() {
		for (int i=0; i<1000000; i++)
			tree2.insert(i, Point.newPoint(Math.random()*16,Math.random()*16));
		assertEquals(tree2.size(),1000000);
		for (int i=0; i<1000000; i++)
			tree2.remove(i);
		assertEquals(tree2.size(),0);
	}

	@Test
	void testSize() {
		assertEquals(tree.size(),0);
		fillTree();
		assertEquals(tree.size(),38);
	}

	// utility for below
	private int showList(String message,Iterable<?> list) {
		int count=0;
		System.out.print(message+": list = {");
		for (Object i:list) {
			System.out.print(i+" ");
			count++;
		}
		System.out.println("}");
		return count;
	}

	@Test
	void testGetItemsWithinBox() {
		fillTree();
		// this is one of the tree's boxes. it contains items 1 and 18
		Box b = Box.boundingBox(Point.newPoint(0.0,0.0,0.0,0.0), Point.newPoint(5.0,5.0,4.0,6.0));
		Iterable<Integer> items = tree.getItemsWithin(b);
		assertEquals(showList("testGetItemsWithinBox",items),2);
		// this is a small region overlapping all quadrants, nodes 1-17 are in there.
		b = Box.boundingBox(Point.newPoint(4,4,3,5), Point.newPoint(6,6,5,7));
		items = tree.getItemsWithin(b);
		assertEquals(showList("testGetItemsWithinBox",items),17);
		// removing a small slice of the above, 8 points are lost
		b = Box.boundingBox(Point.newPoint(4,4,3,5), Point.newPoint(6,6,5,6.9));
		items = tree.getItemsWithin(b);
		assertEquals(showList("testGetItemsWithinBox",items),9);
		// that box should only contain 17
		b = Box.boundingBox(Point.newPoint(4.1,4.1,3.1,5.1), Point.newPoint(5.9,5.9,4.9,6.9));
		items = tree.getItemsWithin(b);
		assertEquals(showList("testGetItemsWithinBox",items),1);
		// this box is empty
		b = Box.boundingBox(Point.newPoint(0,0,0,0.1), Point.newPoint(1,1,1,1));
		items = tree.getItemsWithin(b);
		assertEquals(showList("testGetItemsWithinBox",items),0);
	}

	@Test
	void testGetItemsWithinSphere() {
		fillTree();
		// this is a small region overlapping all quadrants, only node 17 is in there.
		Sphere s = Sphere.newSphere(Point.newPoint(5,5,4,6),1);
		Iterable<Integer> items = tree.getItemsWithin(s);
		assertEquals(showList("testGetItemsWithinSphere",items),1);
		// this is a larger region overlapping all quadrants, nodes 1-17 are in there.
		s = Sphere.newSphere(Point.newPoint(5,5,4,6),2);
		items = tree.getItemsWithin(s);
		assertEquals(showList("testGetItemsWithinSphere",items),17);
		// this sphere is empty
		s = Sphere.newSphere(Point.newPoint(0.5,0.5,0.5,0.5), 0.1);
		items = tree.getItemsWithin(s);
		assertEquals(showList("testGetItemsWithinSphere",items),0);
	}

	@Test
	void testToString() {
		fillTree();
		assertEquals(tree.toString(),"BoundedRegionIndexingTree\n" +
				"region=[[0.0,0.0,0.0,0.0]-[10.0,10.0,8.0,12.0]], items={}\n" +
				"--region=[[0.0,0.0,0.0,0.0]-[5.0,5.0,4.0,6.0]], items={1@[4.0,4.0,3.0,5.0],18@[0.0,0.0,0.0,0.0]}\n" +
				"--region=[[0.0,0.0,0.0,6.0]-[5.0,5.0,4.0,12.0]], items={2@[4.0,4.0,3.0,7.0],22@[0.0,0.0,0.0,12.0],27@[0.0,0.0,0.0,6.0]}\n" +
				"--region=[[0.0,0.0,4.0,0.0]-[5.0,5.0,8.0,6.0]], items={3@[4.0,4.0,5.0,5.0]}\n" +
				"--region=[[0.0,0.0,4.0,6.0]-[5.0,5.0,8.0,12.0]], items={}\n" +
				"----region=[[0.0,0.0,4.0,6.0]-[2.5,2.5,6.0,9.0]], items={}\n" +
				"----region=[[0.0,0.0,4.0,9.0]-[2.5,2.5,6.0,12.0]], items={}\n" +
				"----region=[[0.0,0.0,6.0,6.0]-[2.5,2.5,8.0,9.0]], items={}\n" +
				"----region=[[0.0,0.0,6.0,9.0]-[2.5,2.5,8.0,12.0]], items={32@[2.0,1.0,6.0,11.0],33@[2.0,1.0,7.0,10.0],34@[2.0,2.0,6.0,9.0],21@[0.0,0.0,8.0,12.0],37@[2.0,2.0,6.0,9.0],28@[1.0,1.0,6.0,11.0],29@[1.0,1.0,7.0,10.0],30@[1.0,2.0,6.0,9.0]}\n" +
				"----region=[[0.0,2.5,4.0,6.0]-[2.5,5.0,6.0,9.0]], items={}\n" +
				"----region=[[0.0,2.5,4.0,9.0]-[2.5,5.0,6.0,12.0]], items={}\n" +
				"----region=[[0.0,2.5,6.0,6.0]-[2.5,5.0,8.0,9.0]], items={}\n" +
				"----region=[[0.0,2.5,6.0,9.0]-[2.5,5.0,8.0,12.0]], items={}\n" +
				"----region=[[2.5,0.0,4.0,6.0]-[5.0,2.5,6.0,9.0]], items={}\n" +
				"----region=[[2.5,0.0,4.0,9.0]-[5.0,2.5,6.0,12.0]], items={}\n" +
				"----region=[[2.5,0.0,6.0,6.0]-[5.0,2.5,8.0,9.0]], items={}\n" +
				"----region=[[2.5,0.0,6.0,9.0]-[5.0,2.5,8.0,12.0]], items={35@[4.0,2.0,6.0,10.0],36@[4.0,2.0,6.0,10.0],38@[4.0,2.0,6.0,10.0],31@[3.0,2.0,6.0,10.0]}\n" +
				"----region=[[2.5,2.5,4.0,6.0]-[5.0,5.0,6.0,9.0]], items={4@[4.0,4.0,5.0,7.0]}\n" +
				"----region=[[2.5,2.5,4.0,9.0]-[5.0,5.0,6.0,12.0]], items={}\n" +
				"----region=[[2.5,2.5,6.0,6.0]-[5.0,5.0,8.0,9.0]], items={}\n" +
				"----region=[[2.5,2.5,6.0,9.0]-[5.0,5.0,8.0,12.0]], items={}\n" +
				"--region=[[0.0,5.0,0.0,0.0]-[5.0,10.0,4.0,6.0]], items={5@[4.0,6.0,3.0,5.0]}\n" +
				"--region=[[0.0,5.0,0.0,6.0]-[5.0,10.0,4.0,12.0]], items={6@[4.0,6.0,3.0,7.0]}\n" +
				"--region=[[0.0,5.0,4.0,0.0]-[5.0,10.0,8.0,6.0]], items={7@[4.0,6.0,5.0,5.0],24@[0.0,10.0,8.0,0.0]}\n" +
				"--region=[[0.0,5.0,4.0,6.0]-[5.0,10.0,8.0,12.0]], items={20@[0.0,10.0,8.0,12.0],8@[4.0,6.0,5.0,7.0],25@[0.0,5.0,5.0,12.0],26@[0.0,5.0,8.0,6.0]}\n" +
				"--region=[[5.0,0.0,0.0,0.0]-[10.0,5.0,4.0,6.0]], items={9@[6.0,4.0,3.0,5.0]}\n" +
				"--region=[[5.0,0.0,0.0,6.0]-[10.0,5.0,4.0,12.0]], items={10@[6.0,4.0,3.0,7.0]}\n" +
				"--region=[[5.0,0.0,4.0,0.0]-[10.0,5.0,8.0,6.0]], items={11@[6.0,4.0,5.0,5.0]}\n" +
				"--region=[[5.0,0.0,4.0,6.0]-[10.0,5.0,8.0,12.0]], items={12@[6.0,4.0,5.0,7.0]}\n" +
				"--region=[[5.0,5.0,0.0,0.0]-[10.0,10.0,4.0,6.0]], items={13@[6.0,6.0,3.0,5.0]}\n" +
				"--region=[[5.0,5.0,0.0,6.0]-[10.0,10.0,4.0,12.0]], items={23@[10.0,10.0,0.0,12.0],14@[6.0,6.0,3.0,7.0]}\n" +
				"--region=[[5.0,5.0,4.0,0.0]-[10.0,10.0,8.0,6.0]], items={15@[6.0,6.0,5.0,5.0]}\n" +
				"--region=[[5.0,5.0,4.0,6.0]-[10.0,10.0,8.0,12.0]], items={16@[6.0,6.0,5.0,7.0],17@[5.0,5.0,4.0,6.0],19@[10.0,10.0,8.0,12.0]}\n");
	}

	@Test
	void testToShortString() {
		fillTree();
		assertEquals(tree.toShortString(),"BoundedRegionIndexingTree\n" +
				"region = [[0.0,0.0,0.0,0.0],[10.0,10.0,8.0,12.0]]\n" +
				"items={}\n" +
				"--items={1,18}\n" +
				"--items={2,22,27}\n" +
				"--items={3}\n" +
				"--items={}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={32,33,34,21,37,28,29,30}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={35,36,38,31}\n" +
				"----items={4}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"----items={}\n" +
				"--items={5}\n" +
				"--items={6}\n" +
				"--items={7,24}\n" +
				"--items={20,8,25,26}\n" +
				"--items={9}\n" +
				"--items={10}\n" +
				"--items={11}\n" +
				"--items={12}\n" +
				"--items={13}\n" +
				"--items={23,14}\n" +
				"--items={15}\n" +
				"--items={16,17,19}\n");
	}

}
