package il.org.spartan.spartanizer.ast.safety;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO Yossi Gil: document class {@link }
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-29 */
public abstract class StatementReduce<T> {
  @NotNull protected T map(@NotNull final ArrayAccess ¢) {
    return reduce(map(¢.getArray()), map(¢.getIndex()));
  }

  @NotNull protected T map(@NotNull final ArrayCreation ¢) {
    return reduce(reduce(dimensions(¢)), map(¢.getInitializer()));
  }

  @Nullable private T reduce(@NotNull final List<Expression> xs) {
    T $ = neutralElement();
    for (final Expression ¢ : xs)
      $ = reduce($, map(¢));
    return $;
  }

  @Nullable protected T map(final AssertStatement ¢) {
    return mapAtomic(¢);
  }

  @NotNull protected T map(final Assignment ¢) {
    return reduce(map(to(¢)), map(from(¢)));
  }

  @Nullable protected T map(final Block b) {
    T $ = neutralElement();
    for (final Statement ¢ : statements(b))
      $ = reduce($, map(¢));
    return $;
  }

  @Nullable protected T map(final BreakStatement ¢) {
    return mapAtomic(¢);
  }

  @NotNull protected T map(@NotNull final ClassInstanceCreation ¢) {
    return reduce(map(¢.getExpression()), reduce(arguments(¢)));
  }

  @NotNull protected T map(@NotNull final ConditionalExpression ¢) {
    return reduce(map(¢.getExpression()), map(then(¢)), map(elze(¢)));
  }

  @Nullable protected T map(final ConstructorInvocation ¢) {
    return mapAtomic(¢);
  }

  @Nullable protected T map(final ContinueStatement ¢) {
    return mapAtomic(¢);
  }

  @Nullable protected T map(@NotNull final DoStatement ¢) {
    return map(¢.getBody());
  }

  @Nullable protected T map(final EmptyStatement ¢) {
    return mapAtomic(¢);
  }

  @Nullable protected T map(@NotNull final EnhancedForStatement ¢) {
    return map(¢.getBody());
  }

  @Nullable T map(@NotNull final Expression ¢) {
    switch (¢.getNodeType()) {
      case PREFIX_EXPRESSION:
        return map((PrefixExpression) ¢);
      case INFIX_EXPRESSION:
        return reduce(extract.allOperands((InfixExpression) ¢));
      case CONDITIONAL_EXPRESSION:
        return map((ConditionalExpression) ¢);
      case INSTANCEOF_EXPRESSION:
        return map((InstanceofExpression) ¢);
      case ARRAY_ACCESS:
        return map((ArrayAccess) ¢);
      case PARENTHESIZED_EXPRESSION:
        return map(extract.core(¢));
      case ASSIGNMENT:
        return map((Assignment) ¢);
      case ARRAY_INITIALIZER:
        return map((ArrayAccess) ¢);
      case ARRAY_CREATION:
        return map((ArrayCreation) ¢);
      case CLASS_INSTANCE_CREATION:
        return map((ClassInstanceCreation) ¢);
      case POSTFIX_EXPRESSION:
        return map((PostfixExpression) ¢);
      case METHOD_INVOCATION:
        return map((MethodInvocation) ¢);
      case SUPER_METHOD_INVOCATION:
        return map((SuperMethodInvocation) ¢);
      default:
        return null;
    }
  }

  @Nullable protected T map(@NotNull final InstanceofExpression ¢) {
    return map(¢.getLeftOperand());
  }

  @Nullable protected T map(final ExpressionStatement ¢) {
    return mapAtomic(¢);
  }

  @NotNull protected T map(final IfStatement ¢) {
    return reduceIfStatement(expression(¢), then(¢), elze(¢));
  }

  @Nullable protected T map(@NotNull final LabeledStatement ¢) {
    return map(¢.getBody());
  }

  @NotNull protected T map(final MethodInvocation ¢) {
    return reduce(map(step.expression(¢)), reduce(step.arguments(¢)));
  }

  @Nullable protected T map(final PostfixExpression ¢) {
    return map(step.expression(¢));
  }

  @Nullable protected T map(final PrefixExpression ¢) {
    return map(step.expression(¢));
  }

  @Nullable protected T map(final ReturnStatement ¢) {
    return mapAtomic(¢);
  }

  public final T map(@NotNull final Statement ¢) {
    switch (¢.getNodeType()) {
      case ASSERT_STATEMENT:
        return map((AssertStatement) ¢);
      case BLOCK:
        return map((Block) ¢);
      case BREAK_STATEMENT:
        return map((BreakStatement) ¢);
      case CONSTRUCTOR_INVOCATION:
        return map((ConstructorInvocation) ¢);
      case CONTINUE_STATEMENT:
        return map((ContinueStatement) ¢);
      case DO_STATEMENT:
        return map((DoStatement) ¢);
      case EMPTY_STATEMENT:
        return map((EmptyStatement) ¢);
      case ENHANCED_FOR_STATEMENT:
        return map((EnhancedForStatement) ¢);
      case EXPRESSION_STATEMENT:
        return map((ExpressionStatement) ¢);
      case IF_STATEMENT:
        return map((IfStatement) ¢);
      case LABELED_STATEMENT:
        return map((LabeledStatement) ¢);
      case RETURN_STATEMENT:
        return map((ReturnStatement) ¢);
      case SUPER_CONSTRUCTOR_INVOCATION:
        return map((SuperConstructorInvocation) ¢);
      default:
        assert fault.unreachable() : fault.specifically(String.format("Missing 'case' in switch for class: %s", ¢.getClass().getSimpleName()));
        return null;
    }
  }

  @NotNull protected T map(final SuperConstructorInvocation ¢) {
    return reduce(map(step.expression(¢)), reduce(step.arguments(¢)));
  }

  @NotNull protected T map(final SuperMethodInvocation ¢) {
    return reduce(map(step.expression(¢)), reduce(step.arguments(¢)));
  }

  @Nullable protected T mapAtomic(final Statement i) {
    ___.______unused(i);
    return neutralElement();
  }

  @Nullable protected T neutralElement() {
    return null;
  }

  @NotNull protected abstract T reduce(T t1, T t2);

  @SafeVarargs @NotNull protected final T reduce(final T t1, final T t2, @NotNull final T... ts) {
    T $ = reduce(t1, t2);
    for (final T ¢ : ts)
      $ = reduce($, ¢);
    return $;
  }

  @NotNull protected T reduceIfStatement(@NotNull final Expression x, @NotNull final Statement then, @NotNull final Statement elze) {
    return reduce(map(x), reduce(map(then), map(elze)));
  }
}
