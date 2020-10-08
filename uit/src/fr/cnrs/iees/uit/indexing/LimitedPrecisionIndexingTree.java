package fr.cnrs.iees.uit.indexing;

import fr.cnrs.iees.uit.indexing.location.LocatorFactory;
import fr.cnrs.iees.uit.space.Box;
import fr.cnrs.iees.uit.space.Point;
import fr.cnrs.iees.uit.space.Sphere;

/**
 * 
 * @author Jacques Gignoux - 8 oct. 2020
 *
 */
public class LimitedPrecisionIndexingTree<T,N> 
		extends AbstractIndexingTree<T,LimitedPrecisionIndexingNode<T>> {

	protected LocatorFactory factory = null;
	
	protected LimitedPrecisionIndexingTree(Box domain, double precision) {
		super(domain);
		factory = new LocatorFactory(precision,domain);
	}

	@Override
	public void insert(T item, Point at) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T getNearestItem(Point at) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(T item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<T> getItemsWithin(Box limits) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<T> getItemsWithin(Sphere limits) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}


}
