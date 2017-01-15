package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** This is a unit test for {@link IfTrueOrFalse} of previously failed tests.
 * Related to {@link Version250} and {@link Issue086}.
 * @author Yuval Simon
 * @since 2016-12-08 */
@SuppressWarnings("static-method")
public class Issue0907 {
  @Test public void issue086_1() {
    trimmingOf("if(false)" + "c();\n" + "int a;")//
        .gives("{}int a;")//
        .gives("int a;")//
        .stays();
  }
}
