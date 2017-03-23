package il.org.spartan.spartanizer.utils;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** TODO: Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Oct 7, 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class WrapTest {
  @Test public void dealWithBothKindsOfComment() {
    similar("if (b) {\n /* empty */; \n} { // no else \n throw new Exception();\n}", //
        "if (b) {;} { throw new Exception(); }");
  }

  @Test public void dealWithComment() {
    azzert.that(Wrap.find("if (b) {\n /* empty */} else {\n throw new Exception();\n}"), is(Wrap.Statement));
  }

  @Test public void essenceTest() {
    azzert.that("if(b){;}throw new Exception();", is(trivia.essence("if (b) {\n /* empty */; \n} // no else \n throw new Exception();\n")));
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
    azzert.that(Wrap.find("int f() { int s = 0; for (int i = 0; i <10; ++i) s += i; return s;}"), is(Wrap.Method));
  }

  @Test public void intoCompilationUnit() {
    @NotNull final Wrap w = Wrap.Expression;
    @NotNull final String codeFragment = "a + b * c";
    @NotNull final CompilationUnit u = w.intoCompilationUnit(codeFragment);
    assert u != null;
    azzert.that(w.off(u + ""), containsString(codeFragment));
  }

  @Test public void intoDocument() {
    @NotNull final Wrap w = Wrap.Expression;
    @NotNull final String codeFragment = "a + b * c";
    @NotNull final Document d = w.intoDocument(codeFragment);
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
    similar(trivia.removeComments("if (b) {\n /* empty */} else {\n throw new Exception();\n}"), "if (b) {} else { throw new Exception(); }");
  }

  private void similar(@NotNull final String s1, @NotNull final String s2) {
    azzert.that(trivia.essence(s2), is(trivia.essence(s1)));
  }

  @Test public void statement() {
    azzert.that(Wrap.Statement.off(Wrap.Statement.on("int a;")), is("int a;"));
  }
}
