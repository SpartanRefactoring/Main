package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO orimarco {@code marcovitch.ori@gmail.com} please add a description
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-28 */
@SuppressWarnings("static-method")
public class LazyInitializerTest {
  @Test public void basic() {
    trimmingOf("¢ = ¢ != null ? ¢ : \"\";")//
        .using(ConditionalExpression.class, new DefaultsTo())//
        .using(Assignment.class, new LazyInitializer())//
        .gives("¢ = defaults(¢).to(\"\");")//
        .using(ConditionalExpression.class, new DefaultsTo())//
        .using(Assignment.class, new LazyInitializer())//
        .gives("lazyInitialize(¢).with(()-> \"\");")//
        .stays();
  }
}
