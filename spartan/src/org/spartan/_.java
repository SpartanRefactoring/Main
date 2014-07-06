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
	// No values in an 'enum' used for faking a module which is just
	// a collection of 'static' functions.
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
	 *          an instance of this type
	 * @return its parameter, after verifying that it is not
	 *         <code><b>null</b></code<]>
	 */
	public static <T> T nonNull(final @Nullable T $) {
		assert $ != null;
		return $;
	}
}
