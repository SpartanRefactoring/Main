package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** see issue #815 and #799 for more details
 * @author Oren Afek
 * @author Amir Sagiv
 * @since 16-11-11 */
public class Issue0815 {
  private static ASTNode ASTNodeFromString(final String ¢) {
    return make.ast(¢);
  }
  @Test @SuppressWarnings("static-method") public void assertCoverForOfMethod() {
    try {
      assertNotEquals(guessName.of("A_abc_CLASS_1"), guessName.STATIC_CONSTANT);
      fail();
    } catch (final Error ¢) {
      azzert.that(AssertionError.class, is(¢.getClass()));
    }
  }
  @Test @SuppressWarnings("static-method") public void centCheckForOfMethod() {
    azzert.that(guessName.CENT, is(guessName.of("¢")));
    azzert.that(guessName.CENT, is(guessName.of("¢¢")));
    assertNotEquals(guessName.of(""), guessName.CENT);
  }
  @Test @SuppressWarnings("static-method") public void classConstCheckForOfMethod() {
    azzert.that(guessName.STATIC_CONSTANT, is(guessName.of("A_ABC_CLASS_1")));
    azzert.that(guessName.STATIC_CONSTANT, is(guessName.of("B99")));
    azzert.that(guessName.STATIC_CONSTANT, is(guessName.of("A_35")));
    azzert.that(guessName.STATIC_CONSTANT, is(guessName.of("A______4")));
    assertNotEquals(guessName.of("a_35"), guessName.STATIC_CONSTANT);
    assertNotEquals(guessName.of("_A_A"), guessName.STATIC_CONSTANT);
  }
  @Test @SuppressWarnings("static-method") public void classNameCheckForOfMethod() {
    azzert.that(guessName.CLASS_NAME, is(guessName.of("ClAsS")));
    azzert.that(guessName.CLASS_NAME, is(guessName.of("Oren95")));
    azzert.that(guessName.CLASS_NAME, is(guessName.of("$WhoStartsClassNameWithDollar")));
    assertNotEquals(guessName.of("f4NT4STIC"), guessName.CLASS_NAME);
    assertNotEquals(guessName.of("$$"), guessName.CLASS_NAME);
  }
  @Test @SuppressWarnings("static-method") public void dollarCheckForOfMethod() {
    azzert.that(guessName.DOLLAR, is(guessName.of("$")));
    azzert.that(guessName.DOLLAR, is(guessName.of("$$")));
    assertNotEquals(guessName.of(""), guessName.DOLLAR);
  }
  @Test @SuppressWarnings("static-method") public void getMethodCheckForOfMethod() {
    azzert.that(guessName.GETTER_METHOD, is(guessName.of("getThing")));
    azzert.that(guessName.GETTER_METHOD, is(guessName.of("getMethoD1")));
    azzert.that(guessName.GETTER_METHOD, is(guessName.of("getMyMOODtoBeH4PPY")));
    azzert.that(guessName.GETTER_METHOD, is(guessName.of("getF4NT4STIC")));
    assertNotEquals(guessName.of("GETIT"), guessName.GETTER_METHOD);
  }
  @Test @SuppressWarnings("static-method") public void isclassNameASTCheckTest() {
    assert !guessName.isClassName((ASTNode) null);
    assert guessName.isClassName(ASTNodeFromString("ClAsS"));
    assert guessName.isClassName(ASTNodeFromString("Oren95"));
    assert guessName.isClassName(ASTNodeFromString("$WhoStartsClassNameWithDollar"));
    assert !guessName.isClassName(ASTNodeFromString("f4NT4STIC"));
    assert !guessName.isClassName(ASTNodeFromString("$$"));
  }
  @Test @SuppressWarnings("static-method") public void isclassNameCheckTest() {
    assert guessName.isClassName("ClAsS");
    assert guessName.isClassName("Oren95");
    assert guessName.isClassName("$WhoStartsClassNameWithDollar");
    assert !guessName.isClassName("f4NT4STIC");
    assert !guessName.isClassName("$$");
  }
  @Test @SuppressWarnings("static-method") public void isMethodCheckForOfMethod() {
    azzert.that(guessName.IS_METHOD, is(guessName.of("isOK")));
    azzert.that(guessName.IS_METHOD, is(guessName.of("isLEGAL_1")));
    azzert.that(guessName.IS_METHOD, is(guessName.of("isB_O_R_I_N_G")));
    azzert.that(guessName.IS_METHOD, is(guessName.of("isF4NT4STIC")));
    assertNotEquals(guessName.of("IsOk"), guessName.IS_METHOD);
    assertNotEquals(guessName.of("isok"), guessName.IS_METHOD);
  }
  @Test @SuppressWarnings("static-method") public void MethodOrVariableCheckForOfMethod() {
    azzert.that(guessName.METHOD_OR_VARIABLE, is(guessName.of("methodCOOL")));
    azzert.that(guessName.METHOD_OR_VARIABLE, is(guessName.of("vaRiable99")));
    azzert.that(guessName.METHOD_OR_VARIABLE, is(guessName.of("_alsoOK__")));
    assertNotEquals(guessName.of("NOTCOOL"), guessName.METHOD_OR_VARIABLE);
    assertNotEquals(guessName.of("___"), guessName.METHOD_OR_VARIABLE);
  }
  @Test @SuppressWarnings("static-method") public void nullCheckForOfMethod() {
    azzert.isNull(guessName.of(null));
  }
  @Test @SuppressWarnings("static-method") public void setMethodCheckForOfMethod() {
    azzert.that(guessName.SETTTER_METHOD, is(guessName.of("setThing")));
    azzert.that(guessName.SETTTER_METHOD, is(guessName.of("setMethoD1")));
    azzert.that(guessName.SETTTER_METHOD, is(guessName.of("setMyMOODtoBeH4PPY")));
    azzert.that(guessName.SETTTER_METHOD, is(guessName.of("setF4NT4STIC")));
    assertNotEquals(guessName.of("SETIT"), guessName.SETTTER_METHOD);
  }
  @Test @SuppressWarnings("static-method") public void underScoresCheckForOfMethod() {
    azzert.that(guessName.ANONYMOUS, is(guessName.of("_")));
    azzert.that(guessName.ANONYMOUS, is(guessName.of("__")));
  }
  @Test @SuppressWarnings("static-method") public void weirdoCheckForOfMethod() {
    azzert.that(guessName.WEIRDO, is(guessName.of("_$¢")));
    azzert.that(guessName.WEIRDO, is(guessName.of("_$¢_$¢")));
    azzert.that(guessName.WEIRDO, is(guessName.of("$_¢")));
    azzert.that(guessName.WEIRDO, is(guessName.of("$$__¢_¢_¢")));
    assertNotEquals(guessName.of("___"), guessName.WEIRDO);
    azzert.that(guessName.WEIRDO, is(guessName.of("$__$")));
  }
  @Test @SuppressWarnings("static-method") public void zeroLengthCheckForOfMethod() {
    azzert.isNull(guessName.of(""));
  }
}
