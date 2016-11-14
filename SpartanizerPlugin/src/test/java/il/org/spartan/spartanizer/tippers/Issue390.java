package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

/** @author Alex Kopzon
 * @since 2016 */
@SuppressWarnings("static-method")
public class Issue390 {
  @Test public void demoOfAzzert() {
    azzert.that(NameGuess.of("__"), is(NameGuess.ANONYMOUS));
  }
}
