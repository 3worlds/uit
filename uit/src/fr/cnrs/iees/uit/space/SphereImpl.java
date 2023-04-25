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

import java.util.Objects;

import org.apache.commons.math3.util.FastMath;

/**
 * A straightforward implementation of a {@linkplain Sphere}.
 * @author Jacques Gignoux - 06-09-2018 
 *
 */
//Tested OK on version 0.0.1 on 21/11/2018
class SphereImpl implements Sphere {
	
	private Point centre = null;
	private double radius = 0.0;
	// hash code stored for faster tests
	private int hash = 0;
	
	private static final double PI2 = Math.PI*Math.PI;
	private static final double PI3 = PI2*Math.PI;
	private static final double PI4 = PI3*Math.PI;

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

	/**
	 * <p>Computes the volume of a (hyper)sphere up to dimension 8.</p>
	 * <p>The general formula exists for n>8 but it unimplemented.</p>
	 * <p>Values for volumes and surfaces as given by 
	 * <a href="https://en.wikipedia.org/wiki/N-sphere">wikipedia</a>:</p>
	 * 
		<table border="1" style="text-align: center">
			<tr>
				<th>dimension</th>
				<th>volume</th>
				<th>surface</th>
			</tr>
			<tr>
				<td>1</td>
				<td>2r</td>
				<td>2</td>
			</tr>
			<tr>
				<td>2</td>
				<td>πr<sup>2</sup></td>
				<td>2πr</td>
			</tr>
			<tr>
				<td>3</td>
				<td>4πr<sup>3</sup>/3</td>
				<td>4πr<sup>2</sup></td>
			</tr>
			<tr>
				<td>4</td>
				<td>π<sup>2</sup>r<sup>4</sup>/2</td>
				<td>2π<sup>2</sup>r<sup>3</sup></td>
			</tr>
			<tr>
				<td>5</td>
				<td>8π<sup>2</sup>r<sup>5</sup>/15</td> 
				<td>8π<sup>2</sup>r<sup>4</sup>/3</td>
			</tr>
			<tr>
				<td>6</td>
				<td>π<sup>3</sup>r<sup>6</sup>/6</td>
				<td>π<sup>3</sup>r<sup>5</sup></td>
			</tr>
			<tr>
				<td>7</td>
				<td>16π<sup>3</sup>r<sup>7</sup>/105</td>
				<td>16π<sup>3</sup>r<sup>6</sup>/15</td>
			</tr>
			<tr>
				<td>8</td>
				<td>π<sup>4</sup>r<sup>8</sup>/24</td>
				<td>π<sup>4</sup>r<sup>7</sup>/3</td>
			</tr>
			<tr>
				<td>n = 2p+1</td>
				<td>r<sup>2p+1</sup>2<sup>p+1</sup>π<sup>p</sup>/1.3.5....(2p+1)</td>
				<td></td>
			</tr>
			<tr>
				<td>n = 2p</td>
				<td>r<sup>2p</sup>π<sup>p</sup>/p!</td>
				<td></td>
			</tr>
		</table>
	 *	<br/>
	 */
	@Override
	public double size() {
		if (dim()==2)
			return Math.PI*FastMath.pow(radius,2);
		if (dim()==3)
			return Math.PI*4.0*FastMath.pow(radius,3)/3.0;
		if (dim()==1)
			return 2*radius;
		if (dim()==4)
			return PI2*FastMath.pow(radius,4)/2.0;
		if (dim()==5)
			return PI2*8.0*FastMath.pow(radius,5)/15.0;
		if (dim()==6)
			return PI3*FastMath.pow(radius,6)/6.0;
		if (dim()==7)
			return PI3*16.0*FastMath.pow(radius,7)/105.0;
		if (dim()==8)
			return PI4*FastMath.pow(radius,8)/24.0;
		throw new UnsupportedOperationException("sphere volume not implemented for dim>8");
	}

	@Override
	public String toString() {
		return "["+centre.toString()+","+radius+"]";
	}

	public static Sphere valueOf(String s) {
		return Sphere.valueOf(s);
	}

	@Override
	public int hashCode() {
		if (hash==0)
			hash = Objects.hash(centre, radius);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Sphere))
			return false;
		Sphere other = (Sphere) obj;
		return Objects.equals(centre, other.centre())
				&& Double.doubleToLongBits(radius) == Double.doubleToLongBits(other.radius());
	}
	
}
