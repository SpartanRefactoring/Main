package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Test class for class {@link iz}
 * @author Yossi Gil, Aviad Cohen, Noam Yefet
 * @since 2015-07-17 */
@SuppressWarnings({ "javadoc", "static-method" }) //
public final class izTest {
  private static final String EMPTY_STRING = "\"\"";

  @Test public void booleanLiteralFalseOnNull() {
    azzert.that(iz.booleanLiteral(e("null")), is(false));
  }
  @Test public void booleanLiteralFalseOnNumeric() {
    azzert.that(iz.booleanLiteral(e("12")), is(false));
  }
  @Test public void booleanLiteralFalseOnThis() {
    azzert.that(iz.booleanLiteral(e("this")), is(false));
  }
  @Test public void booleanLiteralTrueOnFalse() {
    azzert.that(iz.booleanLiteral(e("false")), is(true));
  }
  @Test public void booleanLiteralTrueOnTrue() {
    azzert.that(iz.booleanLiteral(e("true")), is(true));
  }
  @Test public void callIsSpecificTrue() {
    azzert.that(iz.constant(e("this")), is(true));
  }
  @Test public void canMakeExpression() {
    e("2+2");
  }
  @Test public void emptyStringLiteral0() {
    assert iz.emptyStringLiteral(e(EMPTY_STRING));
  }
  @Test public void emptyStringLiteral1() {
    assert iz.literal("", e(EMPTY_STRING));
  }
  @Test public void emptyStringLiteral2() {
    assert iz.literal(az.stringLiteral(e(EMPTY_STRING)), "");
  }
  @Test public void emptyStringLiteral3() {
    final StringLiteral ¢ = az.stringLiteral(e(EMPTY_STRING));
    assert ¢ != null && "".equals(¢.getLiteralValue());
  }
  @Test public void emptyStringLiteral4() {
    assert az.stringLiteral(e(EMPTY_STRING)) != null;
  }
  @Test public void emptyStringLiteral5() {
    assert "".equals(az.stringLiteral(e(EMPTY_STRING)).getLiteralValue());
  }
  @Test public void isConstantFalse() {
    azzert.that(iz.constant(e("a")), is(false));
  }
  @Test public void isDeMorganAND() {
    assert iz.deMorgan(CONDITIONAL_AND);
  }
  @Test public void isDeMorganGreater() {
    assert !iz.deMorgan(GREATER);
  }
  @Test public void isDeMorganGreaterEuals() {
    assert !iz.deMorgan(GREATER_EQUALS);
  }
  @Test public void isDeMorganOR() {
    assert iz.deMorgan(CONDITIONAL_OR);
  }
  @Test public void isNullFalse1() {
    azzert.that(iz.nullLiteral(e("this")), is(false));
  }
  @Test public void isNullFalse2() {
    azzert.that(iz.thisLiteral(e("this.a")), is(false));
  }
  @Test public void isNullTrue() {
    azzert.that(iz.nullLiteral(e("null")), is(true));
  }
  @Test public void isOneOf() {
    azzert.that(iz.nodeTypeIn(e("this"), new int[] { CHARACTER_LITERAL, NUMBER_LITERAL, NULL_LITERAL, THIS_EXPRESSION }), is(true));
  }
  @Test public void isThisFalse1() {
    azzert.that(iz.thisLiteral(e("null")), is(false));
  }
  @Test public void isThisFalse2() {
    azzert.that(iz.thisLiteral(e("this.a")), is(false));
  }
  @Test public void isThisTrue() {
    azzert.that(iz.thisLiteral(e("this")), is(true));
  }
  @Test public void negative0() {
    azzert.that(iz.negative(e("0")), is(false));
  }
  @Test public void negative1() {
    azzert.that(iz.negative(e("0")), is(false));
  }
  @Test public void negativeMinus1() {
    azzert.that(iz.negative(e("- 1")), is(true));
  }
  @Test public void negativeMinus2() {
    azzert.that(iz.negative(e("- 2")), is(true));
  }
  @Test public void negativeMinusA() {
    azzert.that(iz.negative(e("- a")), is(true));
  }
  @Test public void negativeNull() {
    azzert.that(iz.negative(e("null")), is(false));
  }
  @Test public void numericLiteralFalse1() {
    azzert.that(iz.numericLiteral(e("2*3")), is(false));
  }
  @Test public void numericLiteralFalse2() {
    azzert.that(iz.numericLiteral(e("2*3")), is(false));
  }
  @Test public void numericLiteralTrue() {
    azzert.that(iz.numericLiteral(e("1")), is(true));
  }
  @Test public void seriesA_3() {
    assert !iz.infixPlus(e("(i+j)"));
    assert iz.infixPlus(core(e("(i+j)")));
    assert !iz.infixMinus(e("(i-j)"));
    assert iz.infixMinus(core(e("(i-j)")));
  }
  @Test public void assignmentTest() {
    assert !iz.assignment(null);
    assert iz.assignment(findFirst.instanceOf(Assignment.class, wizard.ast("x = 5;")));
    assert iz.assignment(findFirst.instanceOf(Assignment.class, wizard.ast("int aa = 5; y = 0.2;")));
    assert !iz.assignment(wizard.ast("if (c == 5) return false;"));
    assert !iz.assignment(wizard.ast("while (true) { }"));
  }
  @Test public void astNodeTest() {
    assert !iz.astNode(null);
    assert !iz.astNode(Integer.valueOf(5));
    assert !iz.astNode(String.valueOf("AAA"));
    assert iz.astNode(wizard.ast("int x = 5;"));
  }
}