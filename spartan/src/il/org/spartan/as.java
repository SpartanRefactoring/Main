/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static org.junit.Assert.*;
import il.org.spartan.iterables.*;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.junit.*;
import org.junit.runners.*;

/**
 * A collection of <code><b>static</b></code> functions for converting from one
 * aggregate type to another.
 *
 * @author Yossi Gil
 * @since Jul 8, 2014
 */
public enum as {
  // No values in an 'enum' which serves as a name space for a collection of
  // 'static' functions.
  ;
  /**
   * Converts a sequence of arguments into an array
   *
   * @param <T> JD
   * @param ts JD
   * @return an array representation of the parameter
   */
  @SafeVarargs public static <T> T[] array(final T... ts) {
    return ts;
  }
  /**
   * Converts a boolean into a bit value
   *
   * @param $ some boolean value
   * @return 1 if the parameter is true, 0 otherwise
   */
  public static int bit(final boolean $) {
    return $ ? 1 : 0;
  }
  /**
   * C like conversion of a reference to an {@link Object} into a 0/1 bit.
   *
   * @param o some object
   * @return <code>0</code> if the parameter is <code><b>null</b></code>.
   *         <code>1</code> otherwise.
   * @see as#bit(Object)
   */
  public static int bit(final @Nullable Object o) {
    return o == null ? 0 : 1;
  }
  /**
   * Converts a sequence of <code><b>int</b></code> values into a {@link List}
   * of non-<code><b>null</b></code> {@link Integer}s.
   *
   * @param is what to covert
   * @return the parameter, converted to the {@link List} of non-
   *         <code><b>int</b></code> {@link Integer}s form.
   */
  public static List<Integer> ingeterList(final int... is) {
    final List<Integer> $ = new ArrayList<>();
    for (final int i : is)
      $.add(box.it(i));
    return $;
  }
  /**
   * Converts a sequence of integer values into an array.
   *
   * @param $ some sequence of values of the type parameter
   * @return the parameters, organized as an array with entries whose type is
   *         the type parameter
   */
  public static int[] intArray(final int... $) {
    return $;
  }
  /**
   * Return a compact representation of a list of {@link Integer}s as an array
   * of type <code><b>int</b></code>.
   *
   * @param is the list to be converted, none of the elements in it can be
   *          <code><b>null</b></code>
   * @return an array of <code><b>int</b></code>. representing the input.
   */
  public static int[] intArray(final List<Integer> is) {
    final int @NonNull [] $ = new int @NonNull [is.size()];
    for (int i = 0; i < $.length; ++i)
      $[i] = is.get(i).intValue();
    return $;
  }
  /**
   * Creates an iterable for an array of objects
   *
   * @param <T> an arbitrary type
   * @param ts what to iterate on
   * @return an {@link Iterable} over the parameter
   */
  @SafeVarargs public static <@Nullable T> PureIterable.Sized<T> iterable(final T... ts) {
    return new PureIterable.Sized<T>() {
      @Override public PureIterator<T> iterator() {
        return new PureIterator<T>() {
          @Override public boolean hasNext() {
            return current < ts.length;
          }
          @Override public T next() {
            return ts[current++];
          }

          int current = 0;
        };
      }
      @Override public int size() {
        return ts.length;
      }
    };
  }
  /**
   * Creates an iterable for an array of objects
   *
   * @param <T> an arbitrary type
   * @param ts what to iterate on
   * @return an {@link Iterable} over the parameter
   */
  @SafeVarargs public static <T> PureIterator<T> iterator(final T @Nullable... ts) {
    return nonNullIterable(ts).iterator();
  }
  /**
   * Converts an {@link Iterable} of a given type into a {@link List} of values
   * of this type.
   *
   * @param <T> type of items to be converted
   * @param $ what to convert
   * @return the parameter, converted to the {@link List} of the given type
   */
  public static <T> List<T> list(final Iterable<? extends T> $) {
    return accumulate.to(new ArrayList<T>()).add($).elements();
  }
  /**
   * Converts a sequence of objects of some common type T into a {@link List} of
   * values
   *
   * @param <T> type of objects to be converted
   * @param $ what to covert
   * @return the result parameter, converted into a {@link List}
   */
  @SafeVarargs public static <T> List<T> list(final @Nullable T @Nullable... $) {
    return accumulate.to(new ArrayList<T>()).add($).elements();
  }
  /**
   * Creates an iterable for an array of objects
   *
   * @param <T> an arbitrary type
   * @param ts what to iterate on
   * @return an {@link Iterable} over the parameter
   */
  @SafeVarargs public static <T> PureIterable.Sized<T> nonNullIterable(final T... ts) {
    return new PureIterable.Sized<T>() {
      @Override public PureIterator<T> iterator() {
        return new PureIterator<T>() {
          @Override public boolean hasNext() {
            return current < ts.length;
          }
          @Override public @NonNull T next() {
            @SuppressWarnings("null") final @NonNull T $ = ts[current++];
            return $;
          }

          int current = 0;
        };
      }
      @Override public int size() {
        return ts.length;
      }
    };
  }
  /**
   * Converts a sequence of objects of a given type into a {@link Set} of values
   *
   * @param <T> type of objects to be converted
   * @param ts what to covert
   * @return the parameter, converted into a {@link Set}
   */
  @SafeVarargs public static <T> Set<? extends T> set(final @Nullable T @Nullable... ts) {
    return accumulate.to(new HashSet<T>()).add(ts).elements();
  }
  /**
   * Converts a value, which can be either a <code><b>null</b></code> or
   * references to valid instances, into a {@link NonNull}
   *
   * @param $ some value
   * @return the parameter, after bing to a non-null string.
   */
  public static String string(@Nullable final Object $) {
    return $ == null ? NULL : as.string($.toString());
  }
  /**
   * Converts a {@link String}, which can be either a <code><b>null</b></code>
   * or an actual String, into a {@link NonNull} String.
   *
   * @param $ some value
   * @return the parameter, after bing to a non-null string.
   */
  public static String string(@Nullable final String $) {
    return $ != null ? $ : NULL;
  }
  /**
   * Converts an {@link Iterable} into an array of {@link String}.
   *
   * @param os what to covert
   * @return an array of the parameter values, each converted to i
   *         {@link String}
   */
  @SuppressWarnings("null") public static String[] strings(final Iterable<? extends @Nullable Object> os) {
    final List<@NonNull String> $ = new ArrayList<>();
    for (final @Nullable Object o : os)
      if (o != null)
        $.add("" + o);
    return cantBeNull($.toArray(new String @NonNull [$.size()]));
  }

