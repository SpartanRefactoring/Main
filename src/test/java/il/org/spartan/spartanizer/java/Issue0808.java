package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** TODO: David Cohen please add a description
 * @author David Cohen
 * @author Shahar Yair
 * @author Zahi Mizrahi
 * @since 16-11-9 * */
public class Issue0808 {
  @Test @SuppressWarnings("static-method") public void test01() {
    final Expression ex = az.numberLiteral(make.ast("5"));
    assertNotEquals(ex, new Term(true, copy.of(ex)).asExpression());
  }

  @Test @SuppressWarnings("static-method") public void test02() {
    final Expression ex = az.simpleName(make.ast("shahar"));
    azzert.that(new Term(false, ex).asExpression(), is(ex));
  }

  @Test @SuppressWarnings("static-method") public void test03() {
    final Expression ex1 = az.simpleName(make.ast("shahar")), ex2 = az.simpleName(make.ast("david")), ex3 = az.simpleName(make.ast("zahi"));
    azzert.that(new Term(false, ex1).asExpression(), is(ex1));
    assertNotEquals(ex2, new Term(true, copy.of(ex2)).asExpression());
    assertNotEquals(ex3, new Term(true, copy.of(ex3)).asExpression());
  }
}
