package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import org.junit.*;

/**
 * Test class for class {@link Is}
 *
 * @author Yossi Gil
 * @since 2015-07-17
 */
@SuppressWarnings({ "javadoc", "static-method" }) //
public class IsTest {
  @Test public void booleanLiteralFalseOnNull() {
    that(Is.booleanLiteral(e("null")), is(false));
  }
  @Test public void booleanLiteralFalseOnNumeric() {
    that(Is.booleanLiteral(e("12")), is(false));
  }
  @Test public void booleanLiteralFalseOnThis() {
    that(Is.booleanLiteral(e("this")), is(false));
  }
  @Test public void booleanLiteralTrueOnFalse() {
    that(Is.booleanLiteral(e("false")), is(true));
  }
  @Test public void booleanLiteralTrueOnTrue() {
    that(Is.booleanLiteral(e("true")), is(true));
  }
  @Test public void callIsSpecificTrue() {
    that(Is.constant(e("this")), is(true));
  }
  @Test public void canMakeExpression() {
    e("2+2");
  }
  @Test public void isConstantFalse() {
    that(Is.constant(e("a")), is(false));
  }
  @Test public void isNullFalse1() {
    that(Is.null_(e("this")), is(false));
  }
  @Test public void isNullFalse2() {
    that(Is.this_(e("this.a")), is(false));
  }
  @Test public void isNullTrue() {
    that(Is.null_(e("null")), is(true));
  }
  @Test public void isOneOf() {
    that(Is.oneOf(e("this"), CHARACTER_LITERAL, NUMBER_LITERAL, NULL_LITERAL, THIS_EXPRESSION), is(true));
  }
  @Test public void isThisFalse1() {
    that(Is.this_(e("null")), is(false));
  }
  @Test public void isThisFalse2() {
    that(Is.this_(e("this.a")), is(false));
  }
  @Test public void isThisTrue() {
    that(Is.this_(e("this")), is(true));
  }
  @Test public void negative0() {
    that(Is.negative(e("0")), is(false));
  }
  @Test public void negative1() {
    that(Is.negative(e("0")), is(false));
  }
  @Test public void negativeMinus1() {
    that(Is.negative(e("- 1")), is(true));
  }
  @Test public void negativeMinus2() {
    that(Is.negative(e("- 2")), is(true));
  }
  @Test public void negativeMinusA() {
    that(Is.negative(e("- a")), is(true));
  }
  @Test public void negativeNull() {
    that(Is.negative(e("null")), is(false));
  }
  @Test public void nonnAssociative() {
    that(Is.nonAssociative(e("1")), is(false));
    that(Is.nonAssociative(e("-1")), is(false));
    that(Is.nonAssociative(e("-1+2")), is(false));
    that(Is.nonAssociative(e("1+2")), is(false));
    that(Is.nonAssociative(e("2-1")), is(true));
    that(Is.nonAssociative(e("2/1")), is(true));
    that(Is.nonAssociative(e("2%1")), is(true));
    that(Is.nonAssociative(e("2*1")), is(false));
  }
  @Test public void numericLiteralFalse1() {
    that(Is.numericLiteral(e("2*3")), is(false));
  }
  @Test public void numericLiteralFalse2() {
    that(Is.numericLiteral(e("2*3")), is(false));
  }
  @Test public void numericLiteralTrue() {
    that(Is.numericLiteral(e("1")), is(true));
  }
  @Test public void deterministicArray1() {
    that(Is.deterministic(e("new a[3]")), is(false));
  }
  @Test public void deterministicArray2() {
    that(Is.deterministic(e("new int[] {12,13}")), is(false));
  }
  @Test public void deterministicArray3() {
    that(Is.deterministic(e("new int[] {12,13, i++}")), is(false));
  }
  @Test public void deterministicArray4() {
    that(Is.deterministic(e("new int[f()]")), is(false));
  }
  @Test public void sideEffectArray1() {
    that(Is.sideEffectFree(e("new a[3]")), is(true));
  }
  @Test public void sideEffectArray2() {
    that(Is.sideEffectFree(e("new int[] {12,13}")), is(true));
  }
  @Test public void sideEffectArray3() {
    that(Is.sideEffectFree(e("new int[] {12,13, i++}")), is(false));
  }
  @Test public void sideEffectArray4() {
    that(Is.sideEffectFree(e("new int[f()]")), is(false));
  }
  @Test public void sideEffectConditional1() {
    that(Is.sideEffectFree(e("3 + 4 < 10 ? 12 : 14")), is(true));
  }
  @Test public void sideEffectConditional2() {
    that(Is.sideEffectFree(e("3 + 4 < 10 + ++i ? 12 : 14")), is(false));
  }
  @Test public void sideEffectConditional3() {
    that(Is.sideEffectFree(e("3 + 4 < 10 + a? 12 * f(): 14")), is(false));
  }
  @Test public void sideEffectConditional4() {
    that(Is.sideEffectFree(e("3 + 4 < 10 ? 12 * 2: 14 * f()")), is(false));
  }
  @Test public void sideEffectFreeAddition() {
    that(Is.sideEffectFree(e("f() + 2")), is(false));
    that(Is.sideEffectFree(e("2 + f() + 2")), is(false));
    that(Is.sideEffectFree(e("3 + true + f() + 2")), is(false));
  }
  @Test public void sideEffectFreeCasetFalsee() {
    that(Is.sideEffectFree(e("(A) f()")), is(false));
  }
  @Test public void sideEffectFreeCastTrue() {
    that(Is.sideEffectFree(e("(A) b")), is(true));
  }
  @Test public void sideEffectFreeNull() {
    that(Is.sideEffectFree(e("null")), is(true));
  }
  @Test public void sideEffectFreeExists() {
    Is.sideEffectFree(e("null"));
  }
  @Test public void sideEffectFreeLArraSFunctionIndex() {
    that(Is.sideEffectFree(e("a[i()]")), is(false));
  }
  @Test public void sideEffectFreeLArrayFunctionArray() {
    that(Is.sideEffectFree(e("a()[i]")), is(false));
  }
  @Test public void sideEffectFreeLArraySimple() {
    that(Is.sideEffectFree(e("a[i*3]")), is(true));
  }
  @Test public void sideEffectFreeLFieldAccess() {
    that(Is.sideEffectFree(e("this.f")), is(true));
  }
  @Test public void sideEffectFreeLiteralBoolean() {
    that(Is.sideEffectFree(e("true")), is(true));
  }
  @Test public void sideEffectFreeLiteralCharacter() {
    that(Is.sideEffectFree(e("'a'")), is(true));
  }
  @Test public void sideEffectFreeLMethodAccess() {
    that(Is.sideEffectFree(e("this.f()")), is(false));
  }
  @Test public void sideEffectFreeLQualifiedName() {
    that(Is.sideEffectFree(e("a.f")), is(true));
  }
  @Test public void sideEffectFreeMinusMinusPre() {
    that(Is.sideEffectFree(e("--a")), is(false));
  }
  @Test public void sideEffectFreeOfAssignment() {
    that(Is.sideEffectFree(e("a=b")), is(false));
  }
  @Test public void sideEffectFreeOfFunctionCall() {
    that(Is.sideEffectFree(e("f()")), is(false));
  }
  @Test public void sideEffectFreeOfFunctionCallParenthesized() {
    that(Is.sideEffectFree(e("(f())")), is(false));
  }
  @Test public void sideEffectFreeOfIntegerConstant() {
    that(Is.sideEffectFree(e("5")), is(true));
  }
  @Test public void sideEffectFreeOfMethodInovacation() {
    that(Is.sideEffectFree(e("a.f()")), is(false));
  }
  @Test public void sideEffectFreeOfMinusMinus() {
    that(Is.sideEffectFree(e("a--")), is(false));
  }
  @Test public void sideEffectFreeOfNewInstance() {
    that(Is.sideEffectFree(e("new A()")), is(false));
  }
  @Test public void sideEffectFreeOfNullLiteral() {
    that(Is.sideEffectFree(e("null")), is(true));
  }
  @Test public void sideEffectFreeOfNullPointer() {
    that(Is.sideEffectFree(null), is(true));
  }
  @Test public void sideEffectFreeOfPlusPlus() {
    that(Is.sideEffectFree(e("a++")), is(false));
  }
  @Test public void sideEffectFreePlusPlusPre() {
    that(Is.sideEffectFree(e("++a")), is(false));
  }
  @Test public void sideEffectfreeSuperAccess() {
    that(Is.sideEffectFree(e("super.f")), is(true));
  }
  @Test public void sideEffectfreeSuperInfocation() {
    that(Is.sideEffectFree(e("super.f()")), is(false));
  }
  @Test public void sideEffectFreeTypeInstanceOfTrue() {
    that(Is.sideEffectFree(e("a instanceof B")), is(true));
  }
  @Test public void sideEffectFreeTypeLiteralt() {
    that(Is.sideEffectFree(e("void.class")), is(true));
    that(Is.sideEffectFree(e("A.class")), is(true));
  }
  @Test public void sideEffectFrePrefix() {
    that(Is.sideEffectFree(e("-!a")), is(true));
    that(Is.sideEffectFree(e("~a")), is(true));
    that(Is.sideEffectFree(e("-a")), is(true));
    that(Is.sideEffectFree(e("+a")), is(true));
  }
  @Test public void sideEffectFrePrefixFunctiont() {
    that(Is.sideEffectFree(e("-!f()")), is(false));
    that(Is.sideEffectFree(e("~f()")), is(false));
    that(Is.sideEffectFree(e("-f()")), is(false));
    that(Is.sideEffectFree(e("+f()")), is(false));
  }
  @Test public void sideEffectSimpleName() {
    that(Is.sideEffectFree(e("a")), is(true));
  }
  @Test public void sideEffectStringLiteral() {
    that(Is.sideEffectFree(e("\"a\"")), is(true));
  }
  @Test public void sideEffectThisl() {
    that(Is.sideEffectFree(e("this")), is(true));
  }
}
