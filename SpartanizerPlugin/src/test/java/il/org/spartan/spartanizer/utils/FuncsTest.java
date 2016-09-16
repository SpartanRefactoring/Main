package il.org.spartan.spartanizer.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.spartanizer.engine.into.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.mockito.Mockito.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** A test suite for class {@link step}
 * @author Yossi Gil
 * @since 2015-07-18
 * @see step */
@SuppressWarnings({ "static-method", "javadoc" }) @FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class FuncsTest {
  @Test public void arrayOfInts() {
    Type t = t("int[][] __;");
    assert t != null;
    System.out.println(t);
    azzert.that(spartan.shorten(t), equalTo("iss"));
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
    assertEquals(i, az.comparison(i));
  }

  @Test public void asComparisonTypicalInfixIsNotNull() {
    final InfixExpression e = mock(InfixExpression.class);
    doReturn(GREATER).when(e).getOperator();
    assert az.comparison(e) != null;
  }

  @Test public void chainComparison() {
    assertEquals("c", right(i("a == true == b == c")) + "");
  }

  @Test public void countNonWhiteCharacters() {
    azzert.that(metrics.countNonWhites(e("1 + 23     *456 + \n /* aa */ 7890")), is(13));
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

  @Test public void listOfInts() {
    azzert.that(spartan.shorten(t("List<Set<Integer>> __;")), equalTo("iss"));
  }

  @Test public void sameOfNullAndSomething() {
    assert !wizard.same(null, e("a"));
  }

  @Test public void sameOfNulls() {
    assert wizard.same((ASTNode) null, (ASTNode) null);
  }

  @Test public void sameOfSomethingAndNull() {
    assert !wizard.same(e("a"), (Expression) null);
  }

  @Test public void sameOfTwoExpressionsIdentical() {
    assert wizard.same(e("a+b"), e("a+b"));
  }

  @Test public void sameOfTwoExpressionsNotSame() {
    assert !wizard.same(e("a+b+c"), e("a+b"));
  }

  @Test public void shortNameASTRewriter() {
    azzert.that(spartan.shorten(t("ASTRewriter __;")), equalTo("r"));
  }

  @Test public void shortNameDouble() {
    azzert.that(spartan.shorten(t("double __;")), equalTo("d"));
  }

  @Test public void shortNameExpression() {
    azzert.that(spartan.shorten(t("Expression __;")), equalTo("x"));
  }

  @Test public void shortNameExpressions() {
    azzert.that(spartan.shorten(t("Expression[] __;")), equalTo("xs"));
  }

  @Test public void shortNameExpressionsList() {
    azzert.that(spartan.shorten(t("List<Expression> __;")), equalTo("xs"));
  }

  @Test public void shortNameInfrastructure() {
    azzert.that(spartan.shorten(t("int __;")), equalTo("i"));
  }

  @Test public void shortNameQualifiedType() {
    azzert.that(spartan.shorten(t("org.eclipse.jdt.core.dom.InfixExpression __;")), equalTo("x"));
  }

  private Type t(final String codeFragment) {
    return findFirst.firstType(s(codeFragment));
  }
}
