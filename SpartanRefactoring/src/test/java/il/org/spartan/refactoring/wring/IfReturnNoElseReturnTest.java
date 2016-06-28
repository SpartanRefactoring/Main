package il.org.spartan.refactoring.wring;

import static il.org.spartan.hamcrest.CoreMatchers.*;
import static il.org.spartan.hamcrest.MatcherAssert.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;
import il.org.spartan.hamcrest.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.AbstractWringTest.OutOfScope;
import il.org.spartan.refactoring.wring.AbstractWringTest.Wringed;
import il.org.spartan.utils.Utils;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameters;

/**
 * Unit tests for
 * {@link Wrings#IFX_SINGLE_RETURN_MISSING_ELSE_FOLLOWED_BY_RETURN}.
 *
 * @author Yossi Gil
 * @since 2014-07-13
 */
@SuppressWarnings({ "javadoc", "static-method" })//
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
public class IfReturnNoElseReturnTest {
  static final Wring<IfStatement> WRING = new IfReturnNoElseReturn();

  @Test public void checkFirstIfStatement1() {
    final String s = "if (a) return b; return a();";
    final IfStatement i = Extract.firstIfStatement(As.STATEMENTS.ast(s));
    JunitHamcrestWrappper.assertNotNull(i);
    assertThat(i.toString(), WRING.scopeIncludes(i), is(true));
  }
  @Test public void checkFirstIfStatement2() {
    final String s = "if (a) return b; else return a();";
    final IfStatement i = Extract.firstIfStatement(As.STATEMENTS.ast(s));
    JunitHamcrestWrappper.assertNotNull(i);
    assertThat(i.toString(), WRING.scopeIncludes(i), is(false));
  }
  @Test public void checkFirstIfStatement3() {
    final String s = "if (a) a= b; else a=c;";
    final IfStatement i = Extract.firstIfStatement(As.STATEMENTS.ast(s));
    JunitHamcrestWrappper.assertNotNull(i);
    assertThat(i.toString(), WRING.scopeIncludes(i), is(false));
  }

  @RunWith(Parameterized.class)//
  public static class OutOfScope extends AbstractWringTest.OutOfScope<IfStatement> {
    static String[][] cases = Utils.asArray(//
        new String[] { "Another distinct assignment", "if (a) a /= b; else a %= c;" }, //
        new String[] { "Literal vs. Literal", "if (a) return b; else c;" }, //
        new String[] { "Nested if return", "if (a) {;{{;;return b; }}} else {{{;return c;};;};}" }, //
        new String[] { "Nested if return", "if (a) {;{{;;throw b; }}} else {{{;throw c;};;};}" }, //
        new String[] { "Not same assignment", "if (a) a /= b; else a /= c;" }, //
        new String[] { "Return only on one side", "if (a) return b; else c;" }, //
        new String[] { "Simple if assign", "if (a) a = b; else a = c;" }, //
        new String[] { "Simple if plus assign", "if (a) a *= b; else a *= c;" }, //
        new String[] { "Simple if plus assign", "if (a) a += b; else a += c;" }, //
        new String[] { "Simple if return", "if (a) return b; else return c;" }, //
        new String[] { "Simple if throw", "if (a) throw b; else throw c;" }, //
        new String[] { "Simply nested if return", "{if (a)  return b; else return c;}" }, //
        new String[] { "Simply nested if throw", "{if (a)  throw b; else throw c;}" }, //
        new String[] { "Vanilla ;;", "if (a) ; else ;" }, //
        new String[] { "Vanilla {}{}", "if (a) {} else {}" }, //
        new String[] { "Vanilla ; ", "if (a) return b; else ;" }, //
        new String[] { "Vanilla ; ", "if (a) return b; else ;", }, //
        new String[] { "Vanilla {;{;;};} ", "if (a) return b; else {;{;{};};{;{}}}" }, //
        new String[] { "Vanilla {}", "if (a) return b; else {}" }, //
        new String[] { "Vanilla if-then-else", "if (a) return b;" }, //
        new String[] { "Vanilla if-then-no-else", "if (a) return b;" }, //
        new String[] { "Simple if plus assign", "if (a) a *= b; else a *= c;" }, //
        new String[] { "Simple if return empty else", "if (a) return b; else ;" }, //
        null);

