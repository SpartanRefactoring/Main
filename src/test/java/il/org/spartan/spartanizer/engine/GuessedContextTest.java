package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.GuessedContext.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings({ "static-method", "javadoc" }) //
@Ignore //
public class GuessedContextTest {
  @Test public void dealWithBothKindsOfComment() {
    similar(
        "if (b) {\n" + "", //
        "if (b) {;} { throw new Exception(); }");
  }

  @Test public void dealWithComment() {
    azzert.that(find("if (b) {\n" + ""), is(STATEMENTS__LOOK__ALIKE));
  }

  @Test public void essenceTest() {
    azzert.that("if(b){;}throw new Exception();", is(essence("if (b) {\n /* empty */; \n} // no else \n throw new Exception();\n")));
  }

  @Test public void expression() {
    azzert.that(EXPRESSION__LOOK__ALIKE.off(EXPRESSION__LOOK__ALIKE.on("a+b")), is("a+b"));
  }

  @Test public void findAddition() {
    azzert.that(find("a+b"), is(EXPRESSION__LOOK__ALIKE));
  }

  @Test public void findDivision() {
    azzert.that(GuessedContext.find("a/b"), is(EXPRESSION__LOOK__ALIKE));
  }

  @Test public void findDivisionOfExpressions() {
    azzert.that(find("(a+b)/++b"), is(EXPRESSION__LOOK__ALIKE));
  }

  @Test public void findEmptyBlock() {
    azzert.that(find("{}"), is(STATEMENTS__LOOK__ALIKE));
  }

  @Test(expected = AssertionError.class) public void findError() {
    azzert.that(find("}} f() { a();} b();}"), is(nullValue()));
  }

  @Test public void findExpression() {
    azzert.that(find("i++"), is(EXPRESSION__LOOK__ALIKE));
  }

  @Test public void findLiteral0() {
    azzert.that(find("true"), is(EXPRESSION__LOOK__ALIKE));
  }

  @Test public void findLiteral1() {
    azzert.that(find("1"), is(EXPRESSION__LOOK__ALIKE));
  }

  @Test public void findLiteral2() {
    azzert.that(find("-0"), is(EXPRESSION__LOOK__ALIKE));
  }

  @Test public void findLiteral3() {
    azzert.that(find("\"\""), is(EXPRESSION__LOOK__ALIKE));
  }

  @Test public void findLiteral4() {
    azzert.that(find("'\"'"), is(EXPRESSION__LOOK__ALIKE));
  }

  @Test public void findMethod() {
    azzert.that(find("f() { a(); b();}"), //
        is(METHOD__LOOKALIKE));
  }

  @Test public void findPlusPlus() {
    azzert.that(find("a++"), is(EXPRESSION__LOOK__ALIKE));
  }

  @Test public void findStatement() {
    azzert.that(find("for(;;);"), is(STATEMENTS__LOOK__ALIKE));
  }

  @Test public void findTwoStatements() {
    azzert.that(find("a(); b();"), is(STATEMENTS__LOOK__ALIKE));
  }

  @Test public void findVariable() {
    azzert.that(find("i"), is(EXPRESSION__LOOK__ALIKE));
  }

  @Test public void intMethod() {
    azzert.that(find("int f() { int s = 0; for (int i = 0; i < 10; ++i) s += i; return s;}"), is(METHOD__LOOKALIKE));
  }

  @Test public void intMethod0() {
    azzert.that(find("int f() { return s;}"), is(METHOD__LOOKALIKE));
  }

  @Test public void intMethod1() {
    azzert.that(find("void f(){}"), is(METHOD__LOOKALIKE));
  }

  @Test public void intoCompilationUnit() {
    final GuessedContext w = EXPRESSION__LOOK__ALIKE;
    final String codeFragment = "a + b * c";
    final CompilationUnit u = w.intoCompilationUnit(codeFragment);
    assert u != null;
    azzert.that(w.off(u + ""), containsString(codeFragment));
  }

  @Test public void intoDocument() {
    final GuessedContext w = EXPRESSION__LOOK__ALIKE;
    final String codeFragment = "a + b * c";
    final Document d = w.intoDocument(codeFragment);
    assert d != null;
    azzert.that(w.off(d.get()), containsString(codeFragment));
  }

  @Test public void method() {
    azzert.that(METHOD__LOOKALIKE.off(METHOD__LOOKALIKE.on("int f() { return a; }")), is("int f() { return a; }"));
  }

  @Test public void offDivision() {
    azzert.that("a/b", is(EXPRESSION__LOOK__ALIKE.off(EXPRESSION__LOOK__ALIKE.on("a/b"))));
  }

  @Test public void removeCommentsTest() {
    similar(removeComments(//
        "if (b) {\n" + ""),
        "if (b) {} else { throw new Exception(); }");
  }

  @Test public void statement() {
    azzert.that(STATEMENTS__LOOK__ALIKE.off(STATEMENTS__LOOK__ALIKE.on("int a;")), is("int a;"));
  }

  private void similar(final String s1, final String s2) {
    azzert.that(essence(s2), is(essence(s1)));
  }
}
