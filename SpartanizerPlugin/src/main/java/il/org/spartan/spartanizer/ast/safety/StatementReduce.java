package il.org.spartan.spartanizer.ast.safety;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.utils.*;

/** Applies bottom up reduction of the statements tree
 * @author Yossi Gil
 * @since 2017-01-29 */
public abstract class StatementReduce<T> {
  protected T map(final ArrayAccess ¢) {
    return reduce(map(¢.getArray()), map(¢.getIndex()));
  }

  protected T map(final ArrayCreation ¢) {
    return reduce(reduce(dimensions(¢)), map(¢.getInitializer()));
  }

  private T reduce(final Iterable<Expression> xs) {
    T $ = neutralElement();
    for (final Expression ¢ : xs)
      $ = reduce($, map(¢));
    return $;
  }

  protected T map(final AssertStatement ¢) {
    return mapAtomic(¢);
  }

  protected T map(final Assignment ¢) {
    return reduce(map(to(¢)), map(from(¢)));
  }

  protected T map(final Block b) {
    T $ = neutralElement();
    for (final Statement ¢ : statements(b))
      $ = reduce($, map(¢));
    return $;
  }

  protected T map(final BreakStatement ¢) {
    return mapAtomic(¢);
  }

  protected T map(final ClassInstanceCreation ¢) {
    return reduce(map(¢.getExpression()), reduce(arguments(¢)));
  }

  protected T map(final ConditionalExpression ¢) {
    return reduce(map(¢.getExpression()), map(then(¢)), map(elze(¢)));
  }

  protected T map(final ConstructorInvocation ¢) {
    return mapAtomic(¢);
  }

  protected T map(final ContinueStatement ¢) {
    return mapAtomic(¢);
  }

  protected T map(final DoStatement ¢) {
    return map(¢.getBody());
  }

  protected T map(final EmptyStatement ¢) {
    return mapAtomic(¢);
  }

  protected T map(final EnhancedForStatement ¢) {
    return map(¢.getBody());
  }

  T map(final Expression ¢) {
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

  protected T map(final InstanceofExpression ¢) {
    return map(¢.getLeftOperand());
  }

  protected T map(final ExpressionStatement ¢) {
    return mapAtomic(¢);
  }

  protected T map(final IfStatement ¢) {
    return reduceIfStatement(expression(¢), then(¢), elze(¢));
  }

  protected T map(final LabeledStatement ¢) {
    return map(¢.getBody());
  }

  protected T map(final MethodInvocation ¢) {
    return reduce(map(expression(¢)), reduce(arguments(¢)));
  }

  protected T map(final PostfixExpression ¢) {
    return map(expression(¢));
  }

  protected T map(final PrefixExpression ¢) {
    return map(expression(¢));
  }

  protected T map(final ReturnStatement ¢) {
    return mapAtomic(¢);
  }

  public final T map(final Statement ¢) {
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
        assert fault.unreachable() : fault.specifically(String.format("Missing 'case' in switch for class: %s", wizard.nodeName(¢)));
        return null;
    }
  }

  protected T map(final SuperConstructorInvocation ¢) {
    return reduce(map(expression(¢)), reduce(arguments(¢)));
  }

  protected T map(final SuperMethodInvocation ¢) {
    return reduce(map(expression(¢)), reduce(arguments(¢)));
  }

  protected T mapAtomic(final Statement i) {
    final Object[] ____ = { i };
    forget.em(____);
    return neutralElement();
  }

  protected T neutralElement() {
    return null;
  }

  protected abstract T reduce(T t1, T t2);

  @SafeVarargs protected final T reduce(final T t1, final T t2, final T... ts) {
    T $ = reduce(t1, t2);
    for (final T ¢ : ts)
      $ = reduce($, ¢);
    return $;
  }

  protected T reduceIfStatement(final Expression x, final Statement then, final Statement elze) {
    return reduce(map(x), reduce(map(then), map(elze)));
  }
}
