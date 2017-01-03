package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-03 */
@SuppressWarnings("static-method")
public class SafeInvocationTest {
  @Test public void basic() {
    trimmingOf("return x == null ? null : x.y();")//
        .withTipper(ConditionalExpression.class, new SafeInvocation())//
        .gives("return safe(x).invoke(()->x.y());")//
        .stays();
  }
}
