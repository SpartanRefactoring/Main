package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** see issue #815 and #799 for more details
 * @author Oren Afek
 * @author Amir Sagiv
 * @since 16-11-11 */
public class Issue815 {
  @Test @SuppressWarnings("static-method") public void nullCheckForOfMethod() {
    azzert.isNull(NameGuess.of(null));
  }

  @Test @SuppressWarnings("static-method") public void zeroLengthCheckForOfMethod() {
    azzert.isNull(NameGuess.of(""));
  }

  @Test @SuppressWarnings("static-method") public void underScoresCheckForOfMethod() {
    azzert.that(NameGuess.ANONYMOUS, is(NameGuess.of("_")));
    azzert.that(NameGuess.ANONYMOUS, is(NameGuess.of("__")));
  }

  @Test @SuppressWarnings("static-method") public void dollarCheckForOfMethod() {
    azzert.that(NameGuess.DOLLAR, is(NameGuess.of("$")));
    azzert.that(NameGuess.DOLLAR, is(NameGuess.of("$$")));
    assertNotEquals(NameGuess.of(""), NameGuess.DOLLAR);
  }

  @Test @SuppressWarnings("static-method") public void centCheckForOfMethod() {
    azzert.that(NameGuess.CENT, is(NameGuess.of("¢")));
    azzert.that(NameGuess.CENT, is(NameGuess.of("¢¢")));
    assertNotEquals(NameGuess.of(""), NameGuess.CENT);
  }

  @Test @SuppressWarnings("static-method") public void weirdoCheckForOfMethod() {
    azzert.that(NameGuess.WEIRDO, is(NameGuess.of("_$¢")));
    azzert.that(NameGuess.WEIRDO, is(NameGuess.of("_$¢_$¢")));
    azzert.that(NameGuess.WEIRDO, is(NameGuess.of("$_¢")));
    azzert.that(NameGuess.WEIRDO, is(NameGuess.of("$$__¢_¢_¢")));
    assertNotEquals(NameGuess.of("___"), NameGuess.WEIRDO);
    azzert.that(NameGuess.WEIRDO, is(NameGuess.of("$__$")));
  }

  @Test @SuppressWarnings("static-method") public void classConstCheckForOfMethod() {
    azzert.that(NameGuess.CLASS_CONSTANT, is(NameGuess.of("A_ABC_CLASS_1")));
    azzert.that(NameGuess.CLASS_CONSTANT, is(NameGuess.of("B99")));
    azzert.that(NameGuess.CLASS_CONSTANT, is(NameGuess.of("A_35")));
    azzert.that(NameGuess.CLASS_CONSTANT, is(NameGuess.of("A______4")));
    assertNotEquals(NameGuess.of("a_35"), NameGuess.CLASS_CONSTANT);
    assertNotEquals(NameGuess.of("_A_A"), NameGuess.CLASS_CONSTANT);
  }

  @Test @SuppressWarnings("static-method") public void isMethodCheckForOfMethod() {
    azzert.that(NameGuess.IS_METHOD, is(NameGuess.of("isOK")));
    azzert.that(NameGuess.IS_METHOD, is(NameGuess.of("isLEGAL_1")));
    azzert.that(NameGuess.IS_METHOD, is(NameGuess.of("isB_O_R_I_N_G")));
    azzert.that(NameGuess.IS_METHOD, is(NameGuess.of("isF4NT4STIC")));
    assertNotEquals(NameGuess.of("IsOk"), NameGuess.IS_METHOD);
    assertNotEquals(NameGuess.of("isok"), NameGuess.IS_METHOD);
  }

  @Test @SuppressWarnings("static-method") public void setMethodCheckForOfMethod() {
    azzert.that(NameGuess.SETTTER_METHOD, is(NameGuess.of("setThing")));
    azzert.that(NameGuess.SETTTER_METHOD, is(NameGuess.of("setMethoD1")));
    azzert.that(NameGuess.SETTTER_METHOD, is(NameGuess.of("setMyMOODtoBeH4PPY")));
    azzert.that(NameGuess.SETTTER_METHOD, is(NameGuess.of("setF4NT4STIC")));
    assertNotEquals(NameGuess.of("SETIT"), NameGuess.SETTTER_METHOD);
  }

  @Test @SuppressWarnings("static-method") public void getMethodCheckForOfMethod() {
    azzert.that(NameGuess.GETTER_METHOD, is(NameGuess.of("getThing")));
    azzert.that(NameGuess.GETTER_METHOD, is(NameGuess.of("getMethoD1")));
    azzert.that(NameGuess.GETTER_METHOD, is(NameGuess.of("getMyMOODtoBeH4PPY")));
    azzert.that(NameGuess.GETTER_METHOD, is(NameGuess.of("getF4NT4STIC")));
    assertNotEquals(NameGuess.of("GETIT"), NameGuess.GETTER_METHOD);
  }

  @Test @SuppressWarnings("static-method") public void classNameCheckForOfMethod() {
    azzert.that(NameGuess.CLASS_NAME, is(NameGuess.of("ClAsS")));
    azzert.that(NameGuess.CLASS_NAME, is(NameGuess.of("Oren95")));
    azzert.that(NameGuess.CLASS_NAME, is(NameGuess.of("$WhoStartsClassNameWithDollar")));
    assertNotEquals(NameGuess.of("f4NT4STIC"), NameGuess.CLASS_NAME);
    assertNotEquals(NameGuess.of("$$"), NameGuess.CLASS_NAME);
  }

  @Test @SuppressWarnings("static-method") public void MethodOrVariableCheckForOfMethod() {
    azzert.that(NameGuess.METHOD_OR_VARIABLE, is(NameGuess.of("methodCOOL")));
    azzert.that(NameGuess.METHOD_OR_VARIABLE, is(NameGuess.of("vaRiable99")));
    azzert.that(NameGuess.METHOD_OR_VARIABLE, is(NameGuess.of("_alsoOK__")));
    assertNotEquals(NameGuess.of("NOTCOOL"), NameGuess.METHOD_OR_VARIABLE);
    assertNotEquals(NameGuess.of("___"), NameGuess.METHOD_OR_VARIABLE);
  }

  @Test @SuppressWarnings("static-method") public void assertCoverForOfMethod() {
    try {
      assertNotEquals(NameGuess.of("A_abc_CLASS_1"), NameGuess.CLASS_CONSTANT);
      fail();
    } catch (final Error ¢) {
      azzert.that(AssertionError.class, is(¢.getClass()));
    }
  }

  @Test @SuppressWarnings("static-method") public void isclassNameCheckTest() {
    assert NameGuess.isClassName("ClAsS");
    assert NameGuess.isClassName("Oren95");
    assert NameGuess.isClassName("$WhoStartsClassNameWithDollar");
    assert !NameGuess.isClassName("f4NT4STIC");
    assert !NameGuess.isClassName("$$");
  }

  @Test @SuppressWarnings("static-method") public void isclassNameASTCheckTest() {
    assert !NameGuess.isClassName((ASTNode) null);
    assert NameGuess.isClassName(ASTNodeFromString("ClAsS"));
    assert NameGuess.isClassName(ASTNodeFromString("Oren95"));
    assert NameGuess.isClassName(ASTNodeFromString("$WhoStartsClassNameWithDollar"));
    assert !NameGuess.isClassName(ASTNodeFromString("f4NT4STIC"));
    assert !NameGuess.isClassName(ASTNodeFromString("$$"));
  }

  private static ASTNode ASTNodeFromString(final String ¢) {
    return wizard.ast(¢);
  }
}
