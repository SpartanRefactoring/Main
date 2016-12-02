package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** see Issue #450 for more details This is a unit test for
 * {@link DeclarationInitializerStatementTerminatingScope, @link
 * DeclarationFragmentInlineIntoNext}
 * @author Sapir Bismot
 * @since 16-11-30 */
@SuppressWarnings("static-method")
public class Issue450 {
  @Test public void test0() {
    trimmingOf("final Separator s = new Separator(\", \");" //
        + "for (final String a : args)" //
        + "System.out.print(s + a);").stays();
  }

  @Test public void test1() {
    trimmingOf("int x = 7;" //
        + "for (final String a : args)" //
        + "System.out.print(x+a.length());")
            .gives("for (final String a : args)" //
                + "System.out.print(7+a.length());");
  }

  @Test public void test2() {
    trimmingOf("final Separator s = \"hello\";" //
        + "for (final String a : args)" //
        + "System.out.print(s + a);")
            .gives("for (final String a : args)" //
                + "System.out.print(\"hello\" + a);");
  }
}
