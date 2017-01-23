package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** TODO:  tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 please add a description 
 @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2017-01-12 
 */

@SuppressWarnings("static-method")
public class Issue0731 {
  @Test public void a() {
    trimmingOf("Integer i = 0; i.toString();")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("Integer i = 0; (i).toString();")//
        .stays();
  }
}

