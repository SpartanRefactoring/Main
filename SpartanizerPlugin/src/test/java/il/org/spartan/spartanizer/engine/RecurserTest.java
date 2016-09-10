package il.org.spartan.spartanizer.engine;

import static org.junit.Assert.*;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Dor Ma'ayan
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc", "boxing" }) public class RecurserTest {
  @Test public void issue101__1() {
    final Expression ¢ = into.i("3+4");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(3, (int) recurse.preVisit(accum));
  }

  @Test public void issue101__2() {
    final Expression ¢ = into.i("3+4");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(3, (int) recurse.postVisit(accum));
  }

  @Test public void issue101__3() {
    final Expression ¢ = into.i("5*6+43*2");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(7, (int) recurse.preVisit(accum));
  }

  @Test public void issue101__4() {
    final Expression ¢ = into.i("3+4*4+6*7+8");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(11, (int) recurse.preVisit(accum));
  }

  @Test public void issue101__5() {
    final Expression ¢ = into.i("3+4*4+6*7+8");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(11, (int) recurse.postVisit(accum));
  }

  @Test public void issue101__6() {
    final Expression ¢ = into.i("3+4+5+6");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(5, (int) recurse.postVisit(accum));
  }

  @Test public void issue101__7() {
    final Expression ¢ = into.e("a==4 ? 34 : 56");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(6, (int) recurse.postVisit(accum));
  }

  @Test public void issue101__8() {
    final Expression ¢ = into.e("a==4 ? 34 : 56+34");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(8, (int) recurse.preVisit(accum));
  }

  @Test public void issue101__9() {
    final Expression ¢ = into.e("56");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Consumer<Recurser<Integer>> changeToken = (x) -> {
      if (x.getRoot().getNodeType() == ASTNode.NUMBER__LITERAL)//
        ((NumberLiteral) x.getRoot()).setToken("99");
    };
    recurse.preVisit(changeToken);
    assertEquals("99", ¢ + "");
  }

  @Test public void issue101__10() {
    final Expression ¢ = into.e("a==4 ? 34 : 56+34+99");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(9, (int) recurse.preVisit(accum));
  }

  @Test public void issue101__11() {
    final Expression ¢ = into.e("!f.g(X,false)||a.b.e(m.h())");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(10, (int) recurse.preVisit(accum));
  }

  @Test public void issue101__12() {
    final Expression ¢ = into.e("g(false)||a(h())");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(5, (int) recurse.preVisit(accum));
  }

  @Test public void issue101__13() {
    final Expression ¢ = into.e("56");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Consumer<Recurser<Integer>> changeToken = (x) -> {
      if (x.getRoot().getNodeType() == ASTNode.NUMBER__LITERAL)//
        ((NumberLiteral) x.getRoot()).setToken("99");
    };
    recurse.postVisit(changeToken);
    assertEquals("99", ¢ + "");
  }

  @Test public void issue101__14() {
    final Expression ¢ = into.i("3+(4*5)+6");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Function<Recurser<Integer>, Integer> accum = (x) -> (1 + x.getCurrent());
    assertEquals(7, (int) recurse.postVisit(accum));
  }

  @Test public void issue101__15() {
    final Expression ¢ = into.e("56+87");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Consumer<Recurser<Integer>> changeToken = (x) -> {
      if (x.getRoot().getNodeType() == ASTNode.NUMBER__LITERAL)//
        ((NumberLiteral) x.getRoot()).setToken("99");
    };
    recurse.preVisit(changeToken);
    assertEquals("99 + 99", ¢ + "");
  }

  @Test public void issue101__16() {
    final Expression ¢ = into.e("b==true ? 67 : 7");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Consumer<Recurser<Integer>> changeToken = (x) -> {
      if (x.getRoot().getNodeType() == ASTNode.NUMBER__LITERAL)//
        ((NumberLiteral) x.getRoot()).setToken("56");
    };
    recurse.preVisit(changeToken);
    assertEquals("b == true ? 56 : 56", ¢ + "");
  }

  @Test public void issue101__17() {
    final Expression ¢ = into.e("b==true ? 67 : 7");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Consumer<Recurser<Integer>> changeToken = (x) -> {
      if (x.getRoot().getNodeType() == ASTNode.BOOLEAN__LITERAL)//
        ((BooleanLiteral) x.getRoot()).setBooleanValue(false);
    };
    recurse.preVisit(changeToken);
    assertEquals("b == false ? 67 : 7", ¢ + "");
  }

  @Test public void issue101__18() {
    final Expression ¢ = into.e("b==true ? 67 : 7");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Consumer<Recurser<Integer>> changeToken = (x) -> {
      if (x.getRoot().getNodeType() == ASTNode.BOOLEAN__LITERAL)//
        ((BooleanLiteral) x.getRoot()).setBooleanValue(false);
    };
    recurse.postVisit(changeToken);
    assertEquals("b == false ? 67 : 7", ¢ + "");
  }

  @Test public void issue101__19() {
    final Expression ¢ = into.e("56+87*234+21l");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Consumer<Recurser<Integer>> changeToken = (x) -> {
      if (x.getRoot().getNodeType() == ASTNode.NUMBER__LITERAL)//
        ((NumberLiteral) x.getRoot()).setToken("99");
    };
    recurse.preVisit(changeToken);
    assertEquals("99 + 99 * 99 + 99", ¢ + "");
  }
}
