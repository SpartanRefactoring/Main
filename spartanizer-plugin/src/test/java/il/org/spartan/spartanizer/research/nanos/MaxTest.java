package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.junit.Test;

/** Tests {@link Max}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-12 */
@SuppressWarnings("static-method")
public class MaxTest {
  @Test public void a() {
    trimmingOf("return x > y ? x : y;")//
        .using(new Max(), ConditionalExpression.class)//
        .gives("return max(x,y);")//
        .stays();
  }
  @Test public void b() {
    trimmingOf("return x >= y ? x : y;")//
        .using(new Max(), ConditionalExpression.class)//
        .gives("return max(x,y);")//
        .stays();
  }
  @Test public void c() {
    trimmingOf("return y < x ? x : y;")//
        .using(new Max(), ConditionalExpression.class)//
        .gives("return max(x,y);")//
        .stays();
  }
  @Test public void d() {
    trimmingOf("return y <= x ? x : y;")//
        .using(new Max(), ConditionalExpression.class)//
        .gives("return max(x,y);")//
        .stays();
  }
}
