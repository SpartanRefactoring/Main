package il.org.spartan.refactoring.wring;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.asSingle;
import static il.org.spartan.refactoring.utils.Funcs.asIfStatement;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import il.org.spartan.refactoring.wring.AbstractWringTest.OutOfScope;
import il.org.spartan.refactoring.wring.AbstractWringTest.Wringed;
import il.org.spartan.utils.Utils;

/* @author Yossi Gil
 *
 * @since 2014-07-13 */
@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class IfExpressionStatementElseSimilarExpressionStatementTest {
  static final IfExpressionStatementElseSimilarExpressionStatement WRING = new IfExpressionStatementElseSimilarExpressionStatement();

  @Test public void checkSteps() {
    final Statement s = asSingle("if (a) f(b); else f(c);");
    assertNotNull(s);
    final IfStatement i = asIfStatement(s);
    assertNotNull(i);
    assertThat(WRING.scopeIncludes(i), is(true));
  }

  @RunWith(Parameterized.class) //
  public static class OutOfScope extends AbstractWringTest.OutOfScope<IfStatement> {
    static String[][] cases = Utils.asArray(//
        new String[] { "Expression vs. Expression", " 6 - 7 < 2 + 1   " }, //
        new String[] { "Literal vs. Literal", "if (a) return b; else c;" }, //
        new String[] { "Simple if return", "if (a) return b; else return c;" }, //
        new String[] { "Simply nested if return", "{if (a)  return b; else return c;}" }, //
        new String[] { "Nested if return", "if (a) {;{{;;return b; }}} else {{{;return c;};;};}" }, //
        new String[] { "Not same assignment", "if (a) a /= b; else a /= c;" }, //
        new String[] { "Another distinct assignment", "if (a) a /= b; else a %= c;" }, //
        new String[] { "Simple if assign", "if (a) a = b; else a = c;", }, //
        new String[] { "Simple if plus assign", "if (a) a += b; else a += c;", }, //
        new String[] { "Simple if plus assign", "if (a) a *= b; else a *= c;", }, //
        null);

    /**
     * Generate test cases for this parameterized class.
     *
     * @return a collection of cases, where each case is an array of three
     *         objects, the test case name, the input, and the file.
     */
    @Parameters(name = DESCRIPTION) //
    public static Collection<Object[]> cases() {
      return collect(cases);
    }
    /** Instantiates the enclosing class ({@link OutOfScope}) */
    public OutOfScope() {
      super(WRING);
    }
  }

  @RunWith(Parameterized.class) //
  @FixMethodOrder(MethodSorters.NAME_ASCENDING) //
  public static class Wringed extends AbstractWringTest.WringedIfStatement {
    private static String[][] cases = Utils.asArray(//
        new String[] { "Vanilla", "if (a) f(b); else f(c);", "f(a ? b: c);" }, //
        new String[] { "Method call", "if (a) x.f(b); else x.f(c);", "x.f(a ? b: c);" }, //
        new String[] { "Distinct receiver", "if (a) y.f(b); else x.f(b);", "(a ?y :x).f(b);" }, //
        new String[] { "Distinct receiver no arguments", "if (a) y.f(); else x.f();", "(a ?y :x).f();" }, //
        new String[] { "Distinct receiver two arguments", "if (a) y.f(a,b,c); else x.f(a,b,c);", "(a ?y :x).f(a,b,c);" }, //
        null);

    /**
     * Generate test cases for this parameterized class.
     *
     * @return a collection of cases, where each case is an array of three
     *         objects, the test case name, the input, and the file.
     */
    @Parameters(name = DESCRIPTION) //
    public static Collection<Object[]> cases() {
      return collect(cases);
    }
    /**
     * Instantiates the enclosing class ({@link Wringed})
     */
    public Wringed() {
      super(WRING);
    }
  }
}
