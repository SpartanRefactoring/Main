package il.org.spartan.athenizer.zoom.zoomers;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

import il.org.spartan.athenizer.zoomers.*;

/** Test case for {@link TernaryPushup}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2017-01-18 */
@SuppressWarnings("static-method")
public class Issue1049 {
  @Test public void t1() {
    bloatingOf("d = a+(cond ? b : c);").gives("d = cond ? a+b : a+c;");
  }
  @Test public void t2() {
    bloatingOf("d = a()+(cond ? b() : c());").gives("d = cond ? a()+b() : a()+c();");
  }
  @Test public void t3() {
    bloatingOf("d = a+(cond() ? b : c);").stays();
  }
  @Test public void t4() {
    bloatingOf("d = (cond ? a : b)+(cond2 ? c : d);").gives("d = cond2 ? (cond ? a : b) + c : (cond ? a : b) + d;");
  }
  @Test public void t5() {
    bloatingOf("d = (cond ? b : c) + a;").gives("d = cond ? b+a : c+a;");
  }
}
