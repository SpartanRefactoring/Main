package il.org.spartan.spartanizer.tippers.junit;

import static il.org.spartan.spartanizer.ast.navigate.step.arguments;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.EQUALS;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.NOT_EQUALS;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.cons;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Replace {@code assertTrue(X)} by {@code assert X;}
 * @author Yossi Gil
 * @author Dor Ma'ayan
 * @since 2016/12/11 */
public final class ExpressionStatementAssertTrueFalse extends ReplaceCurrentNode<ExpressionStatement>//
    implements Category.Idiomatic {
  private static final long serialVersionUID = 0x30875C25D7D8CE7AL;

  @Override public String description(final ExpressionStatement ¢) {
    return "Rewrite '" + expression(¢) + "' as assert command";
  }
  @Override public ASTNode replacement(final ExpressionStatement ¢) {
    return replacement(az.methodInvocation(expression(¢)));
  }
  private static ASTNode replacement(final MethodInvocation ¢) {
    if (¢ == null)
      return null;
    final List<Expression> $ = arguments(¢);
    return replacement(¢, the.firstOf($), the.secondOf($));
  }
  public static ASTNode replacement(final MethodInvocation i, final Expression first, final Expression second) {
    final Expression message = second == null ? null : first, ret = second == null ? first : second;
    final AssertStatement $ = i.getAST().newAssertStatement();
    if (message != null)
      $.setMessage(copy.of(message));
    return replacement(i, ret, $);
  }
  private static ASTNode replacement(final MethodInvocation i, final Expression condition, final AssertStatement $) {
    switch (name(i) + "") {
      case "assertFalse":
        return setAssert($, cons.not(condition));
      case "assertTrue":
        return setAssert($, copy.of(condition));
      case "assertNotNull":
        InfixExpression e = i.getAST().newInfixExpression();
        e.setOperator(NOT_EQUALS);
        e.setRightOperand(i.getAST().newNullLiteral());
        e.setLeftOperand(copy.of(condition));
        return setAssert($, copy.of(e));
      case "assertNull":
        InfixExpression e1 = i.getAST().newInfixExpression();
        e1.setOperator(EQUALS);
        e1.setRightOperand(i.getAST().newNullLiteral());
        e1.setLeftOperand(copy.of(condition));
        return setAssert($, copy.of(e1));
      default:
        return null;
    }
  }
  private static AssertStatement setAssert(final AssertStatement $, final Expression x) {
    $.setExpression(x);
    return $;
  }
}
