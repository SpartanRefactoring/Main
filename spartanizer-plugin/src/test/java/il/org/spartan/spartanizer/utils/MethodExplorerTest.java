package il.org.spartan.spartanizer.utils;

import static fluent.ly.azzert.is;

import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.engine.MethodExplorer;
import il.org.spartan.spartanizer.engine.parse;

@SuppressWarnings({ "static-method", "javadoc" })
public final class MethodExplorerTest {
  @Test public void localVariablesCatchExpression() {
    azzert.that(new MethodExplorer(parse.d(" void f() { try { f(); } catch (final Exception|RuntimeException e) { f(); } }")).localVariables().size(),
        is(1));
  }
  @Test public void localVariablesExtendedForLoop() {
    azzert.that(
        new MethodExplorer(parse.d(" int sum(final int is[]) { int $ = 0; for (final int i : is) $ += i; return $; } ")).localVariables().size(),
        is(2));
  }
  @Test public void localVariablesForLoopNoVariable() {
    azzert.that(new MethodExplorer(parse.d(" int f() { for (f(); i*j <10; i += j++); }")).localVariables().size(), is(0));
  }
  @Test public void localVariablesForLoopOneVariable() {
    azzert.that(new MethodExplorer(parse.d(" int f() { for (int i = 0; i*j <10; i += j++); }")).localVariables().size(), is(1));
  }
  @Test public void localVariablesForLoopTwoVariables() {
    azzert.that(new MethodExplorer(parse.d(" int f() { for (int i = 0, j = 2; i*j <10; i += j++); }")).localVariables().size(), is(2));
  }
  @Test public void localVariablesMultipleFragments() {
    azzert.that(new MethodExplorer(parse.d(" int f() { int a,b; }")).localVariables().size(), is(2));
  }
  @Test public void localVariablesMultipleNestedFragments() {
    azzert.that(new MethodExplorer(parse.d(" int f() { int a,b; {int c, d;}}")).localVariables().size(), is(4));
  }
  @Test public void localVariablesNone() {
    azzert.that(new MethodExplorer(parse.d(" int f() { return new Object() { @Override public boolean equals(Object obj) { "
        + " return super.equals(obj); } @Override public int hashCode() { return super.hashCode(); " //
        + " } }.hashCode(); }")).localVariables().size(), is(0));
  }
  @Test public void localVariablesRepeatedNestedFragments() {
    azzert.that(new MethodExplorer(parse.d(" int f() { int a,b,c,d; {int i, j;} {int i,j; int k;}")).localVariables().size(), is(9));
  }
  @Test public void localVariablesTryClause() {
    azzert.that(new MethodExplorer(parse.d(" void f() { final File f = new File(\"f\"); "
        + " try (final InputStream s = new FileInputStream(f); final InputStreamReader is = new InputStreamReader(s)) { f(); "
        + " } catch (final FileNotFoundException e) { e.f(); } catch (final IOException e) { " //
        + " e.f(); } finally { f(); } } ")).localVariables().size(), is(5));
  }
  @Test public void localVariablesVanilla() {
    azzert.that(new MethodExplorer(parse.d(" int f() { int a; }")).localVariables().size(), is(1));
  }
  @Test public void returnStatementsExists() {
    final MethodDeclaration d = parse.d("int f() { return a; }");
    final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
    assert a != null;
    azzert.that(a.size(), is(1));
  }
  @Test public void returnStatementsExistsNestedType() {
    final MethodDeclaration d = parse.d("int f() { class B {}; return a; }");
    final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
    assert a != null;
    azzert.that(a.size(), is(1));
  }
  @Test public void returnStatementsExistsNestedTypeAnnotation() {
    final MethodDeclaration d = parse.d(" boolean f() { " + //
        " @interface C{static class X{boolean f(){return f();}}}" + //
        " if (f()) " + //
        " return f(); " + //
        " return new B().g(); " + //
        " }"); //
    final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
    assert a != null;
    azzert.that(a.size(), is(2));
  }
  @Test public void returnStatementsExistsNestedTypeWithReturn() {
    final MethodDeclaration d = parse.d("int f() { class B {int g() { return c; } }; return a; }");
    final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
    assert a != null;
    azzert.that(a.size(), is(1));
  }
  @Test public void returnStatementsExistsNestedTypeWithReturn1() {
    final MethodDeclaration d = parse.d(" boolean f() { " + //
        " if (f()) " + //
        " return f(); " + //
        " class B { " + //
        " boolean g() { " + //
        " return g(); " + //
        " } " + //
        " } " + //
        " return new B().g(); " + //
        " }"); //
    final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
    assert a != null;
    azzert.that(a.size(), is(2));
  }
  @Test public void returnStatementsTwoReturns() {
    final MethodDeclaration d = parse.d("int f() { if (b) ; else return c; return a; }");
    final List<ReturnStatement> a = new MethodExplorer(d).returnStatements();
    assert a != null;
    azzert.that(a.size(), is(2));
  }
  @Test public void returnStatementsWithNestedEnum() {
    azzert.that(new MethodExplorer(parse.d("int f() { return new Object() { @Override public boolean equals(Object obj) { "
        + " return super.equals(obj); } @Override public int hashCode() { return super.hashCode(); " //
        + " } }.hashCode(); }")).returnStatements().size(), is(1));
  }
}
