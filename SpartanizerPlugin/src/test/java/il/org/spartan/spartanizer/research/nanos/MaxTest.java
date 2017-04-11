package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link Max}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-12 */
@SuppressWarnings("static-method")
public class MaxTest {
  @Test public void a() {
    topDownTrimming("return x > y ? x : y;")//
        .using(ConditionalExpression.class, new Max())//
        .gives("return max(x,y);")//
        .stays();
  }

  @Test public void b() {
    topDownTrimming("return x >= y ? x : y;")//
        .using(ConditionalExpression.class, new Max())//
        .gives("return max(x,y);")//
        .stays();
  }

  @Test public void c() {
    topDownTrimming("return y < x ? x : y;")//
        .using(ConditionalExpression.class, new Max())//
        .gives("return max(x,y);")//
        .stays();
  }

  @Test public void d() {
    topDownTrimming("return y <= x ? x : y;")//
        .using(ConditionalExpression.class, new Max())//
        .gives("return max(x,y);")//
        .stays();
  }
}
