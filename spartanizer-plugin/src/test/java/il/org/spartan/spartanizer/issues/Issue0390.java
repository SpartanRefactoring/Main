package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.is;

import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.engine.nominal.guessName;

/** Tests for the GitHub issue thus numbered
 * @author Alex Kopzon
 * @since 2016 */
@SuppressWarnings("static-method")
public class Issue0390 {
  @Test public void demoOfAzzert() {
    azzert.that(guessName.of("__"), is(guessName.ANONYMOUS));
  }
}
