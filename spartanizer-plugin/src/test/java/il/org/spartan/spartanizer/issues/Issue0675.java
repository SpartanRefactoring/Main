package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.is;

import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.utils.tdd.enumerate;

/** Unit tests for the GitHub issue thus numbered
 * @author Aviad Cohen & Noam Yefet
 * @since 16-11-1 */
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0675 {
  @Test public void statements_test0() {
    enumerate.statements(null);
    assert true;
  }
  @Test public void statements_test1() {
    azzert.that(0, is(enumerate.statements(null)));
  }
  @Test public void statements_test10() {
    azzert.that(9, is(enumerate.statements(make.ast("int x = 5; while (true) { if (x == 0) { break; } \n else x = x - 1; continue;}"))));
  }
  @Test public void statements_test2() {
    azzert.that(2, is(enumerate.statements(make.ast("return 0;"))));
  }
  @Test public void statements_test3() {
    azzert.that(3, is(enumerate.statements(make.ast("aValue = 8933; return 0;"))));
  }
  @Test public void statements_test4() {
    azzert.that(4, is(enumerate.statements(make.ast("x = 5; aValue = 8933; return 0;"))));
  }
  @Test public void statements_test5() {
    azzert.that(3, is(enumerate.statements(make.ast(";;"))));
  }
  @Test public void statements_test6() {
    azzert.that(4, is(enumerate.statements(make.ast("if (true) { x = 0; }"))));
  }
  @Test public void statements_test7() {
    azzert.that(6, is(enumerate.statements(make.ast("while (true) { if (true) { x = 0; } }"))));
  }
  @Test public void statements_test8() {
    azzert.that(6, is(enumerate.statements(make.ast("while (true) { if (x == 0) { break; } }"))));
  }
  @Test public void statements_test9() {
    azzert.that(8, is(enumerate.statements(make.ast("while (true) { if (x == 0) { break; } \n else x = x - 1; continue;}"))));
  }
}
