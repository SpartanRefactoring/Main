package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;

/** Unit tests for {@link InfixConcatenationEmptyStringLeft}
 * @author Niv Shalmon
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0116 extends TipperTest<InfixExpression> {
  @Override public Tipper<InfixExpression> tipper() {
    return new InfixConcatenationEmptyStringLeft();
  }

  @Override public Class<InfixExpression> tipsOn() {
    return InfixExpression.class;
  }

  @Test public void issue116_01() {
    trimminKof("\"\" + x")//
        .gives("x + \"\"")//
        .stays();
  }

  @Test public void issue116_02() {
    trimminKof("\"\" + x.foo()")//
        .gives("x.foo() + \"\"")//
        .stays();
  }

  @Test public void issue116_03() {
    trimminKof("\"\" + (Integer)(\"\" + x).length()")//
        .gives("(Integer)(\"\" + x).length() + \"\"")//
        .gives("(Integer)(x +\"\").length() + \"\"")//
        .stays();
  }

  @Test public void issue116_04() {
    trimminKof("String s = \"\" + x.foo();")//
        .gives("x.foo();")//
        .stays();
  }

  @Test public void issue116_07() {
    trimminKof("\"\" + 0 + (x - 7)")//
        .gives("0 + \"\" + (x - 7)")//
        .stays();
  }

  @Test public void issue116_08() {
    trimminKof("return x == null ? \"Use isEmpty()\" : \"Use \" + x + \".isEmpty()\";")
        .gives("return \"Use \" + (x == null ? \"isEmpty()\" : \"\" + x + \".isEmpty()\");")
        .gives("return \"Use \" + ((x == null ? \"\" : \"\" + x + \".\")+\"isEmpty()\");")
        .gives("return \"Use \" + (x == null ? \"\" : \"\" + x  + \".\")+\"isEmpty()\";")
        .gives("return \"Use \" + (x == null ? \"\" : x +\"\" + \".\")+\"isEmpty()\";")
        .gives("return \"Use \" + (x == null ? \"\" : x + \".\")+\"isEmpty()\";")//
        .stays();
  }

  @Test public void issue1245() {
    trimmingOf("\"\"+\"abc\"").stays();
  }
}
