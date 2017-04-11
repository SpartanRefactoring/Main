package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit test for {@link LocalVariableIntializedStatementTerminatingScope} this
 * test tests whether the bug mentioned in Issue0 711 had been fixed
 * @author Dan Abramovich
 * @since 22-11-2016 */
@SuppressWarnings("static-method")
public class Issue0711 {
  @Test public void test0() {
    topDownTrimming("int oneLarger(int x) {Function<Integer, Integer> $ = i -> i + 1;return $.eval(x);}")//
        .gives("int oneLarger(int x) {Function<Integer, Integer> $ = λ -> λ + 1;return $.eval(x);}")//
        .stays();
  }

  @Test public void test1() {
    topDownTrimming("Consumer<Integer> x = (i->i+1); use(f);x.accept(6);")//
        .gives("Consumer<Integer> x = (λ->λ+1); use(f);x.accept(6);")//
        .stays();
  }
}
