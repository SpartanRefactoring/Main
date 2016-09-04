package il.org.spartan.refactoring.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.engine.into.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.ast.*;

/** Test class for class {@link iz}
 * @author Yossi Gil
 * @since 2015-07-17 */
@SuppressWarnings({ "javadoc", "static-method" }) //
public class sideEffectsTest {
  @Test public void sideEffectArray1() {
    azzert.that(sideEffects.free(e("new a[3]")), is(true));
  }

  @Test public void sideEffectArray2() {
    azzert.that(sideEffects.free(e("new int[] {12,13}")), is(true));
  }

  @Test public void sideEffectArray3() {
    azzert.that(sideEffects.free(e("new int[] {12,13, i++}")), is(false));
  }

  @Test public void sideEffectArray4() {
    azzert.that(sideEffects.free(e("new int[f()]")), is(false));
  }

  @Test public void sideEffectConditional1() {
    azzert.that(sideEffects.free(e("3 + 4 < 10 ? 12 : 14")), is(true));
  }

  @Test public void sideEffectConditional2() {
    azzert.that(sideEffects.free(e("3 + 4 < 10 + ++i ? 12 : 14")), is(false));
  }

  @Test public void sideEffectConditional3() {
    azzert.that(sideEffects.free(e("3 + 4 < 10 + a? 12 * f(): 14")), is(false));
  }

  @Test public void sideEffectConditional4() {
    azzert.that(sideEffects.free(e("3 + 4 < 10 ? 12 * 2: 14 * f()")), is(false));
  }

  @Test public void sideEffectFreeAddition() {
    azzert.that(sideEffects.free(e("f() + 2")), is(false));
    azzert.that(sideEffects.free(e("2 + f() + 2")), is(false));
    azzert.that(sideEffects.free(e("3 + true + f() + 2")), is(false));
  }

  @Test public void castExpression1() {
    azzert.nay(sideEffects.free(e("(A) f()")));
  }

  @Test public void castExpression2() {
    azzert.nay(sideEffects.free(e("(A) i++")));
  }

  @Test public void castExpression3() {
    azzert.nay(sideEffects.free(e("(A) (j++*i++)")));
  }

  @Test public void sideEffectFreeExists() {
    sideEffects.free(e("null"));
  }

  @Test public void sideEffectFreeLArraSFunctionIndex() {
    azzert.that(sideEffects.free(e("a[i()]")), is(false));
  }

  @Test public void sideEffectFreeLArrayFunctionArray() {
    azzert.that(sideEffects.free(e("a()[i]")), is(false));
  }

  @Test public void sideEffectFreeLArraySimple() {
    azzert.that(sideEffects.free(e("a[i*3]")), is(true));
  }

  @Test public void sideEffectFreeLFieldAccess() {
    azzert.that(sideEffects.free(e("this.f")), is(true));
  }

  @Test public void sideEffectFreeLiteralBoolean() {
    azzert.that(sideEffects.free(e("true")), is(true));
  }

  @Test public void sideEffectFreeLiteralCharacter() {
    azzert.that(sideEffects.free(e("'a'")), is(true));
  }

  @Test public void sideEffectFreeLMethodAccess() {
    azzert.that(sideEffects.free(e("this.f()")), is(false));
  }

  @Test public void sideEffectFreeLQualifiedName() {
    azzert.that(sideEffects.free(e("a.f")), is(true));
  }

  @Test public void sideEffectFreeMinusMinusPre() {
    azzert.that(sideEffects.free(e("--a")), is(false));
  }

  @Test public void sideEffectFreeNull() {
    azzert.that(sideEffects.free(e("null")), is(true));
  }

  @Test public void sideEffectFreeOfAssignment() {
    azzert.that(sideEffects.free(e("a=b")), is(false));
  }

  @Test public void sideEffectFreeOfFunctionCall() {
    azzert.that(sideEffects.free(e("f()")), is(false));
  }

  @Test public void sideEffectFreeOfFunctionCallParenthesized() {
    azzert.that(sideEffects.free(e("(f())")), is(false));
  }

  @Test public void sideEffectFreeOfIntegerConstant() {
    azzert.that(sideEffects.free(e("5")), is(true));
  }

  @Test public void sideEffectFreeOfMethodInovacation() {
    azzert.that(sideEffects.free(e("a.f()")), is(false));
  }

  @Test public void sideEffectFreeOfMinusMinus() {
    azzert.that(sideEffects.free(e("a--")), is(false));
  }

  @Test public void sideEffectFreeOfNewInstance() {
    azzert.that(sideEffects.free(e("new A()")), is(false));
  }

  @Test public void sideEffectFreeOfNullLiteral() {
    azzert.that(sideEffects.free(e("null")), is(true));
  }

  @Test public void sideEffectFreeOfNullPointer() {
    azzert.that(sideEffects.free(null), is(true));
  }

  @Test public void sideEffectFreeOfPlusPlus() {
    azzert.that(sideEffects.free(e("a++")), is(false));
  }

  @Test public void sideEffectFreePlusPlusPre() {
    azzert.that(sideEffects.free(e("++a")), is(false));
  }

  @Test public void sideEffectfreeSuperAccess() {
    azzert.that(sideEffects.free(e("super.f")), is(true));
  }

  @Test public void sideEffectfreeSuperInfocation() {
    azzert.that(sideEffects.free(e("super.f()")), is(false));
  }

  @Test public void sideEffectFreeTypeInstanceOfTrue() {
    azzert.that(sideEffects.free(e("a instanceof B")), is(true));
  }

  @Test public void sideEffectFreeTypeLiteralt() {
    azzert.that(sideEffects.free(e("void.class")), is(true));
    azzert.that(sideEffects.free(e("A.class")), is(true));
  }

  @Test public void sideEffectFrePrefix() {
    azzert.that(sideEffects.free(e("-!a")), is(true));
    azzert.that(sideEffects.free(e("~a")), is(true));
    azzert.that(sideEffects.free(e("-a")), is(true));
    azzert.that(sideEffects.free(e("+a")), is(true));
  }

  @Test public void sideEffectFrePrefixFunctiont() {
    azzert.that(sideEffects.free(e("-!f()")), is(false));
    azzert.that(sideEffects.free(e("~f()")), is(false));
    azzert.that(sideEffects.free(e("-f()")), is(false));
    azzert.that(sideEffects.free(e("+f()")), is(false));
  }

  @Test public void sideEffectSimpleName() {
    azzert.that(sideEffects.free(e("a")), is(true));
  }

  @Test public void sideEffectStringLiteral() {
    azzert.that(sideEffects.free(e("\"a\"")), is(true));
  }

  @Test public void sideEffectThisl() {
    azzert.that(sideEffects.free(e("this")), is(true));
  }
}
