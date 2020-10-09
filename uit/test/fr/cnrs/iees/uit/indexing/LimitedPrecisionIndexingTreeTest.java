package fr.cnrs.iees.uit.indexing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.uit.space.Box;
import fr.cnrs.iees.uit.space.Point;

class LimitedPrecisionIndexingTreeTest {
	
	private Box limits;
	private LimitedPrecisionIndexingTree<Integer> tree;
	
	@BeforeEach
	private void init() {
		limits = Box.boundingBox(Point.newPoint(0,0,0,0),Point.newPoint(10,10,8,12));
		// extreme (4D, not square) testing
		tree = new LimitedPrecisionIndexingTree<>(limits,0.0001);
		tree.setOptimisation(true);
		// a simpler (2D) case
	}

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
		Point p = Point.newPoint(2,2,6,9);
		tree.insert(1, p);
		tree.insert(2, p);
		assertEquals(tree.toShortString(),"LimitedPrecisionIndexingTree\n"
			+ "region = [[0,0,0,0][131072,131072,131072,131072]]\n"
			+ "items={1,2}\n");
		fillTree();
		System.out.println(tree.toShortString());
		
	}

	@Test
	void testRemove() {
		fail("Not yet implemented");
	}

	@Test
	void testGetNearestItem() {
		assertThrows(UnsupportedOperationException.class,
			()->tree.getNearestItem(Point.newPoint(4,2,6,10)));
	}

	@Test
	void testGetNearestItems() {
		fail("Not yet implemented");
	}

	@Test
	void testGetItemsWithinBox() {
		fillTree();
//		p = Point.newPoint(6,4,5,5); 		tree.insert(11, p);
//		p = Point.newPoint(6,6,5,5); 		tree.insert(15, p);
//		p = Point.newPoint(6,6,5,7); 		tree.insert(16, p);
//		p = Point.newPoint(6,4,5,7); 		tree.insert(12, p);
		Box inLimits = Box.boundingBox(Point.newPoint(4.5,0,5,2.3),Point.newPoint(9,9.9,7.0,10.2));
		Collection<Integer> list = tree.getItemsWithin(inLimits);
		assertTrue(list.contains(11));
		assertTrue(list.contains(15));
		assertTrue(list.contains(16));
		assertTrue(list.contains(12));
		assertFalse(list.contains(1));
	}

	@Test
	void testGetItemsWithinSphere() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllItems() {
		fillTree();
		assertEquals(tree.getAllItems().size(),38);
	}

}
