package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.azzert.*;
import static il.org.spartan.azzert.is;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.Restructure.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.utils.*;

@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class ExpressionFlatten {
  @Test public void flattenExists() {
    flatten(i("1+2"));
  }

  @Test public void flattenIsDistinct() {
    final InfixExpression e = i("1+2");
    azzert.that(flatten(e), is(not(e)));
  }

  @Test public void flattenIsNotNull() {
    azzert.that(flatten(i("1+2")), is(not(nullValue())));
  }

  @Test public void flattenIsSame() {
    final InfixExpression e = i("1+2");
    azzert.that(flatten(e).toString(), is(e.toString()));
  }

  @Test public void flattenLeftArgument() {
    azzert.that(left(flatten(i("1+2"))).toString(), is("1"));
  }

  @Test public void flattenOfDeepParenthesisIsCorrect() {
    azzert.that(flatten(i("(((1+2)))+(((3 + (4+5))))")).toString(), is("1 + 2 + 3+ 4+ 5"));
  }

  @Test public void flattenOfDeepParenthesisSize() {
    azzert.that(flatten(i("(1+(2))+(3)")).extendedOperands().size(), is(1));
  }

  @Test public void flattenOfDeepParenthesOtherOperatorsisIsCorrect() {
    azzert.that(flatten(i("(((1+2)))+(((3 + (4*5))))")).toString(), is("1 + 2 + 3+ 4 * 5"));
  }

  @Test public void flattenOfParenthesis() {
    azzert.that(flatten(i("1+2+(3)")).extendedOperands().size(), is(1));
  }

  @Test public void flattenOfTrivialDoesNotAddOperands() {
    azzert.that(i("1+2").extendedOperands().size(), is(0));
  }

  @Test public void hasExtendedOperands() {
    azzert.that(i("1+2").hasExtendedOperands(), is(false));
  }

  @Test public void isNotStringInfixFalse() {
    azzert.nay(Is.notString(i("1+f")));
  }

  @Test public void isNotStringInfixPlain() {
    azzert.nay(Is.notString(e("1+f")));
  }

  @Test public void leftOperandIsNotString() {
    azzert.aye(Is.notString(left(i("1+2"))));
  }

  @Test public void leftOperandIsNumeric() {
    azzert.aye(Is.numericLiteral(left(i("1+2"))));
  }

  @Test public void leftOperandIsOne() {
    azzert.that(left(i("1+2")).toString(), is("1"));
  }

  @Test public void leftOperandNotNull() {
    azzert.notNull(left(i("1+2")));
  }

  @Test public void rightOperandNotNull() {
    azzert.notNull(left(i("1+2")));
  }
}