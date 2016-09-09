package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link DisabledChecker}
 * @author Ori Roth
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue142Test {
  @Test public void disableSpartanizaionInMethod() {
    trimming("/***/ class A {\n" + "")
            .to("/***/ class A {\n" + "");
  }

  @Test public void disableSpartanizaionInClass() {
    trimming("/**@DisableSpartan*/ class A {\n" + "").stays();
  }

  @Test public void disableSpartanizaionWithEnabler() {
    trimming("/**@DisableSpartan*/ class A {\n" + "")
            .to("/**@DisableSpartan*/ class A {\n" + "");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInMethod() {
    trimming("/**@DisableSpartan*/ class A {\n" + "")
            .to("/**@DisableSpartan*/ class A {\n" + "");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInClass() {
    trimming("/**@DisableSpartan*/ class A {\n" + "")
            .to("/**@DisableSpartan*/ class A {\n" + "");
  }
}
