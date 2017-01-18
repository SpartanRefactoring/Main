package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

import il.org.spartan.bloater.bloaters.*;

/** Test case for {@link TernaryPushup}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-18 */
@Ignore
@SuppressWarnings("static-method")
public class Issue1049 {
  @Test public void t1() {
    bloatingOf("d = a+(cond ? b : c);").gives("d = cond ? (a+b) : (a+c)");
  }

  @Test public void t2() {
    bloatingOf("d = a()+(cond ? b() : c());").gives("d = cond ? (a()+b()) : (a()+c())");
  }

  @Test public void t3() {
    bloatingOf("d = a+(cond() ? b : c);").stays();
  }

  @Test public void t4() {
    // need to consider side effects for x++
    bloatingOf("d = (cond ? a : b)+(cond2 ? c : d);").gives("d = (cond2 ? (cond ? a : b) + c : (cond ? a : b) + d);");
  }
}
