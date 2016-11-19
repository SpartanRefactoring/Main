package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

@SuppressWarnings("static-method")
@Ignore
public class Issue849 {
  @Test public void test0() {
    trimmingOf("a-1+2").gives("a+1").gives("a++").stays();
  }

  @Test public void test1() {
    trimmingOf("a-1-2").gives("a-3").stays();
  }

  @Test public void test2() {
    trimmingOf("2+a-1-9").gives("2+a-8").stays();
  }

  @Test public void test3() {
    trimmingOf("3+a+1+2").gives("a+1").gives("a++").stays();
  }
}
