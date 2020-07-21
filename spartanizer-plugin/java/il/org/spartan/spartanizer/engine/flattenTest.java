/* TODO Yossi Gil Document Class
 *
 * @author Yossi Gil
 *
 * @since Sep 11, 2016 */
package il.org.spartan.spartanizer.engine;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.engine.parse.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;

@SuppressWarnings({ "javadoc", "static-method" })
public final class flattenTest {
  @Test public void flattenExists() {
    flatten.of(i("1+2"));
  }
  @Test public void flattenIsDistinct() {
    final InfixExpression e = i("1+2");
    azzert.that(flatten.of(e), is(not(e)));
  }
  @Test public void flattenIsNonNull() {
    azzert.that(flatten.of(i("1+2")), is(not(nullValue())));
  }
  @Test public void flattenIsSame() {
    final InfixExpression e = i("1+2");
    azzert.that(flatten.of(e) + "", is(e + ""));
  }
  @Test public void flattenLeftArgument() {
    azzert.that(left(flatten.of(i("1+2"))) + "", is("1"));
  }
  @Test public void flattenOfDeepParenthesisIsCorrect() {
    azzert.that(flatten.of(i("(((1+2)))+(((3 + (4+5))))")) + "", is("1 + 2 + 3+ 4+ 5"));
  }
  @Test public void flattenOfDeepParenthesisSize() {
    azzert.that(flatten.of(i("(1+(2))+(3)")).extendedOperands().size(), is(1));
  }
  @Test public void flattenOfDeepParenthesOtherOperatorsisIsCorrect() {
    azzert.that(flatten.of(i("(((1+2)))+(((3 + (4*5))))")) + "", is("1 + 2 + 3+ 4 * 5"));
  }
  @Test public void flattenOfParenthesis() {
    azzert.that(flatten.of(i("1+2+(3)")).extendedOperands().size(), is(1));
  }
  @Test public void flattenOfTrivialDoesNotAddOperands() {
    azzert.that(i("1+2").extendedOperands().size(), is(0));
  }
  @Test public void hasExtendedOperands() {
    assert !i("1+2").hasExtendedOperands();
  }
  @Test public void isNotStringInfixFalse() {
    assert !type.isNotString(i("1+f"));
  }
  @Test public void isNotStringInfixPlain() {
    assert !type.isNotString(e("1+f"));
  }
  @Test public void leftOperandIsNotString() {
    assert type.isNotString(left(i("1+2")));
  }
  @Test public void leftOperandIsNumeric() {
    assert iz.numericLiteral(left(i("1+2")));
  }
  @Test public void leftOperandIsOne() {
    azzert.that(left(i("1+2")) + "", is("1"));
  }
  @Test public void leftOperandNonNull() {
    assert left(i("1+2")) != null;
  }
  @Test public void rightOperandNonNull() {
    assert right(i("1+2")) != null;
  }
}
