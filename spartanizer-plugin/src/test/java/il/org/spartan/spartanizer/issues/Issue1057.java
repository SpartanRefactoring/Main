package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

import il.org.spartan.spartanizer.tippers.LocalInitializedStatementToForInitializers;

/** Test class for issue #1057, see
 * {@link LocalInitializedStatementToForInitializers}.
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @since 2017-01-16 */
@SuppressWarnings("static-method")
public class Issue1057 {
  @Test public void a() {
    trimmingOf("/**/" //
        + "void f() {\n" //
        + "  int a = 1;" //
        + "  x();" //
        + "  int y = x();" //
        + "  for (a = 2; y == 1;)" //
        + "    break;" //
        + "}").stays();
  }
  @Test public void b() {
    trimmingOf("/**/" //
        + "private static int nsBranchBreakOrRetInd(final SwitchStatement s, int i) {\n" //
        + "  final List<Statement> l = statements(az.switchStatement(s));\n" //
        + "  int $;\n" //
        + "  x();\n" //
        + "  int cur = i;\n" //
        + "  for ($ = 1; $ < l.size(); ++$)\n" //
        + "    if (iz.switchCase(l.get($)) && !iz.switchCase(l.get($ - 1)) && --cur == 0)\n" //
        + "      break;\n" //
        + "  if ($ == l.size())\n" //
        + "    return -1;\n" //
        + "  Statement k = l.get(--$);\n" //
        + "  return iz.breakStatement(k) || iz.returnStatement(k) ? $ : -1;\n" //
        + "}");
  }
}
