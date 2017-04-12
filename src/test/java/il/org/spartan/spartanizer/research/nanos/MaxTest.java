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
    trimminKof("return x > y ? x : y;")//
        .using(new Max(), ConditionalExpression.class)//
        .gives("return max(x,y);")//
        .stays();
  }

  @Test public void b() {
    trimminKof("return x >= y ? x : y;")//
        .using(new Max(), ConditionalExpression.class)//
        .gives("return max(x,y);")//
        .stays();
  }

  @Test public void c() {
    trimminKof("return y < x ? x : y;")//
        .using(new Max(), ConditionalExpression.class)//
        .gives("return max(x,y);")//
        .stays();
  }

  @Test public void d() {
    trimminKof("return y <= x ? x : y;")//
        .using(new Max(), ConditionalExpression.class)//
        .gives("return max(x,y);")//
        .stays();
  }
}
