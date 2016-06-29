package il.org.spartan.refactoring.utils;

import static il.org.spartan.hamcrest.SpartanAssert.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import org.junit.*;

/**
 * Test class for class {@link Is}
 *
 * @author Yossi Gil
 * @since 2015-07-17
 */
@SuppressWarnings({ "javadoc", "static-method" })//
public class IsTest {
  @Test public void booleanLiteralFalseOnNull() {
    assertThat(Is.booleanLiteral(e("null")), is(false));
  }
  @Test public void booleanLiteralFalseOnNumeric() {
    assertThat(Is.booleanLiteral(e("12")), is(false));
  }
  @Test public void booleanLiteralFalseOnThis() {
    assertThat(Is.booleanLiteral(e("this")), is(false));
  }
  @Test public void booleanLiteralTrueOnFalse() {
    assertThat(Is.booleanLiteral(e("false")), is(true));
  }
  @Test public void booleanLiteralTrueOnTrue() {
    assertThat(Is.booleanLiteral(e("true")), is(true));
  }
  @Test public void callIsSpecificTrue() {
    assertThat(Is.constant(e("this")), is(true));
  }
  @Test public void canMakeExpression() {
    e("2+2");
  }
  @Test public void isConstantFalse() {
    assertThat(Is.constant(e("a")), is(false));
  }
  @Test public void isNullFalse1() {
    assertThat(Is.null_(e("this")), is(false));
  }
  @Test public void isNullFalse2() {
    assertThat(Is.this_(e("this.a")), is(false));
  }
  @Test public void isNullTrue() {
    assertThat(Is.null_(e("null")), is(true));
  }
  @Test public void isOneOf() {
    assertThat(Is.oneOf(e("this"), CHARACTER_LITERAL, NUMBER_LITERAL, NULL_LITERAL, THIS_EXPRESSION), is(true));
  }
  @Test public void isThisFalse1() {
    assertThat(Is.this_(e("null")), is(false));
  }
  @Test public void isThisFalse2() {
    assertThat(Is.this_(e("this.a")), is(false));
  }
  @Test public void isThisTrue() {
    assertThat(Is.this_(e("this")), is(true));
  }
  @Test public void negative0() {
    assertThat(Is.negative(e("0")), is(false));
  }
  @Test public void negative1() {
    assertThat(Is.negative(e("0")), is(false));
  }
  @Test public void negativeMinus1() {
    assertThat(Is.negative(e("- 1")), is(true));
  }
  @Test public void negativeMinus2() {
    assertThat(Is.negative(e("- 2")), is(true));
  }
  @Test public void negativeMinusA() {
    assertThat(Is.negative(e("- a")), is(true));
  }
  @Test public void negativeNull() {
    assertThat(Is.negative(e("null")), is(false));
  }
  @Test public void nonnAssociative() {
    assertThat(Is.nonAssociative(e("1")), is(false));
    assertThat(Is.nonAssociative(e("-1")), is(false));
    assertThat(Is.nonAssociative(e("-1+2")), is(false));
    assertThat(Is.nonAssociative(e("1+2")), is(false));
    assertThat(Is.nonAssociative(e("2-1")), is(true));
    assertThat(Is.nonAssociative(e("2/1")), is(true));
    assertThat(Is.nonAssociative(e("2%1")), is(true));
    assertThat(Is.nonAssociative(e("2*1")), is(false));
  }
  @Test public void numericLiteralFalse1() {
    assertThat(Is.numericLiteral(e("2*3")), is(false));
  }
  @Test public void numericLiteralFalse2() {
    assertThat(Is.numericLiteral(e("2*3")), is(false));
  }
  @Test public void numericLiteralTrue() {
    assertThat(Is.numericLiteral(e("1")), is(true));
  }
  @Test public void deterministicArray1() {
    assertThat(Is.deterministic(e("new a[3]")), is(false));
  }
  @Test public void deterministicArray2() {
    assertThat(Is.deterministic(e("new int[] {12,13}")), is(false));
  }
  @Test public void deterministicArray3() {
    assertThat(Is.deterministic(e("new int[] {12,13, i++}")), is(false));
  }
  @Test public void deterministicArray4() {
    assertThat(Is.deterministic(e("new int[f()]")), is(false));
  }
  @Test public void sideEffectArray1() {
    assertThat(Is.sideEffectFree(e("new a[3]")), is(true));
  }
  @Test public void sideEffectArray2() {
    assertThat(Is.sideEffectFree(e("new int[] {12,13}")), is(true));
  }
  @Test public void sideEffectArray3() {
    assertThat(Is.sideEffectFree(e("new int[] {12,13, i++}")), is(false));
  }
  @Test public void sideEffectArray4() {
    assertThat(Is.sideEffectFree(e("new int[f()]")), is(false));
  }
  @Test public void sideEffectConditional1() {
    assertThat(Is.sideEffectFree(e("3 + 4 < 10 ? 12 : 14")), is(true));
  }
  @Test public void sideEffectConditional2() {
    assertThat(Is.sideEffectFree(e("3 + 4 < 10 + ++i ? 12 : 14")), is(false));
  }
  @Test public void sideEffectConditional3() {
    assertThat(Is.sideEffectFree(e("3 + 4 < 10 + a? 12 * f(): 14")), is(false));
  }
  @Test public void sideEffectConditional4() {
    assertThat(Is.sideEffectFree(e("3 + 4 < 10 ? 12 * 2: 14 * f()")), is(false));
  }
  @Test public void sideEffectFreeAddition() {
    assertThat(Is.sideEffectFree(e("f() + 2")), is(false));
    assertThat(Is.sideEffectFree(e("2 + f() + 2")), is(false));
    assertThat(Is.sideEffectFree(e("3 + true + f() + 2")), is(false));
  }
  @Test public void sideEffectFreeCasetFalsee() {
    assertThat(Is.sideEffectFree(e("(A) f()")), is(false));
  }
  @Test public void sideEffectFreeCastTrue() {
    assertThat(Is.sideEffectFree(e("(A) b")), is(true));
  }
  @Test public void sideEffectFreeNull() {
    assertThat(Is.sideEffectFree(e("null")), is(true));
  }
  @Test public void sideEffectFreeExists() {
    Is.sideEffectFree(e("null"));
  }
  @Test public void sideEffectFreeLArraSFunctionIndex() {
    assertThat(Is.sideEffectFree(e("a[i()]")), is(false));
  }
  @Test public void sideEffectFreeLArrayFunctionArray() {
    assertThat(Is.sideEffectFree(e("a()[i]")), is(false));
  }
  @Test public void sideEffectFreeLArraySimple() {
    assertThat(Is.sideEffectFree(e("a[i*3]")), is(true));
  }
  @Test public void sideEffectFreeLFieldAccess() {
    assertThat(Is.sideEffectFree(e("this.f")), is(true));
  }
  @Test public void sideEffectFreeLiteralBoolean() {
    assertThat(Is.sideEffectFree(e("true")), is(true));
  }
  @Test public void sideEffectFreeLiteralCharacter() {
    assertThat(Is.sideEffectFree(e("'a'")), is(true));
  }
  @Test public void sideEffectFreeLMethodAccess() {
    assertThat(Is.sideEffectFree(e("this.f()")), is(false));
  }
  @Test public void sideEffectFreeLQualifiedName() {
    assertThat(Is.sideEffectFree(e("a.f")), is(true));
  }
  @Test public void sideEffectFreeMinusMinusPre() {
    assertThat(Is.sideEffectFree(e("--a")), is(false));
  }
  @Test public void sideEffectFreeOfAssignment() {
    assertThat(Is.sideEffectFree(e("a=b")), is(false));
  }
  @Test public void sideEffectFreeOfFunctionCall() {
    assertThat(Is.sideEffectFree(e("f()")), is(false));
  }
  @Test public void sideEffectFreeOfFunctionCallParenthesized() {
    assertThat(Is.sideEffectFree(e("(f())")), is(false));
  }
  @Test public void sideEffectFreeOfIntegerConstant() {
    assertThat(Is.sideEffectFree(e("5")), is(true));
  }
  @Test public void sideEffectFreeOfMethodInovacation() {
    assertThat(Is.sideEffectFree(e("a.f()")), is(false));
  }
  @Test public void sideEffectFreeOfMinusMinus() {
    assertThat(Is.sideEffectFree(e("a--")), is(false));
  }
  @Test public void sideEffectFreeOfNewInstance() {
    assertThat(Is.sideEffectFree(e("new A()")), is(false));
  }
  @Test public void sideEffectFreeOfNullLiteral() {
    assertThat(Is.sideEffectFree(e("null")), is(true));
  }
  @Test public void sideEffectFreeOfNullPointer() {
    assertThat(Is.sideEffectFree(null), is(true));
  }
  @Test public void sideEffectFreeOfPlusPlus() {
    assertThat(Is.sideEffectFree(e("a++")), is(false));
  }
  @Test public void sideEffectFreePlusPlusPre() {
    assertThat(Is.sideEffectFree(e("++a")), is(false));
  }
  @Test public void sideEffectfreeSuperAccess() {
    assertThat(Is.sideEffectFree(e("super.f")), is(true));
  }
  @Test public void sideEffectfreeSuperInfocation() {
    assertThat(Is.sideEffectFree(e("super.f()")), is(false));
  }
  @Test public void sideEffectFreeTypeInstanceOfTrue() {
    assertThat(Is.sideEffectFree(e("a instanceof B")), is(true));
  }
  @Test public void sideEffectFreeTypeLiteralt() {
    assertThat(Is.sideEffectFree(e("void.class")), is(true));
    assertThat(Is.sideEffectFree(e("A.class")), is(true));
  }
  @Test public void sideEffectFrePrefix() {
    assertThat(Is.sideEffectFree(e("-!a")), is(true));
    assertThat(Is.sideEffectFree(e("~a")), is(true));
    assertThat(Is.sideEffectFree(e("-a")), is(true));
    assertThat(Is.sideEffectFree(e("+a")), is(true));
  }
  @Test public void sideEffectFrePrefixFunctiont() {
    assertThat(Is.sideEffectFree(e("-!f()")), is(false));
    assertThat(Is.sideEffectFree(e("~f()")), is(false));
    assertThat(Is.sideEffectFree(e("-f()")), is(false));
    assertThat(Is.sideEffectFree(e("+f()")), is(false));
  }
  @Test public void sideEffectSimpleName() {
    assertThat(Is.sideEffectFree(e("a")), is(true));
  }
  @Test public void sideEffectStringLiteral() {
    assertThat(Is.sideEffectFree(e("\"a\"")), is(true));
  }
  @Test public void sideEffectThisl() {
    assertThat(Is.sideEffectFree(e("this")), is(true));
  }
}
