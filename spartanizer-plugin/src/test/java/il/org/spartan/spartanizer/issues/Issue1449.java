package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.junit.Test;

import il.org.spartan.spartanizer.testing.TipperTest;
import il.org.spartan.spartanizer.tippers.InfixMultiplicationSort;
import il.org.spartan.spartanizer.tipping.Tipper;

/** Tests for thus numbered github issue
 * @author Niv Shalmon
 * @since 2017-06-12 */
public class Issue1449 extends TipperTest<InfixExpression> {
  @Override public Tipper<InfixExpression> tipper() {
    return new InfixMultiplicationSort();
  }
  @Override public Class<InfixExpression> tipsOn() {
    return InfixExpression.class;
  }
  @Test public void test00() {
    trimmingOf("(is++) * (--is)").stays();
  }
  @Test public void test01() {
    trimmingOf("f(i++)*j").stays();
  }
  @Test public void test02() {
    trimmingOf("f(i)*j").stays();
  }
  @Test public void test03() {
    trimmingOf("(5-(x++))*j").stays();
  }
  @Test public void test04() {
    trimmingOf("f()*5").gives("5*f()").stays();
  }
}
