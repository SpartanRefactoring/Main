package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.refactoring.spartanizations.GuessedContext.*;
import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.engine.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.*;

/** @author Yossi Gil
 * @since 2015-07-17 */
@SuppressWarnings("javadoc") //
public enum TESTUtils {
  ;
  static final String WHITES = "(?m)\\s+";

  public static String apply(final Trimmer t, final String from) {
    final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(from);
    azzert.notNull(u);
    final Document d = new Document(from);
    azzert.notNull(d);
    return TESTUtils.rewrite(t, u, d).get();
  }

  public static void assertNoChange(final GuessedContext w, final String input) {
    assertSimilar(w, input, GuessedContext.expression_or_something_that_may_be_passed_as_argument
        .off(apply(new Trimmer(), GuessedContext.expression_or_something_that_may_be_passed_as_argument.on(input))));
  }

  static void assertNoOpportunity(final Spartanization s, final String from) {
    final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(from);
    azzert.that(u.toString(), trimming.countOpportunities(s, u), is(0));
  }

  static void assertNotEvenSimilar(final String expected, final String actual) {
    azzert.that(Funcs.gist(actual), is(Funcs.gist(expected)));
  }

  static void assertOneOpportunity(final Spartanization s, final String from) {
    final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(from);
    azzert.notNull(u);
    azzert.that(trimming.countOpportunities(s, u), greaterThanOrEqualTo(1));
  }

  /** A test to check that the actual output is similar to the actual value.
   * @param expected JD
   * @param actual JD */
  public static void assertSimilar(final GuessedContext w, final String expected, final Document actual) {
    final String actual1 = actual.get();
    assertSimilar(w, expected, actual1);
  }

  public static void assertSimilar(final GuessedContext g, //
      final String expected, //
      final String actual) {
    azzert.that(//
        "**" //
            + "\n Expected = '" + expected + "'" //
            + "\n Actual = '" + expected + "'" //
            + "\n Guessed context = " + g + "'"//
            + "\n\t Before I will allow JUnit fail this @Test, let"//
            + "\n\t me show you how I try to make sense of " + "\n\t what you got:\n" //
            + GuessedContext.enumerateFailingAttempts(actual), //
        GuessedContext.essence(actual), //
        is(GuessedContext.essence(expected)));
  }

  /** A test to check that the actual output is similar to the actual value.
   * @param expected JD
   * @param actual JD */
  public static void assertSimilarString(final String message, final String expected, final String actual) {
    if (!expected.equals(actual))
      azzert.that(message, GuessedContext.essence(actual), is(GuessedContext.essence(expected)));
  }

  /** Convert a given {@link String} into an {@link Statement}, or fail the
   * current test, if such a conversion is not possible
   * @param statement a {@link String} that represents a Java statement
   * @return an {@link Statement} data structure representing the parameter. */
  public static Statement asSingle(final String statement) {
    azzert.notNull(statement);
    final ASTNode n = makeAST.STATEMENTS.from(statement);
    azzert.notNull(n);
    return extract.singleStatement(n);
  }

  public static Document rewrite(final Spartanization s, final CompilationUnit u, final Document $) {
    try {
      s.createRewrite(u, null).rewriteAST($, null).apply($);
      return $;
    } catch (MalformedTreeException | BadLocationException e) {
      throw new AssertionError(e);
    }
  }

  static String apply(final Trimmer t, final String from) {
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    azzert.notNull(u);
    final Document d = new Document(from);
    azzert.notNull(d);
    return TESTUtils.rewrite(t, u, d).get();
  }

  static void assertNoOpportunity(final Spartanization s, final String from) {
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    azzert.that("" + u, TrimmerTestsUtils.countOpportunities(s, u), is(0));
  }

  static void assertNotEvenSimilar(final String expected, final String actual) {
    azzert.that(tide.clean(actual), is(tide.clean(expected)));
  }

  static void assertOneOpportunity(final Spartanization s, final String from) {
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    azzert.notNull(u);
    azzert.that(TrimmerTestsUtils.countOpportunities(s, u), greaterThanOrEqualTo(1));
  }
}
