package il.org.spartan.spartanizer.utils;

import static fluent.ly.azzert.is;
import static fluent.ly.azzert.iz;
import static il.org.spartan.spartanizer.engine.parse.e;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.LESS;

import org.eclipse.jdt.core.dom.Expression;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.findFirst;
import il.org.spartan.spartanizer.engine.parse;
import il.org.spartan.spartanizer.engine.type;

@SuppressWarnings({ "javadoc", "static-method" })
public final class PlantTest {
  @Test public void plantIntoLess() {
    azzert.that(subject.pair(parse.e("a + 2"), parse.e("b")).to(LESS), iz("a+2<b"));
  }
  @Test public void plantIntoNull() {
    final String s = "a?b:c";
    final Expression e = e(s);
    assert e != null;
    final Expression e1 = make.plant(e).into(null);
    assert e1 != null;
    azzert.that(e1, iz(s));
  }
  @Test public void plantIntoReturn() {
    final Expression e = parse.e("2");
    final make.PlantingExpression plant = make.plant(e);
    plant.into(e.getAST().newReturnStatement());
    azzert.that(plant.into(e.getAST().newReturnStatement()), iz("2"));
  }
  @Test public void plus() {
    final Expression e = parse.e("a + 2 <b"), plus = findFirst.infixPlus(e);
    azzert.that(plus + "", type.isNotString(plus), is(true));
    azzert.that(e + "", type.isNotString(plus), is(true));
  }
}
