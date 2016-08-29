package il.org.spartan.refactoring.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Into.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.utils.*;

/** A test suite for class {@link negation}
 * @author Yossi Gil
 * @since 2015-07-18
 * @see Funcs */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) public class NegationTest {
  @Test public void negationOfAddition() {
    azzert.that(negation.level(e("-a+-2")), is(0));
  }

  @Test public void levelComplex() {
    azzert.that(negation.level(e("-1/-2*-3/-4*-5*-6/-7/-8/-9")), is(9));
  }

  @Test public void peelComplex() {
    azzert.that(negation.peel(e("-1/-2*-3/-4*-5*-6/-7/-8/-9")), //
        iz("1/2*3/4*5*6/7/8/9"));
  }

  @Test public void negationOfDivision() {
    azzert.that(negation.level(e("+-a/-2")), is(2));
  }

  @Test public void negationOfExpressionManyNegation() {
    azzert.that(negation.level(e("- - - - (- (-a))")), is(6));
  }

  @Test public void negationOfExpressionNoNegation() {
    azzert.that(negation.level(e("((((4))))")), is(0));
  }

  @Test public void negationOfLiteral() {
    azzert.that(negation.level(e("-2")), is(1));
  }

  @Test public void negationOfMinusOneA() {
    azzert.that(negation.level(e("-1")), is(1));
  }

  @Test public void negationOfMinusOneB() {
    azzert.that(negation.level((InfixExpression) e("-1 *-1")), is(2));
  }

  @Test public void negationOfMultiplication() {
    azzert.that(negation.level(e("+-a*-2")), is(2));
  }

  @Test public void negationOfMultiplicationNoSign() {
    azzert.that(negation.level(e("a*2")), is(0));
  }

  @Test public void negationOfMultiplicationPlain() {
    azzert.that(negation.level(e("a*-2")), is(1));
  }

  @Test public void negationOfPrefixNot() {
    azzert.that(negation.level(e("-a")), is(1));
  }

  @Test public void negationOfPrefixPlus() {
    azzert.that(negation.level(e("+a")), is(0));
  }

  @Test public void peelNegationOfAddition() {
    azzert.that(negation.peel(e("-a+-2")), iz("-a+-2"));
  }

  @Test public void peelNegationOfDivizion() {
    azzert.that(negation.peel(e("+-a/-2")), iz("a/2"));
  }

  @Test public void peelNegationOfDivizionA() {
    azzert.that(negation.peel(e("-a/-2")), iz("a/2"));
  }

  @Test public void peelNegationOfDivizionB() {
    azzert.that(negation.peel(i("-a/-2")), iz("a/2"));
  }

  @Test public void peelNegationOfExpressionManyNegation() {
    azzert.that(negation.peel(e("- - - - (- (-a))")), iz("a"));
  }

  @Test public void peelNegationOfExpressionNoNegation() {
    azzert.that(negation.peel(e("((((-+-+-(4)))))")), iz("4"));
  }

  @Test public void peelNegationOfLiteral() {
    azzert.that(negation.peel(e("-2")), iz("2"));
  }

  @Test public void peelNegationOfMinus() {
    azzert.that(negation.peel(e("-a")), iz("a"));
  }

  @Test public void peelNegationOfMinusOneA() {
    azzert.that(negation.peel(e("-1")), iz("1"));
  }

  @Test public void peelNegationOfMinusOneB() {
    azzert.that(negation.peel((InfixExpression) e("-1 *-1")), iz("1*1"));
  }

  @Test public void peelNegationOfMinusOneC() {
    azzert.that(negation.peel((InfixExpression) e("-1 +-1")), iz("-1+-1"));
  }

  @Test public void peelNegationOfMultiplication() {
    azzert.that(negation.peel(e("+-a*-2")), iz("a*2"));
  }

  @Test public void peelNegationOfMultiplicationNoSign() {
    azzert.that(negation.peel(e("a*2")), iz("a*2"));
  }

  @Test public void peelNegationOfMultiplicationPlain() {
    azzert.that(negation.peel(e("a*-2")), iz("a*2"));
  }

  @Test public void peelNegationOfPlus() {
    azzert.that(negation.peel(e("+-a")), iz("a"));
  }

  @Test public void peelNegationOfPlusB() {
    azzert.that(negation.peel(p("+a")), iz("a"));
  }

  @Test public void peelNegationOfPlusA() {
    azzert.that(negation.peel(e("+a")), iz("a"));
  }

  @Test public void peelNegationOfPrefixMinus() {
    azzert.that(negation.peel(e("-a")), iz("a"));
  }

  @Test public void peelNegationOfPrefixPlus() {
    azzert.that(negation.peel(e("+a")), iz("a"));
  }
}
