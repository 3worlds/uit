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

import fr.cnrs.iees.uit.UitException;

/**
 * A straightforward implementation of a {@linkplain Sphere}.
 * @author Jacques Gignoux - 06-09-2018 
 *
 */
//Tested OK on version 0.0.1 on 21/11/2018
public class SphereImpl implements Sphere {
	
	private Point centre = null;
	private double radius = 0.0;

	public SphereImpl(Point centre, double radius) {
		super();
		this.centre = centre;
		this.radius = radius;
	}
	
	@Override
	public int dim() {
		return centre.dim();
	}

	@Override
	public Point centre() {
		return centre;
	}

	@Override
	public double radius() {
		return radius;
	}

	@Override
	public double size() {
		if (dim()==2)
			return Math.PI*radius*radius;
		if (dim()==3)
			return Math.PI*4*radius*radius*radius/3;
		if (dim()==1)
			return 2*radius;
		//veeery complicated in other cases ! cf wikpedia: https://en.wikipedia.org/wiki/Volume_of_an_n-ball
		// TODO
		throw new UitException("sphere volume not implemented for dim>3");
	}

	@Override
	public boolean equals(Object obj) {
		if (Sphere.class.isAssignableFrom(obj.getClass())) {
			Sphere s = (Sphere) obj;
			if (s.dim()!=dim())
				return false;
			return (s.centre().equals(centre) && (s.radius()==radius));
		}
		return false;
	}


	@Override
	public String toString() {
		return "["+centre.toString()+","+radius+"]";
	}

	public static Sphere valueOf(String s) {
		return Sphere.valueOf(s);
	}
	
}
