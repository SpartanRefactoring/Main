/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package org.spartan;

import org.eclipse.jdt.annotation.Nullable;

/**
 * A collection of <code><b>static</b></code> functions with no appropriate
 * 'sedoco' class name was found.
 * 
 * @author Yossi Gil
 * @since Jul 7, 2014
 * 
 */
public enum _ {
	// No values in an 'enum' used as name space for a collection of 'static'
	// functions.
	;
	/**
	 * This function is used to remove the @Nullable annotation placed on the type
	 * of a value. It is used to make <code><b>null</b></code<]> checkers happy.
	 * <p>
	 * This function takes an instance of an arbitrary type, T (with the hidden assumption
	 * that but such that a @Nullable annotation is present on T). Thus, the parameter may be either 
	 * <code><b>null</b></code<]>, or an actual instance of T.
	 * <p>
	 * The function returns the same instance it received as a parameter, except that
	 * this instance is returned as an instance of the type T <i>without</i> the @Nullable 
	 * annotation. Execution is aborted with an {@link AssertionError} if the parameter is null.
	 * <p>
	 * As it turns out, this function is a (slow) logical no-op, but still applicable to 
	 * arguments of type T, where T does not have the @Nullable annotation present on it.
	 * 
	 * @param <T>
	 *          some arbitrary type
	 * @param $
	 *          an instance of the type parameter
	 * @return its parameter, after verifying that it is not
	 *         <code><b>null</b></code<]>
	 */
	public static <T> T nonNull(final @Nullable T $) {
		assert $ != null;
		return $;
	}
	/**
	 * Computes the maximum of two or more integers.
	 * 
	 * @param a
	 *          some integer
	 * @param is
	 *          additional
	 * 
	 * @return the largest of the parameters
	 */
	public static int max(final int a, final int... is) {
		int $ = a;
		for (int i : is)
			$ = Math.max($, i);
		return $;
	}
	/**
	 * Computes the minimum of two or more integers
	 * 
	 * @param a
	 *          some integer
	 * @param is
	 *          additional
	 * 
	 * @return the smallest of the parameters
	 */
	public static int min(final int a, final int... is) {
		int $ = a;
		for (int i : is)
			$ = Math.min($, i);
		return $;
	}
	/**
	 * Determine whether a <code><b>null</b></code> is found in a sequence of
	 * objects
	 * 
	 * @param os
	 *          an unknown number of objects
	 * @return <code><b>null</b></code> <i>iff</i>one of the parameters is
	 *         <code><b>null</b></code>
	 */
	public static boolean hasNull(final Object... os) {
		for (final Object o : os)
			if (o == null)
				return true;
		return false;
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
}
