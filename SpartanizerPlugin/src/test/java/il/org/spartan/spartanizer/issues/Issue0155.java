package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link LocalInitializedStatementTerminatingScope}
 * @author Ori Roth
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0155 {
  @Test public void inlineFinal() {
    trimmingOf("for (int i = 0; i <versionNumbers.length; ++i) {\n  final String nb = versionNumbers[i];\n  $[i] = Integer.parseInt(nb);\n}")
        .gives("for (int i = 0; i <versionNumbers.length; ++i) {\n  $[i] = Integer.parseInt(versionNumbers[i]);\n}");
  }
  @Test public void inlineNonFinalIntoClassInstanceCreation() {
    trimmingOf("void h(int x) {\n  ++x;\n  final int y = x;\n  new Object() {\n    @Override\n    public int hashCode() {\n"
        + "      return y;\n    }\n  };\n}")//
            .stays();
  }
  @Test public void issue64a() {
    trimmingOf("void f() {    final int a = f();\n    new Object() {\n      @Override public int hashCode() { return a; }\n    };}").stays();
  }
  @Test public void issue64b1() {
    trimmingOf("void f() {    new Object() {\n      @Override public int hashCode() { return 3; }\n    };}")//
        .stays();
  }
  @Test public void issue64b2() {
    trimmingOf("void f() {    final int a = 3;\n    new Object() {\n      @Override public int hashCode() { return a; }\n    };}").stays();
  }
  @Test public void issue64c() {
    trimmingOf("void f(int x) {    ++x;\n    final int a = x;\n    new Object() {\n      @Override public int hashCode() { return a; }\n    };}")//
        .stays();
  }
}
