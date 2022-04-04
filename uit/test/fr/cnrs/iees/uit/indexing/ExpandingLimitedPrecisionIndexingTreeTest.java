package fr.cnrs.iees.uit.indexing;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;

import org.junit.jupiter.api.Test;

import fr.cnrs.iees.uit.space.Box;
import fr.cnrs.iees.uit.space.Point;

class ExpandingLimitedPrecisionIndexingTreeTest {

	private ExpandingLimitedPrecisionIndexingTree<Integer> tree;
	
	@Test
	void testInsert() {
		
		Random rng = new Random();
		Box limits = Box.boundingBox(Point.newPoint(0,0),Point.newPoint(100,100));
		tree = new ExpandingLimitedPrecisionIndexingTree<>(limits,1);
		for (int i=0; i<15; i++) {
			double[] coord = new double[2];
			for (int j=0; j<2; j++)
				coord[j] = limits.lowerBound(j)+rng.nextDouble()*limits.sideLength(j);
			tree.insert(i, Point.newPoint(coord));
		}
//		System.out.println(tree.toShortString());
		tree.insert(16, Point.newPoint(-10,50));
//		System.out.println(tree.toShortString());
		
		for (int i=17; i<30; i++) {
			double[] coord = new double[2];
			for (int j=0; j<2; j++)
				coord[j] = limits.lowerBound(j)-rng.nextDouble()*limits.sideLength(j)*10;
			tree.insert(i,Point.newPoint(coord));
		}
//		System.out.println(tree.toShortString());

		tree.clear();
//		System.out.println(tree.toShortString());
		fail("Not yet implemented");
	}

}
