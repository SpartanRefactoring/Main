/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.text;

import static il.org.spartan.Utils.*;
import static fluent.ly.azzert.*;

import org.junit.*;
import fluent.ly.*;

/** A trivial utility module with functions to scatter a text into an array of
 * lines, and, conversely, gather an array of lines into text. Scattering and
 * gathering obey the rule that a line is a {@link String} which does not
 * contain the end-of-line marker, while the text a strings in which all lines,
 * including the last, are terminated by the end-of-line marker.
 * @author Yossi Gil
 * @since 2014-7-31 */
public enum Lines {
  // No enum values in this fake module
  ;
  /** The string which this module considers as line separator. */
  public static final String END_OF_LINE_MARKER = "\n";
  /** A longer and more meaningful name for the array of length zero with
   * {@link String} elements. */
  public static final String[] NO_LINES = new String[0];

  /** Counts the number of liens in a given text
   * @param text count the number of lines in this parameter
   * @return number of lines in the parameter */
  public static int count(final String text) {
    return Lines.scatter(text).length;
  }
  /** Builds text from an array of lines
   * @param lines what needs to be concatenated
   * @return parameters, concatenated together, with {@link #END_OF_LINE_MARKER}
   *         separating consecutive arguments */
  public static String gather(final String... lines) {
    return separate.these(lines).by(END_OF_LINE_MARKER);
  }
  /** Breaks text into lines
   * @param text some string of characters
   * @return parameter, split into an array if lines
   * @see #gather */
  public static String[] scatter(final String text) {
    return text == null || text.isEmpty() ? NO_LINES : cantBeNull(text.split(END_OF_LINE_MARKER));
  }
  static void assertFalse(final String reason, final boolean b) {
    azzert.nay(reason, b);
  }

  /** A static nested class hosting unit tests for the nesting class Unit test
   * for the containing class. Note the naming convention: a) names of test
   * methods do not use are not prefixed by "test". This prefix is redundant. b)
   * test methods begin with the name of the method they check.
   * @author Yossi Gil
   * @since 2014-05-31 */
  @SuppressWarnings("static-method")
  public static class TEST {
    @Test public void countEmpty() {
      azzert.that(count(""), is(0));
    }
    @Test public void countNewLine() {
      azzert.that(count("\n"), is(0));
    }
    @Test public void countOneLine() {
      azzert.that(count("A"), is(1));
    }
    @Test public void countTwo() {
      azzert.that(count("A\nB\n"), is(2));
    }
    @Test public void countTwoVariant() {
      azzert.that(count("A\nB"), is(2));
    }
    @Test public void scatterSanity() {
      azzert.that(Lines.scatter("A").length, is(1));
    }
  }
}
