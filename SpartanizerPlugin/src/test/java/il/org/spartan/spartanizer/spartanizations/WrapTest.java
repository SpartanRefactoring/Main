package il.org.spartan.spartanizer.spartanizations;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.spartanizations.Wrap.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.junit.*;

import il.org.spartan.*;

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
    azzert.that(Wrap.find(""//
        + "if (b) {\n"//
        + " /* empty */"//
        + "} else {\n"//
        + " throw new Exception();\n"//
        + "}"), is(Wrap.Statement));
  }

  @Test public void essenceTest() {
    azzert.that("if(b){;}throw new Exception();", is(essence("if (b) {\n /* empty */; \n} // no else \n throw new Exception();\n")));
  }

  @Test public void expression() {
    azzert.that(Wrap.Expression.off(Wrap.Expression.on("a+b")), is("a+b"));
  }

  @Test public void findAddition() {
    azzert.that(Wrap.find("a+b"), is(Wrap.Expression));
  }

  @Test public void findDivision() {
    azzert.that(Wrap.find("a/b"), is(Wrap.Expression));
  }

  @Test public void findDivisionOfExpressions() {
    azzert.that(Wrap.find("(a+b)/++b"), is(Wrap.Expression));
  }

  @Test public void findEmptyBlock() {
    azzert.that(Wrap.find("{}"), is(Wrap.Statement));
  }

  @Test(expected = AssertionError.class) public void findError() {
    azzert.that(Wrap.find("}} f() { a();} b();}"), is(nullValue()));
  }

  @Test public void findExpression() {
    azzert.that(Wrap.find("i++"), is(Wrap.Expression));
  }

  @Test public void findMethod() {
    azzert.that(Wrap.find("f() { a(); b();}"), is(Wrap.Method));
  }

  @Test public void findStatement() {
    azzert.that(Wrap.find("for(;;);"), is(Wrap.Statement));
  }

  @Test public void findTwoStatements() {
    azzert.that(Wrap.find("a(); b();"), is(Wrap.Statement));
  }

  @Test public void intMethod() {
    azzert.that(Wrap.find("int f() { int s = 0; for (int i = 0; i < 10; ++i) s += i; return s;}"), is(Wrap.Method));
  }

  @Test public void intoCompilationUnit() {
    final Wrap w = Wrap.Expression;
    final String codeFragment = "a + b * c";
    final CompilationUnit u = w.intoCompilationUnit(codeFragment);
    assert u != null;
    azzert.that(w.off("" + u), containsString(codeFragment));
  }

  @Test public void intoDocument() {
    final Wrap w = Wrap.Expression;
    final String codeFragment = "a + b * c";
    final Document d = w.intoDocument(codeFragment);
    assert d != null;
    azzert.that(w.off(d.get()), containsString(codeFragment));
  }

  @Test public void method() {
    azzert.that(Wrap.Method.off(Wrap.Method.on("int f() { return a; }")), is("int f() { return a; }"));
  }

  @Test public void offDivision() {
    azzert.that("a/b", is(Wrap.Expression.off(Wrap.Expression.on("a/b"))));
  }

  @Test public void removeComments() {
    similar(Wrap.removeComments("" + "if (b) {\n" + " /* empty */" + "} else {\n" + " throw new Exception();\n" + "}"),
        "if (b) {} else { throw new Exception(); }");
  }

  private void similar(final String s1, final String s2) {
    azzert.that(essence(s2), is(essence(s1)));
  }

  @Test public void statement() {
    azzert.that(Wrap.Statement.off(Wrap.Statement.on("int a;")), is("int a;"));
  }
}
