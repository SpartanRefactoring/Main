/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package org.spartan;

import static org.junit.Assert.assertEquals;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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
	/**
	 * Converts a boolean into a bit value
	 * 
	 * @param $
	 *          some boolean value
	 * @return 1 if the parameter is true, 0 otherwise
	 */
	public static int bit(final boolean b) {
		return b ? 1 : 0;
	}
	/**
	 * Converts a sequence of literals into an array.
	 * 
	 * @param <T>
	 *          some arbitrary type
	 * @param $
	 *          some sequence of values of the type parameter
	 * @return the parameters, organized as an array with entries whose type is
	 *         the type parameter
	 */
	@SafeVarargs public static <T> T[] array(final T... $) {
		return $;
	}
	/**
	 * The string returned when 'conversion to string' is applied to a <code><b>null</b></code> value. 
	 */
	public static final String NULL = "(null)";
	/**
	 * A static nested class hosting unit tests for the nesting class Unit test for
	 * the containing class. Note the naming convention: a) names of test methods do not use
	 * are not prefixed by "test". This prefix is redundant. b) test methods begin with the name of the
	 * method they check.
	 * 
	 * @author Yossi Gil
	 * @since 2014-05-31
	 */
	@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
	@SuppressWarnings("static-method")//
	public static class TEST {
    @Test public void asBitOfFalse() {
      assertEquals(0, As.bit(false));
    }

    @Test public void asBitOfTrue() {
      assertEquals(1, As.bit(true));
    }
	}
}
