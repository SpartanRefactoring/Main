/* TODO Yossi Gil Document Class
 *
 * @author Yossi Gil
 *
 * @since Oct 7, 2016 */
package il.org.spartan.spartanizer.ast.engine;

import static il.org.spartan.spartanizer.ast.navigate.wizard.eq;
import static il.org.spartan.spartanizer.engine.parse.e;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.junit.Test;

import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.safety.iz;

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
