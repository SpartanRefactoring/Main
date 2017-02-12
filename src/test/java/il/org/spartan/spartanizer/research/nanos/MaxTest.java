package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link Max}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-12 */
@SuppressWarnings("static-method")
public class MaxTest {
  @Test public void field() {
    trimmingOf("return x > y ? x : y;")//
        .using(ConditionalExpression.class, new Max())//
        .gives("return safe(x).get(()->x.y);")//
        .stays();
  }
}
