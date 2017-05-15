package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** TODO dormaayn: document class
 * @author Dor Ma'ayan
 * @since 2017-05-14 */
@SuppressWarnings("static-method")
public class RenamingTests {
  @Test public void test0() {
    trimmingOf("class Classes{private Namer a;private Checker t;}")//
        .gives("class Classes{private Namer namer;private Checker checker;}")//
        .stays();
  }
  @Test public void test1() {
    trimmingOf("class Classes{private Namer a;private Checker t;public Classes(Namer n){a=n;}}")//
        .gives("class Classes{private Namer namer;private Checker checker;public Classes(Namer n){namer=n;}}")//
        .stays();
  }
  @Test public void test2() {
    trimmingOf("class Classes{private Namer a; Checker t;public Classes(Namer n){a=n;}}")//
        .gives("class Classes{private Namer namer;Checker t;public Classes(Namer n){namer=n;}}")//
        .stays();
  }
  @Test public void test3() {
    trimmingOf("private class Classes{private Namer a; CheckerTest t;public Classes(Namer n){a=n;}}")//
        .gives("private class Classes{private Namer namer;CheckerTest checkerTest;public Classes(Namer n){namer=n;}}")//
        .stays();
  }
}
