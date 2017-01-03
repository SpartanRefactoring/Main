package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Ori Marcovitch
 * @since Dec 13, 2016 */
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

  @Test public void basic2() {
    trimmingOf("return ($ == null) ? null : $.size();")//
        .withTipper(ConditionalExpression.class, new Unless())//
        .gives("return unless(($==null)).eval(()->$.size());")//
        .stays();
  }
}
