package il.org.spartan.spartanizer.utils;

import static fluent.ly.azzert.equalTo;
import static fluent.ly.azzert.is;
import static il.org.spartan.spartanizer.ast.navigate.step.right;
import static il.org.spartan.spartanizer.engine.parse.e;
import static il.org.spartan.spartanizer.engine.parse.i;
import static il.org.spartan.spartanizer.engine.parse.s;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_AND;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_OR;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.GREATER;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.GREATER_EQUALS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.Type;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.navigate.countOf;
import il.org.spartan.spartanizer.ast.navigate.findFirst;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.engine.nominal.abbreviate;

/** A test suite for class {@link step}
 * @author Yossi Gil
 * @since 2015-07-18
 * @see step */
@SuppressWarnings({ "static-method", "javadoc" })
public final class FuncsTest {
  @Test public void arrayOfInts() {
    final Type t = t("int[][] __;");
    assert t != null;
    azzert.that(abbreviate.it(t), equalTo("iss"));
  }
  @Test public void asComparisonPrefixlExpression() {
    final PrefixExpression p = mock(PrefixExpression.class);
    doReturn(PrefixExpression.Operator.NOT).when(p).getOperator();
    azzert.isNull(az.comparison(p));
  }
  @Test public void asComparisonTypicalExpression() {
    final InfixExpression i = mock(InfixExpression.class);
    doReturn(GREATER).when(i).getOperator();
    assert az.comparison(i) != null;
  }
  @Test public void asComparisonTypicalExpressionFalse() {
    final InfixExpression i = mock(InfixExpression.class);
    doReturn(CONDITIONAL_OR).when(i).getOperator();
    azzert.isNull(az.comparison(i));
  }
  @Test public void asComparisonTypicalInfixFalse() {
    final InfixExpression i = mock(InfixExpression.class);
    doReturn(CONDITIONAL_AND).when(i).getOperator();
    azzert.isNull(az.comparison(i));
  }
  @Test public void asComparisonTypicalInfixIsCorrect() {
    final InfixExpression i = mock(InfixExpression.class);
    doReturn(GREATER).when(i).getOperator();
    azzert.that(az.comparison(i), is(i));
  }
  @Test public void asComparisonTypicalInfixIsNonNull() {
    final InfixExpression e = mock(InfixExpression.class);
    doReturn(GREATER).when(e).getOperator();
    assert az.comparison(e) != null;
  }
  @Test public void chainComparison() {
    azzert.that(right(i("a == true == b == c")) + "", is("c"));
  }
  @Test public void countNonWhiteCharacters() {
    azzert.that(countOf.nonWhiteCharacters(e("1 + 23     *456 + \n /* aa */ 7890")), is(13));
  }
  @Test public void findFirstType() {
    assert t("int __;") != null;
  }
  @Test public void isDeMorganAND() {
    assert iz.deMorgan(CONDITIONAL_AND);
  }
  @Test public void isDeMorganGreater() {
    assert !iz.deMorgan(GREATER);
  }
  @Test public void isDeMorganGreaterEuals() {
    assert !iz.deMorgan(GREATER_EQUALS);
  }
  @Test public void isDeMorganOR() {
    assert iz.deMorgan(CONDITIONAL_OR);
  }
  private Type t(final String codeFragment) {
    return findFirst.instanceOf(Type.class).in(s(codeFragment));
  }
}
