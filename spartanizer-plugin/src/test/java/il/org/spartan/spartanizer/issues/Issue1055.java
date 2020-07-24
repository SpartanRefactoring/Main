package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

/** Unit test for Issue 1055
 * @author Yossi Gil
 * @since 2016-12-23 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1055 {
  @Test public void vanilla() {
    trimmingOf("switch(x) { case 1: { y=2; } } ") //
        .gives("switch(x) { case 1: y = 2; }") //
    ;
  }
  @Test public void also() {
    trimmingOf("I x = new I(3),y=null;if(a)synchronized(x){{ y=2;}} return x*y;") //
        .gives("I x = new I(3),y=null;if(a)synchronized(x){y=2;} return x*y;") //
        .stays();
  }
}
