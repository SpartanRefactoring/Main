/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package org.spartan.iterables;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.spartan.__.contains;

import java.util.Iterator;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.spartan.As;

public enum Iterables {
	// No values in an 'enum' used as name space for a collection of 'static'
	// functions.
	;
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
	public static boolean isEmpty(final Iterable<?> os) {
		for (final Iterator<?> i = os.iterator(); i.hasNext();)
			if (i.next() != null)
				return false;
		return true;
	}
	public static <T> PureIterable.Sized<T> singleton(final T t) {
		return As.iterable(t);
	}
	public static <T> PureIterator<T> singletonIterator(final T t) {
		return singleton(t).iterator();
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
		@Test public void containsDegenerate() {
			assertFalse(contains("Hello"));
		}
		@Test public void containseturnsFalseTypical() {
			assertFalse(contains("Hello", null, "x", "y", null, "z", "w", "u", "v"));
		}
		@Test public void containsSimple() {
			assertTrue(contains("Hello", "e"));
		}
		@Test public void containsTypical() {
			assertTrue(contains("Hello", "a", "b", "c", "d", "e", "f"));
		}
		@Test public void containsWithNulls() {
			assertTrue(contains("Hello", null, "a", "b", null, "c", "d", "e", "f", null));
		}
		@Test public void countDoesNotIncludeNull() {
			assertEquals(3, count(As.iterable(null, "One", null, "Two", null, "Three")));
		}
		@Test public void countEmpty() {
			assertEquals(0, count(Iterables.<String> empty()));
		}
		@Test public void countSingleton() {
			assertEquals(1, count(singleton(new Object())));
		}
		@Test public void countThree() {
			assertEquals(3, count(As.iterable("One", "Two", "Three")));
		}
	}
}
