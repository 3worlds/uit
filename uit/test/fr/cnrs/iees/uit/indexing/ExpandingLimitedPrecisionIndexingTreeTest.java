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
