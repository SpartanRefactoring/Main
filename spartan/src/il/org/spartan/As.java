/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;
import static il.org.spartan.__.add;
import static il.org.spartan.__.addAll;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.*;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import il.org.spartan.iterables.PureIterable;
import il.org.spartan.iterables.PureIterator;

/**
 * A collection of <code><b>static</b></code> functions for converting from one
 * aggregate type to another.
 *
 * @author Yossi Gil
 * @since Jul 8, 2014
 */
public enum As {
	// No values in an 'enum' which serves as a name space for a collection of
	// 'static' functions.
	;
	/**
	 * The string returned when 'conversion to string' is applied to a
	 * <code><b>null</b></code> value.
	 */
	public static final String NULL = "(null)";
	/**
	 * Converts a sequence of values into an array.
	 *
	 * @param <T> some arbitrary type
	 * @param $ some sequence of values of the type parameter
	 * @return the parameter, organized as an array with entries whose type is the
	 *         type parameter
	 */
	@SafeVarargs public static <T> T[] array(final T... $) {
		return $;
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
	 * @see As#bit(Object)
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
			$.add(Box.it(i));
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
		final int[] $ = new int[is.size()];
		for (int i = 0; i < $.length; i++)
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
	@SafeVarargs public static <T> PureIterable.Sized<T> iterable(final T... ts) {
		return new PureIterable.Sized<T>() {
			@Override public PureIterator<T> iterator() {
				return new PureIterator<T>() {
					int current = 0;
					@Override public boolean hasNext() {
						return current < ts.length;
					}
					@Override public @Nullable T next() {
						return ts[current++];
					}
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
	@SafeVarargs public static <T> PureIterator<T> iterator(final T... ts) {
		return iterable(ts).iterator();
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
		return addAll(new ArrayList<T>(), $);
	}
	/**
	 * Converts a sequence of objects of a given type into a {@link List} of
	 * values
	 *
	 * @param <T> type of objects to be converted
	 * @param $ what to covert
	 * @return the parameter, converted into a {@link List}
	 */
	@SafeVarargs public static <T> List<T> list(final T... $) {
		return addAll(new ArrayList<T>(), $);
	}
	/**
	 * Converts a sequence of objects of a given type into a {@link Set} of values
	 *
	 * @param <T> type of objects to be converted
	 * @param $ what to covert
	 * @return the parameter, converted into a {@link Set}
	 */
	@SafeVarargs public static <T> Set<? extends T> set(final T... $) {
		return add(new HashSet<T>(), $);
	}
	/**
	 * Converts a value, which can be either a <code><b>null</b></code> or
	 * references to valid instances, into a {@link NonNull}
	 *
	 * @param $ some value
	 * @return the parameter, after bing to a non-null string.
	 */
	public static String string(@Nullable final Object $) {
		return $ == null ? NULL : As.string($.toString());
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
	public static String[] strings(final Iterable<? extends Object> os) {
		final List<String> $ = new ArrayList<>();
		for (final Object o : os)
			if (o != null)
				$.add(o.toString());
		return $.toArray(new String[$.size()]);
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
	@SuppressWarnings({ "static-method", "javadoc", "boxing" })//
	public static class TEST {
		@Test public void asBitOfFalse() {
			assertEquals(0, As.bit(false));
		}
		@Test public void asBitOfTrue() {
			assertEquals(1, As.bit(true));
		}
		@Test public void asIntArraySimple() {
			final int[] is = As.intArray(100, 200, 200, 12, 13, 0);
			assertArrayEquals(is, As.intArray(As.ingeterList(is)));
		}
		@Test public void asListSimple() {
			final List<Integer> is = As.list(12, 13, 14);
			assertEquals(Box.it(12), is.get(0));
			assertEquals(Box.it(13), is.get(1));
			assertEquals(Box.it(14), is.get(2));
			assertEquals(3, is.size());
		}
		@Test public void stringOfNull() {
			assertEquals(NULL, As.string(null));
		}
		@Test public void stringWhenToStringReturnsNull() {
			assertEquals(NULL, As.string(new Object() {
				@Override public @Nullable String toString() {
					return null;
				}
			}));
		}
	}
}
