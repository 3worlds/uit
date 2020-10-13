package fr.cnrs.iees.uit.indexing;

import fr.cnrs.iees.uit.space.Box;
import fr.cnrs.iees.uit.space.Point;

/**
 * 
 * @author Jacques Gignoux - 13 oct. 2020
 *
 * @param <T>
 */
public class ExpandingLimitedPrecisionIndexingTree<T> extends LimitedPrecisionIndexingTree<T> {

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
