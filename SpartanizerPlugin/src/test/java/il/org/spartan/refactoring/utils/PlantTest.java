package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Into.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings({ "javadoc", "static-method" }) public class PlantTest {
  @Test public void plantIntoLess() {
    azzert.that(subject.pair(Into.e("a + 2"), Into.e("b")).to(InfixExpression.Operator.LESS), iz("a+2<b"));
  }
  @Test public void plantIntoNull() {
    final String s = "a?b:c";
    final Expression e = e(s);
    azzert.notNull(e);
    final Expression e1 = new Plant(e).into(null);
    azzert.notNull(e1);
    azzert.that(e1, iz(s));
  }
  @Test public void plantIntoReturn() {
    final Expression e = Into.e("2");
    final Plant plant = new Plant(e);
    plant.into(e.getAST().newReturnStatement());
    azzert.that(plant.into(e.getAST().newReturnStatement()), iz("2"));
  }
  @Test public void plus() {
    final Expression e = Into.e("a + 2 < b");
    final Expression plus = extract.firstPlus(e);
    azzert.that(plus.toString(), Is.notString(plus), is(true));
    azzert.that(e.toString(), Is.notString(plus), is(true));
  }
}
