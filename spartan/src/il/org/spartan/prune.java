/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static il.org.spartan.azzert.assertEquals;
import static org.junit.Assert.assertEquals;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.*;
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
   * @param ts JD */
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
    for (@org.jetbrains.annotations.Nullable final T ¢ : ts)
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
  @NotNull public static <T> T[] nulls(@NotNull final T[] ts) {
    @NotNull final List<T> $ = new ArrayList<>();
    for (@org.jetbrains.annotations.Nullable final T ¢ : ts)
      if (¢ != null)
        $.add(¢);
    return cantBeNull($.toArray(shrink(ts)));
  }

  /**
   * @param ss  JD 
   */
  @SafeVarargs @NotNull public static String[] whites(@NotNull final String... ss) {
    @NotNull final List<String> $ = new ArrayList<>();
    for (@org.jetbrains.annotations.Nullable final String ¢ : ss)
      if (¢ != null)
        accumulate.to($).add(¢.trim());
    return asArrray($);
  }

  /** @param $ */
  @NotNull private static String[] asArrray(@NotNull final List<String> $) {
    return cantBeNull($.toArray(new String @NonNull [0]));
  }

  /** Shrink an array size to zero.
   * @param <T> type of elements in the input array.
   * @param ¢ an array of values.
   * @return an array of size 0 of elements of type <code>T</code>. */
  @NotNull private static <T> T[] shrink(@NotNull final T @Nullable [] ¢) {
    return cantBeNull(Arrays.copyOf(¢, 0));
  }

  /** A JUnit test class for the enclosing class.
   * @author Yossi Gil, the Technion.
   * @since 27/08/2008 */
  @SuppressWarnings({ "static-method", "javadoc", "synthetic-access" }) //
  public static class TEST {
    private final NonNullCache<List<String>> sparseCollection = new NonNullCache<List<String>>() {
      @Override @NotNull protected List<@Nullable String> ____() {
        @NotNull final List<@Nullable String> $ = new ArrayList<>();
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
    @org.jetbrains.annotations.Nullable final String[] alternatingArray = new String @NonNull [] { null, "A", null, null, "B", null, null, null, "C",
        null };
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
      @NotNull final String[] a = nulls(sparseCollection.value()).toArray(new String[3]);
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
      assertEquals(0, prune.whites().length);
    }

    @Test public void whitesEmptyList() {
      assertEquals(0, prune.whites().length);
    }
  }
}
