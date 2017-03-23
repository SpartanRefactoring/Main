package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** TODO: Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Jan 6, 2017 */
@Ignore // TODO Dor Ma'ayan
@SuppressWarnings("static-method")
public class Issue0849 {
  @Test public void test0() {
    trimmingOf("a-1+2")//
        .gives("a+1")//
        .gives("a++")//
        .stays();
  }

  @Test public void test1() {
    trimmingOf("a-1-2")//
        .gives("a-3")//
        .stays();
  }

  @Test public void test2() {
    trimmingOf("2+a-1-9")//
        .gives("2+a-8")//
        .stays();
  }

  @Test public void test3() {
    trimmingOf("3+a+1+2")//
        .gives("a+1")//
        .gives("a++")//
        .stays();
  }
}
