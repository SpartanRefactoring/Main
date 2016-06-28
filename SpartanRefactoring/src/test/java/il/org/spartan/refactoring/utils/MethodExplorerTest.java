package il.org.spartan.refactoring.utils;

import static il.org.spartan.hamcrest.CoreMatchers.*;
import static il.org.spartan.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

@SuppressWarnings({ "static-method", "javadoc" }) public class MethodExplorerTest {
  @Test public void localVariablesCatchExpression() {
    assertThat(new MethodExplorer(Into.d("" + "  void f() {\n" + "    try {\n" + "      f();\n"
        + "    } catch (final Exception|RuntimeException e) {\n" + "      f();\n" + "    }\n" + "  }")).localVariables().size(),
    is(1));
  }
  @Test public void localVariablesExtendedForLoop() {
    assertThat(new MethodExplorer(Into.d("" + "  int sum(final int is[]) {\n" + "    int $ = 0;\n" + "    for (final int i : is)\n"
        + "      $ += i;\n" + "    return $;\n" + "  } ")).localVariables().size(), is(2));
  }
  @Test public void localVariablesForLoopNoVariable() {
    assertThat(new MethodExplorer(Into.d("  int f() {\n" + "  for (f(); i*j <10; i += j++);  }")).localVariables().size(), is(0));
  }
  @Test public void localVariablesForLoopOneVariable() {
    assertThat(new MethodExplorer(Into.d("  int f() {\n" + "  for (int i = 0; i*j <10; i += j++);  }")).localVariables().size(),
        is(1));
  }
  @Test public void localVariablesForLoopTwoVariables() {
    assertThat(new MethodExplorer(Into.d("  int f() {\n" + "  for (int i = 0, j = 2; i*j <10; i += j++);  }")).localVariables()
        .size(), is(2));
  }
  @Test public void localVariablesMultipleFragments() {
    assertThat(new MethodExplorer(Into.d("  int f() {\n" + "int a,b;\n" + "  }")).localVariables().size(), is(2));
  }
  @Test public void localVariablesMultipleNestedFragments() {
    assertThat(new MethodExplorer(Into.d("  int f() {\n" + "int a,b;\n" + "  {int c, d;}}")).localVariables().size(), is(4));
  }
  @Test public void localVariablesNone() {
    assertThat(new MethodExplorer(Into.d("  int f() {\n" + "    return new Object() {\n"
        + "      @Override public boolean equals(Object obj) {\n" + "        return super.equals(obj);\n" + "      }\n"
        + "      @Override public int hashCode() {\n" + "        return super.hashCode();\n" + "      }\n" + "    }.hashCode();\n"
        + "  }")).localVariables().size(), is(0));
  }
  @Test public void localVariablesRepeatedNestedFragments() {
    assertThat(new MethodExplorer(Into.d("  int f() {\n" + "int a,b,c,d;\n" + "  {int i, j;} {int i,j; int k;}")).localVariables()
        .size(), is(9));
  }
  @Test public void localVariablesTryClause() {
    assertThat(new MethodExplorer(Into.d("" + "  void f() {\n" + "    final File f = new File(\"f\");\n"
        + "    try (final InputStream s = new FileInputStream(f); final InputStreamReader is = new InputStreamReader(s)) {\n"
        + "      f();\n" + "    } catch (final FileNotFoundException e) {\n" + "      e.printStackTrace();\n"
        + "    } catch (final IOException e) {\n" + "      e.printStackTrace();\n" + "    } finally {\n" + "      f();\n"
        + "    }\n" + "  }\n")).localVariables().size(), is(5));
  }
  @Test public void localVariablesVanilla() {
    assertThat(new MethodExplorer(Into.d("  int f() {\n" + "int a;\n" + "  }")).localVariables().size(), is(1));
  }
  @Test public void returnStatementsExists() {
    final MethodDeclaration d = Into.d("int f() { return a; }");
    final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
    assertThat(a, notNullValue());
    assertThat(a.size(), is(1));
  }
  @Test public void returnStatementsExistsNestedType() {
    final MethodDeclaration d = Into.d("int f() { class B {}; return a; }");
    final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
    assertThat(a, notNullValue());
    assertThat(a.size(), is(1));
  }
  @Test public void returnStatementsExistsNestedTypeAnnotation() {
    final MethodDeclaration d = Into.d("  boolean f() {\n" + //
        "    @interface C{static class X{boolean f(){return f();}}}" + //
        "    if (f())\n" + //
        "      return f();\n" + //
        "    return new B().g();\n" + //
        "  }"); //
    final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
    assertThat(a, notNullValue());
    assertThat(a.size(), is(2));
  }
  @Test public void returnStatementsExistsNestedTypeWithReturn() {
    final MethodDeclaration d = Into.d("int f() { class B {int g() { return c; } }; return a; }");
    final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
    assertThat(a, notNullValue());
    assertThat(a.size(), is(1));
  }
  @Test public void returnStatementsExistsNestedTypeWithReturn1() {
    final MethodDeclaration d = Into.d("  boolean f() {\n" + //
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
    assertThat(a, notNullValue());
    assertThat(a.size(), is(2));
  }
  @Test public void returnStatementsTwoReturns() {
    final MethodDeclaration d = Into.d("int f() { if (b) ; else return c; return a; }");
    final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
    assertThat(a, notNullValue());
    assertThat(a.size(), is(2));
  }
  @Test public void returnStatementsWithNestedEnum() {
    assertThat(new MethodExplorer(Into.d("  int f() {\n" + "    return new Object() {\n"
        + "      @Override public boolean equals(Object obj) {\n" + "        return super.equals(obj);\n" + "      }\n"
        + "      @Override public int hashCode() {\n" + "        return super.hashCode();\n" + "      }\n" + "    }.hashCode();\n"
        + "  }")).returnStatements().size(), is(1));
  }
}
