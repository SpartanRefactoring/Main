package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

/**
 * @author Yossi Gil
 * @since 2015-07-17
 */
@SuppressWarnings("javadoc") public enum TESTUtils {
  ;
  public static void assertNoChange(final String input) {
    assertSimilar(input, Wrap.Expression.off(apply(new Trimmer(), Wrap.Expression.on(input))));
  }
  /**
   * A test to check that the actual output is similar to the actual value.
   *
   * @param expected JD
   * @param actual JD
   */
  public static void assertSimilar(final String expected, final Document actual) {
    assertSimilar(expected, actual.get());
  }
  /**
   * A test to check that the actual output is similar to the actual value.
   *
   * @param expected JD
   * @param actual JD
   */
  public static void assertSimilar(final String expected, final String actual) {
    if (!expected.equals(actual))
      that(Wrap.essence(actual), is(Wrap.essence(expected)));
  }
  /**
   * Convert a given {@link String} into an {@link Statement}, or fail the
   * current test, if such a conversion is not possible
   *
   * @param statement a {@link String} that represents a Java statement
   * @return an {@link Statement} data structure representing the parameter.
   */
  public static Statement asSingle(final String statement) {
    that(statement, notNullValue());
    final ASTNode n = ast.STATEMENTS.from(statement);
    that(n, notNullValue());
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
  public static String apply(final Trimmer t, final String from) {
    final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.from(from);
    that(u, notNullValue());
    final Document d = new Document(from);
    that(d, notNullValue());
    return TESTUtils.rewrite(t, u, d).get();
  }
  static void assertNoOpportunity(final Spartanization s, final String from) {
    final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.from(from);
    that(u.toString(), TrimmerTestsUtils.countOpportunities(s, u), is(0));
  }
  static void assertNotEvenSimilar(final String expected, final String actual) {
    that(compressSpaces(expected), not(compressSpaces(actual)));
  }
  static void assertOneOpportunity(final Spartanization s, final String from) {
    final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.from(from);
    that(u, notNullValue());
    that(TrimmerTestsUtils.countOpportunities(s, u), greaterThanOrEqualTo(1));
  }

  static final String WHITES = "(?m)\\s+";
}
