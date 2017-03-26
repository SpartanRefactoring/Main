package il.org.spartan.spartanizer.ast.safety;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.utils.*;

/** TODO Yossi Gil: document class {@link }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-29 */
public abstract class ExpressionMapReducer<T> extends StatementBottomUp<T> {
  @Override  public T map( final Expression ¢) {
    if (¢ == null)
      return reduce();
    switch (¢.getNodeType()) {
      case SIMPLE_NAME:
        return map((SimpleName) ¢);
      case QUALIFIED_NAME:
        return map((QualifiedName) ¢);
      case THIS_EXPRESSION:
        return map((ThisExpression) ¢);
      case NUMBER_LITERAL:
        return map((NumberLiteral) ¢);
      case NULL_LITERAL:
        return map((NullLiteral) ¢);
      case STRING_LITERAL:
        return map((StringLiteral) ¢);
      case PREFIX_EXPRESSION:
        return map((PrefixExpression) ¢);
      case INFIX_EXPRESSION:
        return reduceExpressions(extract.allOperands((InfixExpression) ¢));
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
        return map((ArrayInitializer) ¢);
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
      case BOOLEAN_LITERAL: 
        return map((BooleanLiteral) ¢);
      case CAST_EXPRESSION:
        return map((CastExpression) ¢);
      case LAMBDA_EXPRESSION:
        return map((LambdaExpression) ¢);
        
      default:
        assert fault.unreachable() : fault.specifically("Unrecognized type", ¢.getClass(), ¢,box.it(¢.getNodeType()));
        return null;
    }
  }

  private T map(LambdaExpression x) {
    return reduce();
  }

  private T map(CastExpression x) {
    return map(x.getExpression());
  }

   private T map(final ArrayInitializer ¢) {
    return reduceExpressions(expressions(¢));
  }

  protected T map(@SuppressWarnings("unused") final StringLiteral ¢) {
    return reduce();
  }

  protected T map(@SuppressWarnings("unused") final NullLiteral ¢) {
    return reduce();
  }

   protected T map( final ArrayAccess ¢) {
    return reduce(map(¢.getArray()), map(¢.getIndex()));
  }

   protected T map( final ArrayCreation ¢) {
    return reduce(reduceExpressions(dimensions(¢)), map(¢.getInitializer()));
  }

   protected T map(final Assignment ¢) {
    return reduce(map(to(¢)), map(from(¢)));
  }

  protected T map(@SuppressWarnings("unused") final BooleanLiteral ¢) {
    return reduce();
  }

   protected T map( final ClassInstanceCreation ¢) {
    return reduce(map(¢.getExpression()), reduceExpressions(arguments(¢)));
  }

   protected T map( final ConditionalExpression ¢) {
    return reduce(map(¢.getExpression()), map(then(¢)), map(elze(¢)));
  }

  protected T map( final InstanceofExpression ¢) {
    return map(¢.getLeftOperand());
  }

   protected T map(final MethodInvocation ¢) {
    return reduce(map(expression(¢)), reduceExpressions(arguments(¢)));
  }

  protected T map(@SuppressWarnings("unused") final NumberLiteral ¢) {
    return reduce();
  }

   protected T map(final PostfixExpression ¢) {
    return map(expression(¢));
  }

   protected T map(final PrefixExpression ¢) {
    return map(expression(¢));
  }

  protected T map( final ThisExpression ¢) {
    return map(¢.getQualifier());
  }

  protected T map(@SuppressWarnings("unused") final SimpleName ¢) {
    return reduce();
  }

   protected T map( final QualifiedName ¢) {
    return reduce(map(¢.getQualifier()), map(¢.getName()));
  }
}