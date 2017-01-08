package il.org.spartan.zoomer.zoomin.expanders;

import il.org.spartan.spartanizer.ast.navigate.*;

/** @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-07
 * [[SuppressWarningsSpartan]]
 */
@SuppressWarnings({ "static-method", "unused" })
public class Issue1040Aux extends ReflectiveTester {
  int a() {
    int i = 0;
    ++i;
    return 0;
  }
  int b() {
    int i = 0;
    ++i;
    return 0;
  }
}
