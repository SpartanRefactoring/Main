package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author David Cohen
 * @author Shahar Yair
 * @author Zahi Mizrahi
 * @since 16-11-9 **/
public class Issue808 {
  @Test @SuppressWarnings("static-method") public void test01() {
    final Expression ex = az.numberLiteral(wizard.ast("5"));
    assertNotEquals(ex, new Term(true, duplicate.of(ex)).asExpression());
  }

  @Test @SuppressWarnings("static-method") public void test02() {
    final Expression ex = az.simpleName(wizard.ast("shahar"));
    azzert.that(new Term(false, ex).asExpression(), is(ex));
  }

  @Test @SuppressWarnings("static-method") public void test03() {
    final Expression ex1 = az.simpleName(wizard.ast("shahar"));
    final Expression ex2 = az.simpleName(wizard.ast("david"));
    final Expression ex3 = az.simpleName(wizard.ast("zahi"));
    azzert.that(new Term(false, ex1).asExpression(), is(ex1));
    assertNotEquals(ex2, new Term(true, duplicate.of(ex2)).asExpression());
    assertNotEquals(ex3, new Term(true, duplicate.of(ex3)).asExpression());
  }
}