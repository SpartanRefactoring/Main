/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package org.spartan;
import static org.junit.Assert.assertEquals;
import static org.spartan.__.sqr;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.Test;

/**
 * A class for lazy, memoizing evaluation of objects of arbitrary type. The
 * evaluation must never return <code><b>null</b>>
 * </b>.
 *
 * @param <T>
 *          some arbitrary type
 * @author Yossi Gil
 * @since 2014-06-20
 */
public abstract class NonNullCache<T> {
	/**
	 * The cached value, null when the cache was not populated
	 */
	private @Nullable T value = null;
	/**
	 * This function is to be implemented by clients, giving a method for
	 * computing the cached value. This class protects this function, guaranteeing
	 * that it would only be called once.
	 *
	 * @return the value to be cached
	 */
	protected abstract T __();
	/**
	 * Compute the cached value, either by looking up the memoized valued, or by
	 * actual computation
	 *
	 * @return the cached value
	 */
	public T value() {
		return value != null ? value : (value = __());
	}
	@SuppressWarnings("javadoc")//
	public static class TEST extends NonNullCache<String> {
		private static final int SOME_OFFSET = 17;
		private int evaluations = 0;
		@Override protected String __() {
			return SOME_OFFSET + "x" + sqr(evaluations++);
		}
		@Test public void firstReturnsFirstOffset() {
			assertEquals(SOME_OFFSET + "x0", value());
		}
		@Test public void restReturnsFirstOffset() {
			value();
			assertEquals(SOME_OFFSET + "x0", value());
			for (int i = 0; i < 10; i++)
				assertEquals(SOME_OFFSET + "x0", value());
		}
	}
}
