package il.org.spartan.refactoring.wring;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.refactoring.utils.Extract.core;
import static il.org.spartan.refactoring.utils.Funcs.asBooleanLiteral;
import static il.org.spartan.refactoring.utils.Funcs.asNot;
import static il.org.spartan.refactoring.utils.Into.p;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.wring.AbstractWringTest.OutOfScope;
import il.org.spartan.utils.Utils;

/**
 * Unit tests for {@link Wrings#ADDITION_SORTER}.
 *
 * @author Yossi Gil
 * @since 2014-07-13
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "static-method" }) //
public class PrefixNotPushdownTest {
  /** The {@link Wring} under test */
  static final PrefixNotPushdown WRING = new PrefixNotPushdown();

  @Test public void notOfFalse() {
    final PrefixExpression e = p("!false");
    assertThat(e, is(notNullValue()));
    assertThat(WRING.scopeIncludes(e), is(true));
    assertThat(WRING.eligible(e), is(true));
    assertThat(asNot(e), is(notNullValue()));
    final Expression inner = core(e.getOperand());
    assertThat(inner, is(notNullValue()));
    assertThat(inner.toString(), is("false"));
    assertThat(Is.booleanLiteral(inner), is(true));
    assertThat(PrefixNotPushdown.perhapsNotOfLiteral(inner), is(notNullValue()));
    assertThat(PrefixNotPushdown.notOfLiteral(asBooleanLiteral(inner)), is(notNullValue()));
    assertThat(PrefixNotPushdown.perhapsNotOfLiteral(inner), is(notNullValue()));
    assertThat(PrefixNotPushdown.pushdownNot(inner), is(notNullValue()));
    assertThat(PrefixNotPushdown.pushdownNot(asNot(e)), is(notNullValue()));
    assertThat(WRING.replacement(e), is(notNullValue()));
  }

  @RunWith(Parameterized.class) //
  public static class OutOfScope extends AbstractWringTest.OutOfScope.Exprezzion<PrefixExpression> {
    static String[][] cases = Utils.asArray(//
        new String[] { "Simple not", "!a" }, //
        new String[] { "Simple not of function", "!f(a)" }, //
        new String[] { "Actual example", "!inRange(m, e)" }, //
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
  public static class Wringed extends AbstractWringTest.WringedExpression<PrefixExpression> {
    private static String[][] cases = Utils.asArray(//
        new String[] { "2 level not of false", "!!false", "false" }, //
        new String[] { "2 level not of true", "!!true", "true" }, //
        new String[] { "double not deeply nested", "!(((!f())))", "f()" }, //
        new String[] { "double not", "!!f()", "f()" }, //
        new String[] { "double not nested", "!(!f())", "f()" }, //
        new String[] { "not of AND", "!(a && b && c)", "!a || !b || !c" }, //
        new String[] { "not of AND", "!(f() && f(5))", "!f() || !f(5)" }, //
        new String[] { "not of AND nested", "!(f() && (f(5)))", "!f() || !f(5)" }, //
        new String[] { "not of EQ", "!(3 == 5)", "3 != 5" }, //
        new String[] { "not of EQ nested", "!((((3 == 5))))", "3 != 5" }, //
        new String[] { "not of false", "!false", "true" }, //
        new String[] { "not of GE", "!(3 >= 5)", "3 < 5" }, //
        new String[] { "not of GT", "!(3 > 5)", "3 <= 5" }, //
        new String[] { "not of LE", "!(3 <= 5)", "3 > 5" }, //
        new String[] { "not of LT", "!(3 < 5)", "3 >= 5" }, //
        new String[] { "not of NE", "!(3 != 5)", "3 == 5" }, //
        new String[] { "not of OR 2", "!(f() || f(5))", "!f() && !f(5)" }, //
        new String[] { "not of OR", "!(a || b || c)", "!a && !b && !c" }, //
        new String[] { "not of wrapped OR", "!((a) || b || c)", "!a && !b && !c" }, //
        new String[] { "not of true", "!true", "false" }, //
        new String[] { "not of true", "!!true", "true" }, //
        new String[] { "Mutliple not", "!(!d || !!!c)", "d && c" }, //
        new String[] { "Mutliple not parenthesis", "!(!(d) || !!!c)", "d && c" }, //
        new String[] { "Mutliple not parenthesis", "!(!(d) || ((!(!(!(((c))))))))", "d && c" }, //
        new String[] { "Nested not", "!(!(a || b))", "a||b" }, //
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
     * Instantiates the enclosing class ({@link WringedExpression})
     */
    public Wringed() {
      super(WRING);
    }
    @Test public void inputIsPrefixExpression() {
      assertNotNull(asPrefixExpression());
    }
  }
}
