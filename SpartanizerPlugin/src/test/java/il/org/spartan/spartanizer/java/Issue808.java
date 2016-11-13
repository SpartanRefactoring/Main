package il.org.spartan.spartanizer.java;

import static org.junit.Assert.*;
import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author David Cohen
 * @author Shahar Yair
 * @author Zahi Mizrahi
 * @since 16-11-9 **/
public class Issue808 {
  @SuppressWarnings("static-method") @Test public void test01() {
    Expression ex = az.numberLiteral(wizard.ast("5"));
    assertNotEquals(ex, (new Term(true, duplicate.of(ex))).asExpression());
  }
  @SuppressWarnings("static-method") @Test public void test02() {
    Expression ex = az.simpleName(wizard.ast("shahar"));
    assertEquals(ex, (new Term(false, ex)).asExpression());
  }
  @SuppressWarnings("static-method") @Test public void test03() {
    Expression ex1 = az.simpleName(wizard.ast("shahar"));
    Expression ex2 = az.simpleName(wizard.ast("david"));
    Expression ex3 = az.simpleName(wizard.ast("zahi"));
    assertEquals(ex1, (new Term(false, ex1)).asExpression());
    assertNotEquals(ex2, (new Term(true, duplicate.of(ex2))).asExpression());
    assertNotEquals(ex3, (new Term(true, duplicate.of(ex3))).asExpression());
  }
}