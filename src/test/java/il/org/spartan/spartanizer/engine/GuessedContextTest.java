package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.cmdline.GuessedContext.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;

@SuppressWarnings({ "static-method", "javadoc" })
public final class GuessedContextTest {
  @Test public void dealWithComment() {
    azzert.that(find("if (b) {\n"), is(STATEMENTS_LOOK_ALIKE));
  }

  @Test public void essenceTest() {
    azzert.that("if(b){;}throw new Exception();", is(wizard.essence("if (b) {\n /* empty */; \n} // no else \n throw new Exception();\n")));
  }

  @Test public void expression() {
    azzert.that(EXPRESSION_LOOK_ALIKE.off(EXPRESSION_LOOK_ALIKE.on("a+b")), is("a+b"));
  }

  @Test public void findAddition() {
    azzert.that(find("a+b"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findDivision() {
    azzert.that(GuessedContext.find("a/b"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findDivisionOfExpressions() {
    azzert.that(find("(a+b)/++b"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findEmptyBlock() {
    azzert.that(find("{}"), is(BLOCK_LOOK_ALIKE));
  }

  @Test(expected = AssertionError.class) public void findError() {
    azzert.that(find("}} f() { a();} b();}"), is(nullValue()));
  }

  @Test public void findExpression() {
    azzert.that(find("i++"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findLiteral0() {
    azzert.that(find("true"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findLiteral1() {
    azzert.that(find("1"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findLiteral2() {
    azzert.that(find("-0"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findLiteral3() {
    azzert.that(find("\"\""), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findLiteral4() {
    azzert.that(find("'\"'"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findMethod() {
    azzert.that(find("f() { a(); b();}"), is(METHOD_LOOKALIKE));
  }

  @Test public void findPlusPlus() {
    azzert.that(find("a++"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findStatement() {
    azzert.that(find("for(;;);"), is(STATEMENTS_LOOK_ALIKE));
  }

  @Test public void findTwoStatements() {
    azzert.that(find("a(); b();"), is(STATEMENTS_LOOK_ALIKE));
  }

  @Test public void intMethod() {
    azzert.that(find("int f() { int s = 0; for (int i = 0; i <10; ++i) s += i; return s;}"), is(METHOD_LOOKALIKE));
  }

  @Test public void intMethod0() {
    azzert.that(find("int f() { return s;}"), is(METHOD_LOOKALIKE));
  }

  @Test public void intMethod1() {
    azzert.that(find("void f(){}"), is(METHOD_LOOKALIKE));
  }

  @Test public void intoCompilationUnit() {
    final GuessedContext w = EXPRESSION_LOOK_ALIKE;
    final String codeFragment = "a + b * c";
    final CompilationUnit u = w.intoCompilationUnit(codeFragment);
    assert u != null;
    azzert.that(w.off(u + ""), containsString(codeFragment));
  }

  @Test public void intoDocument() {
    final GuessedContext w = EXPRESSION_LOOK_ALIKE;
    final String codeFragment = "a + b * c";
    final Document d = w.intoDocument(codeFragment);
    assert d != null;
    azzert.that(w.off(d.get()), containsString(codeFragment));
  }

  @Test public void method() {
    azzert.that(METHOD_LOOKALIKE.off(METHOD_LOOKALIKE.on("int f() { return a; }")), is("int f() { return a; }"));
  }

  @Test public void offDivision() {
    azzert.that("a/b", is(EXPRESSION_LOOK_ALIKE.off(EXPRESSION_LOOK_ALIKE.on("a/b"))));
  }

  @Test public void statement() {
    azzert.that(STATEMENTS_LOOK_ALIKE.off(STATEMENTS_LOOK_ALIKE.on("int a;")), is("int a;"));
  }

  @Test public void complicated() {
    assertEquals(GuessedContext.METHOD_LOOKALIKE,
        GuessedContext.find("public static int getFuzzyDistance(final CharSequence term,final CharSequence query,final Locale l){" //
            + "if (term == null || query == null)" //
            + "throw new IllegalArgumentException(\"Strings must not be null\");" //
            + "ExplodeOnNullWith(l, new IllegalArgumentException(\"Locale must not be null\"));"//
            + "final String termLowerCase = (term + \"\").toLowerCase(l);" //
            + "final String queryLowerCase = (query + \"\").toLowerCase(l);"//
            + "int $ = 0;" //
            + "  return $;" //
            + "for (int termIndex = 0, previousMatchingCharacterIndex = Integer.MIN_VALUE, queryIndex = 0; queryIndex < queryLowerCase"
            + ".length(); ++queryIndex)" //
            + "for (boolean termCharacterMatchFound = false; termIndex < termLowerCase.length()"//
            + "  && !termCharacterMatchFound; ++termIndex)"//
            + "if (queryLowerCase.charAt(queryIndex) == termLowerCase.charAt(termIndex)) {"//
            + "++$;"//
            + " if (previousMatchingCharacterIndex + 1 == termIndex)"//
            + "$ += 2;"//
            + "previousMatchingCharacterIndex = termIndex; "//
            + "termCharacterMatchFound = true; "//
            + "}"//
            + "  return $;"//
            + " }"));
  }

  @Test public void complicated2() {
    assertEquals(GuessedContext.STATEMENTS_LOOK_ALIKE, GuessedContext.find(" for (int $N0 = 0; $N0 < $N1; ++$N0) $N2 ^= $N3.$N4($N5, $N6)[0];" //
    ));
  }

  @Test public void methodInvocation() {
    assertEquals(GuessedContext.EXPRESSION_LOOK_ALIKE, GuessedContext.find("fuo()"));
  }
}
