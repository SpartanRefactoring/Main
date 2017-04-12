package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** see Issue0 #450 for more details This is a unit test for
 * {@link FragmentInitializerStatementTerminatingScope, @link
 * DeclarationFragmentInlineIntoNext}
 * @author Sapir Bismot
 * @since 16-11-30 */
@SuppressWarnings("static-method")
public class Issue0450 {
  private static final String SEPARATOR_CASE = "final Separator s = new Separator(\", \");" //
      + "for (final String a : args)" //
      + "System.out.print(s + a);"//
  ;

  @Test public void test0() {
    trimminKof(SEPARATOR_CASE)//
        .stays();
  }

  @Test public void test0a() {
    trimminKof(SEPARATOR_CASE)//
        .using(new LocalVariableInitializedStatementTerminatingScope(), VariableDeclarationFragment.class)//
        .stays();
  }

  @Test public void test0b() {
    trimminKof(SEPARATOR_CASE)//
        .using(new LocalVariableIntializedInlineIntoNext(), VariableDeclarationFragment.class).stays();
  }

  @Test public void test1() {
    trimminKof("int x = 7;" //
        + "for (final String a : args)" //
        + "System.out.print(x+a.length());")
            .gives("for (final String a : args)" //
                + "System.out.print(7+a.length());");
  }

  @Test public void test2() {
    trimminKof("final Separator s = \"hello\";" //
        + "for (final String a : args)" //
        + "System.out.print(s + a);")
            .gives("for (final String a : args)" //
                + "System.out.print(\"hello\" + a);");
  }
}
