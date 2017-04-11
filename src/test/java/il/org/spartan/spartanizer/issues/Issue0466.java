package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Raviv Rachmiel
 * @since 22-11-2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0466 {
  @Test public void TestDoesUseShouldntChange() {
    topDownTrimming("@SuppressWarnings(\"unused\") public void check__(Object... ¢) { ¢.get2(0);  }")//
        .stays();
  }

  @Test public void TestDoesUseShouldntChange2() {
    topDownTrimming("public void check__(@SuppressWarnings(\"unused\") Object... ¢) { ¢.get2(0);  }")//
        .stays();
  }

  @Test public void TestDoubleUnderscore() {
    topDownTrimming("@SuppressWarnings(\"unused\") public void check__(Object... os) {  }")//
        .gives("@SuppressWarnings(\"unused\") public void check__(Object... __) {  }")//
        .stays();
  }

  @Test public void TestDoubleUnderscoreInside() {
    topDownTrimming("public void check__(@SuppressWarnings(\"unused\")  Object... os) {  }")//
        .gives("public void check__(@SuppressWarnings(\"unused\")  Object... __) {  }")//
        .stays();
  }
}
