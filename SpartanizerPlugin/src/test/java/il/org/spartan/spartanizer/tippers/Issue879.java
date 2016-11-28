package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Dan Abramovich
 * @since 27-11-2016 */
@SuppressWarnings("static-method")
public class Issue879 {
  @Ignore
  @Test public void test0() {
    trimmingOf("void f(){return;}").gives("int f(){}").stays();
  }
}