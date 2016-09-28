package il.org.spartan.spartanizer.leonidas;

/** @author Ori Marcovitch
 * @year 2016 */
import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.spartanizer.engine.*;

public class LeonidasTest {
  @SuppressWarnings("static-method") @Test public void testMatches1() {
    azzert.expression("X ? y == 17 : Z").matches("x == 7 ? y == 17 : 9");
  }

  @SuppressWarnings("static-method") @Test public void testMatches2() {
    azzert.expression("X ? 8 : Z").notmatches("x == 7 ? y == 17 : 9");
  }

  @SuppressWarnings("static-method") @Test public void testMatches3() {
    azzert.statement("X ? y == 17 : Z;").matches("x == 7 ? y == 17 : 9;");
  }

  @SuppressWarnings("static-method") @Test public void testMatches4() {
    azzert.statement("w = X ? 8 : Z;").notmatches("w = x == 7 ? y == 17 : 9;");
  }

  @SuppressWarnings("static-method") @Test public void testMatches5() {
    azzert.expression("x == Y ? X : Y").matches("x == null ? 17 : null");
  }

  @SuppressWarnings("static-method") @Test public void testMatches6() {
    azzert.expression("x == Y ? X : Y").notmatches("x == null ? 17 : 18");
  }

  @SuppressWarnings("static-method") @Test public void testMatches7() {
    azzert.expression("x == Y ? Y : Y").notmatches("x == null ? 17 : null");
  }

  @SuppressWarnings("static-method") @Test public void testMatches8() {
    azzert.expression("$X ? y == 17 : $M").matches("x == 7 ? y == 17 : 9");
  }
}

class azzert {
  public static expression expression(String ¢) {
    return new expression(¢);
  }

  public static statement statement(String ¢) {
    return new statement(¢);
  }
}

class expression {
  final String s;

  public expression(String s) {
    this.s = s;
  }

  public void matches(String s2) {
    assertTrue(Pattern.matches(makeAST.EXPRESSION.from(this.s), makeAST.EXPRESSION.from(s2)));
  }

  public void notmatches(String s2) {
    assertFalse(Pattern.matches(makeAST.EXPRESSION.from(this.s), makeAST.EXPRESSION.from(s2)));
  }
}

class statement {
  final String s;

  public statement(String s) {
    this.s = s;
  }

  public void matches(String s2) {
    assertTrue(Pattern.matches(makeAST.STATEMENTS.from(this.s), makeAST.STATEMENTS.from(s2)));
  }

  public void notmatches(String s2) {
    assertFalse(Pattern.matches(makeAST.STATEMENTS.from(this.s), makeAST.STATEMENTS.from(s2)));
  }
}
