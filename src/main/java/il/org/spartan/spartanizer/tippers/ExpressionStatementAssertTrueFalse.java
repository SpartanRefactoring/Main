package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace <code>assertTrue(X)</code> by <code>assert X;</code>
 * @author Yossi Gil
 * @since 2016/12/11 */
public final class ExpressionStatementAssertTrueFalse extends ReplaceCurrentNode<ExpressionStatement> implements TipperCategory.Idiomatic {
  @Override public String description(final ExpressionStatement ¢) {
    return "Rewrite '" + ¢ + "' as assert command";
  }

  @Override public ASTNode replacement(final ExpressionStatement ¢) {
    return replacement(az.methodInvocation(expression(¢)));
  }

  private static ASTNode replacement(MethodInvocation i) {
    final List<Expression> es = arguments(i);
    return es.size() > 2 || es.isEmpty() ? null : replacement(i, first(es), second(es));
  }

  public static ASTNode replacement(MethodInvocation i, final Expression first, final Expression second) {
    final Expression message = second == null ? null : first;
    final Expression condition = second == null ? first : second;
    final AssertStatement $ = i.getAST().newAssertStatement();
    if (message != null)
      $.setMessage(duplicate.of(message));
     return replacement(i, condition, $);
  }

  public static ASTNode replacement(MethodInvocation i, final Expression condition, final AssertStatement $) {
    switch (name(i) + "") {
      default:
        return null;
      case "assertTrue":
        return setAssert($, duplicate.of(condition));
      case "assertFalse":
        return setAssert($, make.notOf(condition));
      case "assertNotNull": 
        return setAssert($, subject.operands(condition, make.makeNullLiteral(i)).to(NOT_EQUALS));
    }
  }

  public static AssertStatement setAssert(AssertStatement $, Expression x) {
    $.setExpression(x);
    return $;
  }
}
