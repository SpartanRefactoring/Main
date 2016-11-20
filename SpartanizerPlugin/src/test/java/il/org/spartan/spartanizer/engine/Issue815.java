package il.org.spartan.spartanizer.engine;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** see issue #815 and #799 for more details
 * @author Oren Afek
 * @author Amir Sagiv
 * @since 16-11-11 */
public class Issue815 {
  @SuppressWarnings("static-method") @Test public void nullCheckForOfMethod() {
    assertNull(NameGuess.of(null));
  }

  @SuppressWarnings("static-method") @Test public void zeroLengthCheckForOfMethod() {
    assertNull(NameGuess.of(""));
  }

  @SuppressWarnings("static-method") @Test public void underScoresCheckForOfMethod() {
    assertEquals(NameGuess.of("_"), NameGuess.ANONYMOUS);
    assertEquals(NameGuess.of("__"), NameGuess.ANONYMOUS);
  }

  @SuppressWarnings("static-method") @Test public void dollarCheckForOfMethod() {
    assertEquals(NameGuess.of("$"), NameGuess.DOLLAR);
    assertEquals(NameGuess.of("$$"), NameGuess.DOLLAR);
    assertNotEquals(NameGuess.of(""), NameGuess.DOLLAR);
  }

  @SuppressWarnings("static-method") @Test public void centCheckForOfMethod() {
    assertEquals(NameGuess.of("¢"), NameGuess.CENT);
    assertEquals(NameGuess.of("¢¢"), NameGuess.CENT);
    assertNotEquals(NameGuess.of(""), NameGuess.CENT);
  }

  // _$¢
  @SuppressWarnings("static-method") @Test public void weirdoCheckForOfMethod() {
    assertEquals(NameGuess.of("_$¢"), NameGuess.WEIRDO);
    assertEquals(NameGuess.of("_$¢_$¢"), NameGuess.WEIRDO);
    assertEquals(NameGuess.of("$_¢"), NameGuess.WEIRDO);
    assertEquals(NameGuess.of("$$__¢_¢_¢"), NameGuess.WEIRDO);
    assertNotEquals(NameGuess.of("___"), NameGuess.WEIRDO);
    assertEquals(NameGuess.of("$__$"), NameGuess.WEIRDO);
  }

  @SuppressWarnings("static-method") @Test public void classConstCheckForOfMethod() {
    assertEquals(NameGuess.of("A_ABC_CLASS_1"), NameGuess.CLASS_CONSTANT);
    assertEquals(NameGuess.of("B99"), NameGuess.CLASS_CONSTANT);
    assertEquals(NameGuess.of("A_35"), NameGuess.CLASS_CONSTANT);
    assertEquals(NameGuess.of("A______4"), NameGuess.CLASS_CONSTANT);
    assertNotEquals(NameGuess.of("a_35"), NameGuess.CLASS_CONSTANT);
    assertNotEquals(NameGuess.of("_A_A"), NameGuess.CLASS_CONSTANT);
  }

  @SuppressWarnings("static-method") @Test public void isMethodCheckForOfMethod() {
    assertEquals(NameGuess.of("isOK"), NameGuess.IS_METHOD);
    assertEquals(NameGuess.of("isLEGAL_1"), NameGuess.IS_METHOD);
    assertEquals(NameGuess.of("isB_O_R_I_N_G"), NameGuess.IS_METHOD);
    assertEquals(NameGuess.of("isF4NT4STIC"), NameGuess.IS_METHOD);
    assertNotEquals(NameGuess.of("IsOk"), NameGuess.IS_METHOD);
    assertNotEquals(NameGuess.of("isok"), NameGuess.IS_METHOD);
  }

  @SuppressWarnings("static-method") @Test public void setMethodCheckForOfMethod() {
    assertEquals(NameGuess.of("setThing"), NameGuess.SETTTER_METHOD);
    assertEquals(NameGuess.of("setMethoD1"), NameGuess.SETTTER_METHOD);
    assertEquals(NameGuess.of("setMyMOODtoBeH4PPY"), NameGuess.SETTTER_METHOD);
    assertEquals(NameGuess.of("setF4NT4STIC"), NameGuess.SETTTER_METHOD);
    assertNotEquals(NameGuess.of("SETIT"), NameGuess.SETTTER_METHOD);
  }

  @SuppressWarnings("static-method") @Test public void getMethodCheckForOfMethod() {
    assertEquals(NameGuess.of("getThing"), NameGuess.GETTER_METHOD);
    assertEquals(NameGuess.of("getMethoD1"), NameGuess.GETTER_METHOD);
    assertEquals(NameGuess.of("getMyMOODtoBeH4PPY"), NameGuess.GETTER_METHOD);
    assertEquals(NameGuess.of("getF4NT4STIC"), NameGuess.GETTER_METHOD);
    assertNotEquals(NameGuess.of("GETIT"), NameGuess.GETTER_METHOD);
  }

  @SuppressWarnings("static-method") @Test public void classNameCheckForOfMethod() {
    assertEquals(NameGuess.of("ClAsS"), NameGuess.CLASS_NAME);
    assertEquals(NameGuess.of("Oren95"), NameGuess.CLASS_NAME);
    assertEquals(NameGuess.of("$WhoStartsClassNameWithDollar"), NameGuess.CLASS_NAME);
    assertNotEquals(NameGuess.of("f4NT4STIC"), NameGuess.CLASS_NAME);
    assertNotEquals(NameGuess.of("$$"), NameGuess.CLASS_NAME);
  }

  @SuppressWarnings("static-method") @Test public void MethodOrVariableCheckForOfMethod() {
    assertEquals(NameGuess.of("methodCOOL"), NameGuess.METHOD_OR_VARIABLE);
    assertEquals(NameGuess.of("vaRiable99"), NameGuess.METHOD_OR_VARIABLE);
    assertEquals(NameGuess.of("_alsoOK__"), NameGuess.METHOD_OR_VARIABLE);
    assertNotEquals(NameGuess.of("NOTCOOL"), NameGuess.METHOD_OR_VARIABLE);
    assertNotEquals(NameGuess.of("___"), NameGuess.METHOD_OR_VARIABLE);
  }

  @SuppressWarnings("static-method") @Test public void assertCoverForOfMethod() {
    try {
      assertNotEquals(NameGuess.of("A_abc_CLASS_1"), NameGuess.CLASS_CONSTANT);
      fail();
    } catch (final Error e) {
      assertEquals(e.getClass(), AssertionError.class);
    }
  }

  @SuppressWarnings("static-method") @Test public void isclassNameCheckTest() {
    assert NameGuess.isClassName("ClAsS");
    assert NameGuess.isClassName("Oren95");
    assert NameGuess.isClassName("$WhoStartsClassNameWithDollar");
    assert !NameGuess.isClassName("f4NT4STIC");
    assert !NameGuess.isClassName("$$");
  }

  @SuppressWarnings("static-method") @Test public void isclassNameASTCheckTest() {
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
