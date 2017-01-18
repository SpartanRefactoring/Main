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
    bloatingOf("x = foo(cond ? a : b);").gives("x = cond ? f(a) : f(b);");
  }
  
  @Test public void t2() {
    bloatingOf("x = foo(cond() ? a() : b());").gives("x = cond() ? f(a()) : f(b());");
  }
  
  @Test public void t3() {
    bloatingOf("x = foo(d, cond ? a : b);").gives("x = cond ? f(d, a) : f(d, b);");
  }
  
  @Test public void t4() {
    bloatingOf("x = foo(d(), cond ? a : b);").stays();
  }
  
  @Test public void t5() {
    bloatingOf("x = foo(cond ? a : b, cond2 ? c : d);")
    .gives("x = cond ? f(a, cond2 ? c : d) : f(b, cond2 ? c : d);")
    .gives("x = cond ? cond2 ? f(a,c) : f(a,d) : cond2 ? f(b,c) : f(b,d) ;");
  }
}
