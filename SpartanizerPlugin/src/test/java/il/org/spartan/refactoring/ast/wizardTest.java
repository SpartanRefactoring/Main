package il.org.spartan.refactoring.ast;

import static il.org.spartan.refactoring.engine.into.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "static-method" }) //
public class wizardTest {
  @Test public void sameOfNullAndSomething() {
    azzert.nay(wizard.same(null, e("a")));
  }

  @Test public void sameOfNulls() {
    azzert.aye(wizard.same((ASTNode) null, (ASTNode) null));
  }

  @Test public void sameOfSomethingAndNull() {
    azzert.nay(wizard.same(e("a"), (Expression) null));
  }

  @Test public void sameOfTwoExpressionsIdentical() {
    azzert.aye(wizard.same(e("a+b"), e("a+b")));
  }

  @Test public void sameOfTwoExpressionsNotSame() {
    azzert.nay(wizard.same(e("a+b+c"), e("a+b")));
  }
}
