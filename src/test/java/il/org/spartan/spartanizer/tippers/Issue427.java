package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Dan Abramovich
 * @since 27-11-2016 */
@SuppressWarnings("static-method")
public class Issue427 {
  @Ignore
  @Test public void test0() {
    trimmingOf("int f(){return x==y ? y:x;}")
        .gives("int f(){return x;}").stays();
  }
  @Ignore
  @Test public void test1() {
    trimmingOf("int f(){return x==y ? y:z;}")
    .stays();
  }
  @Ignore
  @Test public void test2() {
    trimmingOf("int f(){return g()==y ? y:g();}")
    .stays();
  }
  @Ignore
  @Test public void test4() {
    trimmingOf("int f(){return g()==h() ? g():h();}")
    .stays();
  }
  @Ignore
  @Test public void test5() {
    trimmingOf("int f(){return x==y ? z:w;}")
    .stays();
  }
}
