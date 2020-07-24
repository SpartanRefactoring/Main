package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.junit.Test;

import il.org.spartan.spartanizer.tippers.LocalInitializedUnusedRemove;
import il.org.spartan.spartanizer.tippers.TryMergeCatchers;

/** Unit tests for the GitHub issue thus numbered
 * @author Ori Marcovitch
 * @since Dec 6, 2016 */
@SuppressWarnings("static-method")
public class Issue0895 {
  @Test public void a() {
    trimmingOf("    new C() {" + //
        "     public void d() {" + //
        "       try {" + //
        "         use();" + //
        "       } catch (E e) {" + //
        "         F.g(b);" + //
        "       } catch (H e) {" + //
        "         F.g(b);" + //
        "       }" + //
        "     }" + //
        "     }" //
    )//
        .gives("   new C() {" + //
            "     public void d() {" + //
            "       try {" + //
            "         use();" + //
            "       } catch (H | E e) {" + //
            "         F.g(b);" + //
            "       }" + //
            "     }" + //
            "     }")//
        .stays();
  }
  /** Introduced by Yogi on Tue-Mar-28-03:29:43-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_publicFinalClassAPublicStaticVoidaFinalBbCcNewCDPublicVoiddTryeCatchEfFgbCatchGfFgb() {
    trimmingOf(
        "public final class A{public static void a(final B b){C c=new C(){@D public void d(){try{e();}catch(E f){F.g(b);}catch(G f){F.g(b);}}};}}") //
            .using(new LocalInitializedUnusedRemove(), VariableDeclarationFragment.class) //
            .gives(
                "public final class A{public static void a(final B b){new C(){@D public void d(){try{e();}catch(E f){F.g(b);}catch(G f){F.g(b);}}};}}") //
            .using(new TryMergeCatchers(), TryStatement.class)
            .gives("public final class A{public static void a(final B b){new C(){@D public void d(){try{e();}catch(G|E f){F.g(b);}}};}}") //
            .stays() //
    ;
  }
}
