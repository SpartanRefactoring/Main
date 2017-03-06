package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.utils.Wrap.*;
import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.utils.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2015-07-17 */
@SuppressWarnings("javadoc")
public enum TESTUtils {
  ;
  static final String WHITES = "(?m)\\s+";

  static String apply(final Trimmer t, final String from) {
    final CompilationUnit $ = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    assert $ != null;
    final Document d = new Document(from);
    assert d != null;
    return TESTUtils.rewrite(t, $, d).get();
  }

  public static void assertNoChange(final String input) {
    assertSimilar(input, Wrap.Expression.off(apply(new Trimmer(), Wrap.Expression.on(input))));
  }

  static void assertNoOpportunity(final AbstractGUIApplicator a, final String from) {
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    azzert.that(u + "", TrimmerTestsUtils.countOpportunities(a, u), is(0));
  }

  static void assertNotEvenSimilar(final String expected, final String actual) {
    azzert.that(tide.clean(actual), is(tide.clean(expected)));
  }

  static void assertOneOpportunity(final AbstractGUIApplicator a, final String from) {
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    assert u != null;
    azzert.that(TrimmerTestsUtils.countOpportunities(a, u), greaterThanOrEqualTo(1));
  }

  /** A test to check that the actual output is similar to the actual value.
   * @param expected JD
   * @param actual JD */
  public static void assertSimilar(final String expected, final IDocument actual) {
    assertSimilar(expected, actual.get());
  }

  /** A test to check that the actual output is similar to the actual value.
   * @param expected JD
   * @param actual JD */
  public static void assertSimilar(final String expected, final String actual) {
    if (!expected.equals(actual))
      azzert.that(essence(actual), is(essence(expected)));
  }

  /** Convert a given {@link String} into an {@link Statement}, or fail the
   * current test, if such a conversion is not possible
   * @param statement a {@link String} that represents a Java statement
   * @return an {@link Statement} data structure representing the parameter. */
  public static Statement asSingle(final String statement) {
    assert statement != null;
    final ASTNode $ = makeAST.STATEMENTS.from(statement);
    assert $ != null;
    return extract.singleStatement($);
  }

  public static Document rewrite(final AbstractGUIApplicator a, final CompilationUnit u, final Document $) {
    try {
      a.createRewrite(u).rewriteAST($, null).apply($);
      return $;
    } catch (MalformedTreeException | BadLocationException ¢) {
      throw new AssertionError(¢);
    }
  }
}
