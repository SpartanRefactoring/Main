package il.org.spartan.spartanizer.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;

@SuppressWarnings({ "javadoc", "static-method" }) public class PlantTest {
  @Test public void plantIntoLess() {
    azzert.that(subject.pair(into.e("a + 2"), into.e("b")).to(InfixExpression.Operator.LESS), iz("a+2<b"));
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
    final Expression e = into.e("2");
    final make.PlantingExpression plant = make.plant(e);
    plant.into(e.getAST().newReturnStatement());
    azzert.that(plant.into(e.getAST().newReturnStatement()), iz("2"));
  }

  @Test public void plus() {
    final Expression e = into.e("a + 2 < b");
    final Expression plus = findFirst.firstPlus(e);
    azzert.that(plus + "", stringType.isNot(plus), is(true));
    azzert.that(e + "", stringType.isNot(plus), is(true));
  }
}
