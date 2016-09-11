package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.java.*;

@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class flattenTest {
  @Test public void flattenExists() {
    flatten.of(i("1+2"));
  }

  @Test public void flattenIsDistinct() {
    final InfixExpression e = i("1+2");
    azzert.that(flatten.of(e), is(not(e)));
  }

  @Test public void flattenIsNotNull() {
    azzert.that(flatten.of(i("1+2")), is(not(nullValue())));
  }

  @Test public void flattenIsSame() {
    final InfixExpression e = i("1+2");
    azzert.that(flatten.of(e) + "", is(e + ""));
  }

  @Test public void flattenLeftArgument() {
    azzert.that(step.left(flatten.of(i("1+2"))) + "", is("1"));
  }

  @Test public void flattenOfDeepParenthesisIsCorrect() {
    azzert.that(flatten.of(i("(((1+2)))+(((3 + (4+5))))")) + "", is("1 + 2 + 3+ 4+ 5"));
  }

  @Test public void flattenOfDeepParenthesisSize() {
    azzert.that(flatten.of(i("(1+(2))+(3)")).extendedOperands().size(), is(1));
  }

  @Test public void flattenOfDeepParenthesOtherOperatorsisIsCorrect() {
    azzert.that(flatten.of(i("(((1+2)))+(((3 + (4*5))))")) + "", is("1 + 2 + 3+ 4 * 5"));
  }

  @Test public void flattenOfParenthesis() {
    azzert.that(flatten.of(i("1+2+(3)")).extendedOperands().size(), is(1));
  }

  @Test public void flattenOfTrivialDoesNotAddOperands() {
    azzert.that(i("1+2").extendedOperands().size(), is(0));
  }

  @Test public void hasExtendedOperands() {
    azzert.that(i("1+2").hasExtendedOperands(), is(false));
  }

  @Test public void isNotStringInfixFalse() {
    azzert.nay(stringType.isNot(i("1+f")));
  }

  @Test public void isNotStringInfixPlain() {
    azzert.nay(stringType.isNot(e("1+f")));
  }

  @Test public void leftOperandIsNotString() {
    azzert.aye(stringType.isNot(step.left(i("1+2"))));
  }

  @Test public void leftOperandIsNumeric() {
    azzert.aye(iz.numericLiteral(step.left(i("1+2"))));
  }

  @Test public void leftOperandIsOne() {
    azzert.that(step.left(i("1+2")) + "", is("1"));
  }

  @Test public void leftOperandNotNull() {
    assert step.left(i("1+2")) != null;
  }

  @Test public void rightOperandNotNull() {
    assert step.right(i("1+2")) != null;
  }
}