package il.org.spartan.spartanizer.ast.navigate;

import static fluent.ly.azzert.is;
import static il.org.spartan.spartanizer.ast.navigate.extract.core;
import static il.org.spartan.spartanizer.engine.parse.e;
import static org.eclipse.jdt.core.dom.ASTNode.CHARACTER_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.NULL_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.NUMBER_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.THIS_EXPRESSION;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_AND;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_OR;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.GREATER;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.GREATER_EQUALS;

import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;

/** Test class for class {@link iz}
 * @author Yossi Gil, Aviad Cohen, Noam Yefet
 * @since 2015-07-17 */
@SuppressWarnings({ "javadoc", "static-method", "ConstantConditions" }) //
public final class izTest {
  private static final String EMPTY_STRING = "\"\"";

  @Test public void arrayInitializerTest() {
    assert !iz.arrayInitializer(null);
    assert iz.arrayInitializer(findFirst.instanceOf(ArrayInitializer.class).in(make.ast("Integer arr[] = {2, 5, 8 };")));
    assert !iz.arrayInitializer(findFirst.instanceOf(ArrayInitializer.class).in(make.ast("Integer arr[];")));
  }
  @Test public void assignmentTest() {
    assert !iz.assignment(null);
    assert iz.assignment(findFirst.instanceOf(Assignment.class).in(make.ast("x = 5;")));
    assert iz.assignment(findFirst.instanceOf(Assignment.class).in(make.ast("int aa = 5; y = 0.2;")));
    assert !iz.assignment(make.ast("if (c == 5) return false;"));
    assert !iz.assignment(make.ast("while (true) { }"));
  }
  @Test public void astNodeTest() {
    assert !iz.astNode(null);
    assert !iz.astNode(Integer.valueOf(5));
    assert !iz.astNode("AAA");
    assert iz.astNode(make.ast("int x = 5;"));
  }
  @Test public void blockRequiredInReplacementNullTest() {
    assert !iz.blockRequiredInReplacement(null, null);
  }
  @Test public void bodyDeclarationTest() {
    assert !iz.bodyDeclaration(null);
    assert !iz.bodyDeclaration(findFirst.instanceOf(BodyDeclaration.class).in(make.ast("int x;")));
    assert iz.bodyDeclaration(findFirst.instanceOf(BodyDeclaration.class)
        .in(make.ast("public enum Day { SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY }")));
    assert iz.bodyDeclaration(findFirst.instanceOf(BodyDeclaration.class).in(make.ast("public static void main() { }")));
  }
  @Test public void booleanLiteralFalseOnNull() {
    assert !iz.booleanLiteral(e("null"));
  }
  @Test public void booleanLiteralFalseOnNumeric() {
    assert !iz.booleanLiteral(e("12"));
  }
  @Test public void booleanLiteralFalseOnThis() {
    assert !iz.booleanLiteral(e("this"));
  }
  @Test public void booleanLiteralTrueOnFalse() {
    assert iz.booleanLiteral(e("false"));
  }
  @Test public void booleanLiteralTrueOnTrue() {
    assert iz.booleanLiteral(e("true"));
  }
  @Test public void booleanOrNullLiteralTest() {
    assert !iz.booleanOrNullLiteral(null);
    assert !iz.booleanOrNullLiteral(findFirst.instanceOf(NullLiteral.class).in(make.ast("true")));
    assert iz.booleanOrNullLiteral(findFirst.instanceOf(BooleanLiteral.class).in(make.ast("true")));
    assert iz.booleanOrNullLiteral(findFirst.instanceOf(BooleanLiteral.class).in(make.ast("false")));
    assert !iz.booleanOrNullLiteral(findFirst.instanceOf(BooleanLiteral.class).in(make.ast("if (c == 5) return 5;")));
  }
  @Test public void callIsSpecificTrue() {
    assert iz.constant(e("this"));
  }
  @Test public void canMakeExpression() {
    e("2+2");
  }
  @Test public void castExpressionTest() {
    assert !iz.castExpression(null);
    assert iz.castExpression(findFirst.instanceOf(CastExpression.class).in(make.ast("int x = (Integer) y;")));
    assert !iz.castExpression(findFirst.instanceOf(CastExpression.class).in(make.ast("int x;")));
  }
  @Test public void comparisonTest() {
    final Expression e = null;
    assert !iz.comparison(e);
    assert iz.comparison(e("x==5"));
    assert iz.comparison(e("x!=5"));
    assert !iz.comparison(e("x + 5"));
  }
  @Test public void conditionalOrTest() {
    assert !iz.conditionalOr(null);
    assert !iz.conditionalOr(null);
    assert iz.conditionalOr(e("true || false"));
    assert !iz.conditionalOr(e("x!=5"));
  }
  @Test public void deMorganTest() {
    assert !iz.deMorgan((InfixExpression) null);
    assert !iz.deMorgan((Operator) null);
    assert iz.deMorgan(findFirst.instanceOf(InfixExpression.class).in(make.ast("true || false")));
    assert !iz.deMorgan(findFirst.instanceOf(InfixExpression.class).in(make.ast("(a == 5)")));
  }
  @Test public void emptyStatementTest() {
    assert !iz.emptyStatement(null);
    assert iz.emptyStatement(findFirst.instanceOf(EmptyStatement.class).in(make.ast(";")));
    assert !iz.emptyStatement(findFirst.instanceOf(Statement.class).in(make.ast("a = 5;")));
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
    assert ¢ != null && ¢.getLiteralValue() != null && ¢.getLiteralValue().isEmpty();
  }
  @Test public void emptyStringLiteral4() {
    assert az.stringLiteral(e(EMPTY_STRING)) != null;
  }
  @Test public void emptyStringLiteral5() {
    assert az.stringLiteral(e(EMPTY_STRING)).getLiteralValue() != null && az.stringLiteral(e(EMPTY_STRING)).getLiteralValue().isEmpty();
  }
  @Test public void final¢Test() {
    assert !iz.final¢((VariableDeclarationStatement) null);
    assert iz.final¢(findFirst.instanceOf(VariableDeclarationStatement.class).in(make.ast("final int a = 5;")));
    assert !iz.final¢(findFirst.instanceOf(VariableDeclarationStatement.class).in(make.ast("int a = 5;")));
  }
  @Test public void flipableTest() {
    assert !iz.flipable(null);
    assert iz.flipable(findFirst.instanceOf(InfixExpression.class).in(make.ast("a > 6")).getOperator());
    assert iz.flipable(findFirst.instanceOf(InfixExpression.class).in(make.ast("false & true")).getOperator());
    assert iz.flipable(findFirst.instanceOf(InfixExpression.class).in(make.ast("false | true")).getOperator());
    assert iz.flipable(findFirst.instanceOf(InfixExpression.class).in(make.ast("a == 6")).getOperator());
    assert iz.flipable(findFirst.instanceOf(InfixExpression.class).in(make.ast("a != 6")).getOperator());
    assert iz.flipable(findFirst.instanceOf(InfixExpression.class).in(make.ast("a >= 6")).getOperator());
    assert iz.flipable(findFirst.instanceOf(InfixExpression.class).in(make.ast("a <= 6")).getOperator());
    assert iz.flipable(findFirst.instanceOf(InfixExpression.class).in(make.ast("a < 6")).getOperator());
    assert !iz.flipable(findFirst.instanceOf(InfixExpression.class).in(make.ast("a - 8")).getOperator());
  }
  @Test public void isConstantFalse() {
    assert !iz.constant(e("a"));
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
    assert !iz.nullLiteral(e("this"));
  }
  @Test public void isNullFalse2() {
    assert !iz.thisLiteral(e("this.a"));
  }
  @Test public void isNullTrue() {
    assert iz.nullLiteral(e("null"));
  }
  @Test public void isOneOf() {
    assert iz.nodeTypeIn(e("this"), CHARACTER_LITERAL, NUMBER_LITERAL, NULL_LITERAL, THIS_EXPRESSION);
  }
  @Test public void isThisFalse1() {
    assert !iz.thisLiteral(e("null"));
  }
  @Test public void isThisFalse2() {
    assert !iz.thisLiteral(e("this.a"));
  }
  @Test public void isThisTrue() {
    assert iz.thisLiteral(e("this"));
  }
  @Test public void negative0() {
    assert !iz.negative(e("0"));
  }
  @Test public void negative1() {
    assert !iz.negative(e("0"));
  }
  @Test public void negativeMinus1() {
    assert iz.negative(e("- 1"));
  }
  @Test public void negativeMinus2() {
    assert iz.negative(e("- 2"));
  }
  @Test public void negativeMinusA() {
    assert iz.negative(e("- a"));
  }
  @Test public void negativeNull() {
    assert !iz.negative(e("null"));
  }
  @Test public void numericLiteralFalse1() {
    assert !iz.numericLiteral(e("2*3"));
  }
  @Test public void numericLiteralFalse2() {
    assert !iz.numericLiteral(e("2*3"));
  }
  @Test public void numericLiteralTrue() {
    assert iz.numericLiteral(e("1"));
  }
  @Test public void parseIntB1() {
    azzert.that(iz.parseInt("0b10101"), is(0b10101));
  }
  @Test public void parseIntB2() {
    azzert.that(iz.parseInt("-0b10101"), is(-0b10101));
  }
  @Test public void parseIntB3() {
    azzert.that(iz.parseInt("+0b10101"), is(0b10101));
  }
  @Test public void parseIntB4() {
    azzert.that(iz.parseInt("   0b10101    "), is(0b10101));
  }
  @Test public void parseIntB5() {
    azzert.that(iz.parseInt("0B10101"), is(0B10101));
  }
  @Test public void parseIntB6() {
    azzert.that(iz.parseInt("-0B10101"), is(-0B10101));
  }
  @Test public void parseIntB7() {
    azzert.that(iz.parseInt("+0B10101"), is(0B10101));
  }
  @Test public void parseIntB8() {
    azzert.that(iz.parseInt("   0B10101    "), is(0B10101));
  }
  @Test public void parseIntD1() {
    azzert.that(iz.parseInt("10101"), is(10101));
  }
  @Test public void parseIntD2() {
    azzert.that(iz.parseInt("-10101"), is(-10101));
  }
  @Test public void parseIntD3() {
    azzert.that(iz.parseInt("+10101"), is(10101));
  }
  @Test public void parseIntD4() {
    azzert.that(iz.parseInt("   10101    "), is(10101));
  }
  @Test public void parseIntO1() {
    azzert.that(iz.parseInt("010101"), is(010101));
  }
  @Test public void parseIntO2() {
    azzert.that(iz.parseInt("-010101"), is(-010101));
  }
  @Test public void parseIntO3() {
    azzert.that(iz.parseInt("+010101"), is(010101));
  }
  @Test public void parseIntO4() {
    azzert.that(iz.parseInt("   010101    "), is(010101));
  }
  @Test public void parseIntX1() {
    azzert.that(iz.parseInt("0x10101"), is(0x10101));
  }
  @Test public void parseIntX2() {
    azzert.that(iz.parseInt("-0x10101"), is(-0x10101));
  }
  @Test public void parseIntX3() {
    azzert.that(iz.parseInt("+0x10101"), is(0x10101));
  }
  @Test public void parseIntX4() {
    azzert.that(iz.parseInt("   0x10101    "), is(0x10101));
  }
  @Test public void parseIntX5() {
    azzert.that(iz.parseInt("0X10101"), is(0X10101));
  }
  @Test public void parseIntX6() {
    azzert.that(iz.parseInt("-0X10101"), is(-0X10101));
  }
  @Test public void parseIntX7() {
    azzert.that(iz.parseInt("+0X10101"), is(0X10101));
  }
  @Test public void parseIntX8() {
    azzert.that(iz.parseInt("   0X10101    "), is(0X10101));
  }
  @Test public void parseLongB1() {
    azzert.that(iz.parseLong("0b10101L"), is(0b10101L));
  }
  @Test public void parseLongB2() {
    azzert.that(iz.parseLong("-0b10101L"), is(-0b10101L));
  }
  @Test public void parseLongB3() {
    azzert.that(iz.parseLong("+0b10101L"), is(0b10101L));
  }
  @Test public void parseLongB4() {
    azzert.that(iz.parseLong("   0b10101L    "), is(0b10101L));
  }
  @Test public void parseLongB5() {
    azzert.that(iz.parseLong("0B10101L"), is(0B10101L));
  }
  @Test public void parseLongB6() {
    azzert.that(iz.parseLong("-0B10101L"), is(-0B10101L));
  }
  @Test public void parseLongB7() {
    azzert.that(iz.parseLong("+0B10101L"), is(0B10101L));
  }
  @Test public void parseLongB8() {
    azzert.that(iz.parseLong("   0B10101L    "), is(0B10101L));
  }
  @Test public void parseLongD1() {
    azzert.that(iz.parseLong("10101L"), is(10101L));
  }
  @Test public void parseLongD2() {
    azzert.that(iz.parseLong("-10101L"), is(-10101L));
  }
  @Test public void parseLongD3() {
    azzert.that(iz.parseLong("+10101L"), is(10101L));
  }
  @Test public void parseLongD4() {
    azzert.that(iz.parseLong("   10101L    "), is(10101L));
  }
  @Test public void parseLongO1() {
    azzert.that(iz.parseLong("010101"), is(010101L));
  }
  @Test public void parseLongO2() {
    azzert.that(iz.parseLong("-010101L"), is(-010101L));
  }
  @Test public void parseLongO3() {
    azzert.that(iz.parseLong("+010101l"), is(010101L));
  }
  @Test public void parseLongO4() {
    azzert.that(iz.parseLong("   010101L    "), is(010101L));
  }
  @Test public void parseLongX1() {
    azzert.that(iz.parseLong("0x10101L"), is(0x10101L));
  }
  @Test public void parseLongX2() {
    azzert.that(iz.parseLong("-0x10101L"), is(-0x10101L));
  }
  @Test public void parseLongX3() {
    azzert.that(iz.parseLong("+0x10101L"), is(0x10101L));
  }
  @Test public void parseLongX4() {
    azzert.that(iz.parseLong("   0x10101L    "), is(0x10101L));
  }
  @Test public void parseLongX5() {
    azzert.that(iz.parseLong("0X10101L"), is(0X10101L));
  }
  @Test public void parseLongX6() {
    azzert.that(iz.parseLong("-0X10101L"), is(-0X10101L));
  }
  @Test public void parseLongX7() {
    azzert.that(iz.parseLong("+0X10101L"), is(0X10101L));
  }
  @Test public void parseLongX8() {
    azzert.that(iz.parseLong("   0X10101L    "), is(0X10101L));
  }
  @Test public void plainAssignmentTest() {
    assert !iz.plainAssignment(null);
    assert iz.plainAssignment(findFirst.instanceOf(Assignment.class).in(make.ast("a = 5;")));
    assert !iz.plainAssignment(findFirst.instanceOf(Assignment.class).in(make.ast("a++;")));
  }
  @Test public void postfixExpressionTest() {
    assert !iz.postfixExpression(null);
    assert iz.postfixExpression(findFirst.instanceOf(PostfixExpression.class).in(make.ast("i++;")));
    assert !iz.postfixExpression(findFirst.instanceOf(PostfixExpression.class).in(make.ast("public static void main() { }")));
  }
  @Test public void rightOfAssignmentTest() {
    assert !iz.rightOfAssignment(null);
  }
  @Test public void seriesA_3() {
    assert !iz.infixPlus(e("(i+j)"));
    assert iz.infixPlus(core(e("(i+j)")));
    assert !iz.infixMinus(e("(i-j)"));
    assert iz.infixMinus(core(e("(i-j)")));
  }
  @Test public void singletonStatementTest() {
    assert !iz.singletonStatement(null);
    assert iz.singletonStatement(findFirst.instanceOf(Statement.class).in(make.ast("i = 6;")));
    assert !iz.singletonStatement(findFirst.instanceOf(Statement.class).in(make.ast("i = 6; j = 9;")));
  }
  @Test public void singletonThenTest() {
    assert !iz.singletonThen(null);
    assert iz.singletonThen(findFirst.instanceOf(IfStatement.class).in(make.ast("if (true) { i = 6; }")));
    assert !iz.singletonThen(findFirst.instanceOf(IfStatement.class).in(make.ast("if (true) { i = 6; j = 9; }")));
  }
  @Test public void singleVariableDeclarationTest() {
    assert !iz.singleVariableDeclaration(null);
    assert iz.singleVariableDeclaration(findFirst.instanceOf(SingleVariableDeclaration.class).in(make.ast("try {} catch (Exception e){}")));
    assert !iz.singleVariableDeclaration(findFirst.instanceOf(Statement.class).in(make.ast("Integer a; String b;")));
  }
  @Test public void stringLiteralTest() {
    assert !iz.stringLiteral(null);
    assert iz.stringLiteral(findFirst.instanceOf(StringLiteral.class).in(make.ast("\"5\"")));
    assert !iz.stringLiteral(findFirst.instanceOf(StringLiteral.class).in(make.ast("false")));
  }
  @Test public void thisOrNullTest() {
    assert !iz.thisOrNull(null);
    assert iz.thisOrNull(e("null"));
    assert iz.thisOrNull(e("this"));
    assert !iz.thisOrNull(e("i+5"));
  }
}
