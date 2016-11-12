package il.org.spartan.spartanizer.ast.engine;

import static il.org.spartan.azzert.*;
import static org.junit.Assert.fail;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;

/** Tests of {@link NameGuess}
 * @author Yossi Gil Added more Tests in 12.11.16:
 * @author Amir Sagiv
 * @author Oren Afek
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public class NameGuessTest {
  @Test public void anonymous1() {
    azzert.that(NameGuess.of("_"), is(NameGuess.ANONYMOUS));
  }
  @Test public void anonymous2() {
    azzert.that(NameGuess.of("__"), is(NameGuess.ANONYMOUS));
  }
  @Test public void anonymous3() {
    azzert.that(NameGuess.of("___"), is(NameGuess.ANONYMOUS));
  }
  @Test public void cent1() {
    azzert.that(NameGuess.of("¢"), is(NameGuess.CENT));
  }
  @Test public void cent2() {
    azzert.that(NameGuess.of("¢¢"), is(NameGuess.CENT));
  }
  @Test public void cent3() {
    azzert.that(NameGuess.of("¢¢¢"), is(NameGuess.CENT));
  }
  @Test public void classConstant1() {
    azzert.that(NameGuess.of("ABC"), is(NameGuess.CLASS_CONSTANT));
  }
  @Test public void classConstant2() {
    azzert.that(NameGuess.of("ABC"), is(NameGuess.CLASS_CONSTANT));
  }
  @Test public void classConstant3() {
    azzert.that(NameGuess.of("ABC"), is(NameGuess.CLASS_CONSTANT));
  }
  @Test public void className1() {
    azzert.that(NameGuess.of(this.getClass().getSimpleName()), is(NameGuess.CLASS_NAME));
  }
  @Test public void className2() {
    azzert.that(NameGuess.of("Class"), is(NameGuess.CLASS_NAME));
  }
  @Test public void className3() {
    azzert.that(NameGuess.of("MyClass"), is(NameGuess.CLASS_NAME));
  }
  @Test public void className4() {
    assert NameGuess.isClassName("MyClass");
  }
  @Test public void className5() {
    assert NameGuess.isClassName("$Class");
  }
  @Test public void className6() {
    assert NameGuess.isClassName("YourClass");
  }
  @Test public void dollar1() {
    azzert.that(NameGuess.of("$"), is(NameGuess.DOLLAR));
  }
  @Test public void dollar2() {
    azzert.that(NameGuess.of("$$"), is(NameGuess.DOLLAR));
  }
  @Test public void dollar3() {
    azzert.that(NameGuess.of("$$$"), is(NameGuess.DOLLAR));
  }
  @Test public void variable1() {
    azzert.that(NameGuess.of("multiset1"), is(NameGuess.METHOD_OR_VARIABLE));
  }
  @Test public void null1() {
    azzert.isNull(NameGuess.of(null));
  }
  @Test public void empty1() {
    azzert.isNull(NameGuess.of(""));
  }
  @Test public void weirdo1() {
    azzert.that(NameGuess.of("_$¢"), is(NameGuess.WEIRDO));
  }
  @Test public void weirdo2() {
    azzert.that(NameGuess.of("_$¢_$¢"), is(NameGuess.WEIRDO));
  }
  @Test public void weirdo3() {
    azzert.that(NameGuess.of("$_¢"), is(NameGuess.WEIRDO));
  }
  @Test public void weirdo4() {
    azzert.that(NameGuess.of("$$__¢_¢_¢"), is(NameGuess.WEIRDO));
  }
  @Test public void weirdo5() {
    azzert.that(NameGuess.of("$__$"), is(NameGuess.WEIRDO));
  }
  @Test public void classConstant4() {
    azzert.that(NameGuess.of("A_35"), is(NameGuess.CLASS_CONSTANT));
  }
  @Test public void classConstant5() {
    azzert.that(NameGuess.of("A______4"), is(NameGuess.CLASS_CONSTANT));
  }
  @Test public void classConstant6() {
    azzert.assertNotEquals(NameGuess.of("a_35"), NameGuess.CLASS_CONSTANT);
  }
  @Test public void classConstant7() {
    azzert.assertNotEquals(NameGuess.of("_A_A"), NameGuess.CLASS_CONSTANT);
  }
  @Test public void isMethod1() {
    azzert.that(NameGuess.of("isOK"), is(NameGuess.IS_METHOD));
  }
  @Test public void isMethod2() {
    azzert.that(NameGuess.of("isLEGAL_1"), is(NameGuess.IS_METHOD));
  }
  @Test public void isMethod3() {
    azzert.that(NameGuess.of("isB_O_R_I_N_G"), is(NameGuess.IS_METHOD));
  }
  @Test public void isMethod4() {
    azzert.that(NameGuess.of("isF4NT4STIC"), is(NameGuess.IS_METHOD));
  }
  @Test public void isMethod5() {
    azzert.assertNotEquals(NameGuess.of("IsOk"), NameGuess.IS_METHOD);
  }
  @Test public void isMethod6() {
    azzert.assertNotEquals(NameGuess.of("isok"), NameGuess.IS_METHOD);
  }
  @Test public void setMethod1() {
    azzert.that(NameGuess.of("setThing"), is(NameGuess.SETTTER_METHOD));
  }
  @Test public void setMethod2() {
    azzert.that(NameGuess.of("setMethoD1"), is(NameGuess.SETTTER_METHOD));
  }
  @Test public void setMethod3() {
    azzert.that(NameGuess.of("setMyMOODtoBeH4PPY"), is(NameGuess.SETTTER_METHOD));
  }
  @Test public void setMethod4() {
    azzert.that(NameGuess.of("setF4NT4STIC"), is(NameGuess.SETTTER_METHOD));
  }
  @Test public void setMethod5() {
    azzert.assertNotEquals(NameGuess.of("SETIT"), NameGuess.SETTTER_METHOD);
  }
  @Test public void getMethod1() {
    azzert.that(NameGuess.of("getThing"), is(NameGuess.GETTER_METHOD));
  }
  @Test public void getMethod2() {
    azzert.that(NameGuess.of("getMethoD1"), is(NameGuess.GETTER_METHOD));
  }
  @Test public void getMethod3() {
    azzert.that(NameGuess.of("getMyMOODtoBeH4PPY"), is(NameGuess.GETTER_METHOD));
  }
  @Test public void getMethod4() {
    azzert.that(NameGuess.of("getF4NT4STIC"), is(NameGuess.GETTER_METHOD));
  }
  @Test public void getMethod5() {
    azzert.assertNotEquals(NameGuess.of("GETIT"), NameGuess.GETTER_METHOD);
  }
  @Test public void method1() {
    azzert.that(NameGuess.of("methodCOOL"), is(NameGuess.METHOD_OR_VARIABLE));
  }
  @Test public void variable2() {
    azzert.that(NameGuess.of("vaRiable99"), is(NameGuess.METHOD_OR_VARIABLE));
  }
  @Test public void variable3() {
    azzert.that(NameGuess.of("_alsoOK__"), is(NameGuess.METHOD_OR_VARIABLE));
  }
  @Test public void assert1() {
    try {
      azzert.assertNotEquals(NameGuess.of("A_abc_CLASS_1"), NameGuess.CLASS_CONSTANT);
      fail();
    } catch (final Error e) {
      azzert.assertEquals(e.getClass(), AssertionError.class);
    }
  }
  @Test public void className7() {
    azzert.assertFalse(NameGuess.isClassName("$$"));
  }
  @Test public void classNameAST1() {
    azzert.assertFalse(NameGuess.isClassName((ASTNode) null));
  }
  @Test public void classNameAST2() {
    azzert.assertTrue(NameGuess.isClassName(ASTNodeFromString("ClAsS")));
  }
  @Test public void classNameAST3() {
    azzert.assertTrue(NameGuess.isClassName(ASTNodeFromString("$WhoStartsClassNameWithDollar")));
  }
  @Test public void classNameAST4() {
    azzert.assertTrue(NameGuess.isClassName(ASTNodeFromString("Oren94")));
  }
  @Test public void classNameAST5() {
    azzert.assertFalse(NameGuess.isClassName(ASTNodeFromString("f4NT4STIC")));
  }
  private static ASTNode ASTNodeFromString(final String ¢) {
    return wizard.ast(¢);
  }
}
