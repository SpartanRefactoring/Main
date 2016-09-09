// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spatan.iteration;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.streotypes.*;

/** import il.org.spartan.stereotypes.Utility; /** A <b>Utility class</b>
 * providing functions to remove <code><b>null</b></code> elements from arrays
 * and iterable collections. For example, to process the
 * non-<code><b>null</b></code> elements of an array:
 *
 * <pre>
 * void f(String ss[]) {
 *     for (String s: Prune.nulls(ss))
 *     		// ... s is not null.
 * }
 * </pre>
 *
 * @author Yossi Gil, the Technion.
 * @since 27/08/2008 */
@Utility public enum Prune {
  ;
  public static <T, C extends Collection<T>> C nulls(final C ts) {
    for (final Iterator<T> i = ts.iterator(); i.hasNext();)
      if (i.next() == null)
        i.remove();
    return ts;
  }

  /** Prune <code><b>null</b></code> elements from a given collection.
   * @param <T> type of elements in the collection.
   * @param ts a collection of values.
   * @return a new collection, containing only those non-
   *         <code><b>null</b></code> elements of the parameter, and in the same
   *         order. No <code><b>null</b></code> elements are present on this
   *         returned collection. */
  public static <T> List<T> nulls(final Iterable<T> ts) {
    final ArrayList<T> $ = new ArrayList<>();
    for (final T t : ts)
      if (t != null)
        $.add(t);
    return $;
  }

  /** Prune <code><b>null</b></code> elements from a given array.
   * @param <T> type of elements in the array.
   * @param ts an array of values.
   * @return a new array, containing precisely those non-
   *         <code><b>null</b></code> elements of the parameter, and in the same
   *         order. No <code><b>null</b></code> elements are present on this
   *         returned collection. */
  public static <T> T[] nulls(final T[] ts) {
    final List<T> $ = new ArrayList<>();
    for (final T t : ts)
      if (t != null)
        $.add(t);
    return $.toArray(shrink(ts));
  }

  public static <T> String[] whites(final T... ts) {
    final List<String> $ = new ArrayList<>();
    for (final T t : ts)
      if (t != null)
        addNonEmpty($, t.toString().trim());
    return $.toArray(new String[0]);
  }

  private static void addNonEmpty(final Collection<String> ss, final String s) {
    if (s.length() > 0)
      ss.add(s);
  }

  /** Shrink an array size to zero.
   * @param <T> type of elements in the input array.
   * @param ts an array of values.
   * @return an array of size 0 of elements of type <code>T</code>. */
  private static <T> T[] shrink(final T[] ts) {
    return Arrays.copyOf(ts, 0);
  }

  /** A JUnit test class for the enclosing class.
   * @author Yossi Gil, the Technion.
   * @since 27/08/2008 */
  @SuppressWarnings({ "static-method", "synthetic-access" }) public static class TEST {
    final String[] alternatingArray = new String[] { null, "A", null, null, "B", null, null, null, "C", null };
    final String[] nonNullArray = { "1", "2", "4" };
    private ArrayList<String> sparseCollection;

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
      assertEquals("1", nulls(nonNullArray)[0]);
      assertEquals("2", nulls(nonNullArray)[1]);
      assertEquals("4", nulls(nonNullArray)[2]);
    }

    @Test public void testNonNullArrayLength() {
      assertEquals(nonNullArray.length, nulls(nonNullArray).length);
    }

    @Test public void testPruneArrayAltenatingItems() {
      assertEquals("A", nulls(alternatingArray)[0]);
      assertEquals("B", nulls(alternatingArray)[1]);
      assertEquals("C", nulls(alternatingArray)[2]);
    }

    @Test public void testPruneArrayAltenatingLength() {
      assertEquals(3, nulls(alternatingArray).length);
    }

    @Test public void testPruneSparseCollectionContents() {
      final String[] a = nulls(sparseCollection).toArray(new String[3]);
      assertEquals("A", a[0]);
      assertEquals("B", a[1]);
      assertEquals("C", a[2]);
      assertEquals(3, a.length);
    }

    @Test public void testPruneSparseCollectionLength() {
      assertEquals(3, nulls(sparseCollection).size());
    }

    @Test public void testPrunNotNull() {
      assertNotNull(nulls(sparseCollection));
    }

    @Test public void testShrink() {
      assertEquals(0, shrink(new Object[10]).length);
    }
  }
}
