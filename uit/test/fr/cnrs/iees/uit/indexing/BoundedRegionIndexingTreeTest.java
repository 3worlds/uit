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

class BoundedRegionIndexingTreeTest {

	private Box limits;
	private RegionIndexingTree<Integer> tree;
	
	@BeforeEach
	private void init() {
		limits = Box.boundingBox(Point.newPoint(0,0,0,0),Point.newPoint(10,10,8,12));
		tree = new BoundedRegionIndexingTree<>(limits);
		tree.setOptimisation(true);
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
		assertEquals(tree.toShortString(),"RegionIndexingTree\n" + 
				"region = [[0.0,0.0,0.0,0.0],[10.0,10.0,8.0,12.0]]\n" + 
				"items={1,2,3,4,5,6,7,8,9,10}\n");
		p = Point.newPoint(6,4,5,5); 		tree.insert(11, p);
		p = Point.newPoint(6,4,5,7); 		tree.insert(12, p);
		p = Point.newPoint(6,6,3,5); 		tree.insert(13, p);
		p = Point.newPoint(6,6,3,7); 		tree.insert(14, p);
		p = Point.newPoint(6,6,5,5); 		tree.insert(15, p);
		p = Point.newPoint(6,6,5,7); 		tree.insert(16, p);
		// now there must be only one item in each node
		assertEquals(tree.toShortString(),"RegionIndexingTree\n" + 
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
		assertEquals(tree.toShortString(),"RegionIndexingTree\n" + 
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
		assertEquals(tree.toShortString(),"RegionIndexingTree\n" + 
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
		assertEquals(tree.toShortString(),"RegionIndexingTree\n" + 
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
		assertEquals(showList(nodes),1);
		// this is another (small) region contained within first quadrant, first quadrant is returned
		b = Box.boundingBox(Point.newPoint(0,0,0,0), Point.newPoint(0.1,0.1,0.1,0.1));
		nodes = tree.getNodesWithin(b);
		assertEquals(showList(nodes),1);
		// this is a large region containing 2 quadrants
		b = Box.boundingBox(Point.newPoint(0,0,0,0), Point.newPoint(5,5,4,12));
		nodes = tree.getNodesWithin(b);
		assertEquals(showList(nodes),2);
		// this is a small region overlapping all quadrants, all nodes are returned
		b = Box.boundingBox(Point.newPoint(4,4,3,5), Point.newPoint(6,6,5,7));
		nodes = tree.getNodesWithin(b);
		assertEquals(showList(nodes),16);
	}

	@Test
	void testGetNearestNode() {
		fillTree();
		// the central point of the region - returns the first quadrant found
		// maybe this is not a very safe behaviour...
		Point p = Point.newPoint(5,5,4,6);
		RegionIndexingNode<Integer> node = tree.getNearestNode(p);
		assertEquals(node.toString(),"region=[[0.0,0.0,0.0,0.0]-[5.0,5.0,4.0,6.0]], items={1@[4.0,4.0,3.0,5.0],18@[0.0,0.0,0.0,0.0]}\n");
		p = Point.newPoint(2,1,6,11); // this goes wrong - does not return the correct node
		node = tree.getNearestNode(p);
//		System.out.println(node);
	}

	@Test
	void testGetNearestItem() {
		fillTree();
		Point p = Point.newPoint(5,5,4,6); // 17
		assertEquals(tree.getNearestItem(p),new Integer(17));
		p = Point.newPoint(1,2,6,9); // 30
		assertEquals(tree.getNearestItem(p),new Integer(30));
		p = Point.newPoint(1,2,6,9.01); // very close to 30
		assertEquals(tree.getNearestItem(p),new Integer(30));
		p = Point.newPoint(6,6,3,6); // halfway between 13 and 14
		// returns 13 but should rather return both items...
		assertEquals(tree.getNearestItem(p),new Integer(13));
	}

	@Test
	void testRemove() {
		fillTree();
		
	}

	@Test
	void testSize() {
		assertEquals(tree.size(),0);
		fillTree();
		assertEquals(tree.size(),38);
	}
	
	// utility for below
	private int showList(Iterable<?> list) {
		int count=0;
		System.out.print("list = {");
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
		assertEquals(showList(items),2);
		// this is a small region overlapping all quadrants, nodes 1-17 are in there.
		b = Box.boundingBox(Point.newPoint(4,4,3,5), Point.newPoint(6,6,5,7));
		items = tree.getItemsWithin(b);
		assertEquals(showList(items),17);
		// removing a small slice of the above, 8 points are lost
		b = Box.boundingBox(Point.newPoint(4,4,3,5), Point.newPoint(6,6,5,6.9));
		items = tree.getItemsWithin(b);
		assertEquals(showList(items),9);
		// that box should only contain 17
		b = Box.boundingBox(Point.newPoint(4.1,4.1,3.1,5.1), Point.newPoint(5.9,5.9,4.9,6.9));
		items = tree.getItemsWithin(b);
		assertEquals(showList(items),1);
		// this box is empty
		b = Box.boundingBox(Point.newPoint(0,0,0,0.1), Point.newPoint(1,1,1,1));
		items = tree.getItemsWithin(b);
		assertEquals(showList(items),0);
	}

	@Test
	void testGetItemsWithinSphere() {
		fail("Not yet implemented");
	}

	@Test
	void testToString() {
		fillTree();
		assertEquals(tree.toString(),"RegionIndexingTree\n" + 
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
		assertEquals(tree.toShortString(),"RegionIndexingTree\n" + 
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
