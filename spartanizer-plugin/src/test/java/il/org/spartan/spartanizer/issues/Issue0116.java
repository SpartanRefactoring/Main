package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.junit.Test;

import il.org.spartan.spartanizer.testing.TipperTest;
import il.org.spartan.spartanizer.tippers.InfixConcatenationEmptyStringLeft;
import il.org.spartan.spartanizer.tipping.Tipper;

/** Unit tests for {@link InfixConcatenationEmptyStringLeft}
 * @author Niv Shalmon
 * @since 2016 */
@SuppressWarnings("javadoc")
public final class Issue0116 extends TipperTest<InfixExpression> {
  @Override public Tipper<InfixExpression> tipper() {
    return new InfixConcatenationEmptyStringLeft();
  }
  @Override public Class<InfixExpression> tipsOn() {
    return InfixExpression.class;
  }
  @Test public void issue116_01() {
    trimmingOf("\"\" + x")//
        .gives("x + \"\"")//
        .stays();
  }
  @Test public void issue116_02() {
    trimmingOf("\"\" + x.foo()")//
        .gives("x.foo() + \"\"")//
        .stays();
  }
  @Test public void issue116_03() {
    trimmingOf("\"\" + (Integer)(\"\" + x).length()")//
        .gives("(Integer)(\"\" + x).length() + \"\"")//
        .gives("(Integer)(x +\"\").length() + \"\"")//
        .stays();
  }
  @Test public void issue116_04() {
    trimmingOf("String s = \"\" + x.foo();")//
        .gives("String s=x.foo()+\"\";") //
        .stays();
  }
  @Test public void issue116_07() {
    trimmingOf("\"\" + 0 + (x - 7)")//
        .gives("0 + \"\" + (x - 7)")//
        .stays();
  }
  @Test public void issue116_08() {
    trimmingOf("return \"Use \" + (x == null ? \"\" : \"\" + x  + \".\")+\"isEmpty()\";")
        .gives("return \"Use \" + (x == null ? \"\" : x +\"\" + \".\")+\"isEmpty()\";");
  }
  @Test public void issue1245() {
    trimmingOf("\"\"+\"abc\"").stays();
  }
}
