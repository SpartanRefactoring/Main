package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** Test class for {@link ExpressionComparator}
 * @author Yossi Gil author Assaf Lustig author Dan Abramovich author Arthur
 *         Spozhnikov
 * @since 2015-07-17 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "javadoc", "static-method" }) public final class ExpressionComparatorTest {
  @Test public void alphabeticalCompare() {
    azzert.that(ExpressionComparator.alphabeticalCompare(e("1+2"), e("6+7")), lessThan(0));
  }
  @Test public void characterCompare() {
    azzert.that(ExpressionComparator.characterCompare(e("1+2"), e("6+7")), is(0));
  }
  @Test public void countStatementsDoLoop() {
    azzert.that(cs("do { f(); g();} while( i++);"), is(11));
  }
  @Test public void countStatementsDoLoopNoBlock() {
    azzert.that(cs("do f();  while( i++);"), is(7));
  }
  @Test public void countStatementsDoLoopTrimmed() {
    azzert.that(cs("do  f();  while( i++);"), is(7));
  }
  @Test public void countStatementsDoLoopTrimmedInBlock() {
    azzert.that(cs("do { f(); } while( i++);"), is(7));
  }
  @Test public void countStatementsFor() {
    azzert.that(cs("for (;;) i++;"), is(7));
  }
  @Test public void countStatementsForEnahnced() {
    azzert.that(cs("for (int x : f()) i++;"), is(7));
  }
  @Test public void countStatementsIfPlain() {
    azzert.that(cs("if (f()) g();"), is(7));
  }
  @Test public void countStatementsIfWithElse() {
    azzert.that(cs("if (f()) g(); else h();"), is(11));
  }
  @Test public void countStatementsIfWithElseAndEmptyStatments() {
    azzert.that(cs("if (f()) {;;;g();{}} else h();"), is(11));
  }
  @Test public void countStatementsIfWithElseManyMoreEmptyStatments() {
    azzert.that(cs("if (f()) {;;;g();{}} else {{;;}; {} ; h();;;;}"), is(11));
  }
  @Test public void countStatementsIfWithEMptyBoth() {
    azzert.that(cs("if (f()) ; else ;"), is(5));
  }
  @Test public void countStatementsIfWithEMptyBothIsOk() {
    azzert.that(cs("if (f()) ; "), is(4));
  }
  @Test public void countStatementsIfWithEMptyElseStatement() {
    azzert.that(cs("if (f()) g(); else ;"), is(8));
  }
  @Test public void countStatementsPlain() {
    azzert.that(cs("i++;"), is(3));
  }
  @Test public void countStatementsWithBlock() {
    azzert.that(cs("for (;;) { i++; }"), is(7));
  }
  @Test public void literalAndClassConstant() {
    azzert.that(ExpressionComparator.ADDITION.compare(e("1"), e("BOB")), greaterThan(0));
  }
  @Test public void literalAndProductAddition() {
    azzert.that(ExpressionComparator.ADDITION.compare(e("1"), e("2*3")), greaterThan(0));
  }
  @Test public void literalAndProductMULITIPLICATION() {
    azzert.that(ExpressionComparator.MULTIPLICATION.compare(e("1"), e("2*3")), lessThan(0));
  }
  @Test public void literalCompare() {
    azzert.that(ExpressionComparator.literalCompare(e("1+2"), e("6+7")), is(0));
  }
  @Test public void longLiteralShortLiteralAddition() {
    azzert.that(ExpressionComparator.ADDITION.compare(e("1"), e("12")), lessThan(0));
  }
  @Test public void longLiteralShortLiteralMultiplication() {
    azzert.that(ExpressionComparator.MULTIPLICATION.compare(e("1"), e("12")), lessThan(0));
  }
  @Test public void nodesCompare() {
    azzert.that(ExpressionComparator.nodesCompare(e("1+2"), e("6+7")), is(0));
  }
  @Test public void twoClassConstants() {
    azzert.that(ExpressionComparator.ADDITION.compare(e("SPONGE"), e("BOB")), greaterThan(0));
  }
  @Test public void twoClassConstantsLongExpressionWithClassConstantsWithDigits() {
    azzert.that(ExpressionComparator.ADDITION.compare(e("f(a,b,c)"), e("ABC0")), lessThan(0));
  }
  @Test public void twoExpression() {
    azzert.that(ExpressionComparator.ADDITION.compare(e("1+2"), e("6+7")), lessThan(0));
  }
  @Test public void twoFunctionAddition() {
    azzert.that(ExpressionComparator.ADDITION.compare(e("f(a,b,c)"), e("f(a,b,c)")), is(0));
  }
  @Test public void twoFunctionMultiplication() {
    azzert.that(ExpressionComparator.MULTIPLICATION.compare(e("f(a,b,c)"), e("f(a,b,c)")), is(0));
  }
  @Test public void longerFirstTestTrue() {
    azzert.that(ExpressionComparator.longerFirst((InfixExpression) e("(1+3)+2")), is(true));
  }
  @Test public void longerFirstTestFalse() {
    azzert.that(ExpressionComparator.longerFirst((InfixExpression) e("1+(2+3)")), is(false));
  }
  @Test public void additionSortTest() {
    azzert.that(ExpressionComparator.ADDITION.sort((new ArrayList<Expression>() {
      static final long serialVersionUID = 1L;
      {
        add(e("a"));
        add(e("d+b"));
        add(e("a+b+c"));
        add(e("f"));
      }
    })), is(true));
  }
  @Test public void trialTest() {
    azzert.that(ExpressionComparator.PRUDENT.sort((new ArrayList<Expression>() {
      static final long serialVersionUID = 1L;
      {
        add(e("e"));
        add(e("d"));
        add(e("a"));
        add(e("f"));
      }
    })), is(false));
  }
  private int cs(final String statement) {
    return count.lines(s(statement));
  }
}
