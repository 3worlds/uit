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

class PointTest {
	
	Point p1;
	Point p2;
	Point p3;
	Point p4;

	@Test
	void testCoordinate() {
		assertEquals(p1.coordinate(0),1);
		assertEquals(p4.coordinate(4),957.0);
		assertThrows(Exception.class,()->p3.coordinate(5));
	}

	@Test
	void testX() {
		assertEquals(p1.x(),1.0);
		assertEquals(p2.x(),2.0);
		assertEquals(p3.x(),3.5);
		assertEquals(p4.x(),4500.0);
	}

	@Test
	void testY() {
		assertEquals(p1.y(),Double.NaN);
		assertEquals(p2.y(),18.5);
		assertEquals(p3.y(),45.0);
		assertEquals(p4.y(),5200.0);
	}

	@Test
	void testZ() {
		assertEquals(p1.z(),Double.NaN);
		assertEquals(p2.z(),Double.NaN);
		assertEquals(p3.z(),68.7);
		assertEquals(p4.z(),8754.0);
	}

	@BeforeEach
	@Test
	void testNewPoint() {
		p1 = Point.newPoint(1.0);
		p2 = Point.newPoint(2.0,18.5);
		p3 = Point.newPoint(3.5,45,68.7);
		p4 = Point.newPoint(4500,5200,8754,846,957);
		assertEquals(p1.getClass(),Point1D.class);
		assertEquals(p2.getClass(),Point2D.class);
		assertEquals(p3.getClass(),Point3D.class);
		assertEquals(p4.getClass(),PointND.class);
	}

	@Test
	void testClone() {
		Point p1b = p1.clone();
		assertEquals(p1b.x(),p1.x());
		assertNotSame(p1b,p1);
	}

	@Test
	void testAddPointPoint() {
		assertThrows(UitException.class,()->Point.add(p1,p2) );
		Point p = Point.add(p3, Point.newPoint(1,1,1));
		String s = String.valueOf(p.x())+','+p.y()+','+p.z();
		assertEquals(s,"4.5,46.0,69.7");
	}

	@Test
	void testAddPointDouble() {
		Point p = Point.add(p3,1.0);
		String s = String.valueOf(p.x())+','+p.y()+','+p.z();
		assertEquals(s,"4.5,46.0,69.7");
	}

	@Test
	void testDim() {
		assertEquals(p1.dim(),1);
		assertEquals(p2.dim(),2);
		assertEquals(p3.dim(),3);
		assertEquals(p4.dim(),5);
	}
	
	@Test
	void testEquals() {
		assertTrue(p1.equals(p1));
		assertTrue(p1.equals(Point.newPoint(1.0)));
		assertFalse(p1.equals(p2));
		assertTrue(p2.equals(p2));
		assertTrue(p2.equals(Point.newPoint(2.0,18.5)));
		assertFalse(p2.equals(p1));
		assertTrue(p3.equals(p3));
		assertTrue(p3.equals(Point.newPoint(3.5,45,68.7)));
		assertFalse(p3.equals(p2));
		assertTrue(p4.equals(p4));
		assertTrue(p4.equals(Point.newPoint(4500,5200,8754,846,957)));
		assertFalse(p4.equals(p2));
	}

	@Test
	void testToString()  {
		assertEquals(p1.toString(),"[1.0]");
		assertEquals(p2.toString(),"[2.0,18.5]");
		assertEquals(p3.toString(),"[3.5,45.0,68.7]");
		assertEquals(p4.toString(),"[4500.0,5200.0,8754.0,846.0,957.0]");
	}

}
