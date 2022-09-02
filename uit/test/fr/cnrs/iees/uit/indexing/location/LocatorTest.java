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
package fr.cnrs.iees.uit.indexing.location;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.uit.space.Box;
import fr.cnrs.iees.uit.space.Point;

class LocatorTest {
	
	LocatorFactory lf; 
	
	@BeforeEach
	void init() {
		lf = new LocatorFactory(0.1,Box.boundingBox(Point.newPoint(0,0,0,0),Point.newPoint(1E6,1E6,1E6,1E7)));
	}

	@Test
	void testCoordinate() {
		Point A = Point.newPoint(12.5,458.657,22.40,1E6);
		Locator lA = lf.newLocator(A);
		assertEquals(lA.coordinate(0),125);
		assertEquals(lA.coordinate(1),4587);
		assertEquals(lA.coordinate(2),224);
		assertEquals(lA.coordinate(3),10000000);
	}

	@Test
	void testToLocator() {
		Point A = Point.newPoint(12.5,458.657,22.40,1E6);
		Locator lA = lf.newLocator(A);
//		System.out.print(lA.toString());
		assertEquals(lA.toString(),"[125,4587,224,10000000]");
	}

	@Test
	void testToPoint() {
		Point A = Point.newPoint(12.5,458.657,22.40,1E6);
		Locator lA = lf.newLocator(A);
		Point B = lA.toPoint();
//		System.out.print(B.toString());
//		assertEquals(B.toString(),"[12.5,458.7,22.4,1000000.0]");???
		assertEquals(B.toString(),"[12.5,458.70000000000005,22.400000000000002,1000000.0]");
	}

	@Test
	void testClone() {
		Point A = Point.newPoint(12.5,458.657,22.40,1E6);
		Locator lA =lf.newLocator(A);
		Locator lB = lA.clone();
		assertEquals(lB.toString(),"[125,4587,224,10000000]");
		assertTrue(lA.equals(lB));
		Point C = Point.newPoint(651311357662.65412357);
		assertThrows(IllegalArgumentException.class,()->lf.newLocator(C));
	}

	@Test
	void testAddLocatorLocator() {
		Point A = Point.newPoint(12.5,458.657,22.40,1E6);
		Locator lA = lf.newLocator(A);
		Point B = Point.newPoint(0.45,254008700360000.0,-12.4,0.1);
		Locator lB = lf.newLocator(B);
		Locator lC = Locator.add(lA,lB);
		assertEquals(lC.toString(),"[130,2540087003604587,100,10000001]");
		assertThrows(IllegalArgumentException.class,()->lf.newLocator(Point.newPoint(0.5)));
	}

	@Test
	void testAddLocatorLong() {
		Point A = Point.newPoint(12.5,458.657,22.40,1E6);
		Locator lA = lf.newLocator(A);
		Locator lB = Locator.add(lA,25);
		assertEquals(lB.toString(),"[150,4612,249,10000025]");
	}

	@Test
	void testAddLocatorLongInt() {
		Point A = Point.newPoint(12.5,458.657,22.40,1E6);
		Locator lA = lf.newLocator(A);
		Locator lB = Locator.add(lA,25,2);
		assertEquals(lB.toString(),"[125,4587,249,10000000]");
		lB = Locator.add(lA,25,18);
		assertEquals(lB.toString(),"[125,4587,224,10000000]");
	}

}
