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
package fr.cnrs.iees.uit.space;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DistanceTest {

	@Test
	void testSqr() {
		assertEquals(Distance.sqr(2.0),4.0);
		// this fails because of rounding error on doubles.
		// Distance.sqr(Math.sqrt(3.0)) = 2.9999999999999996
//		assertEquals(Distance.sqr(Math.sqrt(3.0)),3.0);
	}

	@Test
	void testDistance1D() {
		assertEquals(Distance.distance1D(4,3),1);
		assertEquals(Distance.distance1D(-4,3),7);
	}

	@Test
	void testSquaredEuclidianDistanceDoubleDouble() {
		assertEquals(Distance.squaredEuclidianDistance(4,3),1);
		assertEquals(Distance.squaredEuclidianDistance(-4,3),49);
	}

	@Test
	void testSquaredEuclidianDistanceDoubleDoubleDoubleDouble() {
		assertEquals(Distance.squaredEuclidianDistance(3,0,0,4),25);
	}

	@Test
	void testSquaredEuclidianDistanceDoubleDoubleDoubleDoubleDoubleDouble() {
		assertEquals(Distance.squaredEuclidianDistance(3,0,1,0,4,1),25);
	}

	@Test
	void testSquaredEuclidianDistancePointPoint() {
		Point p1 = Point.newPoint(3,0);
		Point p2 = Point.newPoint(0,4);
		assertEquals(Distance.squaredEuclidianDistance(p1,p2),25);
	}

	@Test
	void testEuclidianDistanceDoubleDouble() {
		assertEquals(Distance.euclidianDistance(4,3),1);
		assertEquals(Distance.euclidianDistance(-4,3),7);
	}

	@Test
	void testEuclidianDistanceDoubleDoubleDoubleDouble() {
		assertEquals(Distance.euclidianDistance(3,0,0,4),5);
	}

	@Test
	void testEuclidianDistanceDoubleDoubleDoubleDoubleDoubleDouble() {
		assertEquals(Distance.euclidianDistance(3,0,1,0,4,1),5);
	}

	@Test
	void testEuclidianDistancePointPoint() {
		Point p1 = Point.newPoint(3,0);
		Point p2 = Point.newPoint(0,4);
		assertEquals(Distance.euclidianDistance(p1,p2),5);
	}

	@Test
	void testDistanceToClosestEdge() {
		Point p1 = Point.newPoint(0,0,0);
		Point p2 = Point.newPoint(2,4,1.5);
		Box b = new BoxImpl(p1,p2);
		Point A = Point.newPoint(-1,-1,-1);
		Point B = Point.newPoint(1.5,5,3);
		Point C = Point.newPoint(1,1,1);
		Point D = Point.newPoint(0.4,3,2);
		Point E = Point.newPoint(-0.2,3,0.75);
//		System.out.println(Distance.distanceToClosestEdge(A,b));
//		System.out.println(Distance.distanceToClosestEdge(B,b));
//		System.out.println(Distance.distanceToClosestEdge(C,b));
//		System.out.println(Distance.distanceToClosestEdge(D,b));
		assertEquals(Distance.distanceToClosestEdge(A,b),1.7320508075688774); // result should be sqrt(3)
		assertEquals(Distance.distanceToClosestEdge(B,b),1.8027756377319946); // result should be sqrt(3.25)
		assertEquals(Distance.distanceToClosestEdge(C,b),0.5);
		assertEquals(Distance.distanceToClosestEdge(D,b),0.5);
		assertEquals(Distance.distanceToClosestEdge(E,b),0.2);
	}

}
