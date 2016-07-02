package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Restructure.*;
import il.org.spartan.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.AbstractWringTest.Noneligible;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
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
@SuppressWarnings({ "javadoc", "static-method" })//
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
public class InfixSortAdditionTest {
  static final Wring<InfixExpression> WRING = new InfixSortAddition();
  static final ExpressionComparator COMPARATOR = ExpressionComparator.ADDITION;

  @Test public void subjectOperandsWithParenthesis() {
    final Expression e = Into.e("(2 + a) * b");
    azzert.that(Is.notString(e), is(true));
    final InfixExpression plus = extract.firstPlus(e);
    azzert.that(Is.notString(plus), is(true));
    final List<Expression> operands = extract.operands(flatten(plus));
    that(operands.size(), is(2));
    final InfixExpression r = Subject.operands(operands).to(plus.getOperator());
    that(r, iz("2+a"));
    that(new InfixSortAddition().replacement(plus), iz("a+2"));
  }

  @RunWith(Parameterized.class)//
  public static class Noneligible extends AbstractWringTest.Noneligible.Infix {
    static String[][] cases = as.array(//
        new String[] { "Add 1", "2*a+1" }, //
        new String[] { "Add '1'", "2*a+'1'" }, //
        new String[] { "Add '\\0'", "3*a+'\\0'" }, //
        new String[] { "Plain addition", "5*a+b*c" }, //
        new String[] { "Plain addition plus constant", "5*a+b*c+12" }, //
        new String[] { "Literal addition", "2+3" }, //
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
    /** Instantiates the enclosing class ({@link Noneligible}) */
    public Noneligible() {
      super(WRING);
    }
    @Test public void allNotStringArgument() {
      azzert.that(are.notString(extract.operands(asInfixExpression())), is(true));
    }
    @Override @Test public void flattenIsIdempotentt() {
      final InfixExpression flatten = flatten(asInfixExpression());
      that(flatten(flatten).toString(), is(flatten.toString()));
    }
    @Override @Test public void inputIsInfixExpression() {
      that(asInfixExpression(), notNullValue());
    }
    @Test public void isPlus() {
      azzert.that(asInfixExpression().getOperator() == Operator.PLUS, is(true));
    }
    @Test public void sortTest() {
      that(COMPARATOR.sort(extract.operands(flatten(asInfixExpression()))), is(false));
    }
    @Test public void sortTwice() {
      final List<Expression> operands = extract.operands(flatten(asInfixExpression()));
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
        new String[] { "Add 1 to 2*3", "1+2*3", "2*3+1" }, //
        new String[] { "Add '1' to a*b", "'1'+a*b", "a*b+'1'" }, //
        new String[] { "Add '\\0' to a*.b", "'\0'+a*b", "a*b+'\0'" }, //
        new String[] { "Sort from first to last", "1 + a*b + b*c", "a*b+b*c+1" }, //
        new String[] { "Sort from second to last", "a*b + 2 + b*c", "a*b+b*c+2" }, //
        new String[] { "All literals at the end", "1 + a*b + 2 + b*c + 3 + d*e + 4", "a*b + b*c  + d*e + 1 + 2 + 3+4" }, //
        new String[] { "Add 1", "1+a*b", "a*b+1" }, //
        new String[] { "Add 1", "1+c*d", "c*d+1" }, //
        new String[] { "Literals of distinct length", "123+12+1", "1+12+123" }, //
        new String[] { "Sort expressions by size", "1*f(a,b,c,d) + 2*f(a,b) + 3*f()", "3*f() +2*f(a,b)+ 1*f(a,b,c,d)" }, //
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
    @Test public void allNotStringArgument() {
      azzert.that(are.notString(extract.operands(asInfixExpression())), is(true));
    }
    @Override @Test public void flattenIsIdempotentt() {
      final InfixExpression flatten = flatten(asInfixExpression());
      that(flatten(flatten).toString(), is(flatten.toString()));
    }
    @Override @Test public void inputIsInfixExpression() {
      that(asInfixExpression(), notNullValue());
    }
    @Test public void isPlus() {
      azzert.that(asInfixExpression().getOperator() == Operator.PLUS, is(true));
    }
    @Test public void notString() {
      for (final Expression e : extract.operands(flatten(asInfixExpression())))
        that(e.toString(), Is.notString(e), is(true));
    }
    @Test public void sortTest() {
      final InfixExpression e = asInfixExpression();
      final List<Expression> operands = extract.operands(flatten(e));
      that(operands.size(), greaterThanOrEqualTo(2));
      that(//
          "Before: " + extract.operands(flatten(e)) + "\n" + //
              "After: " + operands + "\n", //
          COMPARATOR.sort(operands), is(true));
    }
    @Test public void sortTwice() {
      final InfixExpression e = asInfixExpression();
      final List<Expression> operands = extract.operands(flatten(e));
      assertTrue(e.toString(), COMPARATOR.sort(operands));
      that(e.toString(), COMPARATOR.sort(operands), is(false));
    }
    @Test public void twoOrMoreArguments() {
      that(extract.operands(asInfixExpression()).size(), greaterThanOrEqualTo(2));
    }
  }
}
