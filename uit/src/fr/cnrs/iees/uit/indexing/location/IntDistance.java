package fr.cnrs.iees.uit.indexing.location;

import fr.cnrs.iees.uit.UitException;

/**
 * Distance computations for Locators, ie running on longs
 * 
 * @author Jacques Gignoux - 8 oct. 2020
 *
 */
public class IntDistance {
	// to prevent any instantiation
	private IntDistance() {}
	
	/** utility: square */
	public static long sqr(long a) {
		return a*a;
	}
	
	/** quick computation of distance in one dimension */
	public static long distance1D(long x1,long x2) {
		return Math.abs(x1-x2);
	}

	/** squared euclidian distance in 1D */
	public static long squaredEuclidianDistance(long x1, long x2) {
		return sqr(distance1D(x1,x2));
	}
	
	/** squared euclidian distance in 2D */
	public static long squaredEuclidianDistance(long x1, long y1, long x2, long y2) {
		return sqr(x1-x2)+sqr(y1-y2);
	}

	/** squared euclidian distance in 3D */
	public static long squaredEuclidianDistance(long x1, long y1, long z1, long x2, long y2, long z2) {
		return sqr(x1-x2)+sqr(y1-y2)+sqr(z1-z2);
	}

	/** squared euclidian distance in nD using Locator */
	public static long squaredEuclidianDistance(Locator p1, Locator p2) {
		if (p1.dim()!=p2.dim())
			throw new UitException("squaredEuclidianDistance: Arguments of different dimensions");
		long dist = 0L;
		for (int i=0; i<p1.dim(); i++)
			dist += sqr(p1.coordinate(i)-p2.coordinate(i));
		return dist;
	}

	/** euclidian distance in 1D */
	public static long euclidianDistance(long x1, long x2) {
		return distance1D(x1,x2);
	}
	
	/** euclidian distance in 2D */
	public static long euclidianDistance(long x1, long y1, long x2, long y2) {
		return Math.round(Math.hypot(x2-x1,y2-y1)); // no inner overflow error
	}

	/** euclidian distance in 3D */
	public static long euclidianDistance(long x1, long y1, long z1, long x2, long y2, long z2) {
		return Math.round(Math.sqrt(squaredEuclidianDistance(x1,y1,z1,x2,y2,z2)));
	}

	/** euclidian distance in nD using Locator */
	public static long euclidianDistance(Locator p1, Locator p2) {
		return Math.round(Math.sqrt(squaredEuclidianDistance(p1,p2)));
	}

//	// helper for distanceToClosestEdge(Locator,Box) - recursive
//	private static long distanceToClosestEdge(boolean in, int dim, long dist, Locator p, Box b) {
//		if (in) { // coming from in
//			if ((p.coordinate(dim)<b.lowerBound(dim))||(p.coordinate(dim)>b.upperBound(dim))) {
//				// now out
//				long d = Math.min(Distance.distance1D(p.coordinate(dim),b.lowerBound(dim)), 
//						Distance.distance1D(p.coordinate(dim),b.upperBound(dim)));
//				dist = d; 
//				in = false;
//			}
//			else { 
//				// now in
//				long d = Math.min(Distance.distance1D(p.coordinate(dim),b.lowerBound(dim)), 
//						Distance.distance1D(p.coordinate(dim),b.upperBound(dim)));
//				dist = Math.min(d, dist);
//				in = true;
//			}
//		}
//		else { // coming from out
//			if ((p.coordinate(dim)<b.lowerBound(dim))||(p.coordinate(dim)>b.upperBound(dim))) {
//				// now out
//				long d = Math.min(Distance.distance1D(p.coordinate(dim),b.lowerBound(dim)), 
//						Distance.distance1D(p.coordinate(dim),b.upperBound(dim)));
//				dist = Math.sqrt(dist*dist+d*d);
//				in = false;
//			}
//			else { 
//				// now in: dist = previous dist, no change
//				in = true;
//			}
//		}
//		if (dim==p.dim()-1)
//			return dist;
//		else 
//			return distanceToClosestEdge(in,dim+1,dist,p,b);
//	}
//	
//	/**
//	 * <p>Computes the distance of a {@link Locator} to the closest edge of a {@link Box}.</p>
//	 * <p>This is not as trivial as it seems. Each edge is a finite segment in its dimension, so that if
//	 * the distance is computed as a perpendicular distance (the intuitive way) only if this
//	 * perpendicular line can be drawn between that Locator and the segment that represents the 
//	 * box edge. In other words,</p>
//	 * <ul> 
//	 * <li>If the Locator is inside the box, then it returns the
//	 * <strong>perpendicular distance</strong> to the closest edge.</li>
//	 * <li>If the Locator is outside the box but a perpendicular distance can be drawn to
//	 * one or more edges (meaning that in some dimensions the Locator falls within the box):
//	 *   <ul>
//	 *   <li>"Inner" distances are ignored.</li>
//	 *   <li>The returned distance is the <strong>square root of the sum of the squared perpendicular 
//	 * "outer" distances</strong>.</li>
//	 *   </ul>
//	 * </li> 
//	 * <li>
//	 * If the Locator is outside the box so that all its components in all dimensions are
//	 * also outside the box, the returned distance is <strong>square root of the sum of the squared perpendicular 
//	 * distances to all edges.</strong>.
//	 * </li>
//	 * </ul>
//	 * @param p the Locator
//	 * @param b the box
//	 * @return
//	 */
//	public static long distanceToClosestEdge(Locator p, Box b) {
//		if (p.dim()!=b.dim())
//			throw new UitException("distanceToClosestEdge: Arguments of different dimensions");
//		long dist = Math.min(Distance.distance1D(p.coordinate(0),b.lowerBound(0)), 
//				Distance.distance1D(p.coordinate(0),b.upperBound(0)));
//		if ((p.coordinate(0)<b.lowerBound(0))||(p.coordinate(0)>b.upperBound(0)))
//			return distanceToClosestEdge(false,1,dist,p,b);
//		else
//			return distanceToClosestEdge(true,1,dist,p,b);
//	}
	

}
