package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link DeclarationInitializerStatementTerminatingScope}
 * @author Ori Roth
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue155 {
  @Ignore @Test public void inlineFinal() {
    trimmingOf("for (int i = 0; i < versionNumbers.length; ++i) {\n" + //
        "  final String nb = versionNumbers[i];\n" + //
        "  $[i] = Integer.parseInt(nb);\n" + //
        "}").gives("for (int i = 0; i < versionNumbers.length; ++i) {\n" + //
            "  $[i] = Integer.parseInt(versionNumbers[i]);\n" + //
            "}");
  }

  @Test public void inlineNonFinalIntoClassInstanceCreation() {
    trimmingOf("void h(int x) {\n" + //
        "  ++x;\n" + //
        "  final int y = x;\n" + //
        "  new Object() {\n" + //
        "    @Override\n" + //
        "    public int hashCode() {\n" + "      return y;\n" + //
        "    }\n" + //
        "  };\n" + //
        "}").stays();
  }

  @Test public void issue64a() {
    trimmingOf("void f() {" + //
        "    final int a = f();\n" + //
        "    new Object() {\n" + //
        "      @Override public int hashCode() { return a; }\n" + //
        "    };" + "}").stays();
  }

  @Test public void issue64b1() {
    trimmingOf("void f() {" + //
        "    new Object() {\n" + //
        "      @Override public int hashCode() { return 3; }\n" + //
        "    };" + //
        "}")//
            .stays();
  }

  @Test public void issue64b2() {
    trimmingOf("void f() {" + //
        "    final int a = 3;\n" + //
        "    new Object() {\n" + //
        "      @Override public int hashCode() { return a; }\n" + //
        "    };" + "}").stays();
  }

  @Test public void issue64c() {
    trimmingOf("void f(int x) {" + //
        "    ++x;\n" + //
        "    final int a = x;\n" + //
        "    new Object() {\n" + //
        "      @Override public int hashCode() { return a; }\n" + //
        "    };" + //
        "}").stays();
  }
}
