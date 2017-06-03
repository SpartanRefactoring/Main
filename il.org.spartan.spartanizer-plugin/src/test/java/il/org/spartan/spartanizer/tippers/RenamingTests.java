package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** TODO dormaayn: All tests regrading renaming (in the spartnizer)
 * @author Dor Ma'ayan
 * @since 2017-05-14 */
@SuppressWarnings("static-method")
public class RenamingTests {
  @Test public void bug1() {
    trimmingOf("class Classes{private final Map<IProject, Boolean> enabled; private Checker t;}")//
        .gives("class Classes{private final Map<IProject, Boolean> map; private Checker checker;}").stays();
  }
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
  @Ignore @Test public void test4() {
    trimmingOf("void f(int a,int b, int c) {}").gives("void f(int _1, int _2, int _3) {}").stays();
  }
  @Ignore @Test public void test5() {
    trimmingOf("void f(int a,int b, int c) {return b;}").gives("void f(int _1, int b, int _2) {return b;}").stays();
  }
  @Ignore @Test public void test6() {
    trimmingOf("void f(int a,int _2, int c) {return _2;}").gives("void f(int _1, int b, int _2) {return b;}").stays();
  }
  @Ignore @Test public void test7() {
    trimmingOf("void f(int __1,int __2, int __3) {return __1+__2+__3;}").gives("void f(int a, int b, int c) {return a+b+c;}").stays();
  }
  @Ignore @Test public void test8() {
    trimmingOf("void f(List a) {}").gives("void f(List _1) {}").stays();
  }
  @Test public void test9() {
    trimmingOf("void f(List a) {return a;}").stays();
  }
  @Test public void test10() {
    trimmingOf("void f(int a,int b,int c) {return a+b+c;}").stays();
  }
}
