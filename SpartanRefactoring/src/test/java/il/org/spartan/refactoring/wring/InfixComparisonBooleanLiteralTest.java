package il.org.spartan.refactoring.wring;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.hamcrest.OrderingComparison.greaterThanOrEqualTo;
import static il.org.spartan.refactoring.utils.Restructure.flatten;
import static il.org.spartan.utils.Utils.compressSpaces;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.assertSimilar;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import il.org.spartan.refactoring.spartanizations.Wrap;

import il.org.spartan.refactoring.spartanizations.Spartanization;
import il.org.spartan.refactoring.utils.As;
import il.org.spartan.refactoring.utils.ExpressionComparator;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.wring.InfixComparisonBooleanLiteral;
import il.org.spartan.refactoring.wring.Trimmer;
import il.org.spartan.refactoring.wring.Wrings;
import il.org.spartan.utils.Utils;

/**
 * Unit tests for {@link Wrings#ADDITION_SORTER}.
 *
 * @author Yossi Gil
 * @since 2014-07-13
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "static-method" }) //
public class InfixComparisonBooleanLiteralTest extends AbstractWringTest<InfixExpression> {
  static final InfixComparisonBooleanLiteral WRING = new InfixComparisonBooleanLiteral();
  public InfixComparisonBooleanLiteralTest() {
    super(WRING);
  }
  @Test public void removeParenthesis() throws MalformedTreeException, BadLocationException {
    final String s = " (2) == true";
    final String wrap = Wrap.Expression.on(s);
    final CompilationUnit u = (CompilationUnit) As.COMPILIATION_UNIT.ast(wrap);
    assertNotNull(u);
    final Document d = new Document(wrap);
    assertNotNull(d);
    final Trimmer t = new Trimmer();
    final ASTRewrite r = t.createRewrite(u, null);
    final TextEdit x = r.rewriteAST(d, null);
    x.apply(d);
    final String unpeeled = d.get();
    if (wrap.equals(unpeeled))
      fail("Nothing done on " + s);
    final String peeled = Wrap.Expression.off(unpeeled);
    if (peeled.equals(s))
      assertNotEquals("No similification of " + s, s, peeled);
    if (compressSpaces(peeled).equals(compressSpaces(s)))
      assertNotEquals("Simpification of " + s + " is just reformatting", compressSpaces(peeled), compressSpaces(s));
    assertSimilar(" 2 ", peeled);
  }

  @RunWith(Parameterized.class) //
  public static class WringedInput extends AbstractWringTest.WringedExpression.Infix {
    static String[][] cases = Utils.asArray(//
        new String[] { "", "a == b == c == true", "a == b == c" }, //
        new String[] { "", "a == true", "a" }, //
        new String[] { "", "a == false", "!a" }, //
        new String[] { "", "(a) == false", "!a" }, //
        new String[] { "", "(a) == (false)", "!a" }, //
        new String[] { "", "true == a", "a" }, //
        new String[] { "", "true == (a)", "a" }, //
        new String[] { "", "(true) == (a)", "a" }, //
        new String[] { "", "a != true", "!a" }, //
        new String[] { "", "a != false", "a" }, //
        new String[] { "", "false == a", "!a" }, //
        new String[] { "", "true != a", "!a" }, //
        new String[] { "", "false != a", "a" }, //
        new String[] { "", "false != false", "false" }, //
        new String[] { "", "false != true", "true" }, //
        new String[] { "", "false == false", "true" }, //
        new String[] { "", "false == true", "false" }, //
        new String[] { "", "false != false", "false" }, //
        new String[] { "", "true != true", "false" }, //
        new String[] { "", "true != false", "true" }, //
        new String[] { "", "true != true", "false" }, //
        null);
    /**
     * Generate test cases for this parameterized class.
     *
     * @return a collection of cases, where each case is an array of three
     *         objects, the test case name, the input, and the file.
     */
    @Parameters(name = DESCRIPTION) //
    public static Collection<Object[]> cases() {
      return collect(cases);
    }
    static Document rewrite(final Spartanization s, final CompilationUnit u, final Document d) {
      try {
        s.createRewrite(u, null).rewriteAST(d, null).apply(d);
        return d;
      } catch (final MalformedTreeException e) {
        fail(e.getMessage());
      } catch (final IllegalArgumentException e) {
        fail(e.getMessage());
      } catch (final BadLocationException e) {
        fail(e.getMessage());
      }
      return null;
    }
    public WringedInput() {
      super(WRING);
    }
    @Override @Test public void flattenIsIdempotentt() {
      final InfixExpression flatten = flatten(asInfixExpression());
      assertThat(flatten(flatten).toString(), is(flatten.toString()));
    }
    @Override @Test public void inputIsInfixExpression() {
      assertNotNull(asInfixExpression());
    }
    @Test public void sortTwice() {
      final List<Expression> operands = Extract.operands(flatten(asInfixExpression()));
      ExpressionComparator.ADDITION.sort(operands);
      assertFalse(ExpressionComparator.ADDITION.sort(operands));
    }
    @Test public void twoOrMoreArguments() {
      assertThat(Extract.operands(asInfixExpression()).size(), greaterThanOrEqualTo(2));
    }
  }
}
