package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Funcs.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;

/**
 * Unit tests for {@link Wrings#ADDITION_SORTER}.
 *
 * @author Yossi Gil
 * @since 2014-07-13
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) public class IfShortestFirstTest {
  static final Wring<IfStatement> WRING = new IfShortestFirst();

  @Test public void statmentCount() {
    final CompilationUnit u = Wrap.Statement.intoCompilationUnit("" + //
        "if (name == null) {\n" + //
        "    if (other.name != null)\n" + //
        "        return false;\n" + //
        "} else if (!name.equals(other.name))\n" + //
        "    return false;\n" + //
        "return true;" //
        + ""//
    );
    final IfStatement s = extract.firstIfStatement(u);
    that(extract.statements(then(s)).size(), is(1));
    that(extract.statements(elze(s)).size(), is(1));
  }

  @RunWith(Parameterized.class) //
  public static class OutOfScope extends AbstractWringTest.OutOfScope<IfStatement> {
    static String[][] cases = as.array(//
        new String[] { "Return only on one side", "if (a) return b; else c;" }, //
        new String[] { "Simple if return", "if (a) return b; else return c;" }, //
        new String[] { "Simply nested if return", "{if (a)  return b; else return c;}" }, //
        new String[] { "Nested if return", "if (a) {;{{;;return b; }}} else {{{;return c;};;};}" }, //
        new String[] { "No else", "if (a) {;{{;;return b; }}}" }, //
        new String[] { "Two statemens are greater than one", //
            "if (a) {i++;j++;} else b(asdf,as,as,asdf,adfasd,adadfadf,asfasdfasdf);", //
        }, //
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
    private static String[][] cases = as.array(//
        new String[] { "Vanilla", "if (a) a(x,y,z,w); else b();", "if (!a) b(); else a(x,y,z,w);" }, //
        new String[] { //
            "Two statemens are greater than one", //
            "if (a) {i++;j++;} else b(asdf,as,as,asdf,adfasd,adadfadf,asfasdfasdf);", //
            "if (!a) b(asdf,as,as,asdf,adfasd,adadfadf,asfasdfasdf); else {i++;j++;} " }, //
        new String[] { //
            "If bug simplified", //
            "" + //
                "    if (x) {\n" + //
                "      if (z)\n" + //
                "        return null;\n" + //
                "      c = f().charAt(3);\n" + //
                "    } else if (y)\n" + //
                "      return;\n" + //
                "",
            "" + //
                "    if (!x) {\n" + //
                "      if (y)\n" + //
                "        return;\n" + //
                "    } else {\n" + //
                "      if (z)\n" + //
                "        return null;\n" + //
                "      c = f().charAt(3);\n" + //
                "    }\n" + //
                ""//
        }, null);

    /**
     * Generate test cases for this parameterized class.
     *
     * @return a collection of cases, where each case is an array of three
     *         objects, the test case name, the input, and the file.
     */
    @Parameters(name = "Test #{index}. ({0}) ") //
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
