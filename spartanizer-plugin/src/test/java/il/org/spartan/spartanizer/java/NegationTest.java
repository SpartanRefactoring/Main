package il.org.spartan.spartanizer.java;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.engine.parse.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** A test suite for class {@link minus}
 * @author Yossi Gil
 * @since 2015-07-18
 * @see step */
@SuppressWarnings({ "static-method", "javadoc" })
public final class NegationTest {
  @Test public void levelComplex() {
    azzert.that(compute.level(e("-1/-2*-3/-4*-5*-6/-7/-8/-9")), is(9));
  }
  @Test public void negationOfAddition() {
    azzert.that(compute.level(e("-a+-2")), is(0));
  }
  @Test public void negationOfDivision() {
    azzert.that(compute.level(e("+-a/-2")), is(2));
  }
  @Test public void negationOfExpressionManyNegation() {
    azzert.that(compute.level(e("- - - - (- (-a))")), is(6));
  }
  @Test public void negationOfExpressionNoNegation() {
    azzert.that(compute.level(e("((((4))))")), is(0));
  }
  @Test public void negationOfLiteral() {
    azzert.that(compute.level(e("-2")), is(1));
  }
  @Test public void negationOfMinusOneA() {
    azzert.that(compute.level(e("-1")), is(1));
  }
  @Test public void negationOfMinusOneB() {
    azzert.that(compute.level((InfixExpression) e("-1 *-1")), is(2));
  }
  @Test public void negationOfMultiplication() {
    azzert.that(compute.level(e("+-a*-2")), is(2));
  }
  @Test public void negationOfMultiplicationNoSign() {
    azzert.that(compute.level(e("a*2")), is(0));
  }
  @Test public void negationOfMultiplicationPlain() {
    azzert.that(compute.level(e("a*-2")), is(1));
  }
  @Test public void negationOfPrefixNot() {
    azzert.that(compute.level(e("-a")), is(1));
  }
  @Test public void negationOfPrefixPlus() {
    azzert.that(compute.level(e("+a")), is(0));
  }
  @Test public void peelComplex() {
    azzert.that(compute.peel(e("-1/-2*-3/-4*-5*-6/-7/-8/-9")), iz("1/2*3/4*5*6/7/8/9"));
  }
  @Test public void peelNegationOfAddition() {
    azzert.that(compute.peel(e("-a+-2")), iz("-a+-2"));
  }
  @Test public void peelNegationOfDivizion() {
    azzert.that(compute.peel(e("+-a/-2")), iz("a/2"));
  }
  @Test public void peelNegationOfDivizionA() {
    azzert.that(compute.peel(e("-a/-2")), iz("a/2"));
  }
  @Test public void peelNegationOfDivizionB() {
    azzert.that(compute.peel(i("-a/-2")), iz("a/2"));
  }
  @Test public void peelNegationOfExpressionManyNegation() {
    azzert.that(compute.peel(e("- - - - (- (-a))")), iz("a"));
  }
  @Test public void peelNegationOfExpressionNoNegation() {
    azzert.that(compute.peel(e("((((-+-+-(4)))))")), iz("4"));
  }
  @Test public void peelNegationOfLiteral() {
    azzert.that(compute.peel(e("-2")), iz("2"));
  }
  @Test public void peelNegationOfMinus() {
    azzert.that(compute.peel(e("-a")), iz("a"));
  }
  @Test public void peelNegationOfMinusOneA() {
    azzert.that(compute.peel(e("-1")), iz("1"));
  }
  @Test public void peelNegationOfMinusOneB() {
    azzert.that(compute.peel((InfixExpression) e("-1 *-1")), iz("1*1"));
  }
  @Test public void peelNegationOfMinusOneC() {
    azzert.that(compute.peel((InfixExpression) e("-1 +-1")), iz("-1+-1"));
  }
  @Test public void peelNegationOfMultiplication() {
    azzert.that(compute.peel(e("+-a*-2")), iz("a*2"));
  }
  @Test public void peelNegationOfMultiplicationNoSign() {
    azzert.that(compute.peel(e("a*2")), iz("a*2"));
  }
  @Test public void peelNegationOfMultiplicationPlain() {
    azzert.that(compute.peel(e("a*-2")), iz("a*2"));
  }
  @Test public void peelNegationOfPlus() {
    azzert.that(compute.peel(e("+-a")), iz("a"));
  }
  @Test public void peelNegationOfPlusA() {
    azzert.that(compute.peel(e("+a")), iz("a"));
  }
  @Test public void peelNegationOfPlusB() {
    azzert.that(compute.peel(p("+a")), iz("a"));
  }
  @Test public void peelNegationOfPrefixMinus() {
    azzert.that(compute.peel(e("-a")), iz("a"));
  }
  @Test public void peelNegationOfPrefixPlus() {
    azzert.that(compute.peel(e("+a")), iz("a"));
  }
}
