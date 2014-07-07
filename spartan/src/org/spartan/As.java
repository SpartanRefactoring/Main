/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package org.spartan;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A collection of <code><b>static</b></code> functions whose most appropriate
 * 'sedoco' class is {@link As}. *
 * 
 * @author Yossi Gil
 * @since Jul 8, 2014
 * 
 */
public enum As {
	// No values in an 'enum' a name space for a collection of 'static' functions.
	;
	/**
	 * Converts a value, which can be either a <code><b>null</b></code> or
	 * references to valid instances, into a {@link NonNull}
	 * 
	 * @param $
	 *          some value
	 * @return the parameter, after bing to a non-null string.
	 */
	public static String string(@Nullable final Object $) {
		return $ == null ? NULL : string($.toString());
	}
	/**
	 * Converts a {@link String}, which can be either a <code><b>null</b></code>
	 * or an actual String, into a {@link NonNull} String.
	 * 
	 * @param $
	 *          some value
	 * @return the parameter, after bing to a non-null string.
	 */
	public static String string(@Nullable final String $) {
		return $ != null ? $ : NULL;
	}
	public static final String NULL = "(null)";
}
