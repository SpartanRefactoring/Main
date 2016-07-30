package il.org.spartan.refactoring.wring;

import static il.org.spartan.hamcrest.CoreMatchers.*;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.hamcrest.OrderingComparison.*;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;
import static il.org.spartan.refactoring.utils.Restructure.*;
import static il.org.spartan.utils.Utils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.utils.*;

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
    final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(wrap);
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
