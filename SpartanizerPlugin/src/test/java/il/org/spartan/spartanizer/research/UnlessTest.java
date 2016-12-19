package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class UnlessTest {
  @Test public void basic() {
    trimmingOf("return k == null ? null : new SynchronizedEntry<K,V>(k,mutex);")//
        .withTipper(ConditionalExpression.class, new Unless())//
        .gives("return unless(k==null).eval(() -> new SynchronizedEntry<K,V>(k,mutex));")//
        .stays();
  }

  @Test public void respect() {
    trimmingOf("return ¢ != null ? ¢ : \"\";")//
        .withTipper(ConditionalExpression.class, new Unless())//
        .withTipper(ConditionalExpression.class, new DefaultsTo())//
        .gives("return default¢(¢).to(\"\");")//
        .stays();
  }

  @Test public void respect2() {
    trimmingOf("return ¢ != null ? ¢ : \"\";")//
        .withTipper(ConditionalExpression.class, new Unless())//
        .stays();
  }
}
