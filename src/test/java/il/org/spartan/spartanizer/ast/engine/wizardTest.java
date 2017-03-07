/** TODO: Yossi Gil please add a description
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since Oct 7, 2016 */
package il.org.spartan.spartanizer.ast.engine;

import static il.org.spartan.spartanizer.engine.into.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "javadoc", "static-method" })
public final class wizardTest {
  @Test public void astExpression() {
    assert iz.expression(wizard.ast("x + y"));
  }

  @Test public void sameOfNullAndSomething() {
    assert !same(null, e("a"));
  }

  @Test @SuppressWarnings("RedundantCast") public void sameOfNulls() {
    assert same((ASTNode) null, (ASTNode) null);
  }

  @Test public void sameOfSomethingAndNull() {
    assert !same(e("a"), (Expression) null);
  }

  @Test public void sameOfTwoExpressionsIdentical() {
    assert same(e("a+b"), e("a+b"));
  }

  @Test public void sameOfTwoExpressionsNotSame() {
    assert !same(e("a+b+c"), e("a+b"));
  }
}
