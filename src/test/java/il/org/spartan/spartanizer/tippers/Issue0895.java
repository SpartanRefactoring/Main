package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since Dec 6, 2016 */
@SuppressWarnings("static-method")
public class Issue0895 {
  @Test public void a() {
    trimmingOf("public final class A {" + //
        " public static void a(final B b) {" + //
        "   C c = new C() {" + //
        "     @D" + //
        "     public void d() {" + //
        "       try {" + //
        "         use();" + //
        "       } catch (E e) {" + //
        "         F.g(b);" + //
        "       } catch (H e) {" + //
        "         F.g(b);" + //
        "       }" + //
        "     }" + //
        "   };" + //
        " }" + //
        "}"//
    )//
        .gives("public final class A {" + //
            " public static void a(final B b) {" + //
            "   C c = new C() {" + //
            "     @D" + //
            "     public void d() {" + //
            "       try {" + //
            "         use();" + //
            "       } catch (H | E e) {" + //
            "         F.g(b);" + //
            "       }" + //
            "     }" + //
            "   };" + //
            " }" + //
            "}"//
        )//
        .stays();
  }
}
