/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.iterables;
import static il.org.spartan.__.cantBeNull;
import static il.org.spartan.__.mustBeNull;
import static org.junit.Assert.assertFalse;

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
 * @param <T> some arbitrary type
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
	@SuppressWarnings({ "javadoc" })//
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