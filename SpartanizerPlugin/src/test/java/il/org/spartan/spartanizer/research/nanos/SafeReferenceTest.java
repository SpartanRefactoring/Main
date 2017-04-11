package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link SafeReference and Infix.SafeReference}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-03 */
@SuppressWarnings("static-method")
public class SafeReferenceTest {
  @Test public void field() {
    topDownTrimming("return x !=null ? x.y : null;")//
        .using(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).get(()->x.y);")//
        .stays();
  }

  @Test public void field2() {
    topDownTrimming("return x ==null ? null : x.field;")//
        .using(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).get(()->x.field);")//
        .stays();
  }

  @Test public void field3() {
    topDownTrimming("return x == null ? null : x.y.z;")//
        .using(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).get(()->x.y.z);")//
        .stays();
  }

  @Test public void field4() {
    topDownTrimming("return x != null && x.y;")//
        .using(InfixExpression.class, new Infix.SafeNavigation())//
        .gives("return safe(x).get(()->x.y);")//
        .stays();
  }

  @Test public void field5() {
    topDownTrimming("return x != null && x.y.z.w;")//
        .using(InfixExpression.class, new Infix.SafeNavigation())//
        .gives("return safe(x).get(()->x.y.z.w);")//
        .stays();
  }

  @Test public void field6() {
    topDownTrimming("return x == null ? 0 : x.y.z;")//
        .using(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).get(()->x.y.z);")//
        .stays();
  }

  @Test public void method() {
    topDownTrimming("return x == null ? null : x.y();")//
        .using(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).invoke(()->x.y());")//
        .stays();
  }

  @Test public void method2() {
    topDownTrimming("return x == null ? null : x.y.z();")//
        .using(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).invoke(()->x.y.z());")//
        .stays();
  }

  @Test public void method3() {
    topDownTrimming("return x == null ? 0 : x.z();")//
        .using(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).invoke(()->x.z());")//
        .stays();
  }

  @Test public void method4() {
    topDownTrimming("return x == null ? 0 : x.y.z();")//
        .using(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).invoke(()->x.y.z());")//
        .stays();
  }

  @Test public void method5() {
    topDownTrimming("return x != null && x.z();")//
        .using(InfixExpression.class, new Infix.SafeNavigation())//
        .gives("return safe(x).invoke(()->x.z());")//
        .stays();
  }

  @Test public void method6() {
    topDownTrimming("return x != null && x.y.z();")//
        .using(InfixExpression.class, new Infix.SafeNavigation())//
        .gives("return safe(x).invoke(()->x.y.z());")//
        .stays();
  }

  @Test public void method7() {
    topDownTrimming("(x != null) && x.y()")//
        .using(InfixExpression.class, new Infix.SafeNavigation())//
        .gives("safe(x).invoke(()->x.y())")//
        .stays();
  }

  @Test public void respect() {
    topDownTrimming("return x ==null ? null : x.field;")//
        .using(ConditionalExpression.class, //
            new Unless(), //
            new DefaultsTo(), //
            new SafeReference())//
        .gives("return safe(x).get(()->x.field);")//
        .using(ConditionalExpression.class, //
            new Unless(), //
            new DefaultsTo(), //
            new SafeReference())//
        .stays();
  }
}
