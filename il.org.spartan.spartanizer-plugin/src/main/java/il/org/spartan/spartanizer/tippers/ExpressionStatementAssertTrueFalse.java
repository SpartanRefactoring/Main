package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** Replace {@code assertTrue(X)} by {@code assert X;}
 * @author Yossi Gil
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
        return setAssert($, make.notOf(condition));
      case "assertTrue":
        return setAssert($, copy.of(condition));
      case "assertNonNull":
      case "NonNull":
        return setAssert($, subject.operands(condition, make.nullLiteral(i)).to(NOT_EQUALS));
      default:
        return null;
    }
  }
  private static AssertStatement setAssert(final AssertStatement $, final Expression x) {
    $.setExpression(x);
    return $;
  }
}
