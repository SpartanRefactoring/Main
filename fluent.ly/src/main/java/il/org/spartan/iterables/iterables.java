/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.iterables;

import static il.org.spartan.Utils.contains;
import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import an.iterable;
import fluent.ly.*;

/** No values in an 'enum' used as name space for a collection of 'static'
 * functions.
 * @author Yossi Gil <Yossi.Gil@GMail.COM> */
public enum iterables {
  ;
  /** Counts the number of items in an {@link Iterable}.
   * @param <T> some arbitrary type
   * @param ts some iterable over items whose type is the type parameter
   * @return number of items the given iterable yields. */
  public static <T> int count(final @Nullable Iterable<T> ts) {
    int $ = 0;
    if (ts != null)
      for (final @Nullable T ¢ : ts)
        $ += as.bit(¢ != null);
    return $;
  }

  /** @param <T> JD
   * @return <code><b>true</b></code> <i>iff</i> the receive is empty */
  @NotNull public static <T> Iterable<T> empty() {
    return iterable.over();
  }

  /** @param os JD */
  public static boolean isEmpty(@NotNull final Iterable<?> os) {
    for (@Nullable final Object name2 : os)
      if (name2 != null)
        return false;
    return true;
  }

  /** wraps a value in a singleton iterator form
   * @param <T> JD
   * @param $ JD
   * @return parameter, but in a singleton iterator form */
  public static <T> Iterator<T> singletonIterator(final T $) {
    return iterable.singleton($).iterator();
  }

  //
  /** A static nested class hosting unit tests for the nesting class Unit test
   * for the containing class. Note the naming convention: a) names of test
   * methods do not use are not prefixed by "test". This prefix is redundant. b)
   * test methods begin with the name of the method they check.
   * @author Yossi Gil
   * @since 2014-05-31 */
  @SuppressWarnings("static-method") public static class TEST {
    @Test public void containsDegenerate() {
      azzert.nay(contains("Hello"));
    }

    @Test public void containseturnsFalseTypical() {
      azzert.nay(contains("Hello", null, "x", "y", null, "z", "w", "u", "v"));
    }

    @Test public void containsSimple() {
      azzert.aye("", contains("Hello", "e"));
    }

    @Test public void containsTypical() {
      azzert.aye("", contains("Hello", "a", "b", "c", "d", "e", "f"));
    }

    @Test public void containsWithNulls() {
      azzert.aye("", contains("Hello", null, "a", "b", null, "c", "d", "e", "f", null));
    }

    @Test public void countDoesNotIncludeNull() {
      assertEquals(3, count(iterable.over(null, "One", null, "Two", null, "Three")));
    }

    @Test public void countEmpty() {
      assertEquals(0, count(iterables.<String> empty()));
    }

    @Test public void countSingleton() {
      assertEquals(1, count(iterable.singleton(new Object())));
    }

    @Test public void countThree() {
      assertEquals(3, count(iterable.over("One", "Two", "Three")));
    }
  }
}
