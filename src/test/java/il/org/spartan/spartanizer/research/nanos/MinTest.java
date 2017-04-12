package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link Min}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-12 */
@SuppressWarnings("static-method")
public class MinTest {
  @Test public void a() {
    trimminKof("return x > y ? y : x;")//
        .using(new Min(), ConditionalExpression.class)//
        .gives("return min(x,y);")//
        .stays();
  }

  @Test public void b() {
    trimminKof("return x >= y ? y : x;")//
        .using(new Min(), ConditionalExpression.class)//
        .gives("return min(x,y);")//
        .stays();
  }

  @Test public void c() {
    trimminKof("return y < x ? y : x;")//
        .using(new Min(), ConditionalExpression.class)//
        .gives("return min(x,y);")//
        .stays();
  }

  @Test public void d() {
    trimminKof("return y <= x ? y : x;")//
        .using(new Min(), ConditionalExpression.class)//
        .gives("return min(x,y);")//
        .stays();
  }
}
