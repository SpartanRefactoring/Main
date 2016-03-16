package il.org.spartan.refactoring.wring;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.hamcrest.OrderingComparison.greaterThanOrEqualTo;
import static il.org.spartan.refactoring.utils.Funcs.right;
import static il.org.spartan.refactoring.utils.Restructure.flatten;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static il.org.spartan.refactoring.utils.Into.e;
import static il.org.spartan.refactoring.utils.Into.i;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import il.org.spartan.refactoring.wring.AbstractWringTest.Noneligible;

import il.org.spartan.refactoring.spartanizations.Spartanization;
import il.org.spartan.refactoring.utils.ExpressionComparator;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.LiteralParser;
import il.org.spartan.refactoring.utils.LiteralParser.Types;
import il.org.spartan.refactoring.wring.InfixSortMultiplication;
import il.org.spartan.refactoring.wring.Toolbox;
import il.org.spartan.refactoring.wring.Wring;
import il.org.spartan.refactoring.wring.Wrings;
import il.org.spartan.utils.Utils;

/**
 * Unit tests for {@link Wrings#MULTIPLCATION_SORTER}.
 *
 * @author Yossi Gil
 * @since 2014-07-13
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "static-method" }) //
public class InfixSortMultiplicationTest extends AbstractWringTest<InfixExpression> {
  static final InfixSortMultiplication WRING = new InfixSortMultiplication();
  static final ExpressionComparator COMPARATOR = ExpressionComparator.MULTIPLICATION;
  public InfixSortMultiplicationTest() {
    super(WRING);
  }
  @Test public void legibleOnShorterChainParenthesisComparisonLast() {
    assertLegible("z * 2 * a * b * c * d * e * f * g * h");
  }
  @Test public void oneMultiplication0() {
    final InfixExpression e = i("f(a,b,c,d) * f(a,b,c)");
    assertEquals("f(a,b,c)", right(e).toString());
    assertThat(inner.scopeIncludes(e), is(true));
    assertThat(inner.eligible(e), is(true));
    final Wring<InfixExpression> s = Toolbox.instance.find(e);
    assertThat(s, instanceOf(InfixSortMultiplication.class));
    assertNotNull(s);
    assertTrue(s.scopeIncludes(e));
    assertTrue(s.eligible(e));
    final ASTNode replacement = ((Wring.ReplaceCurrentNode<InfixExpression>) s).replacement(e);
    assertNotNull(replacement);
    assertEquals("f(a,b,c) * f(a,b,c,d)", replacement.toString());
  }
  @Test public void parseOfToken() {
    assertThat(new LiteralParser(e(" 2  ").toString()).type(), is(Types.INTEGER.ordinal()));
  }
  @Test public void scopeIncludesTrue1() {
    assertTrue(WRING.scopeIncludes(i("2*a")));
  }
  @Test public void scopeIncludesTrue2() {
    assertTrue(WRING.scopeIncludes(i("a*2")));
  }

  @RunWith(Parameterized.class) //
  public static class Noneligible extends AbstractWringTest.Noneligible.Infix {
    static String[][] cases = Utils.asArray(//
        new String[] { "Plain product of two, sorted", "2*a" }, //
        new String[] { "Plain product of two, no order", "a*b" }, //
        new String[] { "Plain product of three, sorted", "2*a*b" }, //
        new String[] { "Plain product of four, sorted", "2*a*b*c" }, //
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
    /** Instantiates the enclosing class ({@link Noneligible}) */
    public Noneligible() {
      super(WRING);
    }
    @Override @Test public void inputIsInfixExpression() {
      assertNotNull(asInfixExpression());
    }
    @Test public void isTimes() {
      assertTrue(asInfixExpression().getOperator() == Operator.TIMES);
    }
    @Test public void sortTest() {
      assertFalse(COMPARATOR.sort(Extract.operands(flatten(asInfixExpression()))));
    }
    @Test public void sortTwice() {
      final InfixExpression e = asInfixExpression();
      final List<Expression> operands = Extract.operands(flatten(e));
      assertFalse(COMPARATOR.sort(operands));
      assertFalse(COMPARATOR.sort(operands));
    }
    @Test public void twoOrMoreArguments() {
      assertThat(Extract.operands(asInfixExpression()).size(), greaterThanOrEqualTo(2));
    }
  }

  @RunWith(Parameterized.class) //
  @FixMethodOrder(MethodSorters.NAME_ASCENDING) //
  public static class Wringed extends AbstractWringTest.WringedExpression.Infix {
    private static String[][] cases = Utils.asArray(//
        new String[] { "Constant first", "a*2", "2*a" }, //
        new String[] { "Constant first two arguments", "a*2*b", "2*a*b" }, //
        new String[] { "Function with fewer arguments first", "f(a,b,c)*f(a,b)*f(a)", "f(a)*f(a,b)*f(a,b,c)" }, //
        new String[] { "Literals of distinct length", "123*12*1", "1*12*123" }, //
        new String[] { "Sort expressions by size", "1*f(a,b,c,d) * 2*f(a,b) * 3*f()", "1*2*3*f()*f(a,b)*f(a,b,c,d)" }, //
        new String[] { "Long alphabetical sorting", "f(t)*g(h1,h2)*y*a*2*b*x", "2*a*b*x*y*f(t)*g(h1,h2)" }, //
        new String[] { "Plain alphabetical sorting", "f(y)*f(x)", "f(x)*f(y)" }, //
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
    /**
     * Instantiates the enclosing class ({@link WringedExpression})
     */
    public Wringed() {
      super(WRING);
    }
    @Override @Test public void flattenIsIdempotentt() {
      final InfixExpression flatten = flatten(asInfixExpression());
      assertThat(flatten(flatten).toString(), is(flatten.toString()));
    }
    @Override @Test public void inputIsInfixExpression() {
      assertNotNull(asInfixExpression());
    }
    @Test public void isTimes() {
      assertTrue(asInfixExpression().getOperator() == Operator.TIMES);
    }
    @Test public void sortTest() {
      final InfixExpression e = asInfixExpression();
      final List<Expression> operands = Extract.operands(flatten(e));
      assertThat("Before: " + Extract.operands(flatten(e)) + "\n" + "After: " + operands + "\n", COMPARATOR.sort(operands), is(true));
    }
    @Test public void sortTwice() {
      final InfixExpression e = asInfixExpression();
      final List<Expression> operands = Extract.operands(flatten(e));
      assertTrue(COMPARATOR.sort(operands));
      assertFalse(COMPARATOR.sort(operands));
    }
    @Test public void twoOrMoreArguments() {
      assertThat(Extract.operands(asInfixExpression()).size(), greaterThanOrEqualTo(2));
    }
  }
}
