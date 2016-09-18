package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;

/** Test class for class {@link iz}
 * @author Yossi Gil
 * @since 2015-07-17 */
@SuppressWarnings({ "javadoc", "static-method" }) //
public final class sideEffectsTest {
  @Test public void addition() {
    azzert.that(sideEffects.free(e("f() + 2")), is(false));
    azzert.that(sideEffects.free(e("2 + f() + 2")), is(false));
    azzert.that(sideEffects.free(e("3 + true + f() + 2")), is(false));
  }

  @Test public void array1() {
    azzert.that(sideEffects.free(e("new a[3]")), is(true));
  }

  @Test public void array2() {
    azzert.that(sideEffects.free(e("new int[] {12,13}")), is(true));
  }

  @Test public void array3() {
    azzert.that(sideEffects.free(e("new int[] {12,13, i++}")), is(false));
  }

  @Test public void array4() {
    azzert.that(sideEffects.free(e("new int[f()]")), is(false));
  }

  @Test public void castExpression1() {
    assert !sideEffects.free(e("(A) f()"));
  }

  @Test public void castExpression2() {
    assert !sideEffects.free(e("(A) i++"));
  }

  @Test public void castExpression3() {
    assert !sideEffects.free(e("(A) (j++*i++)"));
  }

  @Test public void conditional1() {
    azzert.that(sideEffects.free(e("3 + 4 < 10 ? 12 : 14")), is(true));
  }

  @Test public void conditional2() {
    azzert.that(sideEffects.free(e("3 + 4 < 10 + ++i ? 12 : 14")), is(false));
  }

  @Test public void conditional3() {
    azzert.that(sideEffects.free(e("3 + 4 < 10 + a? 12 * f(): 14")), is(false));
  }

  @Test public void conditional4() {
    azzert.that(sideEffects.free(e("3 + 4 < 10 ? 12 * 2: 14 * f()")), is(false));
  }

  @Test public void exists() {
    sideEffects.free(e("null"));
  }

  @Test public void fieldAccess() {
    azzert.that(sideEffects.free(e("this.f")), is(true));
  }

  @Test public void larraSFunctionIndex() {
    azzert.that(sideEffects.free(e("a[i()]")), is(false));
  }

  @Test public void larrayFunctionArray() {
    azzert.that(sideEffects.free(e("a()[i]")), is(false));
  }

  @Test public void larraySimple() {
    azzert.that(sideEffects.free(e("a[i*3]")), is(true));
  }

  @Test public void literalBoolean() {
    azzert.that(sideEffects.free(e("true")), is(true));
  }

  @Test public void literalCharacter() {
    azzert.that(sideEffects.free(e("'a'")), is(true));
  }

  @Test public void methodAccess() {
    azzert.that(sideEffects.free(e("this.f()")), is(false));
  }

  @Test public void minusMinusPre() {
    azzert.that(sideEffects.free(e("--a")), is(false));
  }

  @Test public void nullLiteral() {
    azzert.that(sideEffects.free(e("null")), is(true));
  }

  @Test public void ofAssignment() {
    azzert.that(sideEffects.free(e("a=b")), is(false));
  }

  @Test public void ofFunctionCall() {
    azzert.that(sideEffects.free(e("f()")), is(false));
  }

  @Test public void ofFunctionCallParenthesized() {
    azzert.that(sideEffects.free(e("(f())")), is(false));
  }

  @Test public void ofIntegerConstant() {
    azzert.that(sideEffects.free(e("5")), is(true));
  }

  @Test public void ofMethodInovacation() {
    azzert.that(sideEffects.free(e("a.f()")), is(false));
  }

  @Test public void ofMinusMinus() {
    azzert.that(sideEffects.free(e("a--")), is(false));
  }

  @Test public void ofNewInstance() {
    azzert.that(sideEffects.free(e("new A()")), is(false));
  }

  @Test public void ofNullLiteral() {
    azzert.that(sideEffects.free(e("null")), is(true));
  }

  @Test public void ofNullPointer() {
    azzert.that(sideEffects.free((Expression) null), is(true));
  }

  @Test public void ofPlusPlus() {
    azzert.that(sideEffects.free(e("a++")), is(false));
  }

  @Test public void plusPlusPre() {
    azzert.that(sideEffects.free(e("++a")), is(false));
  }

  @Test public void prefix() {
    azzert.that(sideEffects.free(e("-!a")), is(true));
    azzert.that(sideEffects.free(e("~a")), is(true));
    azzert.that(sideEffects.free(e("-a")), is(true));
    azzert.that(sideEffects.free(e("+a")), is(true));
  }

  @Test public void prefixFunctiont() {
    azzert.that(sideEffects.free(e("-!f()")), is(false));
    azzert.that(sideEffects.free(e("~f()")), is(false));
    azzert.that(sideEffects.free(e("-f()")), is(false));
    azzert.that(sideEffects.free(e("+f()")), is(false));
  }

  @Test public void qualifiedName() {
    azzert.that(sideEffects.free(e("a.f")), is(true));
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

  @Test public void superAccess() {
    azzert.that(sideEffects.free(e("super.f")), is(true));
  }

  @Test public void superInfocation() {
    azzert.that(sideEffects.free(e("super.f()")), is(false));
  }

  @Test public void typeInstanceOfTrue() {
    azzert.that(sideEffects.free(e("a instanceof B")), is(true));
  }

  @Test public void typeLiteralt() {
    azzert.that(sideEffects.free(e("void.class")), is(true));
    azzert.that(sideEffects.free(e("A.class")), is(true));
  }
}
