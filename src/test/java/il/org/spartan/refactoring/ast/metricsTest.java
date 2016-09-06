package il.org.spartan.refactoring.ast;

import static org.junit.Assert.*;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.refactoring.engine.*;

@SuppressWarnings({ "static-method", "javadoc", "boxing" })
public class metricsTest {
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
    assertEquals(7,  metrics.nodes(¢));
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
    assertEquals(8,metrics.nodes(¢));
  }

  @Test public void issue128_8() {
    final Expression ¢ = into.e("a==4 ? 34 : 56+34+99");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(9, (int) recurse.preVisit(accum));
  }

  @Test public void issue128_9() {
    final Expression ¢ = into.e("!f.g(X,false)||a.b.e(m.h())");
    assertEquals(10,metrics.nodes(¢));
  }

  @Test public void issue129_9() {
    final Expression ¢ = into.e("g(false)||a(h())");
    assertEquals(5, metrics.nodes(¢));
  }
  
  @Test public void issue128_10() {
    final Expression ¢ = into.i("3+4");
    assertEquals(1, metrics.internals(¢));
  }
  
  @Test public void issue128_11() {
    final Expression ¢ = into.i("3+4");
    assertEquals(2, metrics.leaves(¢));
  }
  
  @Test public void issue128_12() {
    final Expression ¢ = into.e("a==4 ? 34 : 56");
    assertEquals(2, metrics.internals(¢));
  }
  
  @Test public void issue128_13() {
    final Expression ¢ = into.e("a==4 ? 34 : 56");
    assertEquals(4, metrics.leaves(¢));
  }

}
