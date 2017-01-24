package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO: orimarco <tt>marcovitch.ori@gmail.com</tt> please add a description
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-28 */
@SuppressWarnings("static-method")
public class LazyInitializerTest {
  @Test public void basic() {
    trimmingOf("¢ = ¢ != null ? ¢ : \"\";")//
        .using(ConditionalExpression.class, new DefaultsTo())//
        .using(Assignment.class, new LazyInitializer())//
        .gives("¢ = default¢(¢).to(\"\");")//
        .using(ConditionalExpression.class, new DefaultsTo())//
        .using(Assignment.class, new LazyInitializer())//
        .gives("lazyInitialize(¢).with(()-> \"\");")//
        .stays();
  }
}
