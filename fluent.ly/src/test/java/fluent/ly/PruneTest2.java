package fluent.ly;

import static fluent.ly.azzert.is;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.Before;
import org.junit.Test;

/**
 * A JUnit test class for the enclosing class.
 *
 * @author Yossi Gil, the Technion.
 * @since 27/08/2008
 */
@SuppressWarnings({ "static-method" }) public class PruneTest2 {
  final String[] alternatingArray = new String[] { null, "A", null, null, "B", null, null, null, "C", null };
  final String[] NonNullArray = { "1", "2", "4" };
  private ArrayList<@Nullable String> sparseCollection;

  @Before public void initSparseCollection() {
    sparseCollection = new ArrayList<>();
    sparseCollection.add(null);
    sparseCollection.add(null);
    sparseCollection.add(null);
    sparseCollection.add(null);
    sparseCollection.add(null);
    sparseCollection.add("A");
    sparseCollection.add(null);
    sparseCollection.add(null);
    sparseCollection.add(null);
    sparseCollection.add("B");
    sparseCollection.add(null);
    sparseCollection.add("C");
    sparseCollection.add(null);
    sparseCollection.add(null);
    sparseCollection.add(null);
    sparseCollection.add(null);
  }

  @Test public void testNonNullArrayItems() {
    azzert.that(prune.nulls(NonNullArray)[0], is("1"));
    azzert.that(prune.nulls(NonNullArray)[1], is("2"));
    azzert.that(prune.nulls(NonNullArray)[2], is("4"));
  }

  @Test public void testNonNullArrayLength() {
    azzert.that(prune.nulls(NonNullArray).length, is(NonNullArray.length));
  }

  @Test public void testPruneArrayAltenatingItems() {
    azzert.that(prune.nulls(alternatingArray)[0], is("A"));
    azzert.that(prune.nulls(alternatingArray)[1], is("B"));
    azzert.that(prune.nulls(alternatingArray)[2], is("C"));
  }

  @Test public void testPruneArrayAltenatingLength() {
    azzert.that(prune.nulls(alternatingArray).length, is(3));
  }

  @Test public void testPruneSparseCollectionContents() {
    final var a = prune.nils(sparseCollection).toArray(new String[3]);
    azzert.that(a[0], is("A"));
    azzert.that(a[1], is("B"));
    azzert.that(a[2], is("C"));
    azzert.that(a.length, is(3));
  }

  @Test public void testPruneSparseCollectionLength() {
    azzert.that(prune.nils(sparseCollection).size(), is(3));
  }

  @Test public void testPrunNonNull() {
    assert prune.nils(sparseCollection) != null;
  }

  @Test public void testShrink() {
    azzert.that(prune.shrink(new Object[10]).length, is(0));
  }
}