package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** Tests for the GitHub issue thus numbered
 * @author Alex Kopzon
 * @since 2016 */
@SuppressWarnings("static-method")
public class Issue0390 {
  @Test public void demoOfAzzert() {
    azzert.that(guessName.of("__"), is(guessName.ANONYMOUS));
  }
}
