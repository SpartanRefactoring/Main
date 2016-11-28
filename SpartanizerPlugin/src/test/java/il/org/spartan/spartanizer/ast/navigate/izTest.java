package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
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

  @Test public void arrayInitializerTest() {
    assert !iz.arrayInitializer(null);
    assert iz.arrayInitializer(findFirst.instanceOf(ArrayInitializer.class, wizard.ast("Integer arr[] = {2, 5, 8 };")));
    assert !iz.arrayInitializer(findFirst.instanceOf(ArrayInitializer.class, wizard.ast("Integer arr[];")));
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

  @Test public void blockRequiredInReplacementNullTest() {
    assert !iz.blockRequiredInReplacement(null, null);
  }

  @Test public void bodyDeclarationTest() {
    assert !iz.bodyDeclaration(null);
    assert !iz.bodyDeclaration(findFirst.instanceOf(BodyDeclaration.class, wizard.ast("int x;")));
    assert iz.bodyDeclaration(findFirst.instanceOf(BodyDeclaration.class,
        wizard.ast("public enum Day { SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY }")));
    assert iz.bodyDeclaration(findFirst.instanceOf(BodyDeclaration.class, wizard.ast("public static void main() { }")));
  }

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

  @Test public void booleanOrNullLiteralTest() {
    assert !iz.booleanOrNullLiteral(null);
    assert !iz.booleanOrNullLiteral(findFirst.instanceOf(NullLiteral.class, wizard.ast("true")));
    assert iz.booleanOrNullLiteral(findFirst.instanceOf(BooleanLiteral.class, wizard.ast("true")));
    assert iz.booleanOrNullLiteral(findFirst.instanceOf(BooleanLiteral.class, wizard.ast("false")));
    assert !iz.booleanOrNullLiteral(findFirst.instanceOf(BooleanLiteral.class, wizard.ast("if (c == 5) return 5;")));
  }

  @Test public void callIsSpecificTrue() {
    azzert.that(iz.constant(e("this")), is(true));
  }

  @Test public void canMakeExpression() {
    e("2+2");
  }

  @Test public void castExpressionTest() {
    assert !iz.castExpression(null);
    assert iz.castExpression(findFirst.instanceOf(CastExpression.class, wizard.ast("int x = (Integer) y;")));
    assert !iz.castExpression(findFirst.instanceOf(CastExpression.class, wizard.ast("int x;")));
  }

  @Test public void comparisonTest() {
    final Expression e = null;
    assert !iz.comparison(e);
    assert iz.comparison(e("x==5"));
    assert iz.comparison(e("x!=5"));
    assert !iz.comparison(e("x + 5"));
  }

  @Test public void conditionalOrTest() {
    final Expression e1 = null;
    final InfixExpression e2 = null;
    assert !iz.conditionalOr(e1);
    assert !iz.conditionalOr(e2);
    assert iz.conditionalOr(e("true || false"));
    assert !iz.conditionalOr(e("x!=5"));
  }

  @Test public void deMorganTest() {
    assert !iz.deMorgan((InfixExpression) null);
    assert !iz.deMorgan((Operator) null);
    assert iz.deMorgan(findFirst.instanceOf(InfixExpression.class, wizard.ast("true || false")));
    assert !iz.deMorgan(findFirst.instanceOf(InfixExpression.class, wizard.ast("(a == 5)")));
  }

  @Test public void emptyStatementTest() {
    assert !iz.emptyStatement(null);
    assert iz.emptyStatement(findFirst.instanceOf(EmptyStatement.class, wizard.ast(";")));
    assert !iz.emptyStatement(findFirst.instanceOf(Statement.class, wizard.ast("a = 5;")));
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

  @Test public void final¢Test() {
    assert !iz.final¢((VariableDeclarationStatement) null);
    assert iz.final¢(findFirst.instanceOf(VariableDeclarationStatement.class, wizard.ast("final int a = 5;")));
    assert !iz.final¢(findFirst.instanceOf(VariableDeclarationStatement.class, wizard.ast("int a = 5;")));
  }

  @Test public void flipableTest() {
    assert !iz.flipable(null);
    assert iz.flipable(findFirst.instanceOf(InfixExpression.class, wizard.ast("a > 6")).getOperator());
    assert iz.flipable(findFirst.instanceOf(InfixExpression.class, wizard.ast("false & true")).getOperator());
    assert iz.flipable(findFirst.instanceOf(InfixExpression.class, wizard.ast("false | true")).getOperator());
    assert iz.flipable(findFirst.instanceOf(InfixExpression.class, wizard.ast("a == 6")).getOperator());
    assert iz.flipable(findFirst.instanceOf(InfixExpression.class, wizard.ast("a != 6")).getOperator());
    assert iz.flipable(findFirst.instanceOf(InfixExpression.class, wizard.ast("a >= 6")).getOperator());
    assert iz.flipable(findFirst.instanceOf(InfixExpression.class, wizard.ast("a <= 6")).getOperator());
    assert iz.flipable(findFirst.instanceOf(InfixExpression.class, wizard.ast("a < 6")).getOperator());
    assert !iz.flipable(findFirst.instanceOf(InfixExpression.class, wizard.ast("a - 8")).getOperator());
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

  @Test public void plainAssignmentTest() {
    assert !iz.plainAssignment(null);
    assert iz.plainAssignment(findFirst.instanceOf(Assignment.class, wizard.ast("a = 5;")));
    assert !iz.plainAssignment(findFirst.instanceOf(Assignment.class, wizard.ast("a++;")));
  }

  @Test public void postfixExpressionTest() {
    assert !iz.postfixExpression(null);
    assert iz.postfixExpression(findFirst.instanceOf(PostfixExpression.class, wizard.ast("i++;")));
    assert !iz.postfixExpression(findFirst.instanceOf(PostfixExpression.class, wizard.ast("public static void main() { }")));
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
    assert iz.singletonStatement(findFirst.instanceOf(Statement.class, wizard.ast("i = 6;")));
    assert !iz.singletonStatement(findFirst.instanceOf(Statement.class, wizard.ast("i = 6; j = 9;")));
  }

  @Test public void singletonThenTest() {
    assert !iz.singletonThen(null);
    assert iz.singletonThen(findFirst.instanceOf(IfStatement.class, wizard.ast("if (true) { i = 6; }")));
    assert !iz.singletonThen(findFirst.instanceOf(IfStatement.class, wizard.ast("if (true) { i = 6; j = 9; }")));
  }

  @Test public void singleVariableDeclarationTest() {
    assert !iz.singleVariableDeclaration(null);
    assert iz.singleVariableDeclaration(findFirst.instanceOf(SingleVariableDeclaration.class, wizard.ast("try {} catch (Exception e){}")));
    assert !iz.singleVariableDeclaration(findFirst.instanceOf(Statement.class, wizard.ast("Integer a; String b;")));
  }

  @Test public void stringLiteralTest() {
    assert !iz.stringLiteral(null);
    assert iz.stringLiteral(findFirst.instanceOf(StringLiteral.class, wizard.ast("\"5\"")));
    assert !iz.stringLiteral(findFirst.instanceOf(StringLiteral.class, wizard.ast("false")));
  }

  @Test public void thisOrNullTest() {
    assert !iz.thisOrNull(null);
    assert iz.thisOrNull(e("null"));
    assert iz.thisOrNull(e("this"));
    assert !iz.thisOrNull(e("i+5"));
  }
}