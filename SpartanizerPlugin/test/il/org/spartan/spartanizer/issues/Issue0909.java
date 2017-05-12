package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** This is a unit test for {@link TryBodyEmptyLeaveFinallyIfExists} of
 * previously failed tests. Related to {@link Issue234}.
 * @author Yuval Simon
 * @since 2016-12-08 */
@SuppressWarnings("static-method")
public class Issue0909 {
  @Test public void b$0() {
    trimmingOf("int a = 3; try { } catch(Exception e) { return -1; } return a;")//
        .gives("int a = 3; return a;") //
        .gives("return 3;")//
        .stays();
  }

  @Test public void b$03() {
    trimmingOf("int a; try { } catch(Exception e) { return -1; }")//
        .gives("")//
        .stays();
  }
}
