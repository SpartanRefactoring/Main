package il.org.spartan.spartanizer.engine;

import static org.junit.Assert.*;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Dor Ma'ayan
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc", "boxing" }) public final class RecurserTest {
  @Test public void issue101_1() {
    assertEquals(3, (int) new Recurser<>(into.i("3+4"), 0).preVisit((x) -> (1 + x.getCurrent())));
  }

  @Test public void issue101_10() {
    assertEquals(9, (int) new Recurser<>(into.e("a==4 ? 34 : 56+34+99"), 0).preVisit((x) -> (1 + x.getCurrent())));
  }

  @Test public void issue101_11() {
    assertEquals(10, (int) new Recurser<>(into.e("!f.g(X,false)||a.b.e(m.h())"), 0).preVisit((x) -> (1 + x.getCurrent())));
  }

  @Test public void issue101_12() {
    assertEquals(5, (int) new Recurser<>(into.e("g(false)||a(h())"), 0).preVisit((x) -> (1 + x.getCurrent())));
  }

  @Test public void issue101_13() {
    final Expression ¢ = into.e("56");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Consumer<Recurser<Integer>> changeToken = (x) -> {
      if (x.getRoot().getNodeType() == ASTNode.NUMBER_LITERAL)//
        ((NumberLiteral) x.getRoot()).setToken("99");
    };
    recurse.postVisit(changeToken);
    assertEquals("99", ¢ + "");
  }

  @Test public void issue101_14() {
    assertEquals(7, (int) new Recurser<>(into.i("3+(4*5)+6"), 0).postVisit((x) -> (1 + x.getCurrent())));
  }

  @Test public void issue101_15() {
    final Expression ¢ = into.e("56+87");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Consumer<Recurser<Integer>> changeToken = (x) -> {
      if (x.getRoot().getNodeType() == ASTNode.NUMBER_LITERAL)//
        ((NumberLiteral) x.getRoot()).setToken("99");
    };
    recurse.preVisit(changeToken);
    assertEquals("99 + 99", ¢ + "");
  }

  @Test public void issue101_16() {
    final Expression ¢ = into.e("b==true ? 67 : 7");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Consumer<Recurser<Integer>> changeToken = (x) -> {
      if (x.getRoot().getNodeType() == ASTNode.NUMBER_LITERAL)//
        ((NumberLiteral) x.getRoot()).setToken("56");
    };
    recurse.preVisit(changeToken);
    assertEquals("b == true ? 56 : 56", ¢ + "");
  }

  @Test public void issue101_17() {
    final Expression ¢ = into.e("b==true ? 67 : 7");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Consumer<Recurser<Integer>> changeToken = (x) -> {
      if (x.getRoot().getNodeType() == ASTNode.BOOLEAN_LITERAL)//
        ((BooleanLiteral) x.getRoot()).setBooleanValue(false);
    };
    recurse.preVisit(changeToken);
    assertEquals("b == false ? 67 : 7", ¢ + "");
  }

  @Test public void issue101_18() {
    final Expression ¢ = into.e("b==true ? 67 : 7");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Consumer<Recurser<Integer>> changeToken = (x) -> {
      if (x.getRoot().getNodeType() == ASTNode.BOOLEAN_LITERAL)//
        ((BooleanLiteral) x.getRoot()).setBooleanValue(false);
    };
    recurse.postVisit(changeToken);
    assertEquals("b == false ? 67 : 7", ¢ + "");
  }

  @Test public void issue101_19() {
    final Expression ¢ = into.e("56+87*234+21l");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Consumer<Recurser<Integer>> changeToken = (x) -> {
      if (x.getRoot().getNodeType() == ASTNode.NUMBER_LITERAL)//
        ((NumberLiteral) x.getRoot()).setToken("99");
    };
    recurse.preVisit(changeToken);
    assertEquals("99 + 99 * 99 + 99", ¢ + "");
  }

  @Test public void issue101_2() {
    assertEquals(3, (int) new Recurser<>(into.i("3+4"), 0).postVisit((x) -> (1 + x.getCurrent())));
  }

  @Test public void issue101_3() {
    assertEquals(7, (int) new Recurser<>(into.i("5*6+43*2"), 0).preVisit((x) -> (1 + x.getCurrent())));
  }

  @Test public void issue101_4() {
    assertEquals(11, (int) new Recurser<>(into.i("3+4*4+6*7+8"), 0).preVisit((x) -> (1 + x.getCurrent())));
  }

  @Test public void issue101_5() {
    assertEquals(11, (int) new Recurser<>(into.i("3+4*4+6*7+8"), 0).postVisit((x) -> (1 + x.getCurrent())));
  }

  @Test public void issue101_6() {
    assertEquals(5, (int) new Recurser<>(into.i("3+4+5+6"), 0).postVisit((x) -> (1 + x.getCurrent())));
  }

  @Test public void issue101_7() {
    assertEquals(6, (int) new Recurser<>(into.e("a==4 ? 34 : 56"), 0).postVisit((x) -> (1 + x.getCurrent())));
  }

  @Test public void issue101_8() {
    assertEquals(8, (int) new Recurser<>(into.e("a==4 ? 34 : 56+34"), 0).preVisit((x) -> (1 + x.getCurrent())));
  }

  @Test public void issue101_9() {
    final Expression ¢ = into.e("56");
    final Recurser<Integer> recurse = new Recurser<>(¢, 0);
    final Consumer<Recurser<Integer>> changeToken = (x) -> {
      if (x.getRoot().getNodeType() == ASTNode.NUMBER_LITERAL)//
        ((NumberLiteral) x.getRoot()).setToken("99");
    };
    recurse.preVisit(changeToken);
    assertEquals("99", ¢ + "");
  }
}
