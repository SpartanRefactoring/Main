package il.org.spartan.spartanizer.ast;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum step {
  ;
  /** Expose the list of arguments in a {@link ClassInstanceCreation}
   * @param ¢ JD
   * @return reference to the list of arguments in the argument */
  @SuppressWarnings("unchecked") public static List<Expression> arguments(final ClassInstanceCreation ¢) {
    return ¢.arguments();
  }

  /** Expose the list of arguments in a {@link MethodInvocation}
   * @param ¢ JD
   * @return reference to the list of arguments in the argument */
  @SuppressWarnings("unchecked") public static List<Expression> arguments(final MethodInvocation ¢) {
    return ¢.arguments();
  }

  /** Expose the list of arguments in a {@link SuperMethodInvocation}
   * @param ¢ JD
   * @return reference to the list of arguments in the argument */
  @SuppressWarnings("unchecked") public static List<Expression> arguments(final SuperMethodInvocation ¢) {
    return ¢.arguments();
  }

  /** Expose the list of bodyDeclarations in an {@link AbstractTypeDeclaration}
   * @param ¢ JD
   * @return reference to the list of bodyDeclarations in the argument */
  @SuppressWarnings("unchecked") public static List<BodyDeclaration> bodyDeclarations(final AbstractTypeDeclaration ¢) {
    return ¢.bodyDeclarations();
  }

  /** Expose the list of bodyDeclarations in an
   * {@link AnonymousClassDeclaration}
   * @param ¢ JD
   * @return reference to the list of bodyDeclarations in the argument */
  @SuppressWarnings("unchecked") public static List<BodyDeclaration> bodyDeclarations(final AnonymousClassDeclaration ¢) {
    return ¢.bodyDeclarations();
  }

  /** Expose the list of catchClauses in a {@link TryStatement}
   * @param ¢ JD
   * @return reference to the list of catchClauses in the argument */
  @SuppressWarnings("unchecked") public static List<CatchClause> catchClauses(final TryStatement ¢) {
    return ¢.catchClauses();
  }

  /** Shorthand for {@link ConditionalExpression#getElseExpression()}
   * @param ¢ JD
   * @return else part of the parameter */
  public static Expression elze(final ConditionalExpression ¢) {
    return ¢.getElseExpression();
  }

  /** Shorthand for {@link IfStatement#getElseStatement}
   * @param ¢ JD
   * @return else statement of the parameter */
  public static Statement elze(final IfStatement ¢) {
    return ¢.getElseStatement();
  }

  /** @param n a node to extract an expression from
   * @return null if the statement is not an expression, nor a return statement,
   *         nor a throw statement. Otherwise, the expression in these. */
  public static Expression expression(final ASTNode ¢) {
    if (¢ == null)
      return null;
    switch (¢.getNodeType()) {
      case ASTNode.EXPRESSION_STATEMENT:
        return expression((ExpressionStatement) ¢);
      case ASTNode.RETURN_STATEMENT:
        return expression((ReturnStatement) ¢);
      case ASTNode.THROW_STATEMENT:
        return expression((ThrowStatement) ¢);
      case ASTNode.CLASS_INSTANCE_CREATION:
        return expression((ClassInstanceCreation) ¢);
      case ASTNode.CAST_EXPRESSION:
        return expression((CastExpression) ¢);
      case ASTNode.METHOD_INVOCATION:
        return step.receiver((MethodInvocation) ¢);
      case ASTNode.PARENTHESIZED_EXPRESSION:
        return expression((ParenthesizedExpression) ¢);
      case ASTNode.DO_STATEMENT:
        return expression((DoStatement) ¢);
      default:
        return null;
    }
  }

  public static Expression expression(final CastExpression $) {
    return extract.core($.getExpression());
  }

  public static Expression expression(final ClassInstanceCreation $) {
    return extract.core($.getExpression());
  }

  public static Expression expression(final ConditionalExpression ¢) {
    return extract.core(¢.getExpression());
  }

  public static Expression expression(final DoStatement $) {
    return extract.core($.getExpression());
  }

  public static Expression expression(final ExpressionStatement $) {
    return $ == null ? null : extract.core($.getExpression());
  }

  public static Expression expression(final ParenthesizedExpression $) {
    return extract.core($.getExpression());
  }

  public static Expression expression(final ReturnStatement $) {
    return extract.core($.getExpression());
  }

  public static Expression expression(final ThrowStatement $) {
    return extract.core($.getExpression());
  }

  @SuppressWarnings("unchecked") public static List<Expression> expressions(final ArrayInitializer ¢) {
    return ¢.expressions();
  }

  /** Expose the list of extended operands in an {@link InfixExpression}
   * @param ¢ JD
   * @return reference to the list of extended operands contained in the
   *         parameter */
  @SuppressWarnings("unchecked") public static List<Expression> extendedOperands(final InfixExpression ¢) {
    return ¢.extendedOperands();
  }

  /** Expose the list of fragments in a {@link FieldDeclaration}
   * @param ¢ JD
   * @return reference to the list of fragments in the argument */
  @SuppressWarnings("unchecked") public static List<VariableDeclarationFragment> fragments(final FieldDeclaration ¢) {
    return ¢.fragments();
  }

  /** Expose the list of fragments in a {@link VariableDeclarationExpression}
   * @param ¢ JD
   * @return reference to the list of fragments in the argument */
  @SuppressWarnings("unchecked") public static List<VariableDeclarationFragment> fragments(final VariableDeclarationExpression ¢) {
    return ¢ != null ? ¢.fragments() : new ArrayList<>();
  }

  /** Expose the list of fragments in a {@link VariableDeclarationStatement}
   * @param ¢ JD
   * @return reference to the list of fragments in the argument */
  @SuppressWarnings("unchecked") public static List<VariableDeclarationFragment> fragments(final VariableDeclarationStatement ¢) {
    return ¢.fragments();
  }

  /** Expose the list of initializers contained in a {@link ForStatement}
   * @param ¢ JD
   * @return reference to the list of initializers contained in the argument */
  @SuppressWarnings("unchecked") public static List<Expression> initializers(final ForStatement ¢) {
    return ¢.initializers();
  }

  /** Shorthand for {@link Assignment#getLeftHandSide()}
   * @param ¢ JD
   * @return left operand of the parameter */
  public static Expression to(final Assignment ¢) {
    return ¢.getLeftHandSide();
  }

  /** Shorthand for {@link InfixExpression#getLeftOperand()}
   * @param ¢ JD
   * @return left operand of the parameter */
  public static Expression left(final InfixExpression ¢) {
    return ¢.getLeftOperand();
  }

  /** Shorthand for {@link InstanceofExpression#getLeftOperand()}
   * @param ¢ JD
   * @return left operand of the parameter */
  public static Expression left(final InstanceofExpression ¢) {
    return ¢.getLeftOperand();
  }

  @SuppressWarnings("rawtypes") public static List<ASTNode> marchingList(final ASTNode ¢) {
    final List<ASTNode> $ = new ArrayList<>();
    final List lst = ¢.structuralPropertiesForType();
    for (final Object s : lst) {
      final Object child = ¢.getStructuralProperty((StructuralPropertyDescriptor) s);
      if (iz.astNode(child))
        $.add(az.astNode(child));
    }
    return $;
  }

  @SuppressWarnings("unchecked") public static List<IExtendedModifier> modifiers(final BodyDeclaration ¢) {
    return ¢.modifiers();
  }

  @SuppressWarnings("unchecked") public static List<IExtendedModifier> modifiers(final SingleVariableDeclaration ¢) {
    return ¢.modifiers();
  }

  @SuppressWarnings("unchecked") public static List<IExtendedModifier> modifiers(final VariableDeclarationStatement ¢) {
    return ¢.modifiers();
  }

  public static SimpleName name(final MethodInvocation ¢) {
    return ¢.getName();
  }

  public static SimpleName name(final SuperMethodInvocation ¢) {
    return ¢.getName();
  }

  public static Expression operand(final PostfixExpression ¢) {
    return ¢ == null ? null : extract.core(¢.getOperand());
  }

  public static Expression operand(final PrefixExpression ¢) {
    return ¢ == null ? null : extract.core(¢.getOperand());
  }

  public static Assignment.Operator operator(final Assignment ¢) {
    return ¢ == null ? null : ¢.getOperator();
  }

  public static InfixExpression.Operator operator(final InfixExpression ¢) {
    return ¢ == null ? null : ¢.getOperator();
  }

  public static PostfixExpression.Operator operator(final PostfixExpression ¢) {
    return ¢ == null ? null : ¢.getOperator();
  }

  public static PrefixExpression.Operator operator(final PrefixExpression ¢) {
    return ¢ == null ? null : ¢.getOperator();
  }

  /** Expose the list of parameters in a {@link MethodDeclaration}
   * @param ¢ JD
   * @return result of method {@link MethodDeclaration#parameters} downcasted to
   *         its correct type */
  @SuppressWarnings("unchecked") public static List<SingleVariableDeclaration> parameters(final MethodDeclaration ¢) {
    return ¢.parameters();
  }

  /** Shorthand for {@link ASTNode#getParent()}
   * @param ¢ JD
   * @return parent of the parameter */
  public static ASTNode parent(final ASTNode ¢) {
    return ¢.getParent();
  }

  public static Expression receiver(final MethodInvocation $) {
    return extract.core($.getExpression());
  }

  /** Expose the list of resources contained in a {@link TryStatement}
   * @param ¢ JD
   * @return reference to the list of resources contained in the argument */
  @SuppressWarnings("unchecked") public static List<VariableDeclarationExpression> resources(final TryStatement ¢) {
    return ¢.resources();
  }

  /** Shorthand for {@link Assignment#getRightHandSide()}
   * @param ¢ JD
   * @return left operand of the parameter */
  public static Expression from(final Assignment ¢) {
    return ¢.getRightHandSide();
  }

  /** Shorthand for {@link CastExpression#getExpression()}
   * @param ¢ JD
   * @return right operand of the parameter */
  public static Expression right(final CastExpression ¢) {
    return ¢.getExpression();
  }

  /** Shorthand for {@link InfixExpression#getRightOperand()}
   * @param ¢ JD
   * @return right operand of the parameter */
  public static Expression right(final InfixExpression ¢) {
    return ¢.getRightOperand();
  }

  /** Expose the list of statements contained in a {@link Block}
   * @param ¢ JD
   * @return reference to the list of statements contained in the argument */
  @SuppressWarnings("unchecked") public static List<Statement> statements(final Block ¢) {
    return ¢.statements();
  }

  @SuppressWarnings("unchecked") public static List<TagElement> tags(final Javadoc ¢) {
    return ¢.tags();
  }

  /** Shorthand for {@link ConditionalExpression#getThenExpression()}
   * @param ¢ JD
   * @return then part of the parameter */
  public static Expression then(final ConditionalExpression ¢) {
    return ¢.getThenExpression();
  }

  /** Shorthand for {@link IfStatement#getThenStatement}
   * @param ¢ JD
   * @return then statement of the parameter */
  public static Statement then(final IfStatement ¢) {
    return ¢.getThenStatement();
  }

  public static Type type(final CastExpression ¢) {
    return ¢.getType();
  }

  @SuppressWarnings("unchecked") public static List<Type> typeArguments(final ParameterizedType ¢) {
    return ¢.typeArguments();
  }

  @SuppressWarnings("unchecked") public static List<MemberValuePair> values(final NormalAnnotation ¢) {
    return ¢.values();
  }
}
