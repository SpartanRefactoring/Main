/* TODO: Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 *
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 *
 * @since Sep 7, 2016 */
package il.org.spartan.spartanizer.utils;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

@SuppressWarnings({ "static-method", "javadoc" })
public final class MethodExplorerTest {
  @Test public void localVariablesCatchExpression() {
    azzert.that(new MethodExplorer(
        into.d("  void f() {\n    try {\n      f();\n    } catch (final Exception|RuntimeException e) {\n      f();\n    }\n  }"))
            .localVariables().size(),
        is(1));
  }

  @Test public void localVariablesExtendedForLoop() {
    azzert.that(
        new MethodExplorer(into.d("  int sum(final int is[]) {\n    int $ = 0;\n    for (final int i : is)\n      $ += i;\n    return $;\n  } "))
            .localVariables().size(),
        is(2));
  }

  @Test public void localVariablesForLoopNoVariable() {
    azzert.that(new MethodExplorer(into.d("  int f() {\n  for (f(); i*j <10; i += j++);  }")).localVariables().size(), is(0));
  }

  @Test public void localVariablesForLoopOneVariable() {
    azzert.that(new MethodExplorer(into.d("  int f() {\n  for (int i = 0; i*j <10; i += j++);  }")).localVariables().size(), is(1));
  }

  @Test public void localVariablesForLoopTwoVariables() {
    azzert.that(new MethodExplorer(into.d("  int f() {\n  for (int i = 0, j = 2; i*j <10; i += j++);  }")).localVariables().size(), is(2));
  }

  @Test public void localVariablesMultipleFragments() {
    azzert.that(new MethodExplorer(into.d("  int f() {\nint a,b;\n  }")).localVariables().size(), is(2));
  }

  @Test public void localVariablesMultipleNestedFragments() {
    azzert.that(new MethodExplorer(into.d("  int f() {\nint a,b;\n  {int c, d;}}")).localVariables().size(), is(4));
  }

  @Test public void localVariablesNone() {
    azzert.that(new MethodExplorer(into.d("  int f() {\n    return new Object() {\n      @Override public boolean equals(Object obj) {\n"
        + "        return super.equals(obj);\n      }\n      @Override public int hashCode() {\n        return super.hashCode();\n"
        + "      }\n    }.hashCode();\n  }")).localVariables().size(), is(0));
  }

  @Test public void localVariablesRepeatedNestedFragments() {
    azzert.that(new MethodExplorer(into.d("  int f() {\nint a,b,c,d;\n  {int i, j;} {int i,j; int k;}")).localVariables().size(), is(9));
  }

  @Test public void localVariablesTryClause() {
    azzert.that(new MethodExplorer(into.d("  void f() {\n    final File f = new File(\"f\");\n"
        + "    try (final InputStream s = new FileInputStream(f); final InputStreamReader is = new InputStreamReader(s)) {\n      f();\n"
        + "    } catch (final FileNotFoundException e) {\n      e.printStackTrace();\n    } catch (final IOException e) {\n"
        + "      e.printStackTrace();\n    } finally {\n      f();\n    }\n  }\n")).localVariables().size(), is(5));
  }

  @Test public void localVariablesVanilla() {
    azzert.that(new MethodExplorer(into.d("  int f() {\nint a;\n  }")).localVariables().size(), is(1));
  }

  @Test public void returnStatementsExists() {
    @NotNull final MethodDeclaration d = into.d("int f() { return a; }");
    @NotNull final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
    assert a != null;
    azzert.that(a.size(), is(1));
  }

  @Test public void returnStatementsExistsNestedType() {
    @NotNull final MethodDeclaration d = into.d("int f() { class B {}; return a; }");
    @NotNull final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
    assert a != null;
    azzert.that(a.size(), is(1));
  }

  @Test public void returnStatementsExistsNestedTypeAnnotation() {
    @NotNull final MethodDeclaration d = into.d("  boolean f() {\n" + //
        "    @interface C{static class X{boolean f(){return f();}}}" + //
        "    if (f())\n" + //
        "      return f();\n" + //
        "    return new B().g();\n" + //
        "  }"); //
    @NotNull final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
    assert a != null;
    azzert.that(a.size(), is(2));
  }

  @Test public void returnStatementsExistsNestedTypeWithReturn() {
    @NotNull final MethodDeclaration d = into.d("int f() { class B {int g() { return c; } }; return a; }");
    @NotNull final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
    assert a != null;
    azzert.that(a.size(), is(1));
  }

  @Test public void returnStatementsExistsNestedTypeWithReturn1() {
    @NotNull final MethodDeclaration d = into.d("  boolean f() {\n" + //
        "    if (f())\n" + //
        "      return f();\n" + //
        "    class B {\n" + //
        "      boolean g() {\n" + //
        "        return g();\n" + //
        "      }\n" + //
        "    }\n" + //
        "    return new B().g();\n" + //
        "  }"); //
    @NotNull final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
    assert a != null;
    azzert.that(a.size(), is(2));
  }

  @Test public void returnStatementsTwoReturns() {
    @NotNull final MethodDeclaration d = into.d("int f() { if (b) ; else return c; return a; }");
    @NotNull final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
    assert a != null;
    azzert.that(a.size(), is(2));
  }

  @Test public void returnStatementsWithNestedEnum() {
    azzert.that(new MethodExplorer(into.d("  int f() {\n    return new Object() {\n      @Override public boolean equals(Object obj) {\n"
        + "        return super.equals(obj);\n      }\n      @Override public int hashCode() {\n        return super.hashCode();\n"
        + "      }\n    }.hashCode();\n  }")).returnStatements().size(), is(1));
  }
}
