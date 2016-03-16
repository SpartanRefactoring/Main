package il.org.spartan.refactoring.wring;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import il.org.spartan.refactoring.wring.AbstractWringTest.OutOfScope;
import il.org.spartan.refactoring.wring.AbstractWringTest.Wringed;

import il.org.spartan.refactoring.wring.DeclarationAssignment;
import il.org.spartan.refactoring.wring.Wring;
import il.org.spartan.refactoring.wring.Wrings;
import il.org.spartan.utils.Utils;

/**
 * Unit tests for {@link Wrings#ADDITION_SORTER}.
 *
 * @author Yossi Gil
 * @since 2014-07-13
 */
@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class DeclarationAssignmentTest {
  static final Wring<VariableDeclarationFragment> WRING = new DeclarationAssignment();
  @Test public void placeHolder() {
    // Place holder for future tests
    assertNotNull(WRING);
  }

  @RunWith(Parameterized.class) //
  public static class OutOfScope extends AbstractWringTest.OutOfScope<VariableDeclarationFragment> {
    static String[][] cases = Utils.asArray(//
        new String[] { "Wrong assignnet", "int a = 0; if (x)  a+= 5" }, //
        new String[] { "Wrong assignnet", "int a = 0; if (a)  a= 5;" }, //
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
  public static class Wringed extends AbstractWringTest.Wringed.WringedVariableDeclarationFragmentAndSurrounding {
    private static String[][] cases = Utils.asArray(//
        new String[] { "Vanilla", "int a; a =3;", "int a=3;" }, //
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
