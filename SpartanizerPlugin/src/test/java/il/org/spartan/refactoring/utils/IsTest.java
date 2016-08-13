package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import org.junit.*;

import il.org.spartan.*;

/** Test class for class {@link Is}
 * @author Yossi Gil
 * @since 2015-07-17 */
@SuppressWarnings({ "javadoc", "static-method" }) //
public class IsTest {
  @Test public void booleanLiteralFalseOnNull() {
    azzert.that(Is.booleanLiteral(e("null")), is(false));
  }
  @Test public void booleanLiteralFalseOnNumeric() {
    azzert.that(Is.booleanLiteral(e("12")), is(false));
  }
  @Test public void booleanLiteralFalseOnThis() {
    azzert.that(Is.booleanLiteral(e("this")), is(false));
  }
  @Test public void booleanLiteralTrueOnFalse() {
    azzert.that(Is.booleanLiteral(e("false")), is(true));
  }
  @Test public void booleanLiteralTrueOnTrue() {
    azzert.that(Is.booleanLiteral(e("true")), is(true));
  }
  @Test public void callIsSpecificTrue() {
    azzert.that(Is.constant(e("this")), is(true));
  }
  @Test public void canMakeExpression() {
    e("2+2");
  }
  @Test public void deterministicArray1() {
    azzert.that(Is.deterministic(e("new a[3]")), is(false));
  }
  @Test public void deterministicArray2() {
    azzert.that(Is.deterministic(e("new int[] {12,13}")), is(false));
  }
  @Test public void deterministicArray3() {
    azzert.that(Is.deterministic(e("new int[] {12,13, i++}")), is(false));
  }
  @Test public void deterministicArray4() {
    azzert.that(Is.deterministic(e("new int[f()]")), is(false));
  }
  @Test public void isConstantFalse() {
    azzert.that(Is.constant(e("a")), is(false));
  }
  @Test public void isNullFalse1() {
    azzert.that(Is.null_(e("this")), is(false));
  }
  @Test public void isNullFalse2() {
    azzert.that(Is.this_(e("this.a")), is(false));
  }
  @Test public void isNullTrue() {
    azzert.that(Is.null_(e("null")), is(true));
  }
  @Test public void isOneOf() {
    azzert.that(Is.oneOf(e("this"), CHARACTER_LITERAL, NUMBER_LITERAL, NULL_LITERAL, THIS_EXPRESSION), is(true));
  }
  @Test public void isThisFalse1() {
    azzert.that(Is.this_(e("null")), is(false));
  }
  @Test public void isThisFalse2() {
    azzert.that(Is.this_(e("this.a")), is(false));
  }
  @Test public void isThisTrue() {
    azzert.that(Is.this_(e("this")), is(true));
  }
  @Test public void negative0() {
    azzert.that(Is.negative(e("0")), is(false));
  }
  @Test public void negative1() {
    azzert.that(Is.negative(e("0")), is(false));
  }
  @Test public void negativeMinus1() {
    azzert.that(Is.negative(e("- 1")), is(true));
  }
  @Test public void negativeMinus2() {
    azzert.that(Is.negative(e("- 2")), is(true));
  }
  @Test public void negativeMinusA() {
    azzert.that(Is.negative(e("- a")), is(true));
  }
  @Test public void negativeNull() {
    azzert.that(Is.negative(e("null")), is(false));
  }
  @Test public void nonnAssociative() {
    azzert.that(Is.nonAssociative(e("1")), is(false));
    azzert.that(Is.nonAssociative(e("-1")), is(false));
    azzert.that(Is.nonAssociative(e("-1+2")), is(false));
    azzert.that(Is.nonAssociative(e("1+2")), is(false));
    azzert.that(Is.nonAssociative(e("2-1")), is(true));
    azzert.that(Is.nonAssociative(e("2/1")), is(true));
    azzert.that(Is.nonAssociative(e("2%1")), is(true));
    azzert.that(Is.nonAssociative(e("2*1")), is(false));
  }
  @Test public void numericLiteralFalse1() {
    azzert.that(Is.numericLiteral(e("2*3")), is(false));
  }
  @Test public void numericLiteralFalse2() {
    azzert.that(Is.numericLiteral(e("2*3")), is(false));
  }
  @Test public void numericLiteralTrue() {
    azzert.that(Is.numericLiteral(e("1")), is(true));
  }
  @Test public void sideEffectArray1() {
    azzert.that(Is.sideEffectFree(e("new a[3]")), is(true));
  }
  @Test public void sideEffectArray2() {
    azzert.that(Is.sideEffectFree(e("new int[] {12,13}")), is(true));
  }
  @Test public void sideEffectArray3() {
    azzert.that(Is.sideEffectFree(e("new int[] {12,13, i++}")), is(false));
  }
  @Test public void sideEffectArray4() {
    azzert.that(Is.sideEffectFree(e("new int[f()]")), is(false));
  }
  @Test public void sideEffectConditional1() {
    azzert.that(Is.sideEffectFree(e("3 + 4 < 10 ? 12 : 14")), is(true));
  }
  @Test public void sideEffectConditional2() {
    azzert.that(Is.sideEffectFree(e("3 + 4 < 10 + ++i ? 12 : 14")), is(false));
  }
  @Test public void sideEffectConditional3() {
    azzert.that(Is.sideEffectFree(e("3 + 4 < 10 + a? 12 * f(): 14")), is(false));
  }
  @Test public void sideEffectConditional4() {
    azzert.that(Is.sideEffectFree(e("3 + 4 < 10 ? 12 * 2: 14 * f()")), is(false));
  }
  @Test public void sideEffectFreeAddition() {
    azzert.that(Is.sideEffectFree(e("f() + 2")), is(false));
    azzert.that(Is.sideEffectFree(e("2 + f() + 2")), is(false));
    azzert.that(Is.sideEffectFree(e("3 + true + f() + 2")), is(false));
  }
  @Test public void sideEffectFreeCasetFalsee() {
    azzert.that(Is.sideEffectFree(e("(A) f()")), is(false));
  }
  @Test public void sideEffectFreeCastTrue() {
    azzert.that(Is.sideEffectFree(e("(A) b")), is(true));
  }
  @Test public void sideEffectFreeExists() {
    Is.sideEffectFree(e("null"));
  }
  @Test public void sideEffectFreeLArraSFunctionIndex() {
    azzert.that(Is.sideEffectFree(e("a[i()]")), is(false));
  }
  @Test public void sideEffectFreeLArrayFunctionArray() {
    azzert.that(Is.sideEffectFree(e("a()[i]")), is(false));
  }
  @Test public void sideEffectFreeLArraySimple() {
    azzert.that(Is.sideEffectFree(e("a[i*3]")), is(true));
  }
  @Test public void sideEffectFreeLFieldAccess() {
    azzert.that(Is.sideEffectFree(e("this.f")), is(true));
  }
  @Test public void sideEffectFreeLiteralBoolean() {
    azzert.that(Is.sideEffectFree(e("true")), is(true));
  }
  @Test public void sideEffectFreeLiteralCharacter() {
    azzert.that(Is.sideEffectFree(e("'a'")), is(true));
  }
  @Test public void sideEffectFreeLMethodAccess() {
    azzert.that(Is.sideEffectFree(e("this.f()")), is(false));
  }
  @Test public void sideEffectFreeLQualifiedName() {
    azzert.that(Is.sideEffectFree(e("a.f")), is(true));
  }
  @Test public void sideEffectFreeMinusMinusPre() {
    azzert.that(Is.sideEffectFree(e("--a")), is(false));
  }
  @Test public void sideEffectFreeNull() {
    azzert.that(Is.sideEffectFree(e("null")), is(true));
  }
  @Test public void sideEffectFreeOfAssignment() {
    azzert.that(Is.sideEffectFree(e("a=b")), is(false));
  }
  @Test public void sideEffectFreeOfFunctionCall() {
    azzert.that(Is.sideEffectFree(e("f()")), is(false));
  }
  @Test public void sideEffectFreeOfFunctionCallParenthesized() {
    azzert.that(Is.sideEffectFree(e("(f())")), is(false));
  }
  @Test public void sideEffectFreeOfIntegerConstant() {
    azzert.that(Is.sideEffectFree(e("5")), is(true));
  }
  @Test public void sideEffectFreeOfMethodInovacation() {
    azzert.that(Is.sideEffectFree(e("a.f()")), is(false));
  }
  @Test public void sideEffectFreeOfMinusMinus() {
    azzert.that(Is.sideEffectFree(e("a--")), is(false));
  }
  @Test public void sideEffectFreeOfNewInstance() {
    azzert.that(Is.sideEffectFree(e("new A()")), is(false));
  }
  @Test public void sideEffectFreeOfNullLiteral() {
    azzert.that(Is.sideEffectFree(e("null")), is(true));
  }
  @Test public void sideEffectFreeOfNullPointer() {
    azzert.that(Is.sideEffectFree(null), is(true));
  }
  @Test public void sideEffectFreeOfPlusPlus() {
    azzert.that(Is.sideEffectFree(e("a++")), is(false));
  }
  @Test public void sideEffectFreePlusPlusPre() {
    azzert.that(Is.sideEffectFree(e("++a")), is(false));
  }
  @Test public void sideEffectfreeSuperAccess() {
    azzert.that(Is.sideEffectFree(e("super.f")), is(true));
  }
  @Test public void sideEffectfreeSuperInfocation() {
    azzert.that(Is.sideEffectFree(e("super.f()")), is(false));
  }
  @Test public void sideEffectFreeTypeInstanceOfTrue() {
    azzert.that(Is.sideEffectFree(e("a instanceof B")), is(true));
  }
  @Test public void sideEffectFreeTypeLiteralt() {
    azzert.that(Is.sideEffectFree(e("void.class")), is(true));
    azzert.that(Is.sideEffectFree(e("A.class")), is(true));
  }
  @Test public void sideEffectFrePrefix() {
    azzert.that(Is.sideEffectFree(e("-!a")), is(true));
    azzert.that(Is.sideEffectFree(e("~a")), is(true));
    azzert.that(Is.sideEffectFree(e("-a")), is(true));
    azzert.that(Is.sideEffectFree(e("+a")), is(true));
  }
  @Test public void sideEffectFrePrefixFunctiont() {
    azzert.that(Is.sideEffectFree(e("-!f()")), is(false));
    azzert.that(Is.sideEffectFree(e("~f()")), is(false));
    azzert.that(Is.sideEffectFree(e("-f()")), is(false));
    azzert.that(Is.sideEffectFree(e("+f()")), is(false));
  }
  @Test public void sideEffectSimpleName() {
    azzert.that(Is.sideEffectFree(e("a")), is(true));
  }
  @Test public void sideEffectStringLiteral() {
    azzert.that(Is.sideEffectFree(e("\"a\"")), is(true));
  }
  @Test public void sideEffectThisl() {
    azzert.that(Is.sideEffectFree(e("this")), is(true));
  }
}
