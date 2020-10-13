package fr.cnrs.iees.uit.indexing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.uit.space.Box;
import fr.cnrs.iees.uit.space.Point;
import fr.cnrs.iees.uit.space.Sphere;

class LimitedPrecisionIndexingTreeTest {
	
	private Box limits, limits2;
	private LimitedPrecisionIndexingTree<Integer> tree, tree2;
	
	@BeforeEach
	private void init() {
		limits = Box.boundingBox(Point.newPoint(0,0,0,0),Point.newPoint(10,10,8,12));
		// extreme (4D, not square) testing
		tree = new LimitedPrecisionIndexingTree<>(limits,0.0001);
		tree.setOptimisation(true);
		// a simpler (2D) case
		limits2 = Box.boundingBox(Point.newPoint(0,0),Point.newPoint(100,100));
		tree2 = new LimitedPrecisionIndexingTree<>(limits2,0.0000001);
		tree2.setOptimisation(true);
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
//		System.out.println(tree.toShortString());
	}

	@Test
	void testRemove() {
		fillTree();
		for (int i=1; i<=25; i++)
			tree.remove(i);
		assertEquals(tree.size(),13);
		for (int i=26; i<39; i++)
			tree.remove(i);
		assertEquals(tree.toShortString(),"LimitedPrecisionIndexingTree\n"
				+ "region = [[0,0,0,0][131072,131072,131072,131072]]\n"
				+ "items={}\n");
	}

	@Test
	void testGetNearestItem() {
		assertThrows(UnsupportedOperationException.class,
			()->tree.getNearestItem(Point.newPoint(4,2,6,10)));
	}

	@Test
	void testGetNearestItems() {
		tree2.insert(1, Point.newPoint(0,0));
		tree2.insert(2, Point.newPoint(0,0));
		tree2.insert(3, Point.newPoint(0,0));
		tree2.insert(4, Point.newPoint(0,0));
		tree2.insert(5, Point.newPoint(10,0.5));
		tree2.insert(6, Point.newPoint(0.001,0.0001));
		tree2.insert(7, Point.newPoint(0,100));
		tree2.insert(8, Point.newPoint(100,0));
		tree2.insert(9, Point.newPoint(100,100));
		tree2.insert(10, Point.newPoint(10,0.5));
		tree2.insert(11, Point.newPoint(100,0.5));
//		System.out.println(tree2.toString());
		assertTrue(tree2.getNearestItems(Point.newPoint(99,99)).contains(9));
		assertTrue(tree2.getNearestItems(Point.newPoint(1,1)).contains(6));
		// returns a list of the 4 points located at (0,0)
		Collection<Integer> l = tree2.getNearestItems(Point.newPoint(0.00000001,0.00000001));
		assertTrue(l.contains(1));
		assertTrue(l.contains(2));
		assertTrue(l.contains(3));
		assertTrue(l.contains(4));
		// returns the list of the 2 points at the same distance of focal point
		l = tree2.getNearestItems(Point.newPoint(100,0.25));
		assertTrue(l.contains(11));
		assertTrue(l.contains(8));
		l = tree2.getNearestItems(Point.newPoint(50,80));
		assertTrue(l.contains(7));
		assertTrue(l.contains(9));
	}
	
	@Test
	void testGetNearestItemsRank() {
		tree2.insert(1, Point.newPoint(0,0));
		tree2.insert(2, Point.newPoint(0,0));
		tree2.insert(3, Point.newPoint(0,0));
		tree2.insert(4, Point.newPoint(0,0));
		tree2.insert(5, Point.newPoint(10,0.5));
		tree2.insert(6, Point.newPoint(0.001,0.0001));
		tree2.insert(7, Point.newPoint(0,100));
		tree2.insert(8, Point.newPoint(100,0));
		tree2.insert(9, Point.newPoint(100,100));
		tree2.insert(10, Point.newPoint(10,0.5));
		tree2.insert(11, Point.newPoint(100,0.5));
		// returns closest neighbours and 2nd rank closest neighbours
		Collection<Integer> l = tree2.getNearestItems(Point.newPoint(50,80),2);
		assertTrue(l.contains(7));
		assertTrue(l.contains(9));
		assertTrue(l.contains(5));
		assertTrue(l.contains(10));
		l = tree2.getNearestItems(Point.newPoint(0.01,0.001),4);
//		System.out.println(l.toString());
	}


