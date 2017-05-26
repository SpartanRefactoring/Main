package il.org.spartan.spartanizer.java;

import org.junit.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Checking the associativity function
 * @author Dor Ma'yan
 * @author Sapir Bismot
 * @since 16-11-9 * */
@SuppressWarnings("static-method")
public class associativityTest {
  @Test public void test0() {
    assert !associativity.isLeftToRight(az.infixExpression(make.ast("5+4")).getOperator());
  }
  @Test public void test1() {
    assert !associativity.isRightToLeft(az.expression(make.ast("(7-4)+2")));
  }
  @Test public void test10() {
    assert !associativity.isLeftToRight(az.expression(make.ast("(int)x")));
  }
  @Test public void test11() {
    assert associativity.isLeftToRight(az.expression(make.ast("arr[9]")));
  }
  @Test public void test2() {
    assert !associativity.isRightToLeft(az.expression(make.ast("7-(4+2)")));
  }
  @Test public void test3() {
    assert associativity.isRightToLeft(az.expression(make.ast("!q()")));
  }
  @Test public void test4() {
    assert associativity.isRightToLeft(az.expression(make.ast("x=y=z=17")));
  }
  @Test public void test5() {
    assert associativity.isRightToLeft(az.expression(make.ast("++x")));
  }
  @Test public void test6() {
    assert !associativity.isRightToLeft(az.expression(make.ast("x && y")));
  }
  @Test public void test7() {
    assert associativity.isLeftToRight(az.expression(make.ast("x && !y")));
  }
  @Test public void test8() {
    assert !associativity.isLeftToRight(az.infixExpression(make.ast("a&&b")).getOperator());
  }
  @Test public void test9() {
    assert !associativity.isLeftToRight(az.expression(make.ast("x = y == 8 ? 9 : 6")));
  }
}
