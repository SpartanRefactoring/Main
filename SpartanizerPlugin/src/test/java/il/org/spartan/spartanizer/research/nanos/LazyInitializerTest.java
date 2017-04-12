package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.research.nanos.*;

/** Tests {@link LazyInitializer}
 * @author orimarco {@code  marcovitch.ori@gmail.com}
 * @since 2016-12-28 */
@SuppressWarnings("static-method")
public class LazyInitializerTest {
  @Test public void basic() {
    trimminKof("¢ = ¢ != null ? ¢ : \"\";")//
        .using(new DefaultsTo(), ConditionalExpression.class)//
        .using(new LazyInitializer(), Assignment.class)//
        .gives("¢ = defaults(¢).to(\"\");")//
        .using(new DefaultsTo(), ConditionalExpression.class)//
        .using(new LazyInitializer(), Assignment.class)//
        .gives("lazyInitialize(¢).with(()-> \"\");").stays();
  }
}
