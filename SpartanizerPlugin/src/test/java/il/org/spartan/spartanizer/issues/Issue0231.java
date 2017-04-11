package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0231 {
  @Test public void chocolate1() {
    topDownTrimming("a ? x.f(b) : y.f(b)")//
        .gives("(a?x:y).f(b)");
  }

  @Test public void chocolate2() {
    topDownTrimming("a ? myClass.f(b) : yourClass.f(b)")//
        .gives("(a?myClass:yourClass).f(b)")//
        .stays();
  }

  @Test public void plain() {
    topDownTrimming("a ? x.f(b) : y.f(b)")//
        .gives("(a?x:y).f(b)");
  }

  @Test public void vanilla1() {
    topDownTrimming("a ? y.f(b) : Class.f(b)")//
        .stays();
  }

  @Test public void vanilla2() {
    topDownTrimming("a ? MyClass.f(b) : instanceName.f(b)")//
        .stays();
  }

  @Test public void vanilla3() {
    topDownTrimming("a ? MyClass.f(b) : YourClass.f(b)")//
        .stays();
  }

  @Test public void vanilla4() {
    topDownTrimming("a ? x.f(b) : YourClass.f(b)")//
        .stays();
  }
}
