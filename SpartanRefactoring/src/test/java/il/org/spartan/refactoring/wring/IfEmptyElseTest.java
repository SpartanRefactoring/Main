package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import il.org.spartan.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.AbstractWringTest.OutOfScope;
import il.org.spartan.refactoring.wring.AbstractWringTest.Wringed;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameters;

/**
 * Unit tests for {@link Wrings#IFX_SOMETHING_EXISTING_EMPTY_ELSE}.
 *
 * @author Yossi Gil
 * @since 2014-07-13
 */
@SuppressWarnings("javadoc")//
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
public class IfEmptyElseTest {
  static final Wring<IfStatement> WRING = new IfDegenerateElse();

  @RunWith(Parameterized.class)//
  public static class OutOfScope extends AbstractWringTest.OutOfScope<IfStatement> {
    static String[][] cases = as.array(//
        new String[] { "Return only on one side", "if (a) return b; else c;" }, //
        new String[] { "Simple if return", "if (a) return b; else return c;" }, //
        new String[] { "Simply nested if return", "{if (a)  return b; else return c;}" }, //
        new String[] { "Nested if return", "if (a) {;{{;;return b; }}} else {{{;return c;};;};}" }, //
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
  public static class Wringed extends AbstractWringTest.WringedIfStatement {
    private static String[][] cases = as.array(//
        new String[] { "Vanilla {}", "if (a) f(); else {}", "if (a) f();" }, //
        new String[] { "Vanilla ; ", "if (a) f(); else ;", "if (a) f();" }, //
        new String[] { "Vanilla {;{;;};} ", "if (a) f(); else {;{;{};};{;{}}}", "if (a) f();" }, //
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
    /**
     * Instantiates the enclosing class ({@link Wringed})
     */
    public Wringed() {
      super(WRING);
    }
    @Test public void isfStatementElseIsEmpty() {
      @Nullable final IfStatement asMe = asMe();
      assert asMe != null;
      that(extract.statements(asMe.getElseStatement()).size(), is(0));
    }
    @Test public void isfStatementElseNotNull() {
      that(elze(asMe()), notNullValue());
    }
    @Test public void isIfStatement() {
      that(asMe(), notNullValue());
    }
    @Test public void myScopeIncludes() {
      final IfStatement s = asMe();
      that(s, notNullValue());
      that(elze(s), notNullValue());
      that(extract.statements(elze(s)), notNullValue());
      that(extract.statements(elze(s)).size(), is(0));
    }
  }
}
