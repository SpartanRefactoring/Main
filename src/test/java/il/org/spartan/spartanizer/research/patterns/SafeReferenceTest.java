package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-03 */
@SuppressWarnings("static-method")
public class SafeReferenceTest {
  @Test public void field() {
    trimmingOf("return x !=null ? x.y : null;")//
        .withTipper(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).get(()->x.y);")//
        .stays();
  }

  @Test public void field2() {
    trimmingOf("return x ==null ? null : x.field;")//
        .withTipper(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).get(()->x.field);")//
        .stays();
  }

  @Test public void field3() {
    trimmingOf("return x == null ? null : x.y.z;")//
        .withTipper(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).get(()->x.y.z);")//
        .stays();
  }

  @Test public void field4() {
    trimmingOf("return x != null && x.y;")//
        .withTipper(InfixExpression.class, new Infix.SafeReference())//
        .gives("return safe(x).get(()->x.y);")//
        .stays();
  }

  @Test public void field5() {
    trimmingOf("return x != null && x.y.z.w;")//
        .withTipper(InfixExpression.class, new Infix.SafeReference())//
        .gives("return safe(x).get(()->x.y.z.w);")//
        .stays();
  }

  @Test public void field6() {
    trimmingOf("return x == null ? 0 : x.y.z;")//
        .withTipper(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).get(()->x.y.z);")//
        .stays();
  }

  @Test public void respect() {
    trimmingOf("return x ==null ? null : x.field;")//
        .withTippers(ConditionalExpression.class, //
            new Unless(), //
            new DefaultsTo(), //
            new SafeReference())//
        .gives("return safe(x).get(()->x.field);")//
        .withTippers(ConditionalExpression.class, //
            new Unless(), //
            new DefaultsTo(), //
            new SafeReference())//
        .stays();
  }

  @Test public void method() {
    trimmingOf("return x == null ? null : x.y();")//
        .withTipper(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).invoke(()->x.y());")//
        .stays();
  }

  @Test public void method2() {
    trimmingOf("return x == null ? null : x.y.z();")//
        .withTipper(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).invoke(()->x.y.z());")//
        .stays();
  }

  @Test public void method3() {
    trimmingOf("return x == null ? 0 : x.z();")//
        .withTipper(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).invoke(()->x.z());")//
        .stays();
  }

  @Test public void method4() {
    trimmingOf("return x == null ? 0 : x.y.z();")//
        .withTipper(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).invoke(()->x.y.z());")//
        .stays();
  }

  @Test public void method5() {
    trimmingOf("return x != null && x.z();")//
        .withTipper(InfixExpression.class, new Infix.SafeReference())//
        .gives("return safe(x).invoke(()->x.z());")//
        .stays();
  }

  @Test public void method6() {
    trimmingOf("return x != null && x.y.z();")//
        .withTipper(InfixExpression.class, new Infix.SafeReference())//
        .gives("return safe(x).invoke(()->x.y.z());")//
        .stays();
  }

  @Test public void method7() {
    trimmingOf("(x != null) && x.y()")//
        .withTipper(InfixExpression.class, new Infix.SafeReference())//
        .gives("safe(x).invoke(()->x.y())")//
        .stays();
  }
}
