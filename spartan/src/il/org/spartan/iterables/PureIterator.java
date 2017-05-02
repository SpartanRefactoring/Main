/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.iterables;

import static il.org.spartan.Utils.*;
import static fluent.ly.azzert.*;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.*;
import org.junit.*;

import fluent.ly.*;

/** A kind of {@link Iterator} which does not support the rarely used
 * {@link #remove} operation and saves the user, i.e., who ever chooses to
 * <code><b>implement</b></code> this class, the trouble of providing a vacuous
 * implementation of this function. Moreover, the descendant is actually
 * forbidden from giving any semantics to this function, which would be in
 * contrast with the read-only nature of this iterator.
 * @see PureIterator
 * @author Yossi Gil
 * @since 2014-06-03
 * @param <T> some arbitrary type */
public abstract class PureIterator<T> implements Iterator<T> {
  /** This <code><b>final</b></code> implementation of the method, prevents
   * descendants from giving {link #remove} semantics other than immediately
   * throwing a fresh instance of {@link IllegalArgumentException}.
   * @see java.util.Iterator#remove() */
  @Override public final void remove() {
    throw new IllegalArgumentException();
  }

  /** @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @param <T> JD
   * @since 2016 */
  public abstract static class Staged<T> extends PureIterator<T> {
    /** Stores the next value that this iterator returns. It has non-null
     * content only after {@link #hasNext} returned true. */
    @Nullable private T next;

    @Override @NotNull public final T next() {
      @NotNull final @NonNull T $ = cantBeNull(next);
      clearNext();
      return $;
    }

    protected final void clearNext() {
      cantBeNull(next);
      next = null;
    }

    protected final boolean setNext(final T next) {
      mustBeNull(this.next);
      this.next = next;
      return true;
    }
  }

  public static class TEST extends PureIterator.Staged<String> {
    public static <T> void assertEquals(final String reason, final T t1, final T t2) {
      azzert.that(reason, t2, is(t1));
    }

    public static <T> void assertEquals(final T t1, final T t2) {
      azzert.that(t2, is(t1));
    }

    public static <T> void assertNotEquals(final String reason, final T t1, final T t2) {
      azzert.that(reason, t2, is(t1));
    }

    public static <T> void assertNotEquals(final T t1, final T t2) {
      azzert.that(t2, is(t1));
    }

    @Override public boolean hasNext() {
      return false;
    }

    @Test public void isEmpty() {
      azzert.nay(hasNext());
    }

    @Test(expected = IllegalArgumentException.class) public void tryToRemove() {
      remove();
    }
  }
}