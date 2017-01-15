package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

/** Small fix of bloatingOf()
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2017-01-15 */
@SuppressWarnings("static-method")
public class Issue1076 {
  @Test public void a() {
    bloatingOf("").stays();
  }
}