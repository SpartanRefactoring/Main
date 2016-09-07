package il.org.spartan.spartanizer.ast;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.engine.*;

@SuppressWarnings({ "static-method", "javadoc" }) public class metricsTest {
  @Test public void issue128_1() {
    final Expression ¢ = into.i("3+4");
    assertEquals(3, metrics.nodes(¢));
  }

  @Test public void issue128_2() {
    final Expression ¢ = into.i("3+4");
    assertEquals(3, metrics.nodes(¢));
  }

  @Test public void issue128_3() {
    final Expression ¢ = into.i("5*6+43*2");
    assertEquals(7, metrics.nodes(¢));
  }

  @Test public void issue1128_4() {
    final Expression ¢ = into.i("3+4*4+6*7+8");
    assertEquals(11, metrics.nodes(¢));
  }

  @Test public void issue101_5() {
    final Expression ¢ = into.i("3+4+5+6");
    assertEquals(5, metrics.nodes(¢));
  }

  @Test public void issue128_6() {
    final Expression ¢ = into.e("a==4 ? 34 : 56");
    assertEquals(6, metrics.nodes(¢));
  }

  @Test public void issue128_7() {
    final Expression ¢ = into.e("a==4 ? 34 : 56+34");
    assertEquals(8, metrics.nodes(¢));
  }

  @Test public void issue128_8() {
    final Expression ¢ = into.e("a==4 ? 34 : 56+34+99");
    assertEquals(9, metrics.nodes(¢));
  }

  @Test public void issue128_9() {
    final Expression ¢ = into.e("!f.g(X,false)||a.b.e(m.h())");
    assertEquals(10, metrics.nodes(¢));
  }

  @Test public void issue129_10() {
    final Expression ¢ = into.e("g(false)||a(h())");
    assertEquals(5, metrics.nodes(¢));
  }

  @Test public void issue128_11() {
    final Expression ¢ = into.i("3+4");
    assertEquals(1, metrics.internals(¢));
  }

  @Test public void issue128_12() {
    final Expression ¢ = into.i("3+4");
    assertEquals(2, metrics.leaves(¢));
  }

  @Test public void issue128_13() {
    final Expression ¢ = into.e("a==4 ? 34 : 56");
    assertEquals(2, metrics.internals(¢));
  }

  @Test public void issue128_14() {
    final Expression ¢ = into.e("a==4 ? 34 : 56");
    assertEquals(4, metrics.leaves(¢));
  }

  @Test public void issue128_15() {
    final Expression ¢ = into.e("1+2");
    assertEquals(2, metrics.dexterity(¢));
  }

  @Test public void issue128_16() {
    final Expression ¢ = into.e("a+2");
    assertEquals(3, metrics.dexterity(¢));
  }

  @Test public void issue128_17() {
    final Expression ¢ = into.e("g(false)||a(h())");
    assertEquals(3, metrics.dexterity(¢));
  }

  @Test public void issue128_18() {
    final Expression ¢ = into.e("a==4 ? (34++) : 56+34+99");
    assertEquals(5, metrics.dexterity(¢));
  }
}
