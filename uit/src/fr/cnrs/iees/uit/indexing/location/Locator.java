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

import fr.cnrs.iees.uit.space.Dimensioned;
import fr.cnrs.iees.uit.space.Point;

/**
 * <p>Locator is the limited-precision equivalent of {@link Point}.</p>
 * 
 * <p>It uses integer (long) coordinates
 * so that identical locations can be tested without ambiguity. Immutable.</p>
 * 
 * <p>This class should not be used outside <strong>uit</strong> and should not be saved to files. 
 * It should always be constructed from Points.</p>
 * 
 * <p>Caution: locators should only be added if they have the same dimension</p>
 * 
 * @author Jacques Gignoux - 8 oct. 2020
 *
 */
// Tested OK on 8/10/2020
public interface Locator extends Dimensioned, Cloneable {
	
	/** 
	 * Accessor to locator coordinates
	 * 
	 * @param i the index of the coordinate
	 * @return i<sup>th</sup> coordinate of the locator
	 */
	public abstract long coordinate(int i);

	/** @return clone of the Locator */
	public abstract Locator clone();

	/** Compute a new Locator by addition of the coordinates of two Locators of same dimension 
	 * 
	 * @param A a locator	
	 * @param B another locator of the same dimension as A
	 * @return the new {@code Locator} instance
	 */
	public static Locator add(Locator A, Locator B)  {
		if (A.factory()!=B.factory())
			throw new IllegalArgumentException("Invalid operation: addition of locators from different origins");
		long[] x = new long[A.dim()];
		for (int i=0; i<x.length; i++)
			x[i] += A.coordinate(i)+B.coordinate(i);
		return A.factory().newLocator(x);
	}
	
	/** 
 	 * Compute a new Locator with a scalar added to every coordinate 
	 * 
	 * @param A a locator
	 * @param scalar a value to add to every coordinate
	 * @return the new {@code Locator} instance
	 */
	public static Locator add(Locator A, long scalar) {
		long[] x = new long[A.dim()];
		for (int i=0; i<x.length; i++)
			x[i] = A.coordinate(i)+scalar;
		return A.factory().newLocator(x);
	}
	
	/** 
	 * Compute a new Locator with a scalar added to coordinate <em>i</em> (translation
	 * along axis <em>i</em>)
	 * 
	 * @param A a locator
	 * @param scalar a value to add to i<sup>th</sup> coordinate
	 * @param i the dimension index of the coordinate to which the scalar is added
	 * @return the new {@code Locator} instance
	 */
	public static Locator add(Locator A, long scalar, int i) {
		long[] x = new long[A.dim()];
		for (int j=0; j<x.length; j++) {
			x[j] = A.coordinate(j);
			if (j==i)
				x[j] += scalar;
		}
		return A.factory().newLocator(x);
	}

	/**
	 * Accessor to the {@code LocatorFactory} used to build this {@code Locator} instance.
	 * @return a factory
	 */
	public LocatorFactory factory();
	
	/**
	 * @return This locator as a {@link Point}.
	 */
	public default Point toPoint() {
		return factory().toPoint(this);
	}
	
}
