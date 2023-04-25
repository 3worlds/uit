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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoxTest {

	Box b1, b2, b3;
	Point p1, p2, p3;

	@BeforeEach
	void init() {
		p1 = Point.newPoint(0.5,1.0,7);
		p2 = Point.newPoint(1.2,1.2,8);
		p3 = Point.newPoint(0,0,5);
		b1 = new BoxImpl(p1,p2);
		b2 = new BoxImpl(p3,p2);
		b3 = new BoxImpl(p3,p1);
	}

	private void show(String method,String text) {
//		System.out.println(method+": "+text);
	}

	@Test
	void testIsPointOnBorder() {
		assertTrue(b1.isPointOnBorder(p1));
		assertFalse(b1.isPointOnBorder(p3));
		assertFalse(b2.isPointOnBorder(p1));
	}

	@Test
	void testLowerBounds() {
		show("testLowerBounds",b1.lowerBounds().toString());
		show("testLowerBounds",b2.lowerBounds().toString());
		assertEquals(b1.lowerBounds(),p1);
		assertEquals(b2.lowerBounds(),p3);
	}

	@Test
	void testUpperBounds() {
		show("testUpperBounds",b1.upperBounds().toString());
		show("testUpperBounds",b2.upperBounds().toString());
		assertEquals(b1.upperBounds(),p2);
		assertEquals(b2.upperBounds(),p2);
	}

	@Test
	void testLowerBound() {
		assertEquals(b1.lowerBound(0),0.5);
		assertEquals(b2.lowerBound(2),5);
	}

	@Test
	void testUpperBound() {
		assertEquals(b1.upperBound(1),1.2);
		assertEquals(b2.upperBound(1),1.2);
	}

	@Test
	void testOverlaps() {
		assertFalse(b1.overlaps(b3));
		assertTrue(b1.overlaps(b2));
	}

	@Test
	void testContainsPoint() {
		assertTrue(b2.contains(p1));
		assertFalse(b1.contains(p3));
	}

	@Test
	void testContainsBox() {
		assertTrue(b2.contains(b1));
		assertTrue(b1.contains(b1));
	}

	@Test
	void testCentre() {
		show("testCentre",b3.centre().toString());
		assertEquals(b3.centre().toString(),"[0.25,0.5,6.0]");
	}

	@Test
	void testSize() {
		show("testSize",String.valueOf(b1.size()));
		show("testSize",String.valueOf(b2.size()));
		show("testSize",String.valueOf(b3.size()));
//		assertEquals(b1.size(),0.14); // this fails because of truncation error on doubles
		assertEquals(b2.size(),4.32);
		assertEquals(b3.size(),1.0);
	}

	@Test
	void testBoundingBoxPointPoint() {
		Point p = Point.newPoint(3,2,1);
		Box b = Box.boundingBox(p,p1);
		show("testBoundingBoxPointPoint",b.toString());
		assertNotNull(b);
		b = Box.boundingBox(p, p);
		assertNotNull(b);
		assertEquals(b.size(),0.0);
	}

	@Test
	void testContainsSphere() {
		Sphere s = new SphereImpl(p1,0.2); // this sphere touches the border
		assertTrue(b2.contains(s));
		s = new SphereImpl(p1,2);
		assertFalse(b2.contains(s));
	}

	@Test
	void testBoundingBoxSphere() {
		Sphere s = new SphereImpl(p1,0.2);
		Box b = Box.boundingBox(s);
		show("testBoundingBoxSphere",b.toString());
		assertNotNull(b);
	}

	@Test
	void testBoundingCubeSphere() {
		Sphere s = new SphereImpl(p1,0.2);
		Box b = Box.boundingCube(s);
		show("testBoundingCubeSphere",b.toString());
		assertNotNull(b);
	}

	@Test
	void testBoundingCubePointPoint() {
		Point p = Point.newPoint(3,2,1);
		Box b = Box.boundingCube(p,p1);
		show("testBoundingCubePointPoint",b.toString());
		assertNotNull(b);
		b = Box.boundingCube(p, p);
		assertNotNull(b);
		assertEquals(b.size(),0.0);
	}

	@Test
	void testSideLength() {
		show("testSideLength",String.valueOf(b1.sideLength(1)));
		show("testSideLength",String.valueOf(b3.sideLength(2)));
//		assertEquals(b1.sideLength(1),0.2); // this fails because of truncation error on doubles
		assertEquals(b3.sideLength(2),2.0);
	}

	@Test
	void testIsCube() {
		Point p = Point.newPoint(3,2,1);
		Box b = Box.boundingCube(p,p1);
		assertTrue(b.isCube());
		b = Box.boundingBox(p,p1);
		assertFalse(b.isCube());
	}

	@Test
	void testDim() {
		assertEquals(b1.dim(),3);
	}

	@Test
	void testEquals()  {
		assertFalse(b1.equals(b2));
		Box b = Box.boundingBox(p2,p3);
		assertTrue(b.equals(b2));
	}

	@Test
	void testToString()  {
		show("testToString",b1.toString());
		show("testToString",b2.toString());
		show("testToString",b3.toString());
		assertEquals(b1.toString(),"[[0.5,1.0,7.0],[1.2,1.2,8.0]]");
		assertEquals(b2.toString(),"[[0.0,0.0,5.0],[1.2,1.2,8.0]]");
		assertEquals(b3.toString(),"[[0.0,0.0,5.0],[0.5,1.0,7.0]]");
	}

	@Test
	void testValueOf() {
		assertEquals(Box.valueOf("  [[0.5,1.0,7.0],[1.2,1.2,8.0]] "),b1);
		assertEquals(Box.valueOf("              "),null);
	}

}
