/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.text;
import static il.org.spartan.__.cantBeNull;
import static org.junit.Assert.assertEquals;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import il.org.spartan.Separate;

/**
 * A trivial utility module with functions to scatter a text into an array of
 * lines, and, conversely, gather an array of lines into text. Scattering and
 * gathering obey the rule that a line is a {@link String} which does not
 * contain the end-of-line marker, while the text a strings in which all lines,
 * including the last, are terminated by the end-of-line marker.
 *
 * @author Yossi Gil
 * @since 2014-7-31
 */
public enum Lines {
	// No enum values in this fake module
	;
	/**
	 * The string which this module considers as line separator.
	 */
	public static final String END_OF_LINE_MARKER = "\n";
	/**
	 * Breaks text into lines
	 *
	 * @param text some string of characters
	 * @return the parameter, split into an array if lines
	 * @see #gather
	 */
	public static String[] scatter(final @Nullable String text) {
		return text == null || text.isEmpty() ? NO_LINES : cantBeNull(text.split(END_OF_LINE_MARKER));
	}
	/**
	 * Builds text from an array of lines
	 *
	 * @param lines what needs to be concatenated
	 * @return the parameters, concatenated together, with
	 *         {@link #END_OF_LINE_MARKER} separating consecutive arguments
	 */
	public static String gather(final String... lines) {
		return Separate.these(lines).by(END_OF_LINE_MARKER);
	}
	/**
	 * Counts the number of liens in a given text
	 *
	 * @param text count the number of lines in this parameter
	 * @return the number of lines in the parameter
	 */
	public static int count(final @Nullable String text) {
		return Lines.scatter(text).length;
	}
	/**
	 * A longer and more meaningful name for the array of length zero with
	 * {@link String} elements.
	 */
	public static final String[] NO_LINES = new String[0];
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
		@Test public void scatterSanity() {
			assertEquals(1, Lines.scatter("A").length);
		}
		@Test public void countEmpty() {
			assertEquals(0, count(""));
		}
		@Test public void countNewLine() {
			assertEquals(0, count("\n"));
		}
		@Test public void countOneLine() {
			assertEquals(1, count("A"));
		}
		@Test public void countTwo() {
			assertEquals(2, count("A\nB\n"));
		}
		@Test public void countTwoVariant() {
			assertEquals(2, count("A\nB"));
		}
	}
}
