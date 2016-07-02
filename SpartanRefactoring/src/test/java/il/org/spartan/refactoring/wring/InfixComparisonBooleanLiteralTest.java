package il.org.spartan.refactoring.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;
import static il.org.spartan.refactoring.utils.Restructure.*;
import il.org.spartan.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameters;

/**
 * Unit tests for {@link Wrings#ADDITION_SORTER}.
 *
 * @author Yossi Gil
 * @since 2014-07-13
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
@SuppressWarnings({ "javadoc", "static-method" })//
public class InfixComparisonBooleanLiteralTest extends AbstractWringTest<InfixExpression> {
  static final InfixComparisonBooleanLiteral WRING = new InfixComparisonBooleanLiteral();

  public InfixComparisonBooleanLiteralTest() {
    super(WRING);
  }
  @Test public void removeParenthesis() throws MalformedTreeException, BadLocationException {
    final String s = " (2) == true";
    final String wrap = Wrap.Expression.on(s);
    final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.from(wrap);
    that(u, notNullValue());
    final Document d = new Document(wrap);
    that(d, notNullValue());
    final Trimmer t = new Trimmer();
    final ASTRewrite r = t.createRewrite(u, null);
    final TextEdit x = r.rewriteAST(d, null);
    x.apply(d);
    final String unpeeled = d.get();
    assertThat("Nothing done on " + s, wrap, not(unpeeled));
    final String peeled = Wrap.Expression.off(unpeeled);
    assertThat("No similification of " + s, s, not(peeled));
    assertThat("Simpification of " + s + " is just reformatting", compressSpaces(peeled), not(compressSpaces(s)));
    assertSimilar(" 2 ", peeled);
  }

  @RunWith(Parameterized.class)//
  public static class WringedInput extends AbstractWringTest.WringedExpression.Infix {
    static String[][] cases = as.array(//
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
    @Parameters(name = DESCRIPTION)//
    public static Collection<Object[]> cases() {
      return collect(cases);
    }
    static Document rewrite(final Spartanization s, final CompilationUnit u, final Document d) {
      try {
        s.createRewrite(u, null).rewriteAST(d, null).apply(d);
        return d;
      } catch (MalformedTreeException | IllegalArgumentException | BadLocationException e) {
        e.printStackTrace();
        return null;
      }
    }
    public WringedInput() {
      super(WRING);
    }
    @Override @Test public void flattenIsIdempotentt() {
      final InfixExpression flatten = flatten(asInfixExpression());
      that(flatten(flatten).toString(), is(flatten.toString()));
    }
    @Override @Test public void inputIsInfixExpression() {
      that(asInfixExpression(), notNullValue());
    }
    @Test public void sortTwice() {
      final List<Expression> operands = extract.operands(flatten(asInfixExpression()));
      ExpressionComparator.ADDITION.sort(operands);
      that(ExpressionComparator.ADDITION.sort(operands), is(false));
    }
    @Test public void twoOrMoreArguments() {
      that(extract.operands(asInfixExpression()).size(), greaterThanOrEqualTo(2));
    }
  }
}
