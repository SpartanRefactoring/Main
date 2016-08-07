package il.org.spartan.refactoring.wring;

import java.util.*;
import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;
import il.org.spartan.refactoring.wring.AbstractWringTest.*;
import il.org.spartan.utils.*;

/** Unit tests for {@link Wrings#ADDITION_SORTER}.
 * @author Yossi Gil
 * @since 2014-07-13 */
@SuppressWarnings("javadoc") //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public enum IfReturnFooElseReturnBarTest {
  ;
  static final Wring<IfStatement> WRING = new IfReturnFooElseReturnBar();

  @RunWith(Parameterized.class) //
  public static class OutOfScope extends AbstractWringTest.OutOfScope<IfStatement> {
    static String[][] cases = Utils.asArray(//
        new String[] { "Return only on one side", "if (a) return b; else c;" }, //
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
        // Literal
        new String[] { "Simple if return", "if (a) return b; else return c;", "return a ? b : c;" }, //
        new String[] { "Simply nested if return", "{if (a)  return b; else return c;}", " if(a)return b;else return c;" }, //
        new String[] { "Nested if return", "if (a) {;{{;;return b; }}} else {{{;return c;};;};}", "return a ? b : c;" }, //
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
}
