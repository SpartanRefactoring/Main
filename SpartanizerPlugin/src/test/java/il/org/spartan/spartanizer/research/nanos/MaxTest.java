package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link Max}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-12 */
@SuppressWarnings("static-method")
public class MaxTest {
  @Test public void a() {
    trimmingOf("return x > y ? x : y;")//
        .using(ConditionalExpression.class, new Max())//
        .gives("return max(x,y);")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("return x >= y ? x : y;")//
        .using(ConditionalExpression.class, new Max())//
        .gives("return max(x,y);")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("return y < x ? x : y;")//
        .using(ConditionalExpression.class, new Max())//
        .gives("return max(x,y);")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("return y <= x ? x : y;")//
        .using(ConditionalExpression.class, new Max())//
        .gives("return max(x,y);")//
        .stays();
  }
}
