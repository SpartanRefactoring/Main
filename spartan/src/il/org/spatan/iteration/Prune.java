// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spatan.iteration;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;
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
  @NotNull public static <T, C extends Collection<T>> C nulls(@NotNull final C ts) {
    for (@NotNull final Iterator<T> ¢ = ts.iterator(); ¢.hasNext();)
      if (¢.next() == null)
        ¢.remove();
    return ts;
  }

  /** Prune <code><b>null</b></code> elements from a given collection.
   * @param <T> type of elements in the collection.
   * @param ts a collection of values.
   * @return a new collection, containing only those non-
   *         <code><b>null</b></code> elements of the parameter, and in the same
   *         order. No <code><b>null</b></code> elements are present on this
   *         returned collection. */
  @NotNull public static <T> List<T> nulls(@NotNull final Iterable<T> ts) {
    @NotNull final ArrayList<T> $ = new ArrayList<>();
    for (@Nullable final T ¢ : ts)
      if (¢ != null)
        $.add(¢);
    return $;
  }

  /** Prune <code><b>null</b></code> elements from a given array.
   * @param <T> type of elements in the array.
   * @param ts an array of values.
   * @return a new array, containing precisely those non-
   *         <code><b>null</b></code> elements of the parameter, and in the same
   *         order. No <code><b>null</b></code> elements are present on this
   *         returned collection. */
  public static <T> T[] nulls(@NotNull final T[] ts) {
    @NotNull final List<T> $ = new ArrayList<>();
    for (@Nullable final T ¢ : ts)
      if (¢ != null)
        $.add(¢);
    return $.toArray(shrink(ts));
  }

  public static <T> String[] whites(@NotNull final T... ts) {
    @NotNull final List<String> $ = new ArrayList<>();
    for (@Nullable final T ¢ : ts)
      if (¢ != null)
        addNonEmpty($, (¢ + "").trim());
    return $.toArray(new String[0]);
  }

  private static void addNonEmpty(@NotNull final Collection<String> ss, @NotNull final String s) {
    if (s.length() > 0)
      ss.add(s);
  }

  /** Shrink an array size to zero.
   * @param <T> type of elements in the input array.
   * @param ¢ an array of values.
   * @return an array of size 0 of elements of type <code>T</code>. */
  private static <T> T[] shrink(@NotNull final T[] ¢) {
    return Arrays.copyOf(¢, 0);
  }

  /** A JUnit test class for the enclosing class.
   * @author Yossi Gil, the Technion.
   * @since 27/08/2008 */
  @SuppressWarnings({ "static-method", "synthetic-access" }) public static class TEST {
    @Nullable final String[] alternatingArray = new String[] { null, "A", null, null, "B", null, null, null, "C", null };
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
      azzert.that(nulls(nonNullArray)[0], is("1"));
      azzert.that(nulls(nonNullArray)[1], is("2"));
      azzert.that(nulls(nonNullArray)[2], is("4"));
    }

    @Test public void testNonNullArrayLength() {
      azzert.that(nulls(nonNullArray).length, is(nonNullArray.length));
    }

    @Test public void testPruneArrayAltenatingItems() {
      azzert.that(nulls(alternatingArray)[0], is("A"));
      azzert.that(nulls(alternatingArray)[1], is("B"));
      azzert.that(nulls(alternatingArray)[2], is("C"));
    }

    @Test public void testPruneArrayAltenatingLength() {
      azzert.that(nulls(alternatingArray).length, is(3));
    }

    @Test public void testPruneSparseCollectionContents() {
      @NotNull final String[] a = nulls(sparseCollection).toArray(new String[3]);
      azzert.that(a[0], is("A"));
      azzert.that(a[1], is("B"));
      azzert.that(a[2], is("C"));
      azzert.that(a.length, is(3));
    }

    @Test public void testPruneSparseCollectionLength() {
      azzert.that(nulls(sparseCollection).size(), is(3));
    }

    @Test public void testPrunNotNull() {
      assert nulls(sparseCollection) != null;
    }

    @Test public void testShrink() {
      azzert.that(shrink(new Object[10]).length, is(0));
    }
  }
}
