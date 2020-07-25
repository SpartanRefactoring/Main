package il.org.spartan.spartanizer.ast.navigate;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.Expression;
import org.junit.Test;

import il.org.spartan.spartanizer.engine.parse;

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
    assert !have.booleanLiteral(parse.e("x=y"));
  }
  @Test public void booleanLiteralOneExpretionSuccess() {
    assert have.booleanLiteral(parse.e("true"));
  }
  @Test public void booleanLiteralSeveralExpretionFail() {
    assert !have.booleanLiteral(parse.e("x=y"), parse.e("a=5+6"));
  }
  @Test public void booleanLiteralSeveralExpretionSuccess() {
    assert have.booleanLiteral(parse.e("a=5+6"), parse.e("false"));
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
    return Stream.of(exps).map(parse::e).collect(toList());
  }
  @Test public void hasLiteralTestFalse() {
    assert !have.literal(ExpressionListMaker("1==2"));
  }
  @Test public void hasLiteralTestTrue() {
    assert have.literal(ExpressionListMaker("2"));
  }
  @Test public void literalFailForNumericalLit2() {
    assert !have.numericLiteral(parse.e("true"), parse.e("7==7"), parse.e("x"));
  }
  @Test public void literalFailOnAssignment() {
    assert !have.literal(parse.e("x=y"));
  }
  @Test public void literalFailOnComparison() {
    assert !have.literal(parse.e("value1 == value2"));
  }
  @Test public void literalSuccessForNumericalLit() {
    assert have.literal(parse.e("5"));
  }
  @Test public void literalSuccessForNumericalLit2() {
    assert have.numericLiteral(parse.e("5"), parse.e("7"), parse.e("7"));
  }
  @Test public void literalSucssessForString() {
    assert have.literal(parse.e("\"java is the best!\""));
  }
  @Test public void numericLiteralFailOnBoolean() {
    assert !have.numericLiteral(parse.e("false"));
  }
  @Test public void numericLiteralFailOnNotLiteral() {
    assert !have.numericLiteral(parse.e("a=5+6"));
  }
  @Test public void numericLiteralFailOnString() {
    assert !have.numericLiteral(parse.e("\"java is the best!\""));
  }
  @Test public void numericLiteralIterableFail() {
    assert !have.numericLiteral(parse.es("a=5+6", "false", "\"a String\""));
  }
  @Test public void numericLiteralIterableSucces() {
    assert have.numericLiteral(parse.es("a=5+6", "false", "\"a String\"", "3.14"));
  }
  @Test public void numericLiteralSuccessOnFloat() {
    assert have.numericLiteral(parse.e("c"), parse.e("12.99e-13"));
  }
  @Test public void numericLiteralSuccessOnInt() {
    assert have.numericLiteral(parse.e("55555"), parse.e("x=y"));
  }
}
