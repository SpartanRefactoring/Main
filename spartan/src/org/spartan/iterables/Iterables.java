/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package org.spartan.iterables;
import static org.junit.Assert.assertEquals;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.spartan.As;

public enum Iterables {
	// No values in an 'enum' used as name space for a collection of 'static'
	// functions.
	;
	public static <T> PureIterable.Sized<T> singletonIterable(final T t) {
		return As.iterable(t);
	}
	public static <T> PureIterator<T> singletonIterator(final T t) {
		return singletonIterable(t).iterator();
	}
	/**
	 * Counts the number of items in an {@link Iterable}.
	 * 
	 * @param <T>
	 *          some arbitrary type
	 * @param ts
	 *          some iterable over items whose type is the type parameter
	 * @return the number of items the given iterable yields.
	 */
	public static <T> int count(final @Nullable Iterable<T> ts) {
		int $ = 0;
		if (ts != null)
			for (final @Nullable T t : ts)
				$ += As.bit(t != null);
		return $;
	}
	public static <T> PureIterable.Sized<T> empty() {
		return As.iterable();
	}
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
	@SuppressWarnings({ "static-method", "javadoc" })//
	public static class TEST {
		@Test public void countDoesNotIncludeNull() {
			assertEquals(3, count(As.iterable(null, "One", null, "Two", null, "Three")));
		}
		@Test public void countEmpty() {
			assertEquals(0, count(Iterables.empty()));
		}
		@Test public void countSingletion() {
			assertEquals(1, count(singletonIterable(new Object())));
		}
		@Test public void countThree() {
			assertEquals(3, count(As.iterable("One", "Two", "Three")));
		}
	}
}
