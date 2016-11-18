package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import org.junit.*;

/** @author Dor Ma'ayan
 * @since 18-11-2016 */
@SuppressWarnings("static-method")
public class Issue291 {
  @Test public void test0() {
    trimmingOf("a+2==3").gives("a==3-2").gives("a==1").stays();
  }

  @Test public void test1() {
    trimmingOf("3==a+2").gives("a+2==3").gives("a==3-2").gives("a==1").stays();
  }

  @Test public void test2() {
    trimmingOf("a+5==b+2").gives("a==b+2-5");
  }

  @Test public void test3() {
    trimmingOf("a+2.2==3.89").gives("a==3.89-2.2").gives("a==1.69").stays();
  }

  @Test public void test4() {
    trimmingOf("a+2.2==b").gives("a==b-2.2").stays();
  }
}