  /**
   * The string returned when 'conversion to string' is applied to a
   * <code><b>null</b></code> value.
   */
  public static final String NULL = "(null)";

  /**
   * A static nested class hosting unit tests for the nesting class Unit test
   * for the containing class. Note the naming convention: a) names of test
   * methods do not use are not prefixed by "test". This prefix is redundant. b)
   * test methods begin with the name of the method they check.
   *
   * @author Yossi Gil
   * @since 2014-05-31
   */
  @FixMethodOrder(MethodSorters.NAME_ASCENDING)//
  @SuppressWarnings({ "static-method", "javadoc", "boxing" })//
  public static class TEST {
    @Test public void asBitOfFalse() {
      assertEquals(0, as.bit(false));
    }
    @Test public void asBitOfTrue() {
      assertEquals(1, as.bit(true));
    }
    @Test public void asIntArraySimple() {
      final int @NonNull [] is = as.intArray(100, 200, 200, 12, 13, 0);
      assertArrayEquals(is, as.intArray(as.ingeterList(is)));
    }
    @Test public void asListSimple() {
      final List<@Nullable Integer> is = as.list(12, 13, 14);
      assertEquals(box.it(12), is.get(0));
      assertEquals(box.it(13), is.get(1));
      assertEquals(box.it(14), is.get(2));
      assertEquals(3, is.size());
    }
    @Test public void stringOfNull() {
      assertEquals(NULL, as.string(null));
    }
    @Test public void stringWhenToStringReturnsNull() {
      assertEquals(NULL, as.string(new Object() {
        @Override public @Nullable String toString() {
          return null;
        }
      }));
    }
  }
}
