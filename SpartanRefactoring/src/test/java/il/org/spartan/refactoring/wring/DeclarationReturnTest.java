package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;
import il.org.spartan.hamcrest.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.AbstractWringTest.OutOfScope;
import il.org.spartan.refactoring.wring.AbstractWringTest.Wringed;
import il.org.spartan.utils.Utils;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameters;

/**
 * Unit tests for {@link Wrings#ADDITION_SORTER}.
 *
 * @author Yossi Gil
 * @since 2014-07-13
 */
@SuppressWarnings({ "javadoc", "static-method" })//
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
public class DeclarationReturnTest {
  static final Wring<VariableDeclarationFragment> WRING = new DeclarationInitializerReturnVariable();

  @Test public void placeHolder() {
    JunitHamcrestWrappper.assertNotNull(WRING);
  }

  @RunWith(Parameterized.class)//
  public static class OutOfScope extends AbstractWringTest.OutOfScope<VariableDeclarationFragment> {
    static String[][] cases = Utils.asArray(//
        new String[] { "Simple if return TWO STATEMENTS", "if (a) return b; else a(); f();" }, //
        new String[] { "Vanilla", "int a; a =3;", }, //
        new String[] { "Vanilla", "int a; if (x) b = 3; else ;", }, //
        new String[] { "Vanilla", "int a = 2; if (b) a =3;", }, //
        new String[] { "Vanilla", "int a = 3, b; return;", }, //
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
  public static class Wringed extends AbstractWringTest.Wringed.WringedVariableDeclarationFragmentAndSurrounding {
    private static String[][] cases = Utils
        .asArray(
            //
            new String[] { "Vanilla", "int a = 3; return a;", "return 3;" }, //
            new String[] { "Vanilla", "int a = 3, b; return a;", "return 3;" }, //
            new String[] { "Actual", "ColorChip $ " + "= messageRead ? "//
                + "   !messageFlagged ? mReadColorChip : mFlaggedReadColorChip: " //
                + "    !messageFlagged ? mUnreadColorChip : mFlaggedUnreadColorChip; " //
                + "   return $;", //
                "return messageRead?!messageFlagged?mReadColorChip:mFlaggedReadColorChip:!messageFlagged?mUnreadColorChip:mFlaggedUnreadColorChip;" },
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
    @Test public void traceLegiblity() {
      final VariableDeclarationFragment f = asMe();
      final ASTRewrite r = ASTRewrite.create(f.getAST());
      final Expression initializer = f.getInitializer();
      JunitHamcrestWrappper.assertNotNull(initializer);
      JunitHamcrestWrappper.assertNotNull(Extract.nextStatement(f));
      final ReturnStatement s = Extract.nextReturn(f);
      JunitHamcrestWrappper.assertNotNull(s);
      JunitHamcrestWrappper.assertTrue(same(f.getName(), Extract.expression(s)));
      r.remove(Extract.statement(f), null);
      r.replace(s, Subject.operand(initializer).toReturn(), null);
    }
  }
}