    /**
     * Generate test cases for this parameterized class.
     *
     * @return a collection of cases, where each case is an array of three
     *         objects, the test case name, the input, and the file.
     */
    @Parameters(name = DESCRIPTION)//
    public static Collection<Object[]> cases() {
      return collect(cases);
    }
    /** Instantiates the enclosing class ({@link OutOfScope}) */
    public OutOfScope() {
      super(WRING);
    }
  }

  @RunWith(Parameterized.class)//
  @FixMethodOrder(MethodSorters.NAME_ASCENDING)//
  public static class Wringed extends AbstractWringTest.Wringed.IfStatementAndSurrounding {
    private static String[][] cases = new String[][] { //
      new String[] { "Vanilla {}", "if (a) return b; return a();", "return a ? b: a();" }, //
      new String[] { "Vanilla ; ", "if (a) return b; return a(); b(); c();", "return a ? b: a(); b(); c();" }, //
      new String[] { "Vanilla {;{;;};} ", "if (a) return b; else {;{;{};};{;{}}} return c;", "return a?b:c;" }, //
      null, //
      new String[] { "Compressed complex", " if (x) {;f();;;return a;;;} else {;g();{;;{}}{}}",
      "if (x) {;f();;;return a;;;}\n g();" }, //
      null, //
      new String[] { "Compressed complex", " if (x) {;f();;;return a;;;} else {;g();{;;{}}{}}",
      "  if(x){;f();;;return a;;;} g();" }, //
      new String[] { "Compressed complex", " if (x) {;f();;;return a;;;} else {;g();{;;{}}{}}", "" + //
            " if (x) {\n" + //
            "   f();\n" + //
            "   return a;\n" + //
            " }\n" + //
            " g();\n" + //
            "" }, null, //
      new String[] { "Complex with many junk statements", "" + //
            " if (x) {\n" + //
            "   ;\n" + //
            "   f();\n" + //
            "   return a;\n" + //
            " } else {\n" + //
            "   ;\n" + //
            "   g();\n" + //
            "   {\n" + //
            "   }\n" + //
            " }\n" + //
            "", "" + //
            " if (x) {\n" + //
            "   f();\n" + //
            "   return a;\n" + //
            " }\n" + //
            " g();\n" + //
            "" }, //
      null };

    /**
     * Generate test cases for this parameterized class.
     *
     * @return a collection of cases, where each case is an array of three
     *         objects, the test case name, the input, and the file.
     */
    @Parameters(name = DESCRIPTION)//
    public static Collection<Object[]> cases() {
      return collect(cases);
    }
    /**
     * Instantiates the enclosing class ({@link Wringed})
     */
    public Wringed() {
      super(WRING);
    }
    @Test public void asMeNotNull() {
      JunitHamcrestWrappper.assertNotNull(asMe());
    }
    @Test public void followedByReturn() {
      assertThat(Extract.nextReturn(asMe()), notNullValue());
    }
    @Test public void isfStatementElseIsEmpty() {
      assertThat(Extract.statements(Extract.firstIfStatement(As.STATEMENTS.ast(input)).getElseStatement()).size(), is(0));
    }
    @Test public void isIfStatement() {
      assertThat(input, asMe(), notNullValue());
    }
    @Test public void myScopeIncludes() {
      final IfStatement s = asMe();
      assertThat(s, notNullValue());
      assertThat(Extract.statements(elze(s)), notNullValue());
      assertThat(Extract.statements(elze(s)).size(), is(0));
    }
    @Test public void noElse() {
      assertThat(Extract.statements(elze(asMe())).size(), is(0));
    }
    @Test public void thenIsSingleReturn() {
      assertThat(Extract.returnStatement(then(asMe())), notNullValue());
    }
  }
}
