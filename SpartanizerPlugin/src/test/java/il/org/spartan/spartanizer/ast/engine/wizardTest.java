package il.org.spartan.spartanizer.ast.engine;

import static il.org.spartan.spartanizer.engine.into.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "javadoc", "static-method" })
public final class wizardTest {
  @Test public void sameOfNullAndSomething() {
    assert !wizard.same(null, e("a"));
  }

  @Test public void sameOfNulls() {
    assert wizard.same((ASTNode) null, (ASTNode) null);
  }

  @Test public void sameOfSomethingAndNull() {
    assert !wizard.same(e("a"), (Expression) null);
  }

  @Test public void sameOfTwoExpressionsIdentical() {
    assert wizard.same(e("a+b"), e("a+b"));
  }

  @Test public void sameOfTwoExpressionsNotSame() {
    assert !wizard.same(e("a+b+c"), e("a+b"));
  }

  @Test public void astExpression() {
    assert iz.expression(wizard.ast("x + y"));
  }
}
