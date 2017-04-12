package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.*;

/** Tests {@link Unless}
 * @author Ori Marcovitch
 * @since Dec 13, 2016 */
@SuppressWarnings("static-method")
public class UnlessTest {
  @Test public void basic() {
    trimminKof("return k == null ? null : new SynchronizedEntry<K,V>(k,mutex);")//
        .using(new Unless(), ConditionalExpression.class)//
        .gives("return unless(k==null).eval(() -> new SynchronizedEntry<K,V>(k,mutex)).defaultTo(null);")//
        .stays();
  }

  @Test public void basic2() {
    trimminKof("return ($ == null) ? null : $.size();")//
        .using(new Unless(), ConditionalExpression.class)//
        .gives("return unless(($==null)).eval(()->$.size()).defaultTo(null);")//
        .gives("return unless($==null).eval(()->$.size()).defaultTo(null);")//
        .stays();
  }

  @Test public void respect() {
    trimminKof("return ¢ != null ? ¢ : \"\";")//
        .using(new Unless(), ConditionalExpression.class)//
        .using(new DefaultsTo(), ConditionalExpression.class)//
        .gives("return defaults(¢).to(\"\");")//
        .stays();
  }

  @Test public void respect2() {
    trimminKof("return ¢ != null ? ¢ : \"\";")//
        .using(new Unless(), ConditionalExpression.class)//
        .stays();
  }
}
