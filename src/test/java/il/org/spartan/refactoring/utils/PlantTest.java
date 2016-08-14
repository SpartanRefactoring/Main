package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Into.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

@SuppressWarnings({ "javadoc", "static-method" }) //
public class PlantTest {
  @Test public void plantIntoLess() {
    that(Subject.pair(Into.e("a + 2"), Into.e("b")).to(InfixExpression.Operator.LESS), iz("a+2<b"));
  }
  @Test public void plantIntoNull() {
    final String s = "a?b:c";
    final Expression e = e(s);
    that(e, notNullValue());
    final Expression e1 = new Plant(e).into(null);
    that(e1, notNullValue());
    that(e1, iz(s));
  }
  @Test public void plantIntoReturn() {
    final Expression e = Into.e("2");
    final Plant plant = new Plant(e);
    plant.into(e.getAST().newReturnStatement());
    that(plant.into(e.getAST().newReturnStatement()), iz("2"));
  }
  @Test public void plus() {
    final Expression e = Into.e("a + 2 < b");
    final Expression plus = extract.firstPlus(e);
    that(plus.toString(), Is.notString(plus), is(true));
    that(e.toString(), Is.notString(plus), is(true));
  }
}
