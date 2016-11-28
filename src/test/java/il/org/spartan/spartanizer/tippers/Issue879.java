package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Dan Abramovich
 * @since 28-11-2016 */
@SuppressWarnings("static-method")
public class Issue879 {
  @Test public void test0() {
    trimmingOf("void f(){return;}").gives("void f(){}").stays();
  }
}