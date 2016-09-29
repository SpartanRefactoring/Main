package il.org.spartan.spartanizer.leonidas;

/** @author Ori Marcovitch
 * @year 2016 */
import static org.junit.Assert.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.*;

public class LeonidasTest {
  @SuppressWarnings("static-method") @Test public void testMatches1() {
    azzert.that("$X ? y == 17 : $X2").matches("x == 7 ? y == 17 : 9");
  }

  @SuppressWarnings("static-method") @Test public void testMatches2() {
    azzert.that("$X ? 8 : $X2").notmatches("x == 7 ? y == 17 : 9");
  }

  @SuppressWarnings("static-method") @Test public void testMatches3() {
    azzert.that("w = $X ? y == 17 : $X2;").matches("w = x == 7 ? y == 17 : 9;");
  }

  @SuppressWarnings("static-method") @Test public void testMatches4() {
    azzert.that("w = $X ? 8 : $X2;").notmatches("w = x == 7 ? y == 17 : 9;");
  }

  @SuppressWarnings("static-method") @Test public void testMatches5() {
    azzert.that("x == $X ? $X2 : $X").matches("x == null ? 17 : null");
  }

  @SuppressWarnings("static-method") @Test public void testMatches6() {
    azzert.that("x == $X ? $X2 : $X").notmatches("x == null ? 17 : 18");
  }

  @SuppressWarnings("static-method") @Test public void testMatches7() {
    azzert.that("x == $X ? $X : $X").notmatches("x == null ? 17 : null");
  }

  @SuppressWarnings("static-method") @Test public void testMatches8() {
    azzert.that("$X ? y == 17 : $M").matches("x == 7 ? y == 17 : foo()");
  }
}

class azzert {
  public static expression that(final String ¢) {
    return new expression(¢);
  }
}

class expression {
  final String s;

  public expression(final String s) {
    this.s = s;
  }

  public void matches(final String s2) {
    assertTrue(Matcher.matches(wizard.AST(s), wizard.AST(s2)));
  }

  public void notmatches(final String s2) {
    assertFalse(Matcher.matches(wizard.AST(s), wizard.AST(s2)));
  }
}
