package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.spartanizations.Wrap.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.junit.*;

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
    that(Wrap.find(""//
        + "if (b) {\n"//
        + " /* empty */"//
        + "} else {\n"//
        + " throw new Exception();\n"//
        + "}"), is(Wrap.Statement));
  }
  @Test public void essenceTest() {
    that("if(b){;}throw new Exception();", is(essence("if (b) {\n /* empty */; \n} // no else \n throw new Exception();\n")));
  }
  @Test public void expression() {
    that(Wrap.Expression.off(Wrap.Expression.on("a+b")), is("a+b"));
  }
  @Test public void findAddition() {
    that(Wrap.find("a+b"), is(Wrap.Expression));
  }
  @Test public void findDivision() {
    that(Wrap.find("a/b"), is(Wrap.Expression));
  }
  @Test public void findDivisionOfExpressions() {
    that(Wrap.find("(a+b)/++b"), is(Wrap.Expression));
  }
  @Test public void findEmptyBlock() {
    that(Wrap.find("{}"), is(Wrap.Statement));
  }
  @Test public void findError() {
    that(Wrap.find("}} f() { a();} b();}"), is(nullValue()));
  }
  @Test public void findExpression() {
    that(Wrap.find("i++"), is(Wrap.Expression));
  }
  @Test public void findMethod() {
    that(Wrap.find("f() { a(); b();}"), is(Wrap.Method));
  }
  @Test public void findStatement() {
    that(Wrap.find("for(;;);"), is(Wrap.Statement));
  }
  @Test public void findTwoStatements() {
    that(Wrap.find("a(); b();"), is(Wrap.Statement));
  }
  @Test public void intMethod() {
    that(Wrap.find("int f() { int s = 0; for (int i = 0; i < 10; ++i) s += i; return s;}"), is(Wrap.Method));
  }
  @Test public void intoCompilationUnit() {
    final Wrap w = Wrap.Expression;
    final String codeFragment = "a + b * c";
    final CompilationUnit u = w.intoCompilationUnit(codeFragment);
    that(u, notNullValue());
    that(w.off(u.toString()), containsString(codeFragment));
  }
  @Test public void intoDocument() {
    final Wrap w = Wrap.Expression;
    final String codeFragment = "a + b * c";
    final Document d = w.intoDocument(codeFragment);
    that(d, notNullValue());
    that(w.off(d.get()), containsString(codeFragment));
  }
  @Test public void method() {
    that(Wrap.Method.off(Wrap.Method.on("int f() { return a; }")), is("int f() { return a; }"));
  }
  @Test public void offDivision() {
    that("a/b", is(Wrap.Expression.off(Wrap.Expression.on("a/b"))));
  }
  @Test public void removeComments() {
    similar(Wrap.removeComments("" + "if (b) {\n" + " /* empty */" + "} else {\n" + " throw new Exception();\n" + "}"),
        "if (b) {} else { throw new Exception(); }");
  }
  @Test public void statement() {
    that(Wrap.Statement.off(Wrap.Statement.on("int a;")), is("int a;"));
  }
  private void similar(final String s1, final String s2) {
    that(essence(s2), is(essence(s1)));
  }
}
