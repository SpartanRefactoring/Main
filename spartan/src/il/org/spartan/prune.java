/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.junit.*;

/** A <b>Utility class</b> providing functions to remove
 * <code><b>null</b></code> elements from arrays and iterable collections. For
 * example, to process the non-<code><b>null</b></code> elements of an array:
 *
 * <pre>
 * void f(String ss[]) {
 *     for (String s: Prune.nulls(ss))
 *     		// ... s is not null.
 * }
 * </pre>
 *
 * @author Yossi Gil
 * @since 27/08/2008 */
public enum prune {
  ;
  /** @param <T> JD
   * @param <C> JD
   * @param ts JD
   * @return TODO document return type */
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
    return cantBeNull($.toArray(shrink(ts)));
  }

  /** @param ss JD
   * @return TODO document return type */
  @SafeVarargs public static String[] whites(final String... ss) {
    final List<String> $ = new ArrayList<>();
    for (final String s : ss)
      if (s != null)
        accumulate.to($).add(s.trim());
    return asArrray($);
  }

  /** @param $
   * @return TODO document return type */
  private static String[] asArrray(final List<String> $) {
    return cantBeNull($.toArray(new String @NonNull [0]));
  }

  /** Shrink an array size to zero.
   * @param <T> type of elements in the input array.
   * @param ts an array of values.
   * @return an array of size 0 of elements of type <code>T</code>. */
  private static <T> T[] shrink(final T @Nullable [] ts) {
    return cantBeNull(Arrays.copyOf(ts, 0));
  }

  /** A JUnit test class for the enclosing class.
   * @author Yossi Gil, the Technion.
   * @since 27/08/2008 */
  @SuppressWarnings({ "static-method", "javadoc", "synthetic-access" }) //
  public static class TEST {
    private final NonNullCache<List<String>> sparseCollection = new NonNullCache<List<String>>() {
      @Override protected List<@Nullable String> ____() {
        final List<@Nullable String> $ = new ArrayList<>();
        $.add(null);
        $.add(null);
        $.add(null);
        $.add(null);
        $.add(null);
        $.add("A");
        $.add(null);
        $.add(null);
        $.add(null);
        $.add("B");
        $.add(null);
        $.add("C");
        $.add(null);
        $.add(null);
        $.add(null);
        $.add(null);
        return $;
      }
    };
    final String[] alternatingArray = new String @NonNull [] { null, "A", null, null, "B", null, null, null, "C", null };
    final String[] nonNullArray = { "1", "2", "4" };

    @Test public void nullsNonNullArrayLength() {
      assertEquals(nonNullArray.length, nulls(nonNullArray).length);
    }

    @Test public void nullsNullArrayItems() {
      assertEquals("1", nulls(nonNullArray)[0]);
      assertEquals("2", nulls(nonNullArray)[1]);
      assertEquals("4", nulls(nonNullArray)[2]);
    }

    @Test public void nullsPruneArrayAltenatingItems() {
      assertEquals("A", nulls(alternatingArray)[0]);
      assertEquals("B", nulls(alternatingArray)[1]);
      assertEquals("C", nulls(alternatingArray)[2]);
    }

    @Test public void nullsPruneArrayAltenatingLength() {
      assertEquals(3, nulls(alternatingArray).length);
    }

    @Test public void nullsPruneSparseCollectionContents() {
      final String[] a = nulls(sparseCollection.value()).toArray(new String[3]);
      assertEquals("A", a[0]);
      assertEquals("B", a[1]);
      assertEquals("C", a[2]);
      assertEquals(3, a.length);
    }

    @Test public void nullsPruneSparseCollectionLength() {
      assertEquals(3, nulls(sparseCollection.value()).size());
    }

    @Test public void nullsPrunNotNull() {
      notNull(nulls(sparseCollection.value()));
    }

    @Test public void shrinkArray() {
      assertEquals(0, shrink(new Object @Nullable [10]).length);
    }

    @Test public void shrinkEmptyArray() {
      assertEquals(0, shrink(new Object @Nullable [0]).length);
    }

    @Test public void whitesEmptyArray() {
      assertEquals(0, prune.whites(new String @NonNull [] {}).length);
    }

    @Test public void whitesEmptyList() {
      assertEquals(0, prune.whites().length);
    }
  }
}
