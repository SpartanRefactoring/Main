package il.org.spartan.refactoring.utils;

import static il.org.spartan.hamcrest.SpartanAssert.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

@SuppressWarnings({ "javadoc", "static-method" })//
public class PlantTest {
  @Test public void plantIntoLess() {
    assertThat(Subject.pair(Into.e("a + 2"), Into.e("b")).to(InfixExpression.Operator.LESS), iz("a+2<b"));
  }
  @Test public void plantIntoNull() {
    final String s = "a?b:c";
    final Expression e = e(s);
    assertThat(e, notNullValue());
    final Expression e1 = new Plant(e).into(null);
    assertThat(e1, notNullValue());
    assertThat(e1, iz(s));
  }
  @Test public void plantIntoReturn() {
    final Expression e = Into.e("2");
    final Plant plant = new Plant(e);
    plant.into(e.getAST().newReturnStatement());
    assertThat(plant.into(e.getAST().newReturnStatement()), iz("2"));
  }
  @Test public void plus() {
    final Expression e = Into.e("a + 2 < b");
    final Expression plus = Extract.firstPlus(e);
    assertThat(plus.toString(), Is.notString(plus), is(true));
    assertThat(e.toString(), Is.notString(plus), is(true));
  }
}
