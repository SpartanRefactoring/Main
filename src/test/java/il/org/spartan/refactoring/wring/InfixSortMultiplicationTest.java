package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.azzert.is;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.Restructure.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import static il.org.spartan.refactoring.utils.Funcs.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.suggestions.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.utils.LiteralParser.*;
import il.org.spartan.refactoring.wring.AbstractWringTest.*;

/**
 * Unit tests for {@link Wrings#MULTIPLCATION_SORTER}.
 *
 * @author Yossi Gil
 * @since 2014-07-13
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
@SuppressWarnings({ "javadoc", "static-method" })//
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
    that(right(e).toString(), is("f(a,b,c)"));
    assert inner != null;
    that(inner.scopeIncludes(e), is(true));
    that(inner.eligible(e), is(true));
    final Wring<InfixExpression> s = Toolbox.instance.find(e);
    that(s, instanceOf(InfixSortMultiplication.class));
    that(s, notNullValue());
    that(s.scopeIncludes(e), is(true));
    that(s.eligible(e), is(true));
    final ASTNode replacement = ((Wring.ReplaceCurrentNode<InfixExpression>) s).replacement(e);
    that(replacement, notNullValue());
    that(replacement.toString(), is("f(a,b,c) * f(a,b,c,d)"));
  }
  @Test public void parseOfToken() {
    that(new LiteralParser(e(" 2  ").toString()).type(), is(Types.INTEGER.ordinal()));
  }
  @Test public void scopeIncludesTrue1() {
    that(WRING.scopeIncludes(i("2*a")), is(true));
  }
  @Test public void scopeIncludesTrue2() {
    that(WRING.scopeIncludes(i("a*2")), is(true));
  }

  @RunWith(Parameterized.class)//
  public static class Noneligible extends AbstractWringTest.Noneligible.Infix {
    static String[][] cases = as.array(//
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
    @Parameters(name = DESCRIPTION)//
    public static Collection<Object[]> cases() {
      return collect(cases);
    }
    static Document rewrite(final Spartanization s, final CompilationUnit u, final Document d) {
      try {
        s.createRewrite(u, null).rewriteAST(d, null).apply(d);
        return d;
      } catch (MalformedTreeException | IllegalArgumentException | BadLocationException e) {
        throw new RuntimeException(e);
      }
    }
    /** Instantiates the enclosing class ({@link Noneligible}) */
    public Noneligible() {
      super(WRING);
    }
    @Override @Test public void inputIsInfixExpression() {
      that(asInfixExpression(), notNullValue());
    }
    @Test public void isTimes() {
      that(asInfixExpression().getOperator() == Operator.TIMES, is(true));
    }
    @Test public void sortTest() {
      that(COMPARATOR.sort(extract.operands(flatten(asInfixExpression()))), is(false));
    }
    @Test public void sortTwice() {
      final InfixExpression e = asInfixExpression();
      final List<Expression> operands = extract.operands(flatten(e));
      that(COMPARATOR.sort(operands), is(false));
      that(COMPARATOR.sort(operands), is(false));
    }
    @Test public void twoOrMoreArguments() {
      that(extract.operands(asInfixExpression()).size(), greaterThanOrEqualTo(2));
    }
  }

  @RunWith(Parameterized.class)//
  @FixMethodOrder(MethodSorters.NAME_ASCENDING)//
  public static class Wringed extends AbstractWringTest.WringedExpression.Infix {
    private static String[][] cases = as.array(//
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
    @Parameters(name = DESCRIPTION)//
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
      that(flatten(flatten).toString(), is(flatten.toString()));
    }
    @Override @Test public void inputIsInfixExpression() {
      that(asInfixExpression(), notNullValue());
    }
    @Test public void isTimes() {
      that(asInfixExpression().getOperator() == Operator.TIMES, is(true));
    }
    @Test public void sortTest() {
      final InfixExpression e = asInfixExpression();
      final List<Expression> operands = extract.operands(flatten(e));
      that("Before: " + extract.operands(flatten(e)) + "\n" + "After: " + operands + "\n", COMPARATOR.sort(operands), is(true));
    }
    @Test public void sortTwice() {
      final InfixExpression e = asInfixExpression();
      final List<Expression> operands = extract.operands(flatten(e));
      that(COMPARATOR.sort(operands), is(true));
      that(COMPARATOR.sort(operands), is(false));
    }
    @Test public void twoOrMoreArguments() {
      that(extract.operands(asInfixExpression()).size(), greaterThanOrEqualTo(2));
    }
  }
}
