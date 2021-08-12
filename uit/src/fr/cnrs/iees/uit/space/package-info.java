/**
 * <p>Basic geometric classes internally used by {@link fr.cnrs.iees.uit.IndexingTree IndexingTree}.</p>
 * 
 * <p>The main concept behind this once-again implementation of geometric entities is that
 * we do not care about space dimension. All the classes here represent the same concept in a space
 * of any dimension greater or equal to 1. Dimension is set at instantiation of these classes.
 * This allows to implement QuadTree, OctTrees, <em>k</em>-d trees, with the same class no matter the
 * value of dimension <em>k</em>. Ah, another idea is to keep all this as lightweight as possible -- 
 * there are plenty of geometric libraries all over the internet for more generic uses.</p>
 * 
 * <p>{@link Point} is a point of any dimension. The dimension is set when instantiating it: the number
 * of coordinates passed to {@code Point.newPoint(...)} will determine the dimension. {@link Box} is a
 * n-dimensional rectangular box defined by its lower and upper corners. {@link Sphere} is 
 * a n-dimensional sphere defined by its centre (a n-dim {@code Point}) and radius. Instantiation
 * is made through static factory methods in each class. All instances are <em>immutable</em>, i.e. cannot
 * be modified once constructed. The {@link Dimensioned} superclass handles the dimension information.</p>
 * 
 * <img src="{@docRoot}/../doc/images/uit-space.svg" align="middle" width="800" 
 * alt="the space classes"/>
 * 
 * <p>All these interfaces can be used without overriding. The static factory methods they provide enable
 * you to completely ignore the implementation details, i.e. you should only manipulate the
 * interfaces, not the underlying classes.</p>
 * 
 *  @author Jacques Gignoux - 12 août 2021
 */
package fr.cnrs.iees.uit.space;