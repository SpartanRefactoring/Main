package il.org.spartan.refactoring.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.*;

/**
 * Unit tests for {@link Wrings#ADDITION_SORTER}.
 *
 * @author Yossi Gil
 * @since 2014-07-13
 */
@SuppressWarnings("javadoc")//
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
public enum BlockSimplifyTest {
  ;
  static final Wring<Block> WRING = new BlockSimplify();

  @RunWith(Parameterized.class)//
  public static class OutOfScope extends AbstractWringTest.OutOfScope<Block> {
    static String[][] cases = as.array(//
        new String[] { "Simply nested if return", "{if (a)  return b; else return c;}" }, //
        new String[] { "Simply nested if return", "{if (a)  return b; else return c;}" }, //
        new String[] { "Nested if return", "if (a) {;{{;;return b; }}} else {{{;return c;};;};}" }, //
        new String[] { "Simple block", "{a(); b(); c();}" }, //
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
  public static class Wringed extends AbstractWringTest.WringedBlock {
    private static String[][] cases = as.array(//
        // Literal
        new String[] { "Empty", "{;;}", "" }, //
        new String[] { "Complex empty", "{;;{;{{}}}{;}{};}", "" }, //
        new String[] { "Deeply nested return", " {{{;return c;};;};}", " return c;" }, //
        new String[] { "Singleton", "{if (a)  return b; else return c;}", " if(a)return b;else return c;" }, //
        new String[] { "Complex singleton", "{;{{;;return b; }}}", " return b;" }, //
        new String[] { "Three statements ", "{i++;{{;;return b; }}j++;}", " i++;return b;j++;" }, //
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
  }
}
