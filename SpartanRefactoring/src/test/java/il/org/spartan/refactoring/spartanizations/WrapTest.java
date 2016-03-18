package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.refactoring.spartanizations.Wrap.essence;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;
import org.junit.Test;

@SuppressWarnings({ "static-method", "javadoc" }) public class WrapTest {
  @Test public void dealWithBothKindsOfComment() {
    similar(
        ""//
            + "if (b) {\n"//
            + " /* empty */"//
            + "; \n"//
            + "} { // no else \n"//
            + " throw new Exception();\n"//
            + "}", //
        "if (b) {;} { throw new Exception(); }");
  }
  @Test public void dealWithComment() {
    assertThat(Wrap.find(""//
        + "if (b) {\n"//
        + " /* empty */"//
        + "} else {\n"//
        + " throw new Exception();\n"//
        + "}"), is(Wrap.Statement));
  }
  @Test public void essenceTest() {
    assertEquals(essence("if (b) {\n /* empty */; \n} // no else \n throw new Exception();\n"), "if(b){;}throw new Exception();");
  }
  @Test public void expression() {
    assertThat(Wrap.Expression.off(Wrap.Expression.on("a+b")), is("a+b"));
  }
  @Test public void findAddition() {
    assertThat(Wrap.find("a+b"), is(Wrap.Expression));
  }
  @Test public void findDivision() {
    assertThat(Wrap.find("a/b"), is(Wrap.Expression));
  }
  @Test public void findDivisionOfExpressions() {
    assertThat(Wrap.find("(a+b)/++b"), is(Wrap.Expression));
  }
  @Test public void findEmptyBlock() {
    assertThat(Wrap.find("{}"), is(Wrap.Statement));
  }
  @Test public void findError() {
    assertThat(Wrap.find("}} f() { a();} b();}"), is(nullValue()));
  }
  @Test public void findExpression() {
    assertThat(Wrap.find("i++"), is(Wrap.Expression));
  }
  @Test public void findMethod() {
    assertThat(Wrap.find("f() { a(); b();}"), is(Wrap.Method));
  }
  @Test public void findStatement() {
    assertThat(Wrap.find("for(;;);"), is(Wrap.Statement));
  }
  @Test public void findTwoStatements() {
    assertThat(Wrap.find("a(); b();"), is(Wrap.Statement));
  }
  @Test public void intMethod() {
    assertThat(Wrap.find("int f() { int s = 0; for (int i = 0; i < 10; ++i) s += i; return s;}"), is(Wrap.Method));
  }
  @Test public void intoCompilationUnit() {
    final Wrap w = Wrap.Expression;
    final String codeFragment = "a + b * c";
    final CompilationUnit u = w.intoCompilationUnit(codeFragment);
    assertThat(u, notNullValue());
    assertThat(w.off(u.toString()), containsString(codeFragment));
  }
  @Test public void intoDocument() {
    final Wrap w = Wrap.Expression;
    final String codeFragment = "a + b * c";
    final Document d = w.intoDocument(codeFragment);
    assertThat(d, notNullValue());
    assertThat(w.off(d.get()), containsString(codeFragment));
  }
  @Test public void method() {
    assertThat(Wrap.Method.off(Wrap.Method.on("int f() { return a; }")), is("int f() { return a; }"));
  }
  @Test public void offDivision() {
    assertEquals(Wrap.Expression.off(Wrap.Expression.on("a/b")), "a/b");
  }
  @Test public void removeComments() {
    similar(Wrap.removeComments("" + "if (b) {\n" + " /* empty */" + "} else {\n" + " throw new Exception();\n" + "}"), "if (b) {} else { throw new Exception(); }");
  }
  @Test public void statement() {
    assertThat(Wrap.Statement.off(Wrap.Statement.on("int a;")), is("int a;"));
  }
  private void similar(final String s1, final String s2) {
    assertEquals(essence(s1), essence(s2));
  }
}
