package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

/** Tests for {@link have}, regarding issue #807
 * @author Kfir Marx
 * @authoe Raviv Rachmiel
 * @since 10-11-2016 */
@SuppressWarnings({ "static-method", "javadoc" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class haveTest {
  public List<Expression> ExpressionListMaker(final String[] exps) {
    final List<Expression> $ = new LinkedList<>();
    for (final String e : exps)
      $.add(into.e(e));
    return $;
  }

  @Test public void booleanLiteralTestTrue() {
    azzert.assertTrue(have.booleanLiteral(ExpressionListMaker(new String[] { "1==1", "2==2", "true" })));
  }

  @Test public void booleanLiteralTestFalse() {
    azzert.assertFalse(have.booleanLiteral(ExpressionListMaker(new String[] { "1==1", "2==2" })));
  }

  @Test public void booleanFalseLiteralTestTrue() {
    azzert.assertTrue(have.falseLiteral(ExpressionListMaker(new String[] { "false" })));
  }

  @Test public void booleanFalseLiteralTestFalse() {
    azzert.assertFalse(have.falseLiteral(ExpressionListMaker(new String[] { "true" })));
  }

  @Test public void booleanTrueLiteralTestTrue() {
    azzert.assertTrue(have.trueLiteral(ExpressionListMaker(new String[] { "true" })));
  }

  @Test public void booleanTrueLiteralTestFalse() {
    azzert.assertFalse(have.trueLiteral(ExpressionListMaker(new String[] { "false" })));
  }

  @Test public void hasLiteralTestTrue() {
    azzert.assertTrue(have.literal(ExpressionListMaker(new String[] { "2" })));
  }

  @Test public void hasLiteralTestFalse() {
    azzert.assertFalse(have.literal(ExpressionListMaker(new String[] { "1==2" })));
  }

  @Test public void booleanLiteralOneExpretionSuccess() {
    azzert.assertTrue(have.booleanLiteral(into.e("true")));
  }

  @Test public void booleanLiteralOneExpressionFail() {
    azzert.assertFalse(have.booleanLiteral(into.e("x=y")));
  }

  @Test public void literalFailOnAssignment() {
    azzert.assertFalse(have.literal(into.e("x=y")));
  }

  @Test public void literalFailOnComparison() {
    azzert.assertFalse(have.literal(into.e("value1 == value2")));
  }

  @Test public void literalSuccessForNumericalLit() {
    azzert.assertTrue(have.literal(into.e("5")));
  }

  @Test public void literalSucssessForString() {
    azzert.assertTrue(have.literal(into.e("\"java is the best!\"")));
  }

  @Test public void literalSuccessForNumericalLit2() {
    azzert.assertTrue(have.numericLiteral(into.e("5"), into.e("7"), into.e("7")));
  }

  @Test public void literalFailForNumericalLit2() {
    azzert.assertFalse(have.numericLiteral(into.e("true"), into.e("7==7"), into.e("x")));
  }

  @Test public void booleanLiteralSeveralExpretionSuccess() {
    azzert.assertTrue(have.booleanLiteral(into.e("a=5+6"), into.e("false")));
  }

  @Test public void booleanLiteralSeveralExpretionFail() {
    azzert.assertFalse(have.booleanLiteral(into.e("x=y"), into.e("a=5+6")));
  }

  @Test public void numericLiteralFailOnString() {
    azzert.assertFalse(have.numericLiteral(into.e("\"java is the best!\"")));
  }

  @Test public void numericLiteralFailOnBoolean() {
    azzert.assertFalse(have.numericLiteral(into.e("false")));
  }

  @Test public void numericLiteralFailOnNotLiteral() {
    azzert.assertFalse(have.numericLiteral(into.e("a=5+6")));
  }

  @Test public void numericLiteralSuccessOnInt() {
    azzert.assertTrue(have.numericLiteral(into.e("55555"), into.e("x=y")));
  }

  @Test public void numericLiteralSuccessOnFloat() {
    azzert.assertTrue(have.numericLiteral(into.e("c"), into.e("12.99e-13")));
  }

  @Test public void numericLiteralIterableFail() {
    azzert.assertFalse(have.numericLiteral(into.es("a=5+6", "false", "\"a String\"")));
  }

  @Test public void numericLiteralIterableSucces() {
    azzert.assertTrue(have.numericLiteral(into.es("a=5+6", "false", "\"a String\"", "3.14")));
  }
}
