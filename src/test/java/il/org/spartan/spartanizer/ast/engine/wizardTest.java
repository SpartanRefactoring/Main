/* TODO: Yossi Gil LocalVariableInitializedStatement description
 *
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 *
 * @since Oct 7, 2016 */
package il.org.spartan.spartanizer.ast.engine;

import static il.org.spartan.spartanizer.engine.into.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "javadoc", "static-method" })
public final class wizardTest {
  @Test public void astExpression() {
    assert iz.expression(make.ast("x + y"));
  }

  @Test public void sameOfNullAndSomething() {
    assert !eq(null, e("a"));
  }

  @Test @SuppressWarnings("RedundantCast") public void sameOfNulls() {
    assert eq((ASTNode) null, (ASTNode) null);
  }

  @Test public void sameOfSomethingAndNull() {
    assert !eq(e("a"), (Expression) null);
  }

  @Test public void sameOfTwoExpressionsIdentical() {
    assert eq(e("a+b"), e("a+b"));
  }

  @Test public void sameOfTwoExpressionsNotSame() {
    assert !eq(e("a+b+c"), e("a+b"));
  }
}
