package il.org.spartan.spartanizer.ast.safety;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** Map-reduce on expression structure
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-29 */
public abstract class ExpressionBottomUp<R> extends StatementBottomUp<R> {
  protected R map(final ArrayAccess x) {
    return reduce(map(x.getArray()), map(x.getIndex()));
  }

  protected R map(final ArrayCreation x) {
    return reduce(reduceExpressions(dimensions(x)), map(x.getInitializer()));
  }

  protected R map(final ArrayInitializer x) {
    return reduceExpressions(expressions(x));
  }

  protected R map(final Assignment x) {
    return reduce(map(to(x)), map(from(x)));
  }

  protected R map(@SuppressWarnings("unused") final BooleanLiteral x) {
    return reduce();
  }

  protected R map(final CastExpression ¢) {
    return reduce(map(¢.getExpression()));
  }

  protected R map(@SuppressWarnings("unused") final CharacterLiteral x) {
    return reduce();
  }

  protected R map(final ClassInstanceCreation x) {
    return reduce(map(x.getExpression()), reduceExpressions(arguments(x)));
  }

  protected R map(final ConditionalExpression ¢) {
    return reduce(map(¢.getExpression()), map(then(¢)), map(elze(¢)));
  }

  protected R map(@SuppressWarnings("unused") final CreationReference x) {
    return reduce();
  }

  @Override public R map(final Expression ¢) {
    switch (¢.getNodeType()) {
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
        return reduce();
    }
  }

  protected R map(@SuppressWarnings("unused") final ExpressionMethodReference __) {
    return reduce();
  }

  protected R map(final FieldAccess x) {
    return reduce(map(x.getExpression()), map(x.getName()));
  }

  protected R map(final InfixExpression ¢) {
    return map(¢.getLeftOperand());
  }

  protected R map(final InstanceofExpression ¢) {
    return map(¢.getLeftOperand());
  }

  protected R map(@SuppressWarnings("unused") final LambdaExpression __) {
    return reduce(); 
  }

  protected R map(final MethodInvocation x) {
    return reduce(map(expression(x)), reduceExpressions(arguments(x)));
  }

  protected R map(@SuppressWarnings("unused") final MethodReference x) {
    return reduce();
  }

  protected R map(final Name x) {
    return map(x.isSimpleName() ? (SimpleName) x : (QualifiedName) x);
  }

  protected R map(@SuppressWarnings("unused") final NullLiteral x) {
    return reduce();
  }

  protected R map(@SuppressWarnings("unused") final NumberLiteral x) {
    return reduce();
  }

  protected R map(final ParenthesizedExpression ¢) {
    return map(¢.getExpression());
  }

  protected R map(final PostfixExpression ¢) {
    return map(expression(¢));
  }

  protected R map(final PrefixExpression ¢) {
    return map(expression(¢));
  }

  protected R map(final QualifiedName x) {
    return reduce(map(x.getName()), map(x.getQualifier()));
  }

  protected R map(@SuppressWarnings("unused") final SimpleName x) {
    return reduce();
  }

  protected R map(@SuppressWarnings("unused") final StringLiteral x) {
    return reduce();
  }

  protected R map(@SuppressWarnings("unused") final SuperFieldAccess x) {
    return reduce();
  }

  @Override protected R map(final SuperMethodInvocation x) {
    return map(x.getQualifier());
  }

  protected R map(@SuppressWarnings("unused") final SuperMethodReference x) {
    return reduce();
  }

  protected R map(final ThisExpression ¢) {
    return map(¢.getQualifier());
  }

  protected R map(@SuppressWarnings("unused") final TypeLiteral x) {
    return reduce();
  }

  protected R map(@SuppressWarnings("unused") final TypeMethodReference x) {
    return reduce();
  }

  @Override protected R map(@SuppressWarnings("unused") final VariableDeclarationExpression __) {
    return reduce();
  }
}
