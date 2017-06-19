package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Niv Shalmon
 * @since 2017-06-19 */
public class Issue1163 extends TipperTest<ConditionalExpression> {
  @Override public Tipper<ConditionalExpression> tipper() {
    return new TernaryBooleanLiteral();
  }
  @Override public Class<ConditionalExpression> tipsOn() {
    return ConditionalExpression.class;
  }
  @Test public void test01() {
    trimmingOf("return (from == null) ? true : null;")//
        .stays();
  }
  @Test public void test02() {
    trimmingOf("return (from == null) ? true : f();")//
        .gives("return from == null || f();")//
        .stays();
  }
  @Test public void test03() {
    trimmingOf("return (from == null) ? (Boolean)null : true;")//
        .stays();
  }
}
