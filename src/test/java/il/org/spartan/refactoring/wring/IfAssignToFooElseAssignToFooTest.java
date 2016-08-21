package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.is;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;
import static il.org.spartan.refactoring.utils.Funcs.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.AbstractWringTest.*;
import il.org.spartan.utils.Utils;

/** Unit tests for {@link Wrings#ADDITION_SORTER}.
 * @author Yossi Gil
 * @since 2014-07-13 */
@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class IfAssignToFooElseAssignToFooTest {
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
  public static class Wringed extends AbstractWringTest.WringedIfStatement {
    private static String[][] cases = Utils.asArray(//
        new String[] { "Simple if assign", "if (a) a = b; else a = c;", "a = a ? b : c;" }, //
        new String[] { "Simple if plus assign", "if (a) a += b; else a += c;", "a += a ? b : c;" }, //
        new String[] { "Simple if plus assign", "if (a) a *= b; else a *= c;", "a *= a ? b : c;" }, //
        null);

    /** Generate test cases for this parameterized class.
     * @return a collection of cases, where each case is an array of three
     *         objects, the test case name, the input, and the file. */
    @Parameters(name = DESCRIPTION) //
    public static Collection<Object[]> cases() {
      return collect(cases);
    }

    /** Instantiates the enclosing class ({@link Wringed}) */
    public Wringed() {
      super(WRING);
    }
  }

  static final Wring<IfStatement> WRING = new IfAssignToFooElseAssignToFoo();

  @Test public void checkCompatability() {
    final Assignment a1 = extract.assignment(Into.s("x=a1;"));
    final Assignment a2 = extract.assignment(Into.s("x=a2;"));
    azzert.notNull(a1);
    azzert.notNull(a2);
    azzert.that(a1.toString(), is("x=a1"));
    azzert.that(a2.toString(), is("x=a2"));
    azzert.aye(compatibleOps(a1.getOperator(), a2.getOperator()));
    azzert.aye(Is.isSimpleName(left(a1)));
    azzert.aye(Is.isSimpleName(left(a2)));
    final SimpleName n1 = asSimpleName(left(a1));
    final SimpleName n2 = asSimpleName(left(a2));
    azzert.that(n1.toString(), is("x"));
    azzert.that(n2.toString(), is("x"));
    azzert.aye(same(left(a1), left(a2)));
    azzert.nay(incompatible(a1, a2));
    azzert.aye(compatible(a1, a2));
  }

  @Test public void checkSteps() {
    azzert.notNull(asSingle("if (a) a = b; else a = c;"));
    final IfStatement s = asIfStatement(asSingle("if (a) a = b; else a = c;"));
    azzert.notNull(s);
    final Assignment then = extract.assignment(then(s));
    azzert.notNull(then(s).toString(), then);
    final Assignment elze = extract.assignment(elze(s));
    azzert.notNull(elze);
    azzert.that(compatible(then, elze), is(true));
    azzert.that(WRING.scopeIncludes(s), is(true));
  }
}
