package il.org.spartan.spartanizer.java;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Dor Ma'yan
 * @author Sapir Bismot
 * @since 16-11-9 **/
@SuppressWarnings("static-method")
public class associativityTest {
  @Test public void test0() {
    assertTrue(!associativity.isLeftToRight(az.infixExpression(wizard.ast("5+4")).getOperator()));
  }

  @Test public void test1() {
    assertTrue(!associativity.isRightToLeft(az.expression(wizard.ast("(7-4)+2"))));
  }

  @Test public void test2() {
    assertTrue(!associativity.isRightToLeft(az.expression(wizard.ast("7-(4+2)"))));
  }

  @Test public void test3() {
    assertTrue(associativity.isRightToLeft(az.expression(wizard.ast("!q()"))));
  }

  @Test public void test4() {
    assertTrue(associativity.isRightToLeft(az.expression(wizard.ast("x=y=z=17"))));
  }

  @Test public void test5() {
    assertTrue(associativity.isRightToLeft(az.expression(wizard.ast("++x"))));
  }

  @Test public void test6() {
    assertTrue(!associativity.isRightToLeft(az.expression(wizard.ast("x++"))));
  }

  @Test public void test7() {
    assertTrue(!associativity.isRightToLeft(az.expression(wizard.ast("x && y"))));
  }
}
