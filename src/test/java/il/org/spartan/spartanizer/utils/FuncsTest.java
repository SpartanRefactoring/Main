package il.org.spartan.spartanizer.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.mockito.Mockito.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.NotNull;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** A test suite for class {@link step}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-07-18
 * @see step */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class FuncsTest {
  @Test public void arrayOfInts() {
    @NotNull final Type t = t("int[][] __;");
    assert t != null;
    azzert.that(namer.shorten(t), equalTo("iss"));
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

  @Test public void asComparisonTypicalInfixIsNotNull() {
    final InfixExpression e = mock(InfixExpression.class);
    doReturn(GREATER).when(e).getOperator();
    assert az.comparison(e) != null;
  }

  @Test public void chainComparison() {
    azzert.that(right(i("a == true == b == c")) + "", is("c"));
  }

  @Test public void countNonWhiteCharacters() {
    azzert.that(count.nonWhiteCharacters(e("1 + 23     *456 + \n /* aa */ 7890")), is(13));
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

  @NotNull
  private Type t(@NotNull final String codeFragment) {
    return findFirst.instanceOf(Type.class).in(s(codeFragment));
  }
}
