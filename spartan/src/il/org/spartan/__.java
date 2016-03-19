/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;
import static org.junit.Assert.*;

import java.util.*;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import il.org.spartan.__.FoundHandleForT.FoundHandleForInt;

/**
 * A collection of <code><b>static</b></code> functions with no appropriate
 * 'sedoco' class name was found.
 *
 * @author Yossi Gil
 * @since Jul 7, 2014
 */
public enum __ {
	// No values in an 'enum' used as a name space for a collection of 'static'
	// functions.
	;
	public static final String QUOTE = "'";
	public static <T, C extends Collection<T>> C add(final C $, final Iterable<? extends T> ts) {
		for (final T t : ts)
			if (t != null)
				$.add(t);
		return $;
	}
	@SafeVarargs public static <T, C extends Collection<T>> C add(final C $, final T... ts) {
		for (final T t : ts)
			if (t != null)
				$.add(t);
		return $;
	}
	@SafeVarargs public static <T, C extends Collection<T>> C addAll(final C $, final Collection<? extends T>... tss) {
		for (final Collection<? extends T> ts : tss)
			if (ts != null)
				$.addAll(ts);
		return $;
	}
	@SafeVarargs public static <T, C extends Collection<T>> C addAll(final C $, final Iterable<? extends T>... tss) {
		for (final Iterable<? extends T> ts : tss)
			if (ts != null)
				add($, ts);
		return $;
	}
	@SafeVarargs public static <T, C extends Collection<T>> C addAll(final C $, final T... ts) {
		for (final T t : ts)
			if (t != null)
				add($, t);
		return $;
	}
	public static <F, T> Applicator<F, T> apply(final Function<F, T> f) {
		return new Applicator<>(f);
	}
	/**
	 * Removes the @Nullable annotation present on the type of a value. This
	 * function is mainly used to make <code><b>null</b></code<]> checkers happy.
	 * <p>
	 * The parameter is an instance of an arbitrary type, T. The hidden assumption
	 * is that a @Nullable annotation is present on T.
	 * Thus, the parameter may be either
	 * <code><b>null</b></code<]>, or an actual instance of T.
	 * <p>
	 * The function returns the same instance it received as a parameter, except that
	 * this instance is returned as an instance of the type T <i>without</i> the @Nullable
	 * annotation. Execution is aborted with an {@link AssertionError} if the parameter is null.
	 * <p>
	 * As it turns out, this function is a (slow) logical no-op, but still applicable to
	 * arguments of type T, where T does not have the @Nullable annotation present on it.
	 *  <p>
	 * For reasons related to the way non-nullability is managed in Java, the compiler will
	 * not warn you from doing applying this function to a {@link NonNull} type. However,
	 * there is absolutely no point in removing a @Nullable  annotation if the type that
	 * does not have it. Doing so a is plain clutter. Since the compiler cannot assist you,
	 * you will have to be on the guard.
	 *
	 * @param <T> some arbitrary type
	 * @param $ an instance of the type parameter
	 * @return its parameter, after verifying that it is not <code><b>null</b>
	 *         </code>
	 * @see #mustBeNull(Object)
	 */
	public static <T> T cantBeNull(final @Nullable T $) {
		assert $ != null;
		return $;
	}
	/**
	 * Determine if a string contains any of a list of patterns.
	 *
	 * @param text string to be tested
	 * @param patterns a list of substrings
	 * @return tree iff the the first parameter contains any of the substrings
	 *         found in the second parameter
	 */
	public static boolean contains(final String text, final String... patterns) {
		for (final String pattern : patterns)
			if (null != pattern && text.contains(pattern))
				return true;
		return false;
	}
	public static FoundHandleForInt found(final int i) {
		return new FoundHandleForInt(i);
	}
	public static <T> FoundHandleForT<T> found(final T t) {
		return new FoundHandleForT<>(t);
	}
	/**
	 * Determine whether a <code><b>null</b></code> occurs in a sequence of
	 * objects
	 *
	 * @param os an unknown number of objects
	 * @return <code><b>null</b></code> <i>iff</i> one of the parameters is
	 *         <code><b>null</b></code>
	 */
	public static boolean hasNull(final Object... os) {
		for (final Object o : os)
			if (o == null)
				return true;
		return false;
	}
	/**
	 * Computes the maximum of two or more integers.
	 *
	 * @param a some integer
	 * @param is additional integers
	 * @return the largest of the parameters
	 */
	public static int max(final int a, final int... is) {
		int $ = a;
		for (final int i : is)
			$ = Math.max($, i);
		return $;
	}
	/**
	 * Computes the minimum of two or more integers
	 *
	 * @param a some integer
	 * @param is additional
	 * @return the smallest of the parameters
	 */
	public static int min(final int a, final int... is) {
		int $ = a;
		for (final int i : is)
			$ = Math.min($, i);
		return $;
	}
	/**
	 * Aborts in case a given value is <code><b>null</b></code>.
	 * <p>
	 * This function is the lesser used dual of {@link #cantBeNull(Object)}.
	 *
	 * @param <T> some arbitrary type
	 * @param $ an instance of the type parameter which is required to be
	 *          <code><b>null</b></code<]>.
	 * @return the parameter
	 */
	public static @Nullable <T> Void mustBeNull(final @Nullable T $) {
		assert $ == null;
		return null;
	}
	/**
	 * Prepend a given <code><b>char</b></code> to a {@link StringBuilder}
	 *
	 * @param $ prepend to what
	 * @param c what needs to be prepended
	 * @return the {@link StringBuilder} parameter with the
	 *         <code><b>char</b></code> parameter prepended to it
	 */
	public static StringBuilder prepend(final StringBuilder $, final char c) {
		return cantBeNull($.insert(0, c));
	}
	/**
	 * Prepend a given {@link String} to a {@link StringBuilder}
	 *
	 * @param $ prepend to what
	 * @param s what needs to be prepended
	 * @return the {@link StringBuilder} parameter with the {@link String}
	 *         parameter prepended to it
	 */
	public static StringBuilder prepend(final StringBuilder $, final String s) {
		return cantBeNull($.insert(0, s));
	}
	/**
	 * Quote a given {@link String}
	 *
	 * @param $ some {@link String} to be quoted
	 * @return the parameter, quoted
	 */
	public static String quote(final @Nullable String $) {
		return $ != null ? QUOTE + $ + QUOTE : "<null reference>";
	}
	/**
	 * Computes the square of a given number
	 *
	 * @param d some number
	 * @return the square of the parameter
	 */
	public static double sqr(final double d) {
		return d * d;
	}
	/**
	 * Computes the square of a given number
	 *
	 * @param i some integer
	 * @return the square of the parameter
	 */
	public static int sqr(final int i) {
		return i * i;
	}
	/**
	 * Swap the contents of two cells in a given array
	 *
	 * @param <T> type of array elements
	 * @param ts the given array
	 * @param i index of one cell
	 * @param j index of another cell
	 */
	public static <T> void swap(final T[] ts, final int i, final int j) {
		final T t = ts[i];
		ts[i] = ts[j];
		ts[j] = t;
	}
	/**
	 * Reifies the notion of a function
	 *
	 * @author Yossi Gil
	 * @param <F> the type of the function's argument
	 * @param <T> the type of the function's result
	 */
	public static class Applicator<F, T> {
		private final Function<F, T> function;
		/**
		 * Instantiates this class
		 *
		 * @param function which function to apply?
		 */
		public Applicator(final Function<F, T> function) {
			this.function = function;
		}
		@SafeVarargs public final Iterable<T> to(final F... fs) {
			final List<T> $ = new ArrayList<>();
			for (final F f : fs)
				if (f != null)
					$.add(function.apply(f));
			return $;
		}
		public <FS extends Iterable<? extends F>> Iterable<T> to(final FS fs) {
			final List<T> $ = new ArrayList<>();
			for (final F f : fs)
				if (f != null)
					$.add(function.apply(f));
			return $;
		}
	}
	public static class FoundHandleForT<T> {
		final T candidate;
		/**
		 * Instantiates this class. * @param candidate what to search for
		 */
		public FoundHandleForT(final T candidate) {
			this.candidate = candidate;
		}
		/**
		 * Determine if an integer can be found in a list of values
		 *
		 * @param ts where to search
		 * @return true if the the item is found in the list
		 */
		@SafeVarargs public final boolean in(final T... ts) {
			for (final T t : ts)
				if (t != null && t.equals(candidate))
					return true;
			return false;
		}
		public static class FoundHandleForInt {
			final int candidate;
			/**
			 * Instantiates this class.
			 *
			 * @param candidate what to search for
			 */
			public FoundHandleForInt(final int candidate) {
				this.candidate = candidate;
			}
			/**
			 * Determine if an integer can be found in a list of values
			 *
			 * @param is where to search
			 * @return true if the the item is found in the list
			 */
			@SafeVarargs public final boolean in(final int... is) {
				for (final int i : is)
					if (i == candidate)
						return true;
				return false;
			}
		}
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
		public static Integer[] intToIntegers(final int... is) {
			final Integer[] $ = new Integer[is.length];
			for (int i = 0; i < is.length; i++)
				$[i] = Box.it(is[i]);
			return $;
		}
		@Test public void addAllTypical() {
			final Set<String> ss = new HashSet<>();
			addAll(ss, As.set("A", "B"), null, As.set("B", "C", "D"));
			assertFalse(ss.contains("E"));
			assertFalse(ss.contains(null));
			assertEquals(4, ss.size());
			for (final String s : ss)
				assertTrue(ss.contains(s));
		}
		@Test public void addTypical() {
			final Set<String> ss = new HashSet<>();
			add(ss, null, "A", null, "B", "B", null, "C", "D", null);
			assertFalse(ss.contains("E"));
			assertFalse(ss.contains(null));
			assertEquals(4, ss.size());
			for (final String s : ss)
				assertTrue(ss.contains(s));
		}
		@Test public void cantBeNullOfNull() {
			try {
				cantBeNull(null);
				fail("AssertionError expected prior to this line.");
			} catch (final AssertionError e) {
				assertTrue(true);
			}
		}
		@Test public void cantBeNullTypical() {
			assertNotNull(cantBeNull(new Object()));
		}
		@Test public void isNullTypical() {
			try {
				assertNull(mustBeNull(null));
				fail("AssertionError expected prior to this line.");
			} catch (final AssertionError e) {
				assertTrue(true);
			}
		}
		@Test public void mustBeNullOfNonNull() {
			try {
				mustBeNull(new Object());
				fail("AssertionError expected prior to this line.");
			} catch (final AssertionError e) {
				assertTrue(true);
			}
		}
		@Test public void quoteEmptyString() {
			assertEquals("''", quote(""));
		}
		@Test public void quoteNull() {
			assertEquals("<null reference>", quote(null));
		}
		@Test public void quoteSimpleString() {
			assertEquals("'A'", quote("A"));
		}
		@Test public void swapDegenerate() {
			final String[] ss = As.array("A", "B", "C", "D");
			swap(ss, 1, 1);
			assertArrayEquals(As.array("A", "B", "C", "D"), ss);
		}
		@Test public void swapTypical() {
			final String[] ss = As.array("A", "B", "C", "D");
			swap(ss, 1, 2);
			assertArrayEquals(As.array("A", "C", "B", "D"), ss);
		}
		@Test public void swapTypicalCase() {
			final Integer[] $ = intToIntegers(29, 1, 60);
			swap($, 0, 1);
			assertArrayEquals(intToIntegers(1, 29, 60), $);
		}
	}
}
