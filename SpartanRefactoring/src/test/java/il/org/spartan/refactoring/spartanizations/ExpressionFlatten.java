package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.hamcrest.CoreMatchers.*;
import static il.org.spartan.hamcrest.MatcherAssert.*;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.Restructure.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import il.org.spartan.hamcrest.*;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

@SuppressWarnings({ "javadoc", "static-method" })//
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
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
    assertThat(Is.notString(i("1+f")), is(false));
  }
  @Test public void isNotStringInfixPlain() {
    assertThat(Is.notString(e("1+f")), is(false));
  }
  @Test public void leftOperandIsNotString() {
    JunitHamcrestWrappper.assertTrue(Is.notString(left(i("1+2"))));
  }
  @Test public void leftOperandIsNumeric() {
    JunitHamcrestWrappper.assertTrue(Is.numericLiteral(left(i("1+2"))));
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