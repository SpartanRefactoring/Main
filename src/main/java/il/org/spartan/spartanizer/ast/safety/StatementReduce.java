package il.org.spartan.spartanizer.ast.safety;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** TODO Yossi Gil: document class {@link }
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-29 */
public abstract class StatementReduce<T> {
  protected T map(ArrayAccess ¢) {
    return reduce(map(¢.getArray()), map(¢.getIndex()));
  }

  protected T map(ArrayCreation ¢) {
    return reduce(reduce(dimensions(¢)), map(¢.getInitializer()));
  }

  private T reduce(List<Expression> xs) {
    T $ = neutralElement();
    for (Expression ¢ : xs)
      $ = reduce($, map(¢));
    return $;
  }

  protected T map(AssertStatement ¢) {
    return mapAtomic(¢);
  }

  protected T map(Assignment ¢) {
    return reduce(map(to(¢)), map(from(¢)));
  }

  protected T map(Block b) {
    T $ = neutralElement();
    for (Statement ¢ : statements(b))
      $ = reduce($, map(¢));
    return $;
  }

  protected T map(BreakStatement ¢) {
    return mapAtomic(¢);
  }

  protected T map(ClassInstanceCreation ¢) {
    return reduce(map(¢.getExpression()), reduce(arguments(¢)));
  }

  protected T map(ConditionalExpression ¢) {
    return reduce(map(¢.getExpression()), map(then(¢)), map(elze(¢)));
  }

  protected T map(ConstructorInvocation ¢) {
    return mapAtomic(¢);
  }

  protected T map(ContinueStatement ¢) {
    return mapAtomic(¢);
  }

  protected T map(DoStatement ¢) {
    return map(¢.getBody());
  }

  protected T map(EmptyStatement ¢) {
    return mapAtomic(¢);
  }

  protected T map(EnhancedForStatement ¢) {
    return map(¢.getBody());
  }

  T map(final Expression ¢) {
    switch (¢.getNodeType()) {
      case PREFIX_EXPRESSION:
        return map((PrefixExpression) (¢));
      case INFIX_EXPRESSION:
        return reduce(extract.allOperands((InfixExpression) (¢)));
      case CONDITIONAL_EXPRESSION:
        return map((ConditionalExpression) (¢));
      case INSTANCEOF_EXPRESSION:
        return map((InstanceofExpression) (¢));
      case ARRAY_ACCESS:
        return map((ArrayAccess) (¢));
      case PARENTHESIZED_EXPRESSION:
        return map(extract.core(¢));
      case ASSIGNMENT:
        return map((Assignment) (¢));
      case ARRAY_INITIALIZER:
        return map((ArrayAccess) (¢));
      case ARRAY_CREATION:
        return map((ArrayCreation) (¢));
      case CLASS_INSTANCE_CREATION:
        return map((ClassInstanceCreation) ¢);
      case POSTFIX_EXPRESSION:
        return map((PostfixExpression) ¢);
      case METHOD_INVOCATION:
        return map((MethodInvocation) (¢));
      case SUPER_METHOD_INVOCATION:
        return map((SuperMethodInvocation) (¢));
      default:
        return null;
    }
  }

  protected T map(InstanceofExpression ¢) {
    return map(¢.getLeftOperand());
  }

  protected T map(ExpressionStatement ¢) {
    return mapAtomic(¢);
  }

  protected T map(IfStatement ¢) {
    return reduceIfStatement(expression(¢), then(¢), elze(¢));
  }

  protected T map(LabeledStatement ¢) {
    return map(¢.getBody());
  }

  protected T map(MethodInvocation ¢) {
    return reduce(map(step.expression(¢)), reduce(step.arguments(¢)));
  }

  protected T map(PostfixExpression ¢) {
    return map(step.expression(¢));
  }

  protected T map(PrefixExpression ¢) {
    return map(step.expression(¢));
  }

  protected T map(ReturnStatement ¢) {
    return mapAtomic(¢);
  }

  public final T map(Statement ¢) {
    switch (¢.getNodeType()) {
      case ASSERT_STATEMENT:
        return map((AssertStatement) (¢));
      case BLOCK:
        return map((Block) (¢));
      case BREAK_STATEMENT:
        return map((BreakStatement) (¢));
      case CONSTRUCTOR_INVOCATION:
        return map((ConstructorInvocation) (¢));
      case CONTINUE_STATEMENT:
        return map((ContinueStatement) (¢));
      case DO_STATEMENT:
        return map((DoStatement) (¢));
      case EMPTY_STATEMENT:
        return map((EmptyStatement) (¢));
      case ENHANCED_FOR_STATEMENT:
        return map((EnhancedForStatement) (¢));
      case EXPRESSION_STATEMENT:
        return map((ExpressionStatement) (¢));
      case IF_STATEMENT:
        return map((IfStatement) (¢));
      case LABELED_STATEMENT:
        return map((LabeledStatement) (¢));
      case RETURN_STATEMENT:
        return map((ReturnStatement) (¢));
      case SUPER_CONSTRUCTOR_INVOCATION:
        return map((SuperConstructorInvocation) (¢));
      default:
        assert fault.unreachable() : fault.specifically(String.format("Missing 'case' in switch for class: %s", ¢.getClass().getSimpleName()));
        return null;
    }
  }

  protected T map(SuperConstructorInvocation ¢) {
    return reduce(map(step.expression(¢)), reduce(step.arguments(¢)));
  }

  protected T map(SuperMethodInvocation ¢) {
    return reduce(map(step.expression(¢)), reduce(step.arguments(¢)));
  }

  protected T mapAtomic(Statement i) {
    ___.______unused(i);
    return neutralElement();
  }

  protected T neutralElement() {
    return null;
  }

  protected abstract T reduce(T t1, T t2);

  @SafeVarargs protected final T reduce(T t1, T t2, T... ts) {
    T $ = reduce(t1, t2);
    for (T ¢ : ts)
      $ = reduce($, ¢);
    return $;
  }

  protected T reduceIfStatement(Expression x, Statement then, Statement elze) {
    return reduce(map(x), reduce(map(then), map(elze)));
  }
}
