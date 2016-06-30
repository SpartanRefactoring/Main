package il.org.spartan.refactoring.wring;

import static il.org.spartan.hamcrest.SpartanAssert.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.junit.Assert.*;
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
 * Unit tests for {@link IfFooSequencerIfFooSameSequencer}.
 *
 * @author Yossi Gil
 * @since 2014-07-13
 */
@SuppressWarnings({ "javadoc", "static-method" })//
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
public class IfCommandsSequencerIfSameCommandsSequencerTest {
  static final Wring<IfStatement> WRING = new IfFooSequencerIfFooSameSequencer();

  @Test public void checkFirstIfStatement1() {
    final String s = "if (a) return b; if (b) return b;";
    final ASTNode n = As.STATEMENTS.ast(s);
    assertThat(n, notNullValue());
    final IfStatement i = Extract.firstIfStatement(n);
    assertThat(n.toString(), i, notNullValue());
    assert i != null;
    assertThat(i.toString(), WRING.scopeIncludes(i), is(true));
  }
  @Test public void checkFirstIfStatement2() {
    final String s = "if (a) return b; else return a();";
    final IfStatement i = Extract.firstIfStatement(As.STATEMENTS.ast(s));
    assertThat(i, notNullValue());
    assertThat(i.toString(), WRING.scopeIncludes(i), is(false));
  }
  @Test public void checkFirstIfStatement3() {
    final String s = "if (a) a= b; else a=c;";
    final IfStatement i = Extract.firstIfStatement(As.STATEMENTS.ast(s));
    assertThat(i, notNullValue());
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
        new String[] { "Vanilla {}", "if (a) return b; return a();", }, //
        new String[] { "Vanilla ; ", "if (a) return b; return a(); b(); c();", }, //
        new String[] { "Vanilla {;{;;};} ", "if (a) return b; else {;{;{};};{;{}}} return c;", }, //
        null, //
        new String[] { "Compressed complex", " if (x) {;f();;;return a;;;} else {;g();{;;{}}{}}" }, //
        new String[] { "Compressed complex", " if (x) {;f();;;return a;;;} else {;g();{;;{}}{}}" }, //
        new String[] { "Compressed complex", " if (x) {;f();;;return a;;;} else {;g();{;;{}}{}}" }, //
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
            "" }, //
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
        new String[] { "Return expression", "if (a) return a; if (b) return a;", "if (a || b) return a;" }, //
        new String[] { "Return empty", "if (a) return; if (b) return;", "if (a || b) return;" }, //
        new String[] { "Break expression", "if (a) break a; if (b) break a;", "if (a || b) break a;" }, //
        new String[] { "Break empty", "if (a) break; if (b) break;", "if (a || b) break;" }, //
        new String[] { "Continue expression", "if (a) continue a; if (b) continue a;", "if (a || b) continue a;" }, //
        new String[] { "Continue empty", "if (a) continue; if (b) continue;", "if (a || b) continue;" }, //
        new String[] { "Throw expression", "if (a) throw e; if (b) throw e;", "if (a || b) throw e;" }, //
        new String[] { "Single statement is nested", "if (a) {{{; return a; }}} if (b) {;{;return a;};;}", "if (a || b) return a;" }, //
        new String[] { "Parenthesis where necesary", "if (a=b) return a; if (b=a) return a;", "if ((a=b) || (b =a)) return a;" }, //
        new String[] { "No parenthesis for == ", "if (a==b) return a; if (b==a) return a;", "if (a==b || b ==a) return a;" }, //
        new String[] { "No parenthesis for  && and ||", "if (a&&b) return a; if (b||a) return a;", "if (a&&b || b ||a) return a;" }, //
        new String[] { "No parenthesis for OR", "if (a||b||c) return a; if (a||b||c||d) return a;",
            "if (a||b||c||a||b||c||d) return a;" }, //
        new String[] { "Two statements", "if (a) { f(); return a; } if (b) {f(); return a;}", "if (a || b) {f(); return a;}" }, //
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
      assertThat(asMe(), notNullValue());
    }
    @Test public void followedByReturn() {
      assertThat(Extract.nextIfStatement(asMe()), notNullValue());
    }
    @Test public void isfStatementElseIsEmpty() {
      assertThat(Extract.statements(elze(Extract.firstIfStatement(As.STATEMENTS.ast(input)))).size(), is(0));
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
  }
}
