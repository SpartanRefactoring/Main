package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.hamcrest.OrderingComparison.greaterThanOrEqualTo;
import static il.org.spartan.refactoring.wring.TrimmerTest.countOpportunities;
import static il.org.spartan.utils.Utils.compressSpaces;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;

import il.org.spartan.refactoring.spartanizations.Spartanization;
import il.org.spartan.refactoring.utils.As;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.wring.Trimmer;

/**
 * @author Yossi Gil
 * @since 2015-07-17
 */
@SuppressWarnings("javadoc") public enum TESTUtils {
  ;
  static final String WHITES = "(?m)\\s+";
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
      assertEquals(Wrap.essence(expected), Wrap.essence(actual));
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
    final ASTNode n = As.STATEMENTS.ast(statement);
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
  static String apply(final Trimmer t, final String from) {
    final CompilationUnit u = (CompilationUnit) As.COMPILIATION_UNIT.ast(from);
    assertNotNull(u);
    final Document d = new Document(from);
    assertNotNull(d);
    return TESTUtils.rewrite(t, u, d).get();
  }
  static void assertNoOpportunity(final Spartanization s, final String from) {
    final CompilationUnit u = (CompilationUnit) As.COMPILIATION_UNIT.ast(from);
    assertEquals(u.toString(), 0, countOpportunities(s, u));
  }
  static void assertNotEvenSimilar(final String expected, final String actual) {
    assertNotEquals(compressSpaces(expected), compressSpaces(actual));
  }
  static void assertOneOpportunity(final Spartanization s, final String from) {
    final CompilationUnit u = (CompilationUnit) As.COMPILIATION_UNIT.ast(from);
    assertThat(u, notNullValue());
    assertThat(countOpportunities(s, u), greaterThanOrEqualTo(1));
  }
}
