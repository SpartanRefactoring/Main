package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** @author Aviad Cohen & Noam Yefet
 * @since 16-11-1 */
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue675 {
  @Test public void statements_test0() {
    enumerate.statements(null);
    assert true;
  }
  @Test public void statements_test1() {
    assertEquals(enumerate.statements(null), 0);
  }
  @Test public void statements_test2() {
    assertEquals(enumerate.statements(wizard.ast("return 0;")), 2);
  }
  @Test public void statements_test3() {
    assertEquals(enumerate.statements(wizard.ast("aValue = 8933; return 0;")), 3);
  }
  @Test public void statements_test4() {
    assertEquals(enumerate.statements(wizard.ast("x = 5; aValue = 8933; return 0;")), 4);
  }
  @Test public void statements_test5() {
    assertEquals(enumerate.statements(wizard.ast(";;")), 3);
  }
  @Test public void statements_test6() {
    assertEquals(enumerate.statements(wizard.ast("if (true) { x = 0; }")), 4);
  }
  @Test public void statements_test7() {
    assertEquals(enumerate.statements(wizard.ast("while (true) { if (true) { x = 0; } }")), 6);
  }
  @Test public void statements_test8() {
    assertEquals(enumerate.statements(wizard.ast("while (true) { if (x == 0) { break; } }")), 6);
  }
  @Test public void statements_test9() {
    assertEquals(enumerate.statements(wizard.ast("while (true) { if (x == 0) { break; } \n else x = x - 1; continue;}")), 8);
  }
  @Test public void statements_test10() {
    assertEquals(enumerate.statements(wizard.ast("int x = 5; while (true) { if (x == 0) { break; } \n else x = x - 1; continue;}")), 9);
  }
}
