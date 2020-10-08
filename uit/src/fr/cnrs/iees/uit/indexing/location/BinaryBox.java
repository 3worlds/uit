package fr.cnrs.iees.uit.indexing.location;

import java.util.Arrays;

/**
 * 
 * @author Jacques Gignoux - 8 oct. 2020
 *
 */
public class BinaryBox {
	
	private int depth = 0;
	private long sideLength = 0;
	private int dim = 0;
	
	private Locator lowerBounds;
	private Locator upperBounds;
	
	public BinaryBox(LocatorFactory fac) {
		super();
		double maxSide = 0.0;
		this.dim = fac.dim();
		for (int i=0; i<dim; i++)
			maxSide = Math.max(maxSide,fac.limits.sideLength(i));
		long maxS = Math.round(maxSide/fac.precision());
		depth = 1;
		while (depth < maxS) 
			depth <<= 1;
		// smallest power of 2 that is larger than maxS
		sideLength = 1<<depth;
		long[] init = new long[dim];
		Arrays.fill(init,0L);
		lowerBounds = fac.newLocator(init);
		upperBounds = Locator.add(lowerBounds,sideLength);
	}
	
//	private BinaryBox(int depth, long sideLength, Locator lowerBounds) {
//		super();
//		this.depth = depth;
//		this.sideLength = sideLength;		
//	}
//	
//    private void recurseMin(int depth, int itbase, double[][] result) {
//    	int itmax = 1<<(dim-depth-1);
//    	double min = region.lowerBound(depth);
//    	for (int i=0; i<itmax; i++)
//    		result[i+itbase][depth] = min;
//    	if (depth<dim-1)
//    		recurseMin(depth+1,itbase,result);
//    	double max = (min + region.upperBound(depth))/2;
//    	for (int i=0; i<itmax; i++)
//    		result[itmax+i+itbase][depth] = max;
//    	if (depth<dim-1)
//    		recurseMin(depth+1,itbase+itmax,result);
//    }
//
//	
//	
//	public BinaryBox[] split() {
//		BinaryBox[] result = new BinaryBox[1<<dim];
//		long bounds;
//		for (int i=0; i<dim; i++) {
//			for (int j=0; j<=1; j++) {
//				bounds = Locator.add(lowerBounds,j*sideLength/2,i);
//			}
//		}
//		new BinaryBox(depth+1,j*sideLength/2,lowerBounds);
//		
//		
//		for (int i=0; i<(1<<dim); i++)
//			result[i] = new BinaryBox(depth,sideLength/2,dim);
//		return result;
//	}

}
