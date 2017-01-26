package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** Unit test for {@link FragmentInitializerStatementTerminatingScope} this test
 * tests whether the bug mentioned in Issue0 711 had been fixed
 * @author Dan Abramovich
 * @since 22-11-2016 */
@SuppressWarnings("static-method")
public class Issue0711 {
  @Test public void test0() {
    trimmingOf("int oneLarger(int x) {Function<Integer, Integer> $ = i -> i + 1;return $.eval(x);}")//
        .stays();
  }

  @Test public void test1() {
    trimmingOf("Consumer<Integer> x = (i->i+1); use(f);x.accept(6);")//
        .stays();
  }
}
