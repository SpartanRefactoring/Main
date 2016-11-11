package il.org.spartan.spartanizer.engine;

import static org.junit.Assert.*;

import org.junit.*;


/** see issue #815 and #799 for more details
 * @author Oren Afek
 * @author Amir Sagiv
 * @since 16-11-11 */
public class Issue815 {
  @SuppressWarnings("static-method") @Test public void nullCheckForOfMethod(){
    assertNull(NameGuess.of(null));
  }
}
