package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.engine.*;

@SuppressWarnings({ "static-method", "javadoc" }) public class MethodExplorerTest {
  @Test public void localVariablesCatchExpression() {
    azzert.that(new MethodExplorer(into.d("" + "  void f() {\n" + "    try {\n" + "      f();\n"
        + "    } catch (final Exception|RuntimeException e) {\n" + "      f();\n" + "    }\n" + "  }")).localVariables().size(), is(1));
  }

  @Test public void localVariablesExtendedForLoop() {
    azzert.that(new MethodExplorer(into.d(
        "" + "  int sum(final int is[]) {\n" + "    int $ = 0;\n" + "    for (final int i : is)\n" + "      $ += i;\n" + "    return $;\n" + "  } "))
            .localVariables().size(),
        is(2));
  }

  @Test public void localVariablesForLoopNoVariable() {
    azzert.that(new MethodExplorer(into.d("  int f() {\n" + "  for (f(); i*j <10; i += j++);  }")).localVariables().size(), is(0));
  }

  @Test public void localVariablesForLoopOneVariable() {
    azzert.that(new MethodExplorer(into.d("  int f() {\n" + "  for (int i = 0; i*j <10; i += j++);  }")).localVariables().size(), is(1));
  }

  @Test public void localVariablesForLoopTwoVariables() {
    azzert.that(new MethodExplorer(into.d("  int f() {\n" + "  for (int i = 0, j = 2; i*j <10; i += j++);  }")).localVariables().size(), is(2));
  }

  @Test public void localVariablesMultipleFragments() {
    azzert.that(new MethodExplorer(into.d("  int f() {\n" + "int a,b;\n" + "  }")).localVariables().size(), is(2));
  }

  @Test public void localVariablesMultipleNestedFragments() {
    azzert.that(new MethodExplorer(into.d("  int f() {\n" + "int a,b;\n" + "  {int c, d;}}")).localVariables().size(), is(4));
  }

  @Test public void localVariablesNone() {
    azzert.that(new MethodExplorer(into.d("  int f() {\n" + "    return new Object() {\n" + "      @Override public boolean equals(Object obj) {\n"
        + "        return super.equals(obj);\n" + "      }\n" + "      @Override public int hashCode() {\n" + "        return super.hashCode();\n"
        + "      }\n" + "    }.hashCode();\n" + "  }")).localVariables().size(), is(0));
  }

  @Test public void localVariablesRepeatedNestedFragments() {
    azzert.that(new MethodExplorer(into.d("  int f() {\n" + "int a,b,c,d;\n" + "  {int i, j;} {int i,j; int k;}")).localVariables().size(), is(9));
  }

  @Test public void localVariablesTryClause() {
    azzert
        .that(
            new MethodExplorer(into.d("" + "  void f() {\n" + "    final File f = new File(\"f\");\n"
                + "    try (final InputStream s = new FileInputStream(f); final InputStreamReader is = new InputStreamReader(s)) {\n" + "      f();\n"
                + "    } catch (final FileNotFoundException e) {\n" + "      e.printStackTrace();\n" + "    } catch (final IOException e) {\n"
                + "      e.printStackTrace();\n" + "    } finally {\n" + "      f();\n" + "    }\n" + "  }\n")).localVariables().size(),
            is(5));
  }

  @Test public void localVariablesVanilla() {
    azzert.that(new MethodExplorer(into.d("  int f() {\n" + "int a;\n" + "  }")).localVariables().size(), is(1));
  }

  @Test public void returnStatementsExists() {
    final MethodDeclaration d = into.d("int f() { return a; }");
    final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
   assert null !=(a);
    azzert.that(a.size(), is(1));
  }

  @Test public void returnStatementsExistsNestedType() {
    final MethodDeclaration d = into.d("int f() { class B {}; return a; }");
    final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
   assert null !=(a);
    azzert.that(a.size(), is(1));
  }

  @Test public void returnStatementsExistsNestedTypeAnnotation() {
    final MethodDeclaration d = into.d("  boolean f() {\n" + //
        "    @interface C{static class X{boolean f(){return f();}}}" + //
        "    if (f())\n" + //
        "      return f();\n" + //
        "    return new B().g();\n" + //
        "  }"); //
    final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
   assert null !=(a);
    azzert.that(a.size(), is(2));
  }

  @Test public void returnStatementsExistsNestedTypeWithReturn() {
    final MethodDeclaration d = into.d("int f() { class B {int g() { return c; } }; return a; }");
    final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
   assert null !=(a);
    azzert.that(a.size(), is(1));
  }

  @Test public void returnStatementsExistsNestedTypeWithReturn1() {
    final MethodDeclaration d = into.d("  boolean f() {\n" + //
        "    if (f())\n" + //
        "      return f();\n" + //
        "    class B {\n" + //
        "      boolean g() {\n" + //
        "        return g();\n" + //
        "      }\n" + //
        "    }\n" + //
        "    return new B().g();\n" + //
        "  }"); //
    final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
   assert null !=(a);
    azzert.that(a.size(), is(2));
  }

  @Test public void returnStatementsTwoReturns() {
    final MethodDeclaration d = into.d("int f() { if (b) ; else return c; return a; }");
    final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
   assert null !=(a);
    azzert.that(a.size(), is(2));
  }

  @Test public void returnStatementsWithNestedEnum() {
    azzert.that(new MethodExplorer(into.d("  int f() {\n" + "    return new Object() {\n" + "      @Override public boolean equals(Object obj) {\n"
        + "        return super.equals(obj);\n" + "      }\n" + "      @Override public int hashCode() {\n" + "        return super.hashCode();\n"
        + "      }\n" + "    }.hashCode();\n" + "  }")).returnStatements().size(), is(1));
  }
}
