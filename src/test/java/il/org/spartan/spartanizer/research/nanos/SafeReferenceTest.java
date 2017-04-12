package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.*;

/** Tests {@link SafeReference and Infix.SafeReference}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-03 */
@SuppressWarnings("static-method")
public class SafeReferenceTest {
  @Test public void field() {
    trimminKof("return x !=null ? x.y : null;")//
        .using(new SafeReference(), ConditionalExpression.class)//
        .gives("return safe(x).get(()->x.y);")//
        .stays();
  }

  @Test public void field2() {
    trimminKof("return x ==null ? null : x.field;")//
        .using(new SafeReference(), ConditionalExpression.class)//
        .gives("return safe(x).get(()->x.field);")//
        .stays();
  }

  @Test public void field3() {
    trimminKof("return x == null ? null : x.y.z;")//
        .using(new SafeReference(), ConditionalExpression.class)//
        .gives("return safe(x).get(()->x.y.z);")//
        .stays();
  }

  @Test public void field4() {
    trimminKof("return x != null && x.y;")//
        .using(new Infix.SafeNavigation(), InfixExpression.class)//
        .gives("return safe(x).get(()->x.y);")//
        .stays();
  }

  @Test public void field5() {
    trimminKof("return x != null && x.y.z.w;")//
        .using(new Infix.SafeNavigation(), InfixExpression.class)//
        .gives("return safe(x).get(()->x.y.z.w);")//
        .stays();
  }

  @Test public void field6() {
    trimminKof("return x == null ? 0 : x.y.z;")//
        .using(new SafeReference(), ConditionalExpression.class)//
        .gives("return safe(x).get(()->x.y.z);")//
        .stays();
  }

  @Test public void method() {
    trimminKof("return x == null ? null : x.y();")//
        .using(new SafeReference(), ConditionalExpression.class)//
        .gives("return safe(x).invoke(()->x.y());")//
        .stays();
  }

  @Test public void method2() {
    trimminKof("return x == null ? null : x.y.z();")//
        .using(new SafeReference(), ConditionalExpression.class)//
        .gives("return safe(x).invoke(()->x.y.z());")//
        .stays();
  }

  @Test public void method3() {
    trimminKof("return x == null ? 0 : x.z();")//
        .using(new SafeReference(), ConditionalExpression.class)//
        .gives("return safe(x).invoke(()->x.z());")//
        .stays();
  }

  @Test public void method4() {
    trimminKof("return x == null ? 0 : x.y.z();")//
        .using(new SafeReference(), ConditionalExpression.class)//
        .gives("return safe(x).invoke(()->x.y.z());")//
        .stays();
  }

  @Test public void method5() {
    trimminKof("return x != null && x.z();")//
        .using(new Infix.SafeNavigation(), InfixExpression.class)//
        .gives("return safe(x).invoke(()->x.z());")//
        .stays();
  }

  @Test public void method6() {
    trimminKof("return x != null && x.y.z();")//
        .using(new Infix.SafeNavigation(), InfixExpression.class)//
        .gives("return safe(x).invoke(()->x.y.z());")//
        .stays();
  }

  @Test public void method7() {
    trimminKof("(x != null) && x.y()")//
        .using(new Infix.SafeNavigation(), InfixExpression.class)//
        .gives("safe(x).invoke(()->x.y())")//
        .stays();
  }

  @Test public void respect() {
    trimminKof("return x ==null ? null : x.field;")//
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
