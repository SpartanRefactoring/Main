package il.org.spartan.spartanizer.ast.factory;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** A test suite for class {@link eliminate}
 * @author Yossi Gil
 * @since 2015-07-18
 * @see step */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class minusTest {
  @Test public void levelComplex() {
    azzert.that(eliminate.level(e("-1/-2*-3/-4*-5*-6/-7/-8/-9")), is(9));
  }

  @Test public void negationOfAddition() {
    azzert.that(eliminate.level(e("-a+-2")), is(0));
  }

  @Test public void negationOfDivision() {
    azzert.that(eliminate.level(e("+-a/-2")), is(2));
  }

  @Test public void negationOfExpressionManyNegation() {
    azzert.that(eliminate.level(e("- - - - (- (-a))")), is(6));
  }

  @Test public void negationOfExpressionNoNegation() {
    azzert.that(eliminate.level(e("((((4))))")), is(0));
  }

  @Test public void negationOfLiteral() {
    azzert.that(eliminate.level(e("-2")), is(1));
  }

  @Test public void negationOfMinusOneA() {
    azzert.that(eliminate.level(e("-1")), is(1));
  }

  @Test public void negationOfMinusOneB() {
    azzert.that(eliminate.level((InfixExpression) e("-1 *-1")), is(2));
  }

  @Test public void negationOfMultiplication() {
    azzert.that(eliminate.level(e("+-a*-2")), is(2));
  }

  @Test public void negationOfMultiplicationNoSign() {
    azzert.that(eliminate.level(e("a*2")), is(0));
  }

  @Test public void negationOfMultiplicationPlain() {
    azzert.that(eliminate.level(e("a*-2")), is(1));
  }

  @Test public void negationOfPrefixNot() {
    azzert.that(eliminate.level(e("-a")), is(1));
  }

  @Test public void negationOfPrefixPlus() {
    azzert.that(eliminate.level(e("+a")), is(0));
  }

  @Test public void peelComplex() {
    azzert.that(eliminate.peel(e("-1/-2*-3/-4*-5*-6/-7/-8/-9")), iz("1/2*3/4*5*6/7/8/9"));
  }

  @Test public void peelNegationOfAddition() {
    azzert.that(eliminate.peel(e("-a+-2")), iz("-a+-2"));
  }

  @Test public void peelNegationOfDivizion() {
    azzert.that(eliminate.peel(e("+-a/-2")), iz("a/2"));
  }

  @Test public void peelNegationOfDivizionA() {
    azzert.that(eliminate.peel(e("-a/-2")), iz("a/2"));
  }

  @Test public void peelNegationOfDivizionB() {
    azzert.that(eliminate.peel(i("-a/-2")), iz("a/2"));
  }

  @Test public void peelNegationOfExpressionManyNegation() {
    azzert.that(eliminate.peel(e("- - - - (- (-a))")), iz("a"));
  }

  @Test public void peelNegationOfExpressionNoNegation() {
    azzert.that(eliminate.peel(e("((((-+-+-(4)))))")), iz("4"));
  }

  @Test public void peelNegationOfLiteral() {
    azzert.that(eliminate.peel(e("-2")), iz("2"));
  }

  @Test public void peelNegationOfMinus() {
    azzert.that(eliminate.peel(e("-a")), iz("a"));
  }

  @Test public void peelNegationOfMinusOneA() {
    azzert.that(eliminate.peel(e("-1")), iz("1"));
  }

  @Test public void peelNegationOfMinusOneB() {
    azzert.that(eliminate.peel((InfixExpression) e("-1 *-1")), iz("1*1"));
  }

  @Test public void peelNegationOfMinusOneC() {
    azzert.that(eliminate.peel((InfixExpression) e("-1 +-1")), iz("-1+-1"));
  }

  @Test public void peelNegationOfMultiplication() {
    azzert.that(eliminate.peel(e("+-a*-2")), iz("a*2"));
  }

  @Test public void peelNegationOfMultiplicationNoSign() {
    azzert.that(eliminate.peel(e("a*2")), iz("a*2"));
  }

  @Test public void peelNegationOfMultiplicationPlain() {
    azzert.that(eliminate.peel(e("a*-2")), iz("a*2"));
  }

  @Test public void peelNegationOfPlus() {
    azzert.that(eliminate.peel(e("+-a")), iz("a"));
  }

  @Test public void peelNegationOfPlusA() {
    azzert.that(eliminate.peel(e("+a")), iz("a"));
  }

  @Test public void peelNegationOfPlusB() {
    azzert.that(eliminate.peel(p("+a")), iz("a"));
  }

  @Test public void peelNegationOfPrefixMinus() {
    azzert.that(eliminate.peel(e("-a")), iz("a"));
  }

  @Test public void peelNegationOfPrefixPlus() {
    azzert.that(eliminate.peel(e("+a")), iz("a"));
  }
}
