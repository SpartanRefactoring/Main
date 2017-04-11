package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link MethodInvocationToStringToEmptyStringAddition}
 * @author Niv Shalmon
 * @since 2016
 * @see Issue0209Test */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0224 {
  @Test public void a$01() {
    topDownTrimming("a+b.toString()")//
        .gives("a+\"\"+b")//
        .stays();
  }

  @Test public void a$02() {
    topDownTrimming("b.toString()")//
        .gives("\"\"+b")//
        .gives("b+\"\"")//
        .stays();
  }

  @Test public void a$03() {
    topDownTrimming("\"5\"+b.toString()")//
        .gives("\"5\"+\"\"+b")//
        .gives("\"5\"+b")//
        .stays();
  }
}
