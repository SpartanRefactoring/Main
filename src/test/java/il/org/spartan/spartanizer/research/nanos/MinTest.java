package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link Min}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-12 */
@SuppressWarnings("static-method")
public class MinTest {
  @Test public void a() {
    trimmingOf("return x > y ? y : x;")//
        .using(ConditionalExpression.class, new Min())//
        .gives("return min(x,y);")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("return x >= y ? y : x;")//
        .using(ConditionalExpression.class, new Min())//
        .gives("return min(x,y);")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("return y < x ? y : x;")//
        .using(ConditionalExpression.class, new Min())//
        .gives("return min(x,y);")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("return y <= x ? y : x;")//
        .using(ConditionalExpression.class, new Min())//
        .gives("return min(x,y);")//
        .stays();
  }
}
