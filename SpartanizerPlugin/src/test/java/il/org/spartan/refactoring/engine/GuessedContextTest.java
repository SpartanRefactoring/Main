package il.org.spartan.refactoring.engine;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.engine.GuessedContext.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings({ "static-method", "javadoc" }) //
public class GuessTest {
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
    azzert.that(find(""//
        + "if (b) {\n"//
        + " /* empty */"//
        + "} else {\n"//
        + " throw new Exception();\n"//
        + "}"), is(statement_or_something_that_may_occur_in_a_method));
  }

  @Test public void essenceTest() {
    azzert.that("if(b){;}throw new Exception();", is(essence("if (b) {\n /* empty */; \n} // no else \n throw new Exception();\n")));
  }

  @Test public void expression() {
    azzert.that(
        expression_or_something_that_may_be_passed_as_argument.off(GuessedContext.expression_or_something_that_may_be_passed_as_argument.on("a+b")),
        is("a+b"));
  }

  @Test public void findAddition() {
    azzert.that(GuessedContext.find("a+b"), is(GuessedContext.expression_or_something_that_may_be_passed_as_argument));
  }

  @Test public void findDivision() {
    azzert.that(GuessedContext.find("a/b"), is(GuessedContext.expression_or_something_that_may_be_passed_as_argument));
  }

  @Test public void findDivisionOfExpressions() {
    azzert.that(GuessedContext.find("(a+b)/++b"), is(GuessedContext.expression_or_something_that_may_be_passed_as_argument));
  }

  @Test public void findEmptyBlock() {
    azzert.that(GuessedContext.find("{}"), is(GuessedContext.statement_or_something_that_may_occur_in_a_method));
  }

  @Test(expected = AssertionError.class) public void findError() {
    azzert.that(GuessedContext.find("}} f() { a();} b();}"), is(nullValue()));
  }

  @Test public void findExpression() {
    azzert.that(GuessedContext.find("i++"), is(GuessedContext.expression_or_something_that_may_be_passed_as_argument));
  }

  @Test public void findMethod() {
    azzert.that(GuessedContext.find("f() { a(); b();}"), is(GuessedContext.method_or_class_member_of_some_sort));
  }

  @Test public void findStatement() {
    azzert.that(GuessedContext.find("for(;;);"), is(GuessedContext.statement_or_something_that_may_occur_in_a_method));
  }

  @Test public void findTwoStatements() {
    azzert.that(GuessedContext.find("a(); b();"), is(GuessedContext.statement_or_something_that_may_occur_in_a_method));
  }

  @Test public void intMethod() {
    azzert.that(GuessedContext.find("int f() { int s = 0; for (int i = 0; i < 10; ++i) s += i; return s;}"),
        is(GuessedContext.method_or_class_member_of_some_sort));
  }

  @Test public void intoCompilationUnit() {
    final GuessedContext w = GuessedContext.expression_or_something_that_may_be_passed_as_argument;
    final String codeFragment = "a + b * c";
    final CompilationUnit u = w.intoCompilationUnit(codeFragment);
    azzert.notNull(u);
    azzert.that(w.off(u.toString()), containsString(codeFragment));
  }

  @Test public void intoDocument() {
    final GuessedContext w = GuessedContext.expression_or_something_that_may_be_passed_as_argument;
    final String codeFragment = "a + b * c";
    final Document d = w.intoDocument(codeFragment);
    azzert.notNull(d);
    azzert.that(w.off(d.get()), containsString(codeFragment));
  }

  @Test public void method() {
    azzert.that(
        GuessedContext.method_or_class_member_of_some_sort.off(GuessedContext.method_or_class_member_of_some_sort.on("int f() { return a; }")),
        is("int f() { return a; }"));
  }

  @Test public void offDivision() {
    azzert.that("a/b", is(GuessedContext.expression_or_something_that_may_be_passed_as_argument
        .off(GuessedContext.expression_or_something_that_may_be_passed_as_argument.on("a/b"))));
  }

  @Test public void removeComments() {
    similar(GuessedContext.removeComments("" + "if (b) {\n" + " /* empty */" + "} else {\n" + " throw new Exception();\n" + "}"),
        "if (b) {} else { throw new Exception(); }");
  }

  private void similar(final String s1, final String s2) {
    azzert.that(essence(s2), is(essence(s1)));
  }

  @Test public void statement() {
    azzert.that(GuessedContext.statement_or_something_that_may_occur_in_a_method
        .off(GuessedContext.statement_or_something_that_may_occur_in_a_method.on("int a;")), is("int a;"));
  }
}
