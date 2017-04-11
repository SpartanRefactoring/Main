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
    trimminKof("a ? x.f(b) : y.f(b)")//
        .gives("(a?x:y).f(b)");
  }

  @Test public void chocolate2() {
    trimminKof("a ? myClass.f(b) : yourClass.f(b)")//
        .gives("(a?myClass:yourClass).f(b)")//
        .stays();
  }

  @Test public void plain() {
    trimminKof("a ? x.f(b) : y.f(b)")//
        .gives("(a?x:y).f(b)");
  }

  @Test public void vanilla1() {
    trimminKof("a ? y.f(b) : Class.f(b)")//
        .stays();
  }

  @Test public void vanilla2() {
    trimminKof("a ? MyClass.f(b) : instanceName.f(b)")//
        .stays();
  }

  @Test public void vanilla3() {
    trimminKof("a ? MyClass.f(b) : YourClass.f(b)")//
        .stays();
  }

  @Test public void vanilla4() {
    trimminKof("a ? x.f(b) : YourClass.f(b)")//
        .stays();
  }
}
