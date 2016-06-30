package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.SpartanAssert.*;
import static il.org.spartan.Utils.*;
import static org.junit.Assert.*;
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
      assertThat(Wrap.essence(actual), is(Wrap.essence(expected)));
  }
  /**
   * Convert a given {@link String} into an {@link Statement}, or fail the
   * current test, if such a conversion is not possible
   *
   * @param statement a {@link String} that represents a Java statement
   * @return an {@link Statement} data structure representing the parameter.
   */
  public static Statement asSingle(final String statement) {
    assertThat(statement, notNullValue());
    final ASTNode n = ast.STATEMENTS.ast(statement);
    assertThat(n, notNullValue());
    return Extract.singleStatement(n);
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
    final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.ast(from);
    assertThat(u, notNullValue());
    final Document d = new Document(from);
    assertThat(d, notNullValue());
    return TESTUtils.rewrite(t, u, d).get();
  }
  static void assertNoOpportunity(final Spartanization s, final String from) {
    final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.ast(from);
    assertThat(u.toString(), TrimmerTestsUtils.countOpportunities(s, u), is(0));
  }
  static void assertNotEvenSimilar(final String expected, final String actual) {
    assertThat(compressSpaces(expected), not(compressSpaces(actual)));
  }
  static void assertOneOpportunity(final Spartanization s, final String from) {
    final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.ast(from);
    assertThat(u, notNullValue());
    assertThat(TrimmerTestsUtils.countOpportunities(s, u), greaterThanOrEqualTo(1));
  }

  static final String WHITES = "(?m)\\s+";
}
