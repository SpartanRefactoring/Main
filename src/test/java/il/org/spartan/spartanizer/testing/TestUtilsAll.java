package il.org.spartan.spartanizer.testing;

import static il.org.spartan.azzert.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-07-17 */
@SuppressWarnings("javadoc")
public enum TestUtilsAll {
  ;
  /** A test to check that the actual output is similar to the actual value.
   * @param expected JD
   * @param actual JD */
  public static void assertSimilar(final String expected, final String actual) {
    if (!expected.equals(actual))
      azzert.that(trivia.essence(actual), is(trivia.essence(expected)));
  }
}
