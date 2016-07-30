package il.org.spartan.refactoring.wring;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.hamcrest.OrderingComparison.greaterThanOrEqualTo;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.assertNoChange;
import static il.org.spartan.refactoring.utils.Funcs.flip;
import static il.org.spartan.refactoring.utils.Funcs.left;
import static il.org.spartan.refactoring.utils.Into.i;
import static il.org.spartan.refactoring.utils.Restructure.flatten;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import il.org.spartan.refactoring.utils.ExpressionComparator;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Funcs;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.MakeAST;
import il.org.spartan.refactoring.wring.AbstractWringTest.Noneligible;
import il.org.spartan.refactoring.wring.AbstractWringTest.OutOfScope;
import il.org.spartan.utils.Utils;

/**
 * Unit tests for {@link Wrings#ADDITION_SORTER}.
 *
 * @author Yossi Gil
 * @since 2014-07-13
 */
@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class InfixComparisonSpecificTest extends AbstractWringTest<InfixExpression> {
  static final InfixComparisonSpecific WRING = new InfixComparisonSpecific();
  /** Instantiates this class */
  public InfixComparisonSpecificTest() {
    super(WRING);
  }
  @Test public void comparisonWithSpecific0z0() {
    assertWithinScope("this != a");
  }
  @Test public void comparisonWithSpecific0z1() {
    assertLegible("this != a");
  }
  @Test public void comparisonWithSpecificNoChange() {
    assertNoChange("a != this");
    assertNoChange("a != null");
    assertNoChange("a == this");
    assertNoChange("a == null");
    assertNoChange("a <= this");
    assertNoChange("a <= null");
    assertNoChange("a >= this");
    assertNoChange("a >= null");
  }
  @Test public void comparisonWithSpecificNoChangeWithLongEpxressions() {
    assertNoChange("very(complicate,func,-ction,call) != this");
    assertNoChange("very(complicate,func,-ction,call) != null");
    assertNoChange("very(complicate,func,-ction,call) == this");
    assertNoChange("very(complicate,func,-ction,call) == null");
    assertNoChange("very(complicate,func,-ction,call) <= this");
    assertNoChange("very(complicate,func,-ction,call) <= null");
    assertNoChange("very(complicate,func,-ction,call) >= this");
    assertNoChange("very(complicate,func,-ction,call) >= null");
  }
  @Test public void comparisonWithSpecificWithinScope() {
    assertTrue(Is.constant(left(i("this != a"))));
    final ASTNode n = MakeAST.EXPRESSION.from("a != this");
    assertThat(n, notNullValue());
    assertWithinScope(Funcs.asExpression(n));
    correctScopeExpression(n);
  }
  @Test public void comparisonWithSpecificWithinScope1() {
    final InfixExpression e = i("this != a");
    assertTrue(Is.constant(left(e)));
    assertTrue(inner.scopeIncludes(e));
    assertLegible(e.toString());
  }
  @Test public void comparisonWithSpecificWithinScope2() {
    assertWithinScope("this != a");
  }
  @Test public void scopeIncludesFalse1() {
    assertFalse(WRING.scopeIncludes(i("13455643294 * 22")));
  }
  @Test public void scopeIncludesFalse1expanded() {
    final InfixExpression e = i("13455643294 * 22");
    assertTrue(!e.hasExtendedOperands());
    assertFalse(Is.comparison(e));
  }
  @Test public void scopeIncludesFalse2() {
    assertFalse(WRING.scopeIncludes(i("1 + 2 < 3 & 7 + 4 > 2 + 1 || 6 - 7 < 2 + 1")));
  }
  @Test public void scopeIncludesFalse3() {
    assertFalse(WRING.scopeIncludes(i("1 + 2 < 3 & 7 + 4 > 2 + 1")));
  }
  @Test public void scopeIncludesFalse4() {
    assertFalse(WRING.scopeIncludes(i(" 6 - 7 < 2 + 1   ")));
  }
  @Test public void scopeIncludesFalse6() {
    assertTrue(WRING.scopeIncludes(i("1 < 102333")));
  }
  @Test public void scopeIncludesFalse7() {
    assertFalse(WRING.scopeIncludes(i("1 + 2 < 3 & 7 + 4 > 2 + 1 || 6 - 7 < 2 + 1")));
  }
  @Test public void scopeIncludesFalse8() {
    assertFalse(WRING.scopeIncludes(i("1 + 2 < 3 & 7 + 4 > 2 + 1")));
  }
  @Test public void scopeIncludesFalse9() {
    assertFalse(WRING.scopeIncludes(i(" 6 - 7 < 2 + 1   ")));
  }
  @Test public void scopeIncludesTrue1() {
    assertTrue(WRING.scopeIncludes(i("a == this")));
  }
  @Test public void scopeIncludesTrue2() {
    assertTrue(WRING.scopeIncludes(i("this == null")));
  }
  @Test public void scopeIncludesTrue3() {
    assertTrue(WRING.scopeIncludes(i("12 == this")));
  }
  @Test public void scopeIncludesTrue4() {
    assertTrue(WRING.scopeIncludes(i("a == 11")));
  }
  @Test public void scopeIncludesTrue5() {
    assertTrue(WRING.scopeIncludes(i("13455643294 < 22")));
  }
  @Test public void scopeIncludesTrue7() {
    assertTrue(WRING.scopeIncludes(i("1 < 102333")));
  }
  @Test public void scopeIncludesTrue8() {
    assertTrue(WRING.scopeIncludes(i("13455643294 < 22")));
  }

  @RunWith(Parameterized.class) //
  public static class Noneligible extends AbstractWringTest.Noneligible.Infix {
    static String[][] cases = Utils.asArray(//
        // Literal
        new String[] { "LT/literal", "a<2" }, //
        new String[] { "LE/literal", "a<=2" }, //
        new String[] { "GT/literal", "a>2" }, //
        new String[] { "GE/literal", "a>=2" }, //
        new String[] { "EQ/literal", "a==2" }, //
        new String[] { "NE/literal", "a!=2" }, //
        // This
        new String[] { "LT/this", "a<this" }, //
        new String[] { "LE/this", "a<=this" }, //
        new String[] { "GT/this", "a>this" }, //
        new String[] { "GE/this", "a>=this" }, //
        new String[] { "EQ/this", "a==this" }, //
        new String[] { "NE/this", "a!=this" }, //
        // Null
        new String[] { "LT/null", "a<null" }, //
        new String[] { "LE/null", "a<=null" }, //
        new String[] { "GT/null", "a>null" }, //
        new String[] { "GE/null", "a>=null" }, //
        new String[] { "EQ/null", "a==null" }, //
        new String[] { "NE/null", "a!=null" }, //
        // Character literal
        new String[] { "LT/character literal", "a<'a'" }, //
        new String[] { "LE/character literal", "a<='a'" }, //
        new String[] { "GT/character literal", "a>'a'" }, //
        new String[] { "GE/character literal", "a>='a'" }, //
        new String[] { "EQ/character literal", "a=='a'" }, //
        new String[] { "NE/character literal", "a!='a'" }, //
        // Misc
        new String[] { "Correct order", "1 + 2 < 3 " }, //
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
    /** Instantiates the enclosing class ({@link Noneligible}) */
    public Noneligible() {
      super(WRING);
    }
    @Override @Test public void flattenIsIdempotentt() {
      final InfixExpression flatten = flatten(asInfixExpression());
      assertThat(flatten(flatten).toString(), is(flatten.toString()));
    }
    @Override @Test public void inputIsInfixExpression() {
      assertNotNull(asInfixExpression());
    }
    @Test public void twoOrMoreArguments() {
      assertThat(Extract.operands(asInfixExpression()).size(), greaterThanOrEqualTo(2));
    }
  }

  @RunWith(Parameterized.class) //
  public static class OutOfScope extends AbstractWringTest.OutOfScope.Exprezzion.Infix {
    static String[][] cases = Utils.asArray(//
        new String[] { "Expression vs. Expression", " 6 - 7 < 2 + 1   " }, //
        new String[] { "Literal vs. Literal", "1 < 102333" }, //
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
    /** Instantiates the enclosing class ({@link OutOfScope}) */
    public OutOfScope() {
      super(WRING);
    }
  }

  @RunWith(Parameterized.class) //
  @FixMethodOrder(MethodSorters.NAME_ASCENDING) //
  public static class Wringed extends AbstractWringTest.WringedExpression.Infix {
    private static String[][] cases = Utils.asArray(//
        // Literal
        new String[] { "LT/literal", "2<a", "a>2" }, //
        new String[] { "LE/literal", "2<=a", "a>=2" }, //
        new String[] { "GT/literal", "2>a", "a<2" }, //
        new String[] { "GE/literal", "2>=a", "a<=2" }, //
        new String[] { "EQ/literal", "2==a", "a==2" }, //
        new String[] { "NE/literal", "2!=a", "a!=2" }, //
        // This
        new String[] { "LT/this", "this<a", "a>this" }, //
        new String[] { "LE/this", "this<=a", "a>=this" }, //
        new String[] { "GT/this", "this>a", "a<this" }, //
        new String[] { "GE/this", "this>=a", "a<=this" }, //
        new String[] { "EQ/this", "this==a", "a==this" }, //
        new String[] { "NE/this", "this!=a", "a!=this" }, //
        // Null
        new String[] { "LT/null", "null<a", "a>null" }, //
        new String[] { "LE/null", "null<=a", "a>=null" }, //
        new String[] { "GT/null", "null>a", "a<null" }, //
        new String[] { "GE/null", "null>=a", "a<=null" }, //
        new String[] { "EQ/null", "null==a", "a==null" }, //
        new String[] { "NE/null", "null!=a", "a!=null" }, //
        // Character literal
        new String[] { "LT/character literal", "'b'<a", "a>'b'" }, //
        new String[] { "LE/character literal", "'b'<=a", "a>='b'" }, //
        new String[] { "GT/character literal", "'b'>a", "a<'b'" }, //
        new String[] { "GE/character literal", "'b'>=a", "a<='b'" }, //
        new String[] { "EQ/character literal", "'b'==a", "a=='b'" }, //
        new String[] { "NE/character literal", "'b'!=a", "a!='b'" }, //
        // Misc
        new String[] { "Crazy comparison", "null == this", "this == null" }, //
        new String[] { "Crazy comparison", "null == 1", "1 == null" }, //
        new String[] { "Negative number", "-1 == a", "a == -1" }, //
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
    @Test public void flipIsNotNull() {
      assertNotNull(flip(asInfixExpression()));
    }
    @Override @Test public void inputIsInfixExpression() {
      assertNotNull(asInfixExpression());
    }
    @Test public void sortTwiceADDITION() {
      final InfixExpression e = asInfixExpression();
      final List<Expression> operands = Extract.operands(flatten(e));
      ExpressionComparator.ADDITION.sort(operands);
      assertFalse(ExpressionComparator.ADDITION.sort(operands));
    }
    @Test public void sortTwiceMULTIPLICATION() {
      final List<Expression> operands = Extract.operands(flatten(asInfixExpression()));
      ExpressionComparator.MULTIPLICATION.sort(operands);
      assertFalse(ExpressionComparator.MULTIPLICATION.sort(operands));
    }
    @Test public void twoOrMoreArguments() {
      assertThat(Extract.operands(asInfixExpression()).size(), greaterThanOrEqualTo(2));
    }
  }
}
