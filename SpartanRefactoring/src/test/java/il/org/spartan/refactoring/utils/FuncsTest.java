package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.ExpressionComparator.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.mockito.Mockito.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

/**
 * A test suite for class {@link Funcs}
 *
 * @author Yossi Gil
 * @since 2015-07-18
 * @see Funcs
 */
@SuppressWarnings({ "static-method", "javadoc" }) @FixMethodOrder(MethodSorters.NAME_ASCENDING)//
public class FuncsTest {
  @Test public void arrayOfInts() {
    that(shortName(t("int[][] _;")), equalTo("iss"));
  }
  @Test public void asComparisonPrefixlExpression() {
    final PrefixExpression p = mock(PrefixExpression.class);
    doReturn(PrefixExpression.Operator.NOT).when(p).getOperator();
    assertNull(asComparison(p));
  }
  @Test public void asComparisonTypicalExpression() {
    final InfixExpression i = mock(InfixExpression.class);
    doReturn(GREATER).when(i).getOperator();
    that(asComparison(i), notNullValue());
  }
  @Test public void asComparisonTypicalExpressionFalse() {
    final InfixExpression i = mock(InfixExpression.class);
    doReturn(CONDITIONAL_OR).when(i).getOperator();
    assertNull(asComparison(i));
  }
  @Test public void asComparisonTypicalInfixFalse() {
    final InfixExpression i = mock(InfixExpression.class);
    doReturn(CONDITIONAL_AND).when(i).getOperator();
    assertNull(asComparison(i));
  }
  @Test public void asComparisonTypicalInfixIsCorrect() {
    final InfixExpression i = mock(InfixExpression.class);
    doReturn(GREATER).when(i).getOperator();
    that(asComparison(i), is(i));
  }
  @Test public void asComparisonTypicalInfixIsNotNull() {
    final InfixExpression e = mock(InfixExpression.class);
    doReturn(GREATER).when(e).getOperator();
    that(asComparison(e), notNullValue());
  }
  @Test public void chainComparison() {
    that(right(i("a == true == b == c")).toString(), is("c"));
  }
  @Test public void countNonWhiteCharacters() {
    that(countNonWhites(e("1 + 23     *456 + \n /* aa */ 7890")), is(13));
  }
  @Test public void findFirstType() {
    that(t("int _;"), notNullValue());
  }
  @Test public void isDeMorganAND() {
    that(Is.deMorgan(CONDITIONAL_AND), is(true));
  }
  @Test public void isDeMorganGreater() {
    that(Is.deMorgan(GREATER), is(false));
  }
  @Test public void isDeMorganGreaterEuals() {
    that(Is.deMorgan(GREATER_EQUALS), is(false));
  }
  @Test public void isDeMorganOR() {
    that(Is.deMorgan(CONDITIONAL_OR), is(true));
  }
  @Test public void listOfInts() {
    that(shortName(t("List<Set<Integer>> _;")), equalTo("iss"));
  }
  @Test public void sameOfNullAndSomething() {
    that(Funcs.same(null, e("a")), is(false));
  }
  @Test public void sameOfNulls() {
    that(Funcs.same((ASTNode) null, (ASTNode) null), is(true));
  }
  @Test public void negation0Trivial() {
    that(negationLevel(e("a")), is(0));
  }
  @Test public void negation1Trivial() {
    that(negationLevel(e("-a")), is(1));
  }
  @Test public void negationOfExpressionNoNegation() {
    that(negationLevel(e("((((4))))")), is(0));
  }
  @Test public void negationOfExpressionManyNegation() {
    that(negationLevel(e("- - - - (- (-a))")), is(6));
  }
  @Test public void sameOfSomethingAndNull() {
    that(Funcs.same(e("a"), null), is(false));
  }
  @Test public void sameOfTwoExpressionsIdentical() {
    that(Funcs.same(e("a+b"), e("a+b")), is(true));
  }
  @Test public void sameOfTwoExpressionsNotSame() {
    that(Funcs.same(e("a+b+c"), e("a+b")), is(false));
  }
  @Test public void shortNameASTRewriter() {
    that(shortName(t("ASTRewriter _;")), equalTo("r"));
  }
  @Test public void shortNameDouble() {
    that(shortName(t("double _;")), equalTo("d"));
  }
  @Test public void shortNameExpression() {
    that(shortName(t("Expression _;")), equalTo("e"));
  }
  @Test public void shortNameInfrastructure() {
    that(shortName(t("int _;")), equalTo("i"));
  }
  @Test public void shortNameQualifiedType() {
    that(shortName(t("org.eclipse.jdt.core.dom.InfixExpression _;")), equalTo("e"));
  }
  private Type t(final String codeFragment) {
    return Extract.firstType(s(codeFragment));
  }
}
