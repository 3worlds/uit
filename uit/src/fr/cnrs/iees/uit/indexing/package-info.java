/**
 * <p>The {@link fr.cnrs.iees.uit.indexing.IndexingTree IndexingTree} interface (representing 
 * a generic <em>k</em>-d tree) and different kinds of implementation.</p>
 * 
 * <p><em>k</em>-d trees partition space as a binary tree, so that searching for nearby points
 * based on their locations reduces to refine the region of space in which they lie (= going deeper
 * into the tree) until only one
 * point is found (<a href="https://en.wikipedia.org/wiki/Binary_space_partitioning">binary space partitioning</a>).
 * Partitioning can be based on <em>points</em> or on <em>regions</em>. <strong>We have only fully
 * implemented <em>region-based</em> trees here.</strong></p>
 * 
 * <p>The {@link IndexingTree} interface groups methods that are expected for any indexing tree:
 * finding nearest neighbours, finding items in a small region. Any kind of object can be stored
 * in an {@code IndexingTree} ({@code T} parameter). Implementations of {@code IndexingTree}
 * differ in important details:</p>
 * 
 * <ul>
 * <li>Descendants of {@link RegionIndexingTree} all use double precision coordinates. This implicitly
 * assumes that no two items can be found at the same location.</li>
 * <li>There is a significant improvement in search performance when the region where items lie
 * is known <em>a priori</em>. We designed the {@link BoundedRegionIndexingTree} class for this purpose.</li>
 * <li>If the search region is unknown, it can be build on-the-fly from item locations using
 * {@link ExpandingRegionIndexingTree}, but there is a performance cost induced by a possibly strongly
 * unbalanced tree in this case.</li>
 * <li>The two {@code LimitedPrecision}-prefixed classes ({@link LimitedPrecisionIndexingTree} and
 * {@link ExpandingLimitedPrecisionIndexingTree}) are duplicates of 
 * {@code BoundedRegionIndexingTree} and {@code ExpandingRegionIndexingTree} using <em>discrete</em> 
 * (i.e., {@code long}s) instead of continuous (i.e., {@code double}s) coordinates. This implicitly assumes
 * that two items can be found at the same location.</li>
 * </ul>
 * 
 * <p>The {@link IndexingNode} hierarchy represent the nodes of the various indexing tree 
 * implementations. {@code IndexingNode} and its subclasses are only internally used by 
 * {@code IndexingTree} implementations, so their visibility is limited to this package.</p>
 * 
 * <img src="{@docRoot}/../doc/images/uit-indexing.svg" align="middle" width="1200" 
 * alt="the indexing tree hierarchy"/>
 * 
 * <p>We welcome any contribution to improve these implementations and to propose new ones 
 * (eg for point-based indexing trees)</p>
 * 
 *  @author Jacques Gignoux - 12 août 2021
 */
package fr.cnrs.iees.uit.indexing;