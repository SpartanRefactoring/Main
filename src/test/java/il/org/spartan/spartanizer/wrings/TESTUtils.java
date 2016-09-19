package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.spartanizations.*;

/** @author Yossi Gil
 * @since 2015-07-17 */
@SuppressWarnings("javadoc") public enum TESTUtils {
  ;
  static final String WHITES = "(?m)\\s+";

  public static void assertNoChange(final String input) {
    assertSimilar(input, Wrap.Expression.off(apply(new Trimmer(), Wrap.Expression.on(input))));
  }

  /** A test to check that the actual output is similar to the actual value.
   * @param expected JD
   * @param actual JD */
  public static void assertSimilar(final String expected, final Document actual) {
    assertSimilar(expected, actual.get());
  }

  /** A test to check that the actual output is similar to the actual value.
   * @param expected JD
   * @param actual JD */
  public static void assertSimilar(final String expected, final String actual) {
    if (!expected.equals(actual))
      azzert.that(Wrap.essence(actual), is(Wrap.essence(expected)));
  }

  /** Convert a given {@link String} into an {@link Statement}, or fail the
   * current test, if such a conversion is not possible
   * @param statement a {@link String} that represents a Java statement
   * @return an {@link Statement} data structure representing the parameter. */
  public static Statement asSingle(final String statement) {
    assert statement != null;
    final ASTNode n = makeAST.STATEMENTS.from(statement);
    assert n != null;
    return extract.singleStatement(n);
  }

  public static Document rewrite(final GUI$Applicator a, final CompilationUnit u, final Document $) {
    try {
      a.createRewrite(u).rewriteAST($, null).apply($);
      return $;
    } catch (MalformedTreeException | BadLocationException e) {
      throw new AssertionError(e);
    }
  }

  static String apply(final Trimmer t, final String from) {
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    assert u != null;
    final Document d = new Document(from);
    assert d != null;
    return TESTUtils.rewrite(t, u, d).get();
  }

  static void assertNoOpportunity(final GUI$Applicator a, final String from) {
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    azzert.that(u + "", TrimmerTestsUtils.countOpportunities(a, u), is(0));
  }

  static void assertNotEvenSimilar(final String expected, final String actual) {
    azzert.that(tide.clean(actual), is(tide.clean(expected)));
  }

  static void assertOneOpportunity(final GUI$Applicator a, final String from) {
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    assert u != null;
    azzert.that(TrimmerTestsUtils.countOpportunities(a, u), greaterThanOrEqualTo(1));
  }
}
