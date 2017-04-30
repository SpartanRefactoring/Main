package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link LazyInitializer}
 * @author orimarco {@code  marcovitch.ori@gmail.com}
 * @since 2016-12-28 */
@SuppressWarnings("static-method")
public class LazyInitializerTest {
  @Test public void basic() {
    trimmingOf("¢ = ¢ != null ? ¢ : \"\";")//
        .using(new DefaultsTo(), ConditionalExpression.class)//
        .gives("¢ = defaults(¢).to(\"\");")//
        .using(new LazyInitializer(), Assignment.class)//
        .gives("lazyInitialize(¢).with(()-> \"\");").stays();
  }
}
