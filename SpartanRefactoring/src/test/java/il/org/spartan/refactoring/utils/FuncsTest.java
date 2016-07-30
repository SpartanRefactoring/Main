package il.org.spartan.refactoring.utils;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.refactoring.utils.ExpressionComparator.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
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
@SuppressWarnings({ "static-method", "javadoc" }) @FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class FuncsTest {
  @Test public void arrayOfInts() {
    assertThat(shortName(t("int[][] _;")), equalTo("iss"));
  }
  @Test public void asComparisonPrefixlExpression() {
    final PrefixExpression p = mock(PrefixExpression.class);
    doReturn(PrefixExpression.Operator.NOT).when(p).getOperator();
    assertNull(asComparison(p));
  }
  @Test public void asComparisonTypicalExpression() {
    final InfixExpression i = mock(InfixExpression.class);
    doReturn(GREATER).when(i).getOperator();
    assertNotNull(asComparison(i));
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
    assertEquals(i, asComparison(i));
  }
  @Test public void asComparisonTypicalInfixIsNotNull() {
    final InfixExpression e = mock(InfixExpression.class);
    doReturn(GREATER).when(e).getOperator();
    assertNotNull(asComparison(e));
  }
  @Test public void chainComparison() {
    assertEquals("c", right(i("a == true == b == c")).toString());
  }
  @Test public void countNonWhiteCharacters() {
    assertThat(countNonWhites(e("1 + 23     *456 + \n /* aa */ 7890")), is(13));
  }
  @Test public void findFirstType() {
    assertNotNull(t("int _;"));
  }
  @Test public void isDeMorganAND() {
    assertTrue(Is.deMorgan(CONDITIONAL_AND));
  }
  @Test public void isDeMorganGreater() {
    assertFalse(Is.deMorgan(GREATER));
  }
  @Test public void isDeMorganGreaterEuals() {
    assertFalse(Is.deMorgan(GREATER_EQUALS));
  }
  @Test public void isDeMorganOR() {
    assertTrue(Is.deMorgan(CONDITIONAL_OR));
  }
  @Test public void listOfInts() {
    assertThat(shortName(t("List<Set<Integer>> _;")), equalTo("iss"));
  }
  @Test public void sameOfNullAndSomething() {
    assertFalse(Funcs.same(null, e("a")));
  }
  @Test public void sameOfNulls() {
    assertTrue(Funcs.same((ASTNode) null, (ASTNode) null));
  }
  @Test public void negationOfExpressionNoNegation() {
    assertThat(negationLevel(e("((((4))))")), is(0));
  }
  @Test public void negationOfExpressionManyNegation() {
    assertThat(negationLevel(e("- - - - (- (-a))")), is(6));
  }
  @Test public void sameOfSomethingAndNull() {
    assertFalse(Funcs.same(e("a"), null));
  }
  @Test public void sameOfTwoExpressionsIdentical() {
    assertTrue(Funcs.same(e("a+b"), e("a+b")));
  }
  @Test public void sameOfTwoExpressionsNotSame() {
    assertFalse(Funcs.same(e("a+b+c"), e("a+b")));
  }
  @Test public void shortNameASTRewriter() {
    assertThat(shortName(t("ASTRewriter _;")), equalTo("r"));
  }
  @Test public void shortNameDouble() {
    assertThat(shortName(t("double _;")), equalTo("d"));
  }
  @Test public void shortNameExpression() {
    assertThat(shortName(t("Expression _;")), equalTo("e"));
  }
  @Test public void shortNameInfrastructure() {
    assertThat(shortName(t("int _;")), equalTo("i"));
  }
  @Test public void shortNameQualifiedType() {
    assertThat(shortName(t("org.eclipse.jdt.core.dom.InfixExpression _;")), equalTo("e"));
  }
  private Type t(final String codeFragment) {
    return extract.firstType(s(codeFragment));
  }
}
