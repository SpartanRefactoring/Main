package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit test for {@link ModifierFinalAbstractMethodRedundant}
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2016-12-23 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1042 {
  @Test public void test0() {
    trimmingOf("abstract int f(final int a, final int b);")//
        .gives("abstract int f(int a, int b);")//
        .stays();
  }

  @Test public void test1() {
    trimmingOf("interface a { int f(final int a, final int b);}")//
        .gives("interface a { int f(int a, int b);}")//
        .stays();
  }

  @Test public void test2() {
    trimmingOf("int f(final int a, final int b) { return a + b; }")//
        .stays();
  }

  @Test public void test3() {
    trimmingOf("interface a { default int f(final int a, final int b) { return a + b;}")//
        .stays();
  }

  @Test public void test4() {
    trimmingOf("for (final File f:fs);") //
        .stays();
  }
}
