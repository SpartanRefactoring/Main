package il.org.spartan.spartanizer.leonidas;

/** @author Ori Marcovitch
 * @year 2016 */
import static org.junit.Assert.*;
import org.junit.*;

public class LeonidasTest {
  @SuppressWarnings("static-method") @Test public void testMatches1() {
    azzert.that("$X1 ? y == 17 : $X2").matches("x == 7 ? y == 17 : 9");
  }

  @SuppressWarnings("static-method") @Test public void testMatches2() {
    azzert.that("$X1 ? 8 : $X2").notmatches("x == 7 ? y == 17 : 9");
  }

  @SuppressWarnings("static-method") @Test public void testMatches3() {
    azzert.that("w = $X ? y == 17 : $X2;").matches("w = x == 7 ? y == 17 : 9;");
  }

  @SuppressWarnings("static-method") @Test public void testMatches4() {
    azzert.that("w = $X1 ? 8 : $X2;").notmatches("w = x == 7 ? y == 17 : 9;");
  }

  @SuppressWarnings("static-method") @Test public void testMatches5() {
    azzert.that("x == $X1 ? $X2 : $X1").matches("x == null ? 17 : null");
  }

  @SuppressWarnings("static-method") @Test public void testMatches6() {
    azzert.that("x == $X1 ? $X2 : $X1").notmatches("x == null ? 17 : 18");
  }

  @SuppressWarnings("static-method") @Test public void testMatches7() {
    azzert.that("x == $X ? $X : $X").notmatches("x == null ? 17 : null");
  }

  @SuppressWarnings("static-method") @Test public void testMatches8() {
    azzert.that("$X ? y == 17 : $M").matches("x == 7 ? y == 17 : x()");
  }
}

class azzert {
  public static that that(String ¢) {
    return new that(¢);
  }
}

class that {
  final String s;

  public that(String s) {
    this.s = s;
  }

  public void matches(String s2) {
    assertTrue(TipperFactory.matches(TipperFactory.toAST(this.s), TipperFactory.toAST(s2)));
  }

  public void notmatches(String s2) {
    assertFalse(TipperFactory.matches(TipperFactory.toAST(this.s), TipperFactory.toAST(s2)));
  }
}
