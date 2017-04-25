package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link MethodInvocationToStringToEmptyStringAddition}
 * @author Niv Shalmon
 * @since 2016
 * @see Issue0209Test */
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0224 {
  @Test public void a$01() {
    trimmingOf("a+b.toString()")//
        .gives("a+\"\"+b")//
        .stays();
  }

  @Test public void a$02() {
    trimmingOf("b.toString()")//
        .gives("\"\"+b")//
        .gives("b+\"\"")//
        .stays();
  }

  @Test public void a$03() {
    trimmingOf("\"5\"+b.toString()")//
        .gives("\"5\"+\"\"+b")//
        .gives("\"5\"+b")//
        .stays();
  }
}
