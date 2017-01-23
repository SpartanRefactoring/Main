package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** TODO:  Alex Kopzon
 please add a description 
 @author Alex Kopzon
 * @since 2016 
 */

@SuppressWarnings("static-method")
public class Issue0390 {
  @Test public void demoOfAzzert() {
    azzert.that(NameGuess.of("__"), is(NameGuess.ANONYMOUS));
  }
}

