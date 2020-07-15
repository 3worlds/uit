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

import fr.cnrs.iees.uit.UitException;

class SphereTest {

	private Sphere s1, s2, s3;

	@BeforeEach
	private void init() {
		Point p = Point.newPoint(1,1,1);
		s1 = new SphereImpl(p,1);
		p = Point.newPoint(3,2);
		s2 = new SphereImpl(p,1);
		s3 = new SphereImpl(p,2);
	}

	@Test
	void testCentre() {
		assertEquals(s1.centre(),Point.newPoint(1,1,1));
	}

	@Test
	void testRadius() {
		assertEquals(s2.radius(),1);
	}

	@Test
	void testSize() {
		assertEquals(s2.size(),Math.PI);
		assertEquals(s1.size(),Math.PI*4/3);
	}

	@Test
	void testContainsPoint() {
		assertTrue(s1.contains(Point.newPoint(0.9,0.8,0.8)));
		assertFalse(s1.contains(Point.newPoint(0,0,0)));
		try {
			s1.contains(Point.newPoint(1,1));
			fail("Wrong dimension exception not thrown");
		}
		catch (UitException e) {
			// OK - exception was thrown.
		}
	}

	@Test
	void testContainsBox() {
		Box b = Box.boundingBox(s1);
		assertFalse(s1.contains(b));
		b = new BoxImpl(Point.newPoint(0.5,0.5,0.5),Point.newPoint(1.5,1.5,1.5));
		assertTrue(s1.contains(b));
	}

	@Test
	void testOverlapsSphere() {
		Sphere s = new SphereImpl(Point.newPoint(1,1),1);
		assertFalse(s.overlaps(s2));
		assertTrue(s.overlaps(s3));
		assertTrue(s3.overlaps(s2));
	}

	@Test
	void testOverlapsBox() {
		fail("Not yet implemented");
	}

	@Test
	void testInSphere() {
		Box b = Box.boundingBox(s1);
		Sphere s = Sphere.inSphere(b);
		assertTrue(s.equals(s1));
	}

	@Test
	void testOutSphere() {
		Box b = Box.boundingBox(s2);
		Sphere s = Sphere.outSphere(b);
		assertEquals(s.centre(),s2.centre());
		assertEquals(s.radius(),s2.radius()*Math.sqrt(2));
	}

	@Test
	void testDim() {
		assertEquals(s1.dim(),3);
		assertEquals(s2.dim(),2);
	}

	@Test
	void testEquals()  {
		Sphere s = new SphereImpl(s2.centre().clone(),s2.radius());
		assertTrue(s.equals(s2));
		assertFalse(s.equals(s3));
	}

	@Test
	void testToString()  {
		assertEquals(s1.toString(),"[[1.0,1.0,1.0],1.0]");
		assertEquals(s2.toString(),"[[3.0,2.0],1.0]");
		assertEquals(s3.toString(),"[[3.0,2.0],2.0]");
	}

	@Test
	void testValueOf() {
		assertEquals(Sphere.valueOf(" [[1.0,1.0,1.0],1.0] "),s1);
		assertEquals(Sphere.valueOf("              "),null);
	}


}
