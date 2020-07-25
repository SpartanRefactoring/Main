/* TODO Yossi Gil Document Class
 *
 * @author Yossi Gil
 *
 * @since Jan 6, 2017 */
package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

@SuppressWarnings("static-method")
public class Issue0467 {
  @Test public void a() {
    trimmingOf("switch(x--){case 1: y=3; case 2: y=4;}")//
        .stays();
  }
  @Test public void b() {
    trimmingOf("{switch(x++){case 1: y=3; case 2: y=4;}}")//
        .gives("switch(x++){case 1: y=3; case 2: y=4;}")//
        .stays();
  }
  @Test public void c() {
    trimmingOf("y=2; switch(y+x--){case 1: y=3; case 2: y=4;} y=6;")//
        .stays();
  }
  @Test public void d() {
    trimmingOf("y=2; switch(x-- + x--){case 1: y=3; case 2: y=4;} y=6;")//
        .stays();
  }
  @Test public void e() {
    trimmingOf("y=2; {switch(x--){case 1: y=3; case 2: y=4;}} y=6;")//
        .gives("y=2; switch(x--){case 1: y=3; case 2: y=4;} y=6;")//
        .stays();
  }
  @Test public void f() {
    trimmingOf("switch(x.z--){case 1: y=3; case 2: y=4;}")//
        .stays();
  }
}
