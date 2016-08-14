package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.azzert.*;
import static il.org.spartan.azzert.is;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.Restructure.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import static il.org.spartan.refactoring.utils.Funcs.*;

import il.org.spartan.refactoring.utils.*;

@SuppressWarnings({ "javadoc", "static-method" })//
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
public class ExpressionFlatten {
  @Test public void flattenExists() {
    flatten(i("1+2"));
  }
  @Test public void flattenIsDistinct() {
    final InfixExpression e = i("1+2");
    that(flatten(e), is(not(e)));
  }
  @Test public void flattenIsNotNull() {
    that(flatten(i("1+2")), is(not(nullValue())));
  }
  @Test public void flattenIsSame() {
    final InfixExpression e = i("1+2");
    that(flatten(e).toString(), is(e.toString()));
  }
  @Test public void flattenLeftArgument() {
    that(left(flatten(i("1+2"))).toString(), is("1"));
  }
  @Test public void flattenOfDeepParenthesisIsCorrect() {
    that(flatten(i("(((1+2)))+(((3 + (4+5))))")).toString(), is("1 + 2 + 3+ 4+ 5"));
  }
  @Test public void flattenOfDeepParenthesisSize() {
    that(flatten(i("(1+(2))+(3)")).extendedOperands().size(), is(1));
  }
  @Test public void flattenOfDeepParenthesOtherOperatorsisIsCorrect() {
    that(flatten(i("(((1+2)))+(((3 + (4*5))))")).toString(), is("1 + 2 + 3+ 4 * 5"));
  }
  @Test public void flattenOfParenthesis() {
    that(flatten(i("1+2+(3)")).extendedOperands().size(), is(1));
  }
  @Test public void flattenOfTrivialDoesNotAddOperands() {
    that(i("1+2").extendedOperands().size(), is(0));
  }
  @Test public void hasExtendedOperands() {
    that(i("1+2").hasExtendedOperands(), is(false));
  }
  @Test public void isNotStringInfixFalse() {
    that(Is.notString(i("1+f")), is(false));
  }
  @Test public void isNotStringInfixPlain() {
    that(Is.notString(e("1+f")), is(false));
  }
  @Test public void leftOperandIsNotString() {
    that(Is.notString(left(i("1+2"))), is(true));
  }
  @Test public void leftOperandIsNumeric() {
    that(Is.numericLiteral(left(i("1+2"))), is(true));
  }
  @Test public void leftOperandIsOne() {
    that(left(i("1+2")).toString(), is("1"));
  }
  @Test public void leftOperandNotNull() {
    that(left(i("1+2")), not(nullValue()));
  }
  @Test public void rightOperandNotNull() {
    that(left(i("1+2")), not(nullValue()));
  }
}