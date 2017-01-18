package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** Test Class for the Bug described on Issue #1013
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-01-16 */
@SuppressWarnings("static-method")
public class Issue1013 {
  @Test public void t1() {
    trimmingOf("a[x] = a[x] + x++;++x;").stays();
  }
  
  @Test public void t2() {
    trimmingOf("a[x] = x;++x;").stays();
  }
}
