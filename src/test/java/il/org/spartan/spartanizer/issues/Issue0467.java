/* TODO Yossi Gil LocalVariableInitializedStatement description
 *
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 *
 * @since Jan 6, 2017 */
package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

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
