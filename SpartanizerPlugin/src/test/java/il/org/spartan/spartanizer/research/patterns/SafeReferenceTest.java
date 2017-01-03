package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-03 */
@SuppressWarnings("static-method")
public class SafeReferenceTest {
  @Test public void basic() {
    trimmingOf("return x !=null ? x.y : null;")//
        .withTipper(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).get(()->x.y);")//
        .stays();
  }

  @Test public void basic2() {
    trimmingOf("return x ==null ? null : x.field;")//
        .withTipper(ConditionalExpression.class, new SafeReference())//
        .gives("return safe(x).get(()->x.field);")//
        .stays();
  }

  @Test public void respect() {
    trimmingOf("return x ==null ? null : x.field;")//
        .withTippers(ConditionalExpression.class, //
            new Unless(), //
            new DefaultsTo(), //
            new SafeReference())//
        .gives("return safe(x).get(()->x.field);")//
        .withTippers(ConditionalExpression.class, //
            new Unless(), //
            new DefaultsTo(), //
            new SafeReference())//
        .stays();
  }
  // @Test public void respect() {
  // trimmingOf("return ¢ != null ? ¢ : \"\";")//
  // .withTipper(ConditionalExpression.class, new Unless())//
  // .withTipper(ConditionalExpression.class, new DefaultsTo())//
  // .gives("return default¢(¢).to(\"\");")//
  // .stays();
  // }
  //
  // @Test public void respect2() {
  // trimmingOf("return ¢ != null ? ¢ : \"\";")//
  // .withTipper(ConditionalExpression.class, new Unless())//
  // .stays();
  // }
  //
  // @Test public void basic2() {
  // trimmingOf("return ($ == null) ? null : $.size();")//
  // .withTipper(ConditionalExpression.class, new Unless())//
  // .gives("return unless(($==null)).eval(()->$.size());")//
  // .stays();
  // }
}
