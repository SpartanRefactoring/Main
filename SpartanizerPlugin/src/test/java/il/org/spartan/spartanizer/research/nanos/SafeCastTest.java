package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link SafeCast}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-23 */
@SuppressWarnings("static-method")
public class SafeCastTest {
  @Test public void a() {
    trimmingOf("if(a instanceof b) ((b)a).g();")//
        .using(new SafeCast(), CastExpression.class)//
        .gives("if(a instanceof b) (safeCast(a)).g();")//
        .gives("if(a instanceof b) safeCast(a).g();")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("if(a.b.g() instanceof b && isGood.enough()) ((b)a.b.g()).g();")//
        .using(new SafeCast(), CastExpression.class)//
        .gives("if(a.b.g() instanceof b && isGood.enough()) (safeCast(a.b.g())).g();")//
        .gives("if(a.b.g() instanceof b && isGood.enough()) safeCast(a.b.g()).g();")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("if(a    instanceof      b) ((b)  a).g();")//
        .using(new SafeCast(), CastExpression.class)//
        .gives("if(a instanceof b) (safeCast(a)).g();")//
        .gives("if(a instanceof b) safeCast(a).g();")//
        .stays();
  }
}