	@Test
	void testGetItemsWithinBox() {
		fillTree();
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
		fillTree();
		fillTree();
		Sphere inLimits = Sphere.newSphere(Point.newPoint(4.5,0,5,2.3), 6.5);
		Collection<Integer> l = tree.getItemsWithin(inLimits);
		assertTrue(l.contains(18));
		assertTrue(l.contains(27));
	}

	@Test
	void testGetAllItems() {
		fillTree();
		assertEquals(tree.getAllItems().size(),38);
	}
	
	@Test
	void stressTest() {
		System.out.println("Stress test:");
		double precision = 0.000001;
		tree2 = new LimitedPrecisionIndexingTree<>(limits2,precision);
		Random rng = new Random();
		int aVeryLargeNumber = 1000000;
//		
//		for (int i=0; i<100; i++)
//		for (int j=0; j<100; j++)
//			tree2.insert(i*100+j, Point.newPoint(i*1.0,j*1.0));
//		
		System.out.println("Inserting "+aVeryLargeNumber+ " items");
		long t0 =System.currentTimeMillis();
		for (int i=0; i<aVeryLargeNumber; i++) {
			double[] coord = new double[2];
			for (int j=0; j<2; j++)
				coord[j] = limits2.lowerBound(j)+rng.nextDouble()*limits2.sideLength(j);
			tree2.insert(i, Point.newPoint(coord));
		}
		System.out.println("...done in "+(System.currentTimeMillis()-t0)+" ms.");
		
		System.out.println("Randomly removing 30% of items");
		t0 =System.currentTimeMillis();
		for (int i=0; i<aVeryLargeNumber*0.3; i++) {
			int item = rng.nextInt(aVeryLargeNumber);
			tree2.remove(item);
		}
		System.out.println("tree now of size "+tree2.size());
		System.out.println("...done in "+(System.currentTimeMillis()-t0)+" ms.");
		
//		System.out.println("finding nearest neighbours 100 times (item ids)");
//		t0 =System.currentTimeMillis();
//		for (int i=0; i<100; i++) {
//			double[] coord = new double[2];
//			for (int j=0; j<2; j++)
//				coord[j] = limits2.lowerBound(j)+rng.nextDouble()*limits2.sideLength(j);
//			System.out.print(tree2.getNearestItems(Point.newPoint(coord)).toString()+" ");
//		}	
//		System.out.println("\n...done in "+(System.currentTimeMillis()-t0)+" ms.");
		
		System.out.println("finding neighbours 100 times within box of side 10 (#items)");
		t0 =System.currentTimeMillis();
		for (int i=0; i<100; i++) {
			double side = 10;
			double[] coords = new double[2];
			for (int j=0; j<2; j++)
				coords[j] = limits2.lowerBound(j)+rng.nextDouble()*limits2.sideLength(j);
			Point A = Point.newPoint(coords);
			Box lim = Box.boundingBox(A,Point.add(A,side));
			System.out.print(tree2.getItemsWithin(lim).size()+" ");
		}	
		System.out.println("\n...done in "+(System.currentTimeMillis()-t0)+" ms.");
		
		System.out.println("finding neighbours 100 times within radius 5 (#items)");
		t0 =System.currentTimeMillis();
		for (int i=0; i<100; i++) {
			double radius = 5;
			double[] coords = new double[2];
			for (int j=0; j<2; j++)
				coords[j] = limits2.lowerBound(j)+rng.nextDouble()*limits2.sideLength(j);
			Point A = Point.newPoint(coords);
			Sphere S = Sphere.newSphere(A, radius);
			System.out.print(tree2.getItemsWithin(S).size()+" ");
		}	
		System.out.println("\n...done in "+(System.currentTimeMillis()-t0)+" ms.");
		
		System.out.println("clearing tree");
		t0 =System.currentTimeMillis();
		tree2.clear();
		System.out.println("...done in "+(System.currentTimeMillis()-t0)+" ms.");
	}

}
