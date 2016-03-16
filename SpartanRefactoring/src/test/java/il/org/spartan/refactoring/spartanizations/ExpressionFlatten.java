package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.refactoring.utils.Funcs.left;
import static il.org.spartan.refactoring.utils.Restructure.flatten;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static il.org.spartan.refactoring.utils.Into.e;
import static il.org.spartan.refactoring.utils.Into.i;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import il.org.spartan.refactoring.utils.Is;

@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class ExpressionFlatten {
  @Test public void flattenExists() {
    flatten(i("1+2"));
  }
  @Test public void flattenIsDistinct() {
    final InfixExpression e = i("1+2");
    assertThat(flatten(e), is(not(e)));
  }
  @Test public void flattenIsNotNull() {
    assertThat(flatten(i("1+2")), is(not(nullValue())));
  }
  @Test public void flattenIsSame() {
    final InfixExpression e = i("1+2");
    assertThat(flatten(e).toString(), is(e.toString()));
  }
  @Test public void flattenLeftArgument() {
    assertThat(left(flatten(i("1+2"))).toString(), is("1"));
  }
  @Test public void flattenOfDeepParenthesisIsCorrect() {
    assertThat(flatten(i("(((1+2)))+(((3 + (4+5))))")).toString(), is("1 + 2 + 3+ 4+ 5"));
  }
  @Test public void flattenOfDeepParenthesisSize() {
    assertThat(flatten(i("(1+(2))+(3)")).extendedOperands().size(), is(1));
  }
  @Test public void flattenOfDeepParenthesOtherOperatorsisIsCorrect() {
    assertThat(flatten(i("(((1+2)))+(((3 + (4*5))))")).toString(), is("1 + 2 + 3+ 4 * 5"));
  }
  @Test public void flattenOfParenthesis() {
    assertThat(flatten(i("1+2+(3)")).extendedOperands().size(), is(1));
  }
  @Test public void flattenOfTrivialDoesNotAddOperands() {
    assertThat(i("1+2").extendedOperands().size(), is(0));
  }
  @Test public void hasExtendedOperands() {
    assertThat(i("1+2").hasExtendedOperands(), is(false));
  }
  @Test public void isNotStringInfixFalse() {
    assertFalse(Is.notString(i("1+f")));
  }
  @Test public void isNotStringInfixPlain() {
    assertFalse(Is.notString(e("1+f")));
  }
  @Test public void leftOperandIsNotString() {
    assertTrue(Is.notString(left(i("1+2"))));
  }
  @Test public void leftOperandIsNumeric() {
    assertTrue(Is.numericLiteral(left(i("1+2"))));
  }
  @Test public void leftOperandIsOne() {
    assertThat(left(i("1+2")).toString(), is("1"));
  }
  @Test public void leftOperandNotNull() {
    assertThat(left(i("1+2")), not(nullValue()));
  }
  @Test public void rightOperandNotNull() {
    assertThat(left(i("1+2")), not(nullValue()));
  }
}