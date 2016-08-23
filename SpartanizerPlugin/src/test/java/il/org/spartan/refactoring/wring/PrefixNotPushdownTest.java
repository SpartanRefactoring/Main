package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.AbstractWringTest.*;

/** Unit tests for {@link Wrings#ADDITION_SORTER}.
 * @author Yossi Gil
 * @since 2014-07-13 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "static-method" }) //
public class PrefixNotPushdownTest {
  @RunWith(Parameterized.class) //
  public static class OutOfScope extends AbstractWringTest.OutOfScope.Exprezzion<PrefixExpression> {
    static String[][] cases = as.array(//
        new String[] { "Simple not", "!a" }, //
        new String[] { "Simple not of function", "!f(a)" }, //
        new String[] { "Actual example", "!inRange(m, e)" }, //
        null);

    /** Generate test cases for this parameterized class.
     * @return a collection of cases, where each case is an array of three
     *         objects, the test case name, the input, and the file. */
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
    private static String[][] cases = as.array(//
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

    /** Generate test cases for this parameterized class.
     * @return a collection of cases, where each case is an array of three
     *         objects, the test case name, the input, and the file. */
    @Parameters(name = DESCRIPTION) //
    public static Collection<Object[]> cases() {
      return collect(cases);
    }

    /** Instantiates the enclosing class ({@link WringedExpression}) */
    public Wringed() {
      super(WRING);
    }

    @Test public void inputIsPrefixExpression() {
      azzert.notNull(asPrefixExpression());
    }
  }

  /** The {@link Wring} under test */
  static final PrefixNotPushdown WRING = new PrefixNotPushdown();

  @Test public void notOfFalse() {
    final PrefixExpression e = p("!false");
    azzert.notNull(e);
    azzert.that(WRING.scopeIncludes(e), is(true));
    azzert.that(WRING.eligible(e), is(true));
    azzert.notNull(asNot(e));
    final Expression inner = operand(e);
    azzert.notNull(inner);
    azzert.that(inner.toString(), is("false"));
    azzert.that(Is.booleanLiteral(inner), is(true));
    azzert.notNull(PrefixNotPushdown.perhapsNotOfLiteral(inner));
    azzert.notNull(PrefixNotPushdown.notOfLiteral(asBooleanLiteral(inner)));
    azzert.notNull(PrefixNotPushdown.perhapsNotOfLiteral(inner));
    azzert.notNull(PrefixNotPushdown.pushdownNot(inner));
    azzert.notNull(PrefixNotPushdown.pushdownNot(asNot(e)));
    azzert.notNull(WRING.replacement(e));
  }
}
