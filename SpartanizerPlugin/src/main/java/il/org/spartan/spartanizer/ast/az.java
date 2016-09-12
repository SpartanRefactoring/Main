package il.org.spartan.spartanizer.ast;

import static il.org.spartan.idiomatic.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum az {
  ;
  /** Down-cast, if possible, to {@link AbstractTypeDeclaration}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static AbstractTypeDeclaration abstractTypeDeclaration(final ASTNode $) {
    return eval(() -> ((AbstractTypeDeclaration) $)).when($ instanceof AbstractTypeDeclaration);
  }

  /** Convert an {@link Expression} into {@link InfixExpression} whose operator
   * is either {@link org.eclipse.jdt.core.dom.InfixExpression.Operator#AND} or
   * {@link org.eclipse.jdt.core.dom.InfixExpression.Operator#OR}.
   * @param $ result
   * @return parameter thus converted, or <code><b>null</b> if the conversion is
   *         not possible for it */
  public static InfixExpression andOrOr(final Expression $) {
    return !iz.infixExpression($) || !iz.deMorgan(infixExpression($).getOperator()) ? null : infixExpression($);
  }

  /** Down-cast, if possible, to {@link Annotation}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static Annotation annotation(final IExtendedModifier $) {
    return !iz.annotation($) ? null : (Annotation) $;
  }

  /** Down-cast, if possible, to {@link ArrayInitializer}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static ArrayInitializer arrayInitializer(final Expression $) {
    return !iz.is($, ARRAY_INITIALIZER) ? null : (ArrayInitializer) $;
  }

  /** Convert, is possible, an {@link ASTNode} to an {@link Assignment}
   * @param $ result
   * @return argument, but down-casted to a {@link Assignment}, or
   *         <code><b>null</b></code> if the downcast is impossible. */
  public static Assignment assignment(final ASTNode $) {
    return !iz.is($, ASSIGNMENT) ? null : (Assignment) $;
  }

  /** Down-cast, if possible, to {@link Statement}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static Statement asStatement(final ASTNode $) {
    return !iz.statement($) ? null : (Statement) $;
  }

  /** Convert, if possible, an {@link Object} to a {@link ASTNode}
   * @param $ result
   * @return argument, but down-casted to a {@link ASTNode}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static ASTNode astNode(final Object $) {
    return !iz.astNode($) ? null : (ASTNode) $;
  }

  /** Converts a boolean into a bit value
   * @param $ result
   * @return 1 if the parameter is <code><b>true</b></code>, 0 if it is
   *         <code><b>false</b></code> */
  public static int bit(final boolean $) {
    return $ ? 1 : 0;
  }

  /** Convert, is possible, an {@link ASTNode} to a {@link Block}
   * @param $ result
   * @return argument, but down-casted to a {@link Block}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static Block block(final ASTNode $) {
    return !iz.is($, BLOCK) ? null : (Block) $;
  }

  /** Down-cast, if possible, to {@link BooleanLiteral}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static BooleanLiteral booleanLiteral(final ASTNode $) {
    return !iz.is($, BOOLEAN_LITERAL) ? null : (BooleanLiteral) $;
  }

  /** Convert an {@link Expression} into {@link InfixExpression} whose operator
   * is one of the six comparison operators: <code><</code>, <code><=</code>,
   * <code>></code>, <code>>=</code>, <code>!=</code>, or <code>==</code>.
   * @param $ result
   * @return parameter thus converted, or <code><b>null</b> if the conversion is
   *         not possible for it */
  public static InfixExpression comparison(final Expression $) {
    return !($ instanceof InfixExpression) ? null : az.comparison((InfixExpression) $);
  }

  public static InfixExpression comparison(final InfixExpression $) {
    return iz.comparison($) ? $ : null;
  }

  /** Convert, is possible, an {@link ASTNode} to a
   * {@link ConditionalExpression}
   * @param $ result
   * @return argument, but down-casted to a {@link ConditionalExpression}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static ConditionalExpression conditionalExpression(final ASTNode $) {
    return !($ instanceof ConditionalExpression) ? null : (ConditionalExpression) $;
  }

  /** Down-cast, if possible, to {@link Expression}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static Expression expression(final ASTNode $) {
    return !($ instanceof Expression) ? null : (Expression) $;
  }

  /** Down-cast, if possible, to {@link ExpressionStatement}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static ExpressionStatement expressionStatement(final ASTNode $) {
    return !($ instanceof ExpressionStatement) ? null : (ExpressionStatement) $;
  }

  /** Down-cast, if possible, to {@link ForStatement}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static ForStatement forStatement(final Statement $) {
    return !iz.is($, FOR_STATEMENT) ? null : (ForStatement) $;
  }

  /** Down-cast, if possible, to {@link IfStatement}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static IfStatement ifStatement(final ASTNode $) {
    return !iz.is($, IF_STATEMENT) ? null : (IfStatement) $;
  }

  /** Down-cast, if possible, to {@link InfixExpression}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static InfixExpression infixExpression(final ASTNode $) {
    return !($ instanceof InfixExpression) ? null : (InfixExpression) $;
  }

  /** Down-cast, if possible, to {@link InstanceofExpression}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static InstanceofExpression instanceofExpression(final Expression $) {
    return !iz.is($, INSTANCEOF_EXPRESSION) ? null : (InstanceofExpression) $;
  }

  /** Convert, is possible, an {@link ASTNode} to a {@link MethodDeclaration}
   * @param $ result
   * @return argument, but down-casted to a {@link MethodDeclaration}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static MethodDeclaration methodDeclaration(final ASTNode $) {
    return eval(() -> ((MethodDeclaration) $)).when($ instanceof MethodDeclaration);
  }

  /** Down-cast, if possible, to {@link MethodInvocation}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static MethodInvocation methodInvocation(final Expression $) {
    return !($ instanceof MethodInvocation) ? null : (MethodInvocation) $;
  }

  /** Down-cast, if possible, to {@link Modifier}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static Modifier modifier(final ASTNode $) {
    return !iz.modifier($) ? null : (Modifier) $;
  }

  /** Down-cast, if possible, to {@link NormalAnnotation}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static NormalAnnotation normalAnnotation(final Annotation $) {
    return !($ instanceof NormalAnnotation) ? null : (NormalAnnotation) $;
  }

  /** Convert an {@link Expression} into a {@link PrefixExpression} whose
   * operator is <code>!</code>,
   * @param $ result
   * @return parameter thus converted, or <code><b>null</b> if the conversion is
   *         not possible for it */
  public static PrefixExpression not(final Expression $) {
    return !($ instanceof PrefixExpression) ? null : not(prefixExpression($));
  }

  public static PrefixExpression not(final PrefixExpression $) {
    return $ != null && $.getOperator() == NOT ? $ : null;
  }

  /** Down-cast, if possible, to {@link NumberLiteral}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static NumberLiteral numberLiteral(final ASTNode $) {
    return !iz.isNumberLiteral($) ? null : (NumberLiteral) $;
  }

  /** Down-cast, if possible, to {@link ParenthesizedExpression}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static ParenthesizedExpression parenthesizedExpression(final Expression $) {
    return !iz.is($, PARENTHESIZED_EXPRESSION) ? null : (ParenthesizedExpression) $;
  }

  /** Down-cast, if possible, to {@link InfixExpression}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static PostfixExpression postfixExpression(final ASTNode $) {
    return eval(() -> (PostfixExpression) $).when($ instanceof PostfixExpression);
  }

  /** Down-cast, if possible, to {@link PrefixExpression}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static PrefixExpression prefixExpression(final ASTNode $) {
    return eval(() -> (PrefixExpression) $).when($ instanceof PrefixExpression);
  }

  /** Down-cast, if possible, to {@link ReturnStatement}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static ReturnStatement returnStatement(final ASTNode $) {
    return !iz.is($, RETURN_STATEMENT) ? null : (ReturnStatement) $;
  }

  /** Convert, is possible, an {@link ASTNode} to a {@link SimpleName}
   * @param $ result
   * @return argument, but down-casted to a {@link SimpleName}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static SimpleName simpleName(final ASTNode $) {
    return eval(() -> (SimpleName) $).when($ instanceof SimpleName);
  }

  /** Down-cast, if possible, to {@link SingleMemberAnnotation}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static SingleMemberAnnotation singleMemberAnnotation(final Annotation $) {
    return !($ instanceof SingleMemberAnnotation) ? null : (SingleMemberAnnotation) $;
  }

  /** Down-cast, if possible, to {@link StringLiteral}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static StringLiteral stringLiteral(final ASTNode $) {
    return !iz.is($, STRING_LITERAL) ? null : (StringLiteral) $;
  }

  /** Convert, is possible, an {@link ASTNode} to a {@link ThrowStatement}
   * @param $ result
   * @return argument, but down-casted to a {@link ThrowStatement}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static ThrowStatement throwStatement(final ASTNode $) {
    return !iz.is($, THROW_STATEMENT) ? null : (ThrowStatement) $;
  }

  /** Convert, if possible, an {@link Expression} to a
   * {@link VariableDeclarationExpression}
   * @param $ result
   * @return argument, but down-casted to a
   *         {@link VariableDeclarationExpression}, or <code><b>null</b></code>
   *         if no such down-cast is possible.. */
  public static VariableDeclarationExpression variableDeclarationExpression(final Expression $) {
    return !iz.is($, VARIABLE_DECLARATION_EXPRESSION) ? null : (VariableDeclarationExpression) $;
  }

  /** Convert, is possible, an {@link ASTNode} to a {@link WildcardType}
   * @param $ result
   * @return argument, but down-casted to a {@link WildcardType}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static WildcardType wildcardType(final ASTNode $) {
    return !iz.is($, WILDCARD_TYPE) ? null : (WildcardType) $;
  }
}
