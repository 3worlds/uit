package fr.cnrs.iees.uit.indexing;

import fr.cnrs.iees.uit.space.Box;
import fr.cnrs.iees.uit.space.BoxImpl;
import fr.cnrs.iees.uit.space.Point;

/**
 * A helper class to test recursion on 2^dim subnodes for generalized quadtrees. Veeery tricky.
 * @author Jacques Gignoux - 09-08-2018 
 *
 */
public class QuadTest {
    public QuadTest parent = null;
    public QuadTest[] children = null; 
    public Box region;
    private int dim=0;

    public QuadTest(QuadTest parent, Box region) {
    	super();
    	dim=region.dim();
        this.parent = parent;
        this.region = region; 
    	double[][] mins = new double[1<<dim][dim];
    	recurse(0,0,mins);
    	for (int i=0; i<(1<<dim); i++) {
    		for (int d=0; d<dim; d++)
    			System.out.print(mins[i][d]+", ");
    		System.out.println();
    	}
    }

    private void recurse(int depth, int itbase, double[][] result) {
    	int itmax = 1<<(dim-depth-1);
    	double min = region.lowerBound(depth);
    	for (int i=0; i<itmax; i++)
    		result[i+itbase][depth] = min;
    	if (depth<dim-1)
    		recurse(depth+1,itbase,result);
    	double max = (min + region.upperBound(depth))/2;
    	for (int i=0; i<itmax; i++)
    		result[itmax+i+itbase][depth] = max;
    	if (depth<dim-1)
    		recurse(depth+1,itbase+itmax,result);
    }
    
    public void index(Point p) {	
		int[] ix = new int[dim]; 
		if (children!=null) {
			for (int i=0; i<dim; i++) {
				double prop = (p.coordinate(i)-region.lowerBound(i))
						/(region.upperBound(i)-region.lowerBound(i));
				int n = (int) Math.round(2*prop);
				if (n==0)
					ix[i]=0;
				else
					ix[i] = n-1;
			}
			int index=0;
			for (int i=0; i<dim; i++)
				index += ix[i]*(1<<(dim-i-1));
			System.out.println("index = "+index);
		}
    }
    
    
    // veery tricky to setup - cf QuadTest for checking this code
    private void recurseMin(int depth, int itbase, double[][] result) {
    	int itmax = 1<<(dim-depth-1);
    	double min = region.lowerBound(depth);
    	for (int i=0; i<itmax; i++)
    		result[i+itbase][depth] = min;
    	if (depth<dim-1)
    		recurseMin(depth+1,itbase,result);
    	double max = (min + region.upperBound(depth))/2;
    	for (int i=0; i<itmax; i++)
    		result[itmax+i+itbase][depth] = max;
    	if (depth<dim-1)
    		recurseMin(depth+1,itbase+itmax,result);
    }
    private void recurseMax(int depth, int itbase, double[][] result) {
    	int itmax = 1<<(dim-depth-1);
    	double min = (region.lowerBound(depth)+region.upperBound(depth))/2;
    	for (int i=0; i<itmax; i++)
    		result[i+itbase][depth] = min;
    	if (depth<dim-1)
    		recurseMax(depth+1,itbase,result);
    	double max = region.upperBound(depth);
    	for (int i=0; i<itmax; i++)
    		result[itmax+i+itbase][depth] = max;
    	if (depth<dim-1)
    		recurseMax(depth+1,itbase+itmax,result);
    }
    
    public void initQuad() {
    	double[][] mins = new double[1<<dim][dim];
    	recurseMin(0,0,mins);
    	double[][] maxs = new double[1<<dim][dim];
    	recurseMax(0,0,maxs);
    	children = new QuadTest[1<<dim];
    	for (int i=0; i<(1<<dim); i++) {
    		Point lower = Point.newPoint(mins[i]);
    		Point upper = Point.newPoint(maxs[i]);
    		Box reg = new BoxImpl(lower, upper); 
    		children[i] = new QuadTest(this,reg);
    	}
    }

	
	// TESTING
	public static void main(String[] args) {
		System.out.println("3D tree (octree)");
		Point A = Point.newPoint(1,4,7);
		Point B = Point.newPoint(3,6,9);
		QuadTest q = new QuadTest(null,new BoxImpl(A,B));
		q.initQuad();
		for (int i=0; i<1<<q.dim; i++)
			System.out.println(q.children[i].region.toString());
		Point p = Point.newPoint(1.5,4.5,7.5);
		q.index(p);
		q.index(Point.newPoint(1.5,4.5,8.5));
		q.index(Point.newPoint(1.5,5.5,7.5));
		q.index(Point.newPoint(1.5,5.5,8.5));
		q.index(Point.newPoint(2.5,4.5,7.5));
		q.index(Point.newPoint(2.5,4.5,8.5));
		q.index(Point.newPoint(2.5,5.5,7.5));
		q.index(Point.newPoint(2.5,5.5,8.5));
		q.index(Point.newPoint(2,4,8.0));
		q.index(Point.newPoint(3,6,9));
		q.index(Point.newPoint(2,5,8));
		
//		System.out.println("==========================");
//		System.out.println("2D tree (quadtree)");
//		Point C = new PointImpl(1,4);
//		Point D = new PointImpl(3,6);
//		QuadTest r = new QuadTest(null,new BoxImpl(C,D));
//		System.out.println("1D tree (binary tree)");
//		Point E = new PointImpl(1);
//		Point F = new PointImpl(3);
//		QuadTest s = new QuadTest(null,new BoxImpl(E,F));
//		System.out.println("5D tree ");
//		Point G = new PointImpl(1,4,7,10,13);
//		Point H = new PointImpl(3,6,9,12,15);
//		QuadTest t = new QuadTest(null,new BoxImpl(G,H));
	}


}
