package il.org.spartan.spartanizer.java;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

public class associativityTest {
  @SuppressWarnings("static-method") @Test public void test0() {
    assertTrue(!associativity.isLeftToRight(az.infixExpression(wizard.ast("5+4")).getOperator()));
  }

  @SuppressWarnings("static-method") @Test public void test1() {
    assertTrue(!associativity.isRightToLeft(az.expression(wizard.ast("(7-4)+2"))));
  }

  @SuppressWarnings("static-method") @Test public void test2() {
    assertTrue(!associativity.isRightToLeft(az.expression(wizard.ast("7-(4+2)"))));
  }

  @SuppressWarnings("static-method") @Test public void test3() {
    assertTrue(associativity.isRightToLeft(az.expression(wizard.ast("!q()"))));
  }
}
