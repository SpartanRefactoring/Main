/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.iterables;

import static il.org.spartan.Utils.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import an.*;
import fluent.ly.*;

/** No values in an 'enum' used as name space for a collection of 'static'
 * functions.
 * @author Yossi Gil <Yossi.Gil@GMail.COM> */
public enum iterables {
  ;
  //
  /** A static nested class hosting unit tests for the nesting class Unit test
   * for the containing class. Note the naming convention: a) names of test
   * methods do not use are not prefixed by "test". This prefix is redundant. b)
   * test methods begin with the name of the method they check.
   * @author Yossi Gil
   * @since 2014-05-31 */
  @SuppressWarnings("static-method")
  public static class TEST {
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
      assertEquals(3, fluent.ly.count.count(iterable.over(null, "One", null, "Two", null, "Three")));
    }
    @Test public void countEmpty() {
      assertEquals(0, fluent.ly.count.count(the.<String> empty()));
    }
    @Test public void countSingleton() {
      assertEquals(1, fluent.ly.count.count(iterable.singleton(new Object())));
    }
    @Test public void countThree() {
      assertEquals(3, fluent.ly.count.count(iterable.over("One", "Two", "Three")));
    }
  }

  public abstract static class RangeIterator<T> extends ReadonlyIterator<T> {
    private final int n;
    private int i;
  
    public RangeIterator(final int n) {
      this.n = n;
    }
    @Override public final boolean hasNext() {
      return i < n;
    }
    @Override public T next() {
      final T $ = value();
      ++i;
      return $;
    }
    protected int i() {
      return i;
    }
    protected abstract T value();
  }

  public abstract static class ReadonlyIterator<T> implements Iterator<T> {
    @Override public final void remove() {
      throw new UnsupportedOperationException();
    }
  }

  /** An {@linkplain "http://en.wikipedia.org/wiki/Adapter_pattern Adapter"} of
   * a scalar object, adapting it to the {@link Iterable} interface whereby
   * making it possible to iterate over this object in the following sense:. If
   * the object is non-null, then the iteration will return the object and
   * terminate. If the object is null, then the iteration is vacuous.
   * @author Yossi Gil
   * @since Oct 19, 2009
   * @param <T> type of objects in the array */
  public static class Singleton<T> implements Iterable<T> {
    public static <T> Iterable<T> make(final T ¢) {
      return ¢ == null ? null : new Singleton<>(¢);
    }
  
    T t;
  
    /** Instantiate the adapter with an object
     * @param t the object on which we can iterate. */
    public Singleton(final T t) {
      this.t = t;
    }
    @Override public Iterator<T> iterator() {
      return new iterables.ReadonlyIterator<T>() {
        @Override public boolean hasNext() {
          return t != null;
        }
        @Override public T next() {
          final T $ = t;
          t = null;
          return $;
        }
      };
    }
  }
}
