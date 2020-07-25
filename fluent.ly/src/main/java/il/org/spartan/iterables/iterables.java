/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.iterables;

import java.util.Iterator;

/**
 * No values in an 'enum' used as name space for a collection of 'static'
 * functions.
 *
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 */
public enum iterables {
  ;
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
      final var $ = value();
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
}
