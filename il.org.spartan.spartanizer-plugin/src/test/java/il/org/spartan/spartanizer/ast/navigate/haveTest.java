package il.org.spartan.spartanizer.ast.navigate;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.engine.*;

/** Tests for {@link have} , regarding issue #807
 * @author Kfir Marx
 * @authoe Raviv Rachmiel
 * @since 10-11-2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class haveTest {
  @Test public void booleanFalseLiteralTestFalse() {
    assert !have.falseLiteral(ExpressionListMaker("true"));
  }
  @Test public void booleanFalseLiteralTestTrue() {
    assert have.falseLiteral(ExpressionListMaker("false"));
  }
  @Test public void booleanLiteralOneExpressionFail() {
    assert !have.booleanLiteral(into.e("x=y"));
  }
  @Test public void booleanLiteralOneExpretionSuccess() {
    assert have.booleanLiteral(into.e("true"));
  }
  @Test public void booleanLiteralSeveralExpretionFail() {
    assert !have.booleanLiteral(into.e("x=y"), into.e("a=5+6"));
  }
  @Test public void booleanLiteralSeveralExpretionSuccess() {
    assert have.booleanLiteral(into.e("a=5+6"), into.e("false"));
  }
  @Test public void booleanLiteralTestFalse() {
    assert !have.booleanLiteral(ExpressionListMaker("1==1", "2==2"));
  }
  @Test public void booleanLiteralTestTrue() {
    assert have.booleanLiteral(ExpressionListMaker("1==1", "2==2", "true"));
  }
  @Test public void booleanTrueLiteralTestFalse() {
    assert !have.trueLiteral(ExpressionListMaker("false"));
  }
  @Test public void booleanTrueLiteralTestTrue() {
    assert have.trueLiteral(ExpressionListMaker("true"));
  }
  public Collection<Expression> ExpressionListMaker(final String... exps) {
    return Stream.of(exps).map(into::e).collect(toList());
  }
  @Test public void hasLiteralTestFalse() {
    assert !have.literal(ExpressionListMaker("1==2"));
  }
  @Test public void hasLiteralTestTrue() {
    assert have.literal(ExpressionListMaker("2"));
  }
  @Test public void literalFailForNumericalLit2() {
    assert !have.numericLiteral(into.e("true"), into.e("7==7"), into.e("x"));
  }
  @Test public void literalFailOnAssignment() {
    assert !have.literal(into.e("x=y"));
  }
  @Test public void literalFailOnComparison() {
    assert !have.literal(into.e("value1 == value2"));
  }
  @Test public void literalSuccessForNumericalLit() {
    assert have.literal(into.e("5"));
  }
  @Test public void literalSuccessForNumericalLit2() {
    assert have.numericLiteral(into.e("5"), into.e("7"), into.e("7"));
  }
  @Test public void literalSucssessForString() {
    assert have.literal(into.e("\"java is the best!\""));
  }
  @Test public void numericLiteralFailOnBoolean() {
    assert !have.numericLiteral(into.e("false"));
  }
  @Test public void numericLiteralFailOnNotLiteral() {
    assert !have.numericLiteral(into.e("a=5+6"));
  }
  @Test public void numericLiteralFailOnString() {
    assert !have.numericLiteral(into.e("\"java is the best!\""));
  }
  @Test public void numericLiteralIterableFail() {
    assert !have.numericLiteral(into.es("a=5+6", "false", "\"a String\""));
  }
  @Test public void numericLiteralIterableSucces() {
    assert have.numericLiteral(into.es("a=5+6", "false", "\"a String\"", "3.14"));
  }
  @Test public void numericLiteralSuccessOnFloat() {
    assert have.numericLiteral(into.e("c"), into.e("12.99e-13"));
  }
  @Test public void numericLiteralSuccessOnInt() {
    assert have.numericLiteral(into.e("55555"), into.e("x=y"));
  }
}
