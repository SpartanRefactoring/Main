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

  @SuppressWarnings("static-method") @Test public void zeroLengthCheckForOfMethod(){
    assertNull(NameGuess.of(""));
  }
  
  @SuppressWarnings("static-method") @Test public void underScoresCheckForOfMethod(){
    assertEquals(NameGuess.of("_"), NameGuess.ANONYMOUS);
    assertEquals(NameGuess.of("__"), NameGuess.ANONYMOUS);
  }
  
  @SuppressWarnings("static-method") @Test public void dollarCheckForOfMethod(){
    assertEquals(NameGuess.of("$"), NameGuess.DOLLAR);
    assertEquals(NameGuess.of("$$"), NameGuess.DOLLAR);
    assertNotEquals(NameGuess.of(""), NameGuess.DOLLAR);
  }
  
  @SuppressWarnings("static-method") @Test public void centCheckForOfMethod(){
    assertEquals(NameGuess.of("¢"), NameGuess.CENT);
    assertEquals(NameGuess.of("¢¢"), NameGuess.CENT);
    assertNotEquals(NameGuess.of(""), NameGuess.CENT);
  }
  
  
}
