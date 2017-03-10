package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** TODO: Raviv Rachmiel please add a description
 * @author Raviv Rachmiel
 * @since 22-11-2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0466 {
  @Test public void TestDoesUseShouldntChange() {
    trimmingOf("@SuppressWarnings(\"unused\") public void check__(Object... ¢) { ¢.get2(0);  }")//
        .stays();
  }

  @Test public void TestDoesUseShouldntChange2() {
    trimmingOf("public void check__(@SuppressWarnings(\"unused\") Object... ¢) { ¢.get2(0);  }")//
        .stays();
  }

  @Test public void TestDoubleUnderscore() {
    trimmingOf("@SuppressWarnings(\"unused\") public void check__(Object... os) {  }")//
        .gives("@SuppressWarnings(\"unused\") public void check__(Object... __) {  }")//
        .stays();
  }

  @Test public void TestDoubleUnderscoreInside() {
    trimmingOf("public void check__(@SuppressWarnings(\"unused\")  Object... os) {  }")//
        .gives("public void check__(@SuppressWarnings(\"unused\")  Object... __) {  }")//
        .stays();
  }
}
