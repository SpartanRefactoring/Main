package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

/** Test case for {@link OutlineTernaryMethodInvocation}
 * 
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-18
 */
@Ignore
@SuppressWarnings("static-method")
public class Issue1091 {
  @Test public void t1() {
    bloatingOf("x = f(cond ? a : b);").gives("x = (cond ? f(a) : f(b));");
  }
  
  @Test public void t2() {
    bloatingOf("x = f(cond() ? a() : b());").gives("x = (cond() ? f(a()) : f(b()));");
  }
  
  @Test public void t3() {
    bloatingOf("x = f(d, cond ? a : b);").gives("x = (cond ? f(d, a) : f(d, b));");
  }
  
  @Test public void t4() {
    bloatingOf("x = f(d(), cond ? a : b);").stays();
  }
  
  @Test public void t5() {
    bloatingOf("x = y + f(cond ? a : b);").gives("x = y + (cond ? f(a) : f(b));").stays();
  }
}
