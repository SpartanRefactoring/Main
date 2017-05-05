package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link EvaluateUnlessDefaultsTo}
 * @author Ori Marcovitch
 * @since Dec 13, 2016 */
@SuppressWarnings("static-method")
public class UnlessTest {
  @Test public void basic() {
    trimmingOf("return k == null ? null : new SynchronizedEntry<K,V>(k,mutex);")//
        .using(new EvaluateUnlessDefaultsTo(), ConditionalExpression.class)//
        .gives("return unless(k==null).eval(() -> new SynchronizedEntry<K,V>(k,mutex)).defaultTo(null);")//
        .stays();
  }
  @Test public void basic2() {
    trimmingOf("return ($ == null) ? null : $.size();")//
        .using(new EvaluateUnlessDefaultsTo(), ConditionalExpression.class)//
        .gives("return unless(($==null)).eval(()->$.size()).defaultTo(null);")//
        .gives("return unless($==null).eval(()->$.size()).defaultTo(null);")//
        .stays();
  }
  @Test public void respect() {
    trimmingOf("return ¢ != null ? ¢ : \"\";")//
        .using(new EvaluateUnlessDefaultsTo(), ConditionalExpression.class)//
        .using(new DefaultsTo(), ConditionalExpression.class)//
        .gives("return defaults(¢).to(\"\");")//
        .stays();
  }
  @Test public void respect2() {
    trimmingOf("return ¢ != null ? ¢ : \"\";")//
        .using(new EvaluateUnlessDefaultsTo(), ConditionalExpression.class)//
        .stays();
  }
}
