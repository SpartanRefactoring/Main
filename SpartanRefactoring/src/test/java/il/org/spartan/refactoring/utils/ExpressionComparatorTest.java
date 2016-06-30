package il.org.spartan.refactoring.utils;

import static il.org.spartan.SpartanAssert.*;
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
    assertThat(ExpressionComparator.alphabeticalCompare(e("1+2"), e("6+7")), lessThan(0));
  }
  @Test public void characterCompare() {
    assertThat(ExpressionComparator.characterCompare(e("1+2"), e("6+7")), is(0));
  }
  @Test public void countStatementsDoLoop() {
    assertThat(cs("do { f(); g();} while( i++);"), is(11));
  }
  @Test public void countStatementsDoLoopNoBlock() {
    assertThat(cs("do f();  while( i++);"), is(7));
  }
  @Test public void countStatementsDoLoopTrimmed() {
    assertThat(cs("do  f();  while( i++);"), is(7));
  }
  @Test public void countStatementsDoLoopTrimmedInBlock() {
    assertThat(cs("do { f(); } while( i++);"), is(7));
  }
  @Test public void countStatementsFor() {
    assertThat(cs("for (;;) i++;"), is(7));
  }
  @Test public void countStatementsForEnahnced() {
    assertThat(cs("for (int x : f()) i++;"), is(7));
  }
  @Test public void countStatementsIfPlain() {
    assertThat(cs("if (f()) g();"), is(7));
  }
  @Test public void countStatementsIfWithElse() {
    assertThat(cs("if (f()) g(); else h();"), is(11));
  }
  @Test public void countStatementsIfWithElseAndEmptyStatments() {
    assertThat(cs("if (f()) {;;;g();{}} else h();"), is(11));
  }
  @Test public void countStatementsIfWithElseManyMoreEmptyStatments() {
    assertThat(cs("if (f()) {;;;g();{}} else {{;;}; {} ; h();;;;}"), is(11));
  }
  @Test public void countStatementsIfWithEMptyBoth() {
    assertThat(cs("if (f()) ; else ;"), is(5));
  }
  @Test public void countStatementsIfWithEMptyBothIsOk() {
    assertThat(cs("if (f()) ; "), is(4));
  }
  @Test public void countStatementsIfWithEMptyElseStatement() {
    assertThat(cs("if (f()) g(); else ;"), is(8));
  }
  @Test public void countStatementsPlain() {
    assertThat(cs("i++;"), is(3));
  }
  @Test public void countStatementsWithBlock() {
    assertThat(cs("for (;;) { i++; }"), is(7));
  }
  @Test public void literalAndClassConstant() {
    assertThat(ExpressionComparator.ADDITION.compare(e("1"), e("BOB")), greaterThan(0));
  }
  @Test public void literalAndProductAddition() {
    assertThat(ExpressionComparator.ADDITION.compare(e("1"), e("2*3")), greaterThan(0));
  }
  @Test public void literalAndProductMULITIPLICATION() {
    assertThat(ExpressionComparator.MULTIPLICATION.compare(e("1"), e("2*3")), lessThan(0));
  }
  @Test public void literalCompare() {
    assertThat(ExpressionComparator.literalCompare(e("1+2"), e("6+7")), is(0));
  }
  @Test public void longLiteralShortLiteralAddition() {
    assertThat(ExpressionComparator.ADDITION.compare(e("1"), e("12")), lessThan(0));
  }
  @Test public void longLiteralShortLiteralMultiplication() {
    assertThat(ExpressionComparator.MULTIPLICATION.compare(e("1"), e("12")), lessThan(0));
  }
  @Test public void nodesCompare() {
    assertThat(ExpressionComparator.nodesCompare(e("1+2"), e("6+7")), is(0));
  }
  @Test public void twoClassConstants() {
    assertThat(ExpressionComparator.ADDITION.compare(e("SPONGE"), e("BOB")), greaterThan(0));
  }
  @Test public void twoClassConstantsLongExpressionWithClassConstantsWithDigits() {
    assertThat(ExpressionComparator.ADDITION.compare(e("f(a,b,c)"), e("ABC0")), lessThan(0));
  }
  @Test public void twoExpression() {
    assertThat(ExpressionComparator.ADDITION.compare(e("1+2"), e("6+7")), lessThan(0));
  }
  @Test public void twoFunctionAddition() {
    assertThat(ExpressionComparator.ADDITION.compare(e("f(a,b,c)"), e("f(a,b,c)")), is(0));
  }
  @Test public void twoFunctionMultiplication() {
    assertThat(ExpressionComparator.MULTIPLICATION.compare(e("f(a,b,c)"), e("f(a,b,c)")), is(0));
  }
  private int cs(final String statement) {
    return ExpressionComparator.lineCount(s(statement));
  }
}
