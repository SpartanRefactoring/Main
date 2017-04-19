package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

import il.org.spartan.athenizer.bloaters.*;

/** Test case for {@link OutlineTernaryMethodInvocation}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2017-01-18 */
@SuppressWarnings("static-method")
public class Issue1091 {
  @Test public void t5() {
    bloatingOf("x = y + f(cond ? a : b);").gives("x = y + (cond ? f(a) : f(b));");
  }

  @Test public void t1() {
    bloatingOf("x = f(cond ? a : b);").gives("x = (cond ? f(a) : f(b));");
  }

  @Test public void t2() {
    bloatingOf("x = f(cond() ? a() : b());").stays();
  }

  @Test public void t3() {
    bloatingOf("x = f(d, cond ? a : b);").gives("x = (cond ? f(d, a) : f(d, b));");
  }

  @Test public void t4() {
    bloatingOf("x = f(d(), cond ? a : b);").stays();
  }
}
