package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Into.*;

import org.junit.*;
import org.junit.runners.*;

/**
 * Test class for {@link ExpressionComparator}
 *
 * @author Yossi Gil
 * @since 2015-07-17
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
@SuppressWarnings({ "javadoc", "static-method" })//
public class ExpressionComparatorTest {
  @Test public void alphabeticalCompare() {
    that(ExpressionComparator.alphabeticalCompare(e("1+2"), e("6+7")), lessThan(0));
  }
  @Test public void characterCompare() {
    that(ExpressionComparator.characterCompare(e("1+2"), e("6+7")), is(0));
  }
  @Test public void countStatementsDoLoop() {
    that(cs("do { f(); g();} while( i++);"), is(11));
  }
  @Test public void countStatementsDoLoopNoBlock() {
    that(cs("do f();  while( i++);"), is(7));
  }
  @Test public void countStatementsDoLoopTrimmed() {
    that(cs("do  f();  while( i++);"), is(7));
  }
  @Test public void countStatementsDoLoopTrimmedInBlock() {
    that(cs("do { f(); } while( i++);"), is(7));
  }
  @Test public void countStatementsFor() {
    that(cs("for (;;) i++;"), is(7));
  }
  @Test public void countStatementsForEnahnced() {
    that(cs("for (int x : f()) i++;"), is(7));
  }
  @Test public void countStatementsIfPlain() {
    that(cs("if (f()) g();"), is(7));
  }
  @Test public void countStatementsIfWithElse() {
    that(cs("if (f()) g(); else h();"), is(11));
  }
  @Test public void countStatementsIfWithElseAndEmptyStatments() {
    that(cs("if (f()) {;;;g();{}} else h();"), is(11));
  }
  @Test public void countStatementsIfWithElseManyMoreEmptyStatments() {
    that(cs("if (f()) {;;;g();{}} else {{;;}; {} ; h();;;;}"), is(11));
  }
  @Test public void countStatementsIfWithEMptyBoth() {
    that(cs("if (f()) ; else ;"), is(5));
  }
  @Test public void countStatementsIfWithEMptyBothIsOk() {
    that(cs("if (f()) ; "), is(4));
  }
  @Test public void countStatementsIfWithEMptyElseStatement() {
    that(cs("if (f()) g(); else ;"), is(8));
  }
  @Test public void countStatementsPlain() {
    that(cs("i++;"), is(3));
  }
  @Test public void countStatementsWithBlock() {
    that(cs("for (;;) { i++; }"), is(7));
  }
  @Test public void literalAndClassConstant() {
    that(ExpressionComparator.ADDITION.compare(e("1"), e("BOB")), greaterThan(0));
  }
  @Test public void literalAndProductAddition() {
    that(ExpressionComparator.ADDITION.compare(e("1"), e("2*3")), greaterThan(0));
  }
  @Test public void literalAndProductMULITIPLICATION() {
    that(ExpressionComparator.MULTIPLICATION.compare(e("1"), e("2*3")), lessThan(0));
  }
  @Test public void literalCompare() {
    that(ExpressionComparator.literalCompare(e("1+2"), e("6+7")), is(0));
  }
  @Test public void longLiteralShortLiteralAddition() {
    that(ExpressionComparator.ADDITION.compare(e("1"), e("12")), lessThan(0));
  }
  @Test public void longLiteralShortLiteralMultiplication() {
    that(ExpressionComparator.MULTIPLICATION.compare(e("1"), e("12")), lessThan(0));
  }
  @Test public void nodesCompare() {
    that(ExpressionComparator.nodesCompare(e("1+2"), e("6+7")), is(0));
  }
  @Test public void twoClassConstants() {
    that(ExpressionComparator.ADDITION.compare(e("SPONGE"), e("BOB")), greaterThan(0));
  }
  @Test public void twoClassConstantsLongExpressionWithClassConstantsWithDigits() {
    that(ExpressionComparator.ADDITION.compare(e("f(a,b,c)"), e("ABC0")), lessThan(0));
  }
  @Test public void twoExpression() {
    that(ExpressionComparator.ADDITION.compare(e("1+2"), e("6+7")), lessThan(0));
  }
  @Test public void twoFunctionAddition() {
    that(ExpressionComparator.ADDITION.compare(e("f(a,b,c)"), e("f(a,b,c)")), is(0));
  }
  @Test public void twoFunctionMultiplication() {
    that(ExpressionComparator.MULTIPLICATION.compare(e("f(a,b,c)"), e("f(a,b,c)")), is(0));
  }
  private int cs(final String statement) {
    return ExpressionComparator.lineCount(s(statement));
  }
}
