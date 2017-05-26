package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

import il.org.spartan.athenizer.bloaters.*;

/** Unit tests for {@link DeclarationWithInitializerBloater}
 * @author tomerdragucki {@code tomerd@campus.technion.ac.il}
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class Issue0996 {
  @Test public void a() {
    bloatingOf("int a = 0;")//
        .gives("int a;" //
            + "a = 0;")
        .stays();
  }
  @Test public void b() {
    bloatingOf("int a = f();")//
        .gives("int a;" //
            + "a = f();")
        .stays();
  }
  @Test public void c() {
    bloatingOf("final String[] command = { \"/bin/bash\", \"-c\", shellCommand };")//
        .stays();
  }
  @Test public void d() {
    bloatingOf("@SuppressWarnings(\"unchecked\") int a = f();")//
        .stays();
  }
  @Test public void e() {
    bloatingOf("final int a = 5; switch(x) { case a: return 1; default: }")//
        .stays();
  }
}
