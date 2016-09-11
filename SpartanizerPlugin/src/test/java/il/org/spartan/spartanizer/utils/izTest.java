package il.org.spartan.spartanizer.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.ast.extract.*;
import static il.org.spartan.spartanizer.engine.into.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.java.*;

/** Test class for class {@link iz}
 * @author Yossi Gil
 * @since 2015-07-17 */
@SuppressWarnings({ "javadoc", "static-method" }) //
public class izTest {
  @Test public void booleanLiteralFalseOnNull() {
    azzert.that(iz.booleanLiteral(e("null")), is(false));
  }

  @Test public void booleanLiteralFalseOnNumeric() {
    azzert.that(iz.booleanLiteral(e("12")), is(false));
  }

  @Test public void booleanLiteralFalseOnThis() {
    azzert.that(iz.booleanLiteral(e("this")), is(false));
  }

  @Test public void booleanLiteralTrueOnFalse() {
    azzert.that(iz.booleanLiteral(e("false")), is(true));
  }

  @Test public void booleanLiteralTrueOnTrue() {
    azzert.that(iz.booleanLiteral(e("true")), is(true));
  }

  @Test public void callIsSpecificTrue() {
    azzert.that(iz.constant(e("this")), is(true));
  }

  @Test public void canMakeExpression() {
    e("2+2");
  }

  @Test public void deterministicArray1() {
    azzert.that(sideEffects.deterministic(e("new a[3]")), is(false));
  }

  @Test public void deterministicArray2() {
    azzert.that(sideEffects.deterministic(e("new int[] {12,13}")), is(false));
  }

  @Test public void deterministicArray3() {
    azzert.that(sideEffects.deterministic(e("new int[] {12,13, i++}")), is(false));
  }

  @Test public void deterministicArray4() {
    azzert.that(sideEffects.deterministic(e("new int[f()]")), is(false));
  }

  @Test public void isConstantFalse() {
    azzert.that(iz.constant(e("a")), is(false));
  }

  @Test public void isNullFalse1() {
    azzert.that(iz.nullLiteral(e("this")), is(false));
  }

  @Test public void isNullFalse2() {
    azzert.that(iz.thisLiteral(e("this.a")), is(false));
  }

  @Test public void isNullTrue() {
    azzert.that(iz.nullLiteral(e("null")), is(true));
  }

  @Test public void isOneOf() {
    azzert.that(iz.oneOf(e("this"), CHARACTER_LITERAL, NUMBER_LITERAL, NULL_LITERAL, THIS_EXPRESSION), is(true));
  }

  @Test public void isThisFalse1() {
    azzert.that(iz.thisLiteral(e("null")), is(false));
  }

  @Test public void isThisFalse2() {
    azzert.that(iz.thisLiteral(e("this.a")), is(false));
  }

  @Test public void isThisTrue() {
    azzert.that(iz.thisLiteral(e("this")), is(true));
  }

  @Test public void negative0() {
    azzert.that(iz.negative(e("0")), is(false));
  }

  @Test public void negative1() {
    azzert.that(iz.negative(e("0")), is(false));
  }

  @Test public void negativeMinus1() {
    azzert.that(iz.negative(e("- 1")), is(true));
  }

  @Test public void negativeMinus2() {
    azzert.that(iz.negative(e("- 2")), is(true));
  }

  @Test public void negativeMinusA() {
    azzert.that(iz.negative(e("- a")), is(true));
  }

  @Test public void negativeNull() {
    azzert.that(iz.negative(e("null")), is(false));
  }

  @Test public void nonnAssociative() {
    azzert.that(wizard.nonAssociative(e("1")), is(false));
    azzert.that(wizard.nonAssociative(e("-1")), is(false));
    azzert.that(wizard.nonAssociative(e("-1+2")), is(false));
    azzert.that(wizard.nonAssociative(e("1+2")), is(false));
    azzert.that(wizard.nonAssociative(e("2-1")), is(true));
    azzert.that(wizard.nonAssociative(e("2/1")), is(true));
    azzert.that(wizard.nonAssociative(e("2%1")), is(true));
    azzert.that(wizard.nonAssociative(e("2*1")), is(false));
  }

  @Test public void numericLiteralFalse1() {
    azzert.that(iz.numericLiteral(e("2*3")), is(false));
  }

  @Test public void numericLiteralFalse2() {
    azzert.that(iz.numericLiteral(e("2*3")), is(false));
  }

  @Test public void numericLiteralTrue() {
    azzert.that(iz.numericLiteral(e("1")), is(true));
  }

  @Test public void seriesA_3() {
    assert !iz.infixPlus(e("(i+j)"));
    assert iz.infixPlus(core(e("(i+j)")));
    assert !iz.infixMinus(e("(i-j)"));
    assert iz.infixMinus(core(e("(i-j)")));
  }
}
