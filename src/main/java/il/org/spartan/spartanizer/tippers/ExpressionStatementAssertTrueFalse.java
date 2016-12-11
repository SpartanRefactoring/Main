package il.org.spartan.spartanizer.tippers;

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
    if (es.size() > 2 || es.isEmpty())
      return null;
    final Expression first = first(es);
    final Expression second = second(es);
    Expression condition = second == null ? first : second;
    switch (name(i) + "") {
      default:
        return null;
      case "assertFalse":
        condition = make.notOf(condition);
        //$FALL-THROUGH$
      case "assertTrue":
        condition = duplicate.of(condition);
        AssertStatement $ = i.getAST().newAssertStatement();
        $.setExpression(condition);
        Expression message = second == null ? null : first;
        if (message != null)
          $.setMessage(duplicate.of(message));
        return $;
    }
  }
}
