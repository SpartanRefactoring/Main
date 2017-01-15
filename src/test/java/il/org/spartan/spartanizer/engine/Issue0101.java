package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;

/** @author Dor Ma'ayan
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc", "boxing" })
public final class Issue0101 {
  @Test public void a_1() {
    azzert.that(new Recurser<>(into.i("3+4"), 0).preVisit((x) -> (1 + x.getCurrent())), is(3));
  }

  @Test public void a_10() {
    azzert.that(new Recurser<>(into.e("a==4 ? 34 : 56+34+99"), 0).preVisit((x) -> (1 + x.getCurrent())), is(9));
  }

  @Test public void a_11() {
    azzert.that(new Recurser<>(into.e("!f.g(X,false)||a.b.e(m.h())"), 0).preVisit((x) -> (1 + x.getCurrent())), is(10));
  }

  @Test public void a_12() {
    azzert.that(new Recurser<>(into.e("g(false)||a(h())"), 0).preVisit((x) -> (1 + x.getCurrent())), is(5));
  }

  @Test public void a_13() {
    final Expression ¢ = into.e("56");
    new Recurser<>(¢, 0).postVisit((x) -> {
      if (x.getRoot().getNodeType() == ASTNode.NUMBER_LITERAL)
        ((NumberLiteral) x.getRoot()).setToken("99");
    });
    azzert.that(¢ + "", is("99"));
  }

  @Test public void a_14() {
    azzert.that(new Recurser<>(into.i("3+(4*5)+6"), 0).postVisit((x) -> (1 + x.getCurrent())), is(7));
  }

  @Test public void a_15() {
    final Expression ¢ = into.e("56+87");
    new Recurser<>(¢, 0).preVisit((x) -> {
      if (x.getRoot().getNodeType() == ASTNode.NUMBER_LITERAL)
        ((NumberLiteral) x.getRoot()).setToken("99");
    });
    azzert.that(¢ + "", is("99 + 99"));
  }

  @Test public void a_16() {
    final Expression ¢ = into.e("b==true ? 67 : 7");
    new Recurser<>(¢, 0).preVisit((x) -> {
      if (x.getRoot().getNodeType() == ASTNode.NUMBER_LITERAL)
        ((NumberLiteral) x.getRoot()).setToken("56");
    });
    azzert.that(¢ + "", is("b == true ? 56 : 56"));
  }

  @Test public void a_17() {
    final Expression ¢ = into.e("b==true ? 67 : 7");
    new Recurser<>(¢, 0).preVisit((x) -> {
      if (x.getRoot().getNodeType() == ASTNode.BOOLEAN_LITERAL)
        ((BooleanLiteral) x.getRoot()).setBooleanValue(false);
    });
    azzert.that(¢ + "", is("b == false ? 67 : 7"));
  }

  @Test public void a_18() {
    final Expression ¢ = into.e("b==true ? 67 : 7");
    new Recurser<>(¢, 0).postVisit((x) -> {
      if (x.getRoot().getNodeType() == ASTNode.BOOLEAN_LITERAL)
        ((BooleanLiteral) x.getRoot()).setBooleanValue(false);
    });
    azzert.that(¢ + "", is("b == false ? 67 : 7"));
  }

  @Test public void a_19() {
    final Expression ¢ = into.e("56+87*234+21l");
    new Recurser<>(¢, 0).preVisit((x) -> {
      if (x.getRoot().getNodeType() == ASTNode.NUMBER_LITERAL)
        ((NumberLiteral) x.getRoot()).setToken("99");
    });
    azzert.that(¢ + "", is("99 + 99 * 99 + 99"));
  }

  @Test public void a_2() {
    azzert.that(new Recurser<>(into.i("3+4"), 0).postVisit((x) -> (1 + x.getCurrent())), is(3));
  }

  @Test public void a_3() {
    azzert.that(new Recurser<>(into.i("5*6+43*2"), 0).preVisit((x) -> (1 + x.getCurrent())), is(7));
  }

  @Test public void a_4() {
    azzert.that(new Recurser<>(into.i("3+4*4+6*7+8"), 0).preVisit((x) -> (1 + x.getCurrent())), is(11));
  }

  @Test public void a_5() {
    azzert.that(new Recurser<>(into.i("3+4*4+6*7+8"), 0).postVisit((x) -> (1 + x.getCurrent())), is(11));
  }

  @Test public void a_6() {
    azzert.that(new Recurser<>(into.i("3+4+5+6"), 0).postVisit((x) -> (1 + x.getCurrent())), is(5));
  }

  @Test public void a_7() {
    azzert.that(new Recurser<>(into.e("a==4 ? 34 : 56"), 0).postVisit((x) -> (1 + x.getCurrent())), is(6));
  }

  @Test public void a_8() {
    azzert.that(new Recurser<>(into.e("a==4 ? 34 : 56+34"), 0).preVisit((x) -> (1 + x.getCurrent())), is(8));
  }

  @Test public void a_9() {
    final Expression ¢ = into.e("56");
    new Recurser<>(¢, 0).preVisit((x) -> {
      if (x.getRoot().getNodeType() == ASTNode.NUMBER_LITERAL)
        ((NumberLiteral) x.getRoot()).setToken("99");
    });
    azzert.that(¢ + "", is("99"));
  }
}
