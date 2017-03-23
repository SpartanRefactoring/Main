package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace {@code assertTrue(X)} by {@code assert X;}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016/12/11 */
public final class ExpressionStatementAssertTrueFalse extends ReplaceCurrentNode<ExpressionStatement>//
    implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = 3496864953281334906L;

  @Override @NotNull public String description(final ExpressionStatement ¢) {
    return "Rewrite '" + expression(¢) + "' as assert command";
  }

  @Override @Nullable public ASTNode replacement(final ExpressionStatement ¢) {
    return replacement(az.methodInvocation(expression(¢)));
  }

  @Nullable private static ASTNode replacement(@Nullable final MethodInvocation ¢) {
    if (¢ == null)
      return null;
    @NotNull final List<Expression> $ = arguments(¢);
    return replacement(¢, first($), second($));
  }

  @Nullable public static ASTNode replacement(@NotNull final MethodInvocation i, final Expression first, @Nullable final Expression second) {
    @Nullable final Expression message = second == null ? null : first, condition = second == null ? first : second;
    final AssertStatement $ = i.getAST().newAssertStatement();
    if (message != null)
      $.setMessage(copy.of(message));
    return replacement(i, condition, $);
  }

  private static ASTNode replacement(@NotNull final MethodInvocation i, @NotNull final Expression condition, @NotNull final AssertStatement $) {
    switch (name(i) + "") {
      case "assertFalse":
        return setAssert($, make.notOf(condition));
      case "assertTrue":
        return setAssert($, copy.of(condition));
      case "assertNotNull":
      case "notNull":
        return setAssert($, subject.operands(condition, make.makeNullLiteral(i)).to(NOT_EQUALS));
      default:
        return null;
    }
  }

  @NotNull private static AssertStatement setAssert(@NotNull final AssertStatement $, final Expression x) {
    $.setExpression(x);
    return $;
  }
}
