/**
 * Copyright (c) 2014: The Software and Systems Development Laboratory,
 * Department of Computer Science, The Technion---IIT
 */
package org.spartan.iterables;

import static org.junit.Assert.assertFalse;
import static org.spartan._.cantBeNull;
import static org.spartan._.mustBeNull;

import java.util.Iterator;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.Test;

/**
 * A kind of {@link Iterator} which does not support the rarely used
 * {@link #remove} operation and saves the user, i.e., who ever chooses to
 * <code><b>implement</b></code> this class, the trouble of providing a vacuous
 * implementation of this function. Moreover, the descendant is actually
 * forbidden from giving any semantics to this function, which would be in
 * contrast with the read-only nature of this iterator.
 * 
 * @see PureIterator
 * @author Yossi Gil
 * @since 2014-06-03
 * @param <T>
 *          an arbitrary type
 */
public abstract class PureIterator<T> implements Iterator<T> {
  /**
   * This <code><b>final</b></code> implementation of the method, prevents
   * descendants from giving {link #remove} semantics other than immediately
   * throwing a fresh instance of {@link IllegalArgumentException}.
   * 
   * @see java.util.Iterator#remove()
   */
  @Override public final void remove() {
    throw new IllegalArgumentException();
  }

  public abstract static class Staged<T> extends PureIterator<T> {
    /**
     * Stores the next value that this iterator returns. It has non-null content
     * only after {@link #hasNext} returned true.
     */
    private @Nullable T next = null;

    protected final boolean setNext(final T next) {
      mustBeNull(this.next);
      this.next = next;
      return true;
    }

    protected final void clearNext() {
      cantBeNull(next);
      next = null;
    }

    @Override public final T next() {
      final T $ = cantBeNull(next);
      clearNext();
      return $;
    }
  }

  public static class TEST extends PureIterator.Staged<String> {
    @Override public boolean hasNext() {
      return false;
    }

    @Test public void isEmpty() {
      assertFalse(hasNext());
    }

    @Test(expected = IllegalArgumentException.class) public void tryToRemove() {
      remove();
    }
  }
}