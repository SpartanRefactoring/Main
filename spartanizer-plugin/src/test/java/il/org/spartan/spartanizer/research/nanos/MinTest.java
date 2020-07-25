package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.junit.Test;

/** Tests {@link Min}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-12 */
@SuppressWarnings("static-method")
public class MinTest {
  @Test public void a() {
    trimmingOf("return x > y ? y : x;")//
        .using(new Min(), ConditionalExpression.class)//
        .gives("return min(x,y);")//
        .stays();
  }
  @Test public void b() {
    trimmingOf("return x >= y ? y : x;")//
        .using(new Min(), ConditionalExpression.class)//
        .gives("return min(x,y);")//
        .stays();
  }
  @Test public void c() {
    trimmingOf("return y < x ? y : x;")//
        .using(new Min(), ConditionalExpression.class)//
        .gives("return min(x,y);")//
        .stays();
  }
  @Test public void d() {
    trimmingOf("return y <= x ? y : x;")//
        .using(new Min(), ConditionalExpression.class)//
        .gives("return min(x,y);")//
        .stays();
  }
}
