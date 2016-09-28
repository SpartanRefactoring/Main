package il.org.spartan.spartanizer.leonidas;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.spartanizer.engine.*;

public class LeonidasTest {
  @Test public void testMatches() {
    Pattern.getChildren(into.e("x == 7 ? y == 17 : 9"));
    azzert.expression("X ? y == 17 : Z").matches("x == 7 ? y == 17 : 9");
    azzert.expression("X ? 8 : Z").notmatches("x == 7 ? y == 17 : 9");
    azzert.statement("X ? y == 17 : Z;").matches("x == 7 ? y == 17 : 9;");
    azzert.statement("w = X ? 8 : Z;").notmatches("w = x == 7 ? y == 17 : 9;");
    azzert.expression("x == Y ? X : Y").matches("x == null ? 17 : null");
    azzert.expression("x == Y ? X : Y").notmatches("x == null ? 17 : 18");
    azzert.expression("x == Y ? Y : Y").notmatches("x == null ? 17 : null");

  }
}

class azzert {
  public static expression expression(String s) {
    return new expression(s);
  }

  public static statement statement(String s) {
    return new statement(s);
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
