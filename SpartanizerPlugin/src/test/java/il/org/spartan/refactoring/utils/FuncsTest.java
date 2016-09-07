package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.engine.ExpressionComparator.*;
import static il.org.spartan.refactoring.engine.into.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.mockito.Mockito.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.engine.*;

/** A test suite for class {@link step}
 * @author Yossi Gil
 * @since 2015-07-18
 * @see step */
@SuppressWarnings({ "static-method", "javadoc" }) @FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class FuncsTest {
  @Test public void arrayOfInts() {
    azzert.that(spartan.shorten(t("int[][] _;")), equalTo("iss"));
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
    assertEquals("c", "" + step.right(i("a == true == b == c")));
  }

  @Test public void countNonWhiteCharacters() {
    azzert.that(countNonWhites(e("1 + 23     *456 + \n /* aa */ 7890")), is(13));
  }

  @Test public void findFirstType() {
   assert null !=(t("int _;"));
  }

  @Test public void isDeMorganAND() {
    azzert.aye(iz.deMorgan(CONDITIONAL_AND));
  }

  @Test public void isDeMorganGreater() {
    azzert.nay(iz.deMorgan(GREATER));
  }

  @Test public void isDeMorganGreaterEuals() {
    azzert.nay(iz.deMorgan(GREATER_EQUALS));
  }

  @Test public void isDeMorganOR() {
    azzert.aye(iz.deMorgan(CONDITIONAL_OR));
  }

  @Test public void listOfInts() {
    azzert.that(spartan.shorten(t("List<Set<Integer>> _;")), equalTo("iss"));
  }

  @Test public void sameOfNullAndSomething() {
    azzert.nay(wizard.same(null, e("a")));
  }

  @Test public void sameOfNulls() {
    azzert.aye(wizard.same((ASTNode) null, (ASTNode) null));
  }

  @Test public void sameOfSomethingAndNull() {
    azzert.nay(wizard.same(e("a"), (Expression) null));
  }

  @Test public void sameOfTwoExpressionsIdentical() {
    azzert.aye(wizard.same(e("a+b"), e("a+b")));
  }

  @Test public void sameOfTwoExpressionsNotSame() {
    azzert.nay(wizard.same(e("a+b+c"), e("a+b")));
  }

  @Test public void shortNameASTRewriter() {
    azzert.that(spartan.shorten(t("ASTRewriter _;")), equalTo("r"));
  }

  @Test public void shortNameDouble() {
    azzert.that(spartan.shorten(t("double _;")), equalTo("d"));
  }

  @Test public void shortNameExpression() {
    azzert.that(spartan.shorten(t("Expression _;")), equalTo("x"));
  }

  @Test public void shortNameExpressions() {
    azzert.that(spartan.shorten(t("Expression[] _;")), equalTo("xs"));
  }

  @Test public void shortNameExpressionsList() {
    azzert.that(spartan.shorten(t("List<Expression> _;")), equalTo("xs"));
  }

  @Test public void shortNameInfrastructure() {
    azzert.that(spartan.shorten(t("int _;")), equalTo("i"));
  }

  @Test public void shortNameQualifiedType() {
    azzert.that(spartan.shorten(t("org.eclipse.jdt.core.dom.InfixExpression _;")), equalTo("x"));
  }

  private Type t(final String codeFragment) {
    return extract.firstType(s(codeFragment));
  }
}
