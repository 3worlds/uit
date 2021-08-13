package fr.cnrs.iees.uit.indexing;

import fr.cnrs.iees.uit.space.Box;
import fr.cnrs.iees.uit.space.Point;

/**
 * As a {@link ExpandingRegionIndexingTree}, but using discrete coordinates internally instead
 * of continuous ones.
 * 
 * @author Jacques Gignoux - 13 oct. 2020
 *
 * @param <T>
 */
public class ExpandingLimitedPrecisionIndexingTree<T> extends LimitedPrecisionIndexingTree<T> {

	/**
	 * Constructor using an initial box to start with. This box can be later enlarged to fit
	 * items located outside the initial box. The precision argument is used to scale the
	 * coordinates internally: all coordinate values are multiplied by 1/precision and truncated 
	 * to {@code long}s, so that exact comparison of locations becomes possible. In this system, 
	 * two or more items can have exactly the same location.
	 * 
	 * @param domain the initial domain to start the tree with
	 * @param precision the precision of coordinates - a distance smaller than precision is considered 
	 * equal to zero
	 */
	public ExpandingLimitedPrecisionIndexingTree(Box domain, double precision) {
		super(domain, precision);
	}
	
	@Override
	public void insert(T item, Point at) {
        while (!regionContainsLocator(root.lowerBounds,root.upperBounds,factory.newLocator(at)))
        	root = root.expandRootRegion(at);
        super.insert(item, at);
	}

}
