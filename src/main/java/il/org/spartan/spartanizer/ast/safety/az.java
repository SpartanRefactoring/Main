package il.org.spartan.spartanizer.ast.safety;

import static il.org.spartan.idiomatic.*;
import static il.org.spartan.lisp.*;
import static il.org.spartan.utils.Unbox.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum az {
  ;
  /** A fluent API to parse numeric literals, including provisions for unary
   * minus.
   * @author Yossi Gil
   * @year 2016 */
  public interface throwing {
    static String chop¢necessaryQuestionMark(final String ¢) {
      return ¢.substring(0, ¢.length() - 1);
    }

    static double double¢(final Expression ¢) throws NumberFormatException {
      assert iz.pseudoNumber(¢);
      return !iz.longType(¢) ? !iz.prefixExpression(¢) ? double¢(token(¢)) : -double¢(token(¢))
          : iz.numberLiteral(¢) ? double¢(chop¢necessaryQuestionMark(token(az.numberLiteral(¢))))
              : -double¢(chop¢necessaryQuestionMark(token(az.prefixExpression(¢))));
    }

    static double double¢(final String token) throws NumberFormatException {
      return Double.parseDouble(token);
    }

    static int int¢(final Expression ¢) throws NumberFormatException {
      assert iz.pseudoNumber(¢);
      return !iz.prefixExpression(¢) ? int¢(token(¢)) : -int¢(token(¢));
    }

    static int int¢(final String token) throws NumberFormatException {
      return Integer.parseInt(token);
    }

    static long long¢(final Expression ¢) throws NumberFormatException {
      assert iz.pseudoNumber(¢);
      return !iz.numberLiteral(¢) ? -long¢(chop¢necessaryQuestionMark(token(¢)))
          : long¢(iz.intType(¢) ? token(¢) : chop¢necessaryQuestionMark(token(¢)));
    }

    static long long¢(final String token) throws NumberFormatException {
      return Long.parseLong(token);
    }

    static NumberLiteral negativeLiteral(final Expression ¢) {
      return throwing.negativeLiteral(prefixExpression(¢));
    }

    static NumberLiteral negativeLiteral(final PrefixExpression ¢) {
      return operator(¢) != MINUS1 || !iz.numericLiteral(operand(¢)) ? null : numberLiteral(operand(¢));
    }

    static String token(final Expression ¢) {
      return iz.numberLiteral(¢) ? token(az.numberLiteral(¢)) : iz.prefixExpression(¢) ? token(prefixExpression(¢)) : null;
    }

    static String token(final NumberLiteral ¢) {
      return ¢.getToken();
    }

    static String token(final PrefixExpression ¢) {
      return az.numberLiteral(operand(¢)).getToken();
    }
  }

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
    return !iz.nodeTypeEquals($, ARRAY_INITIALIZER) ? null : (ArrayInitializer) $;
  }

  /** Convert, is possible, an {@link ASTNode} to an {@link Assignment}
   * @param $ result
   * @return argument, but down-casted to a {@link Assignment}, or
   *         <code><b>null</b></code> if the downcast is impossible. */
  public static Assignment assignment(final ASTNode $) {
    return !iz.nodeTypeEquals($, ASSIGNMENT) ? null : (Assignment) $;
  }

  /** Down-cast, if possible, to {@link Statement}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static Statement statement(final ASTNode $) {
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
    return !iz.nodeTypeEquals($, BLOCK) ? null : (Block) $;
  }

  public static SwitchCase switchCase(final ASTNode $) {
    return !iz.nodeTypeEquals($, SWITCH_CASE) ? null : (SwitchCase) $;
  }

  public static BodyDeclaration bodyDeclaration(final ASTNode ¢) {
    return ¢ == null || !(¢ instanceof BodyDeclaration) ? null : (BodyDeclaration) ¢;
  }

  /** Down-cast, if possible, to {@link BooleanLiteral}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static BooleanLiteral booleanLiteral(final ASTNode $) {
    return !iz.nodeTypeEquals($, BOOLEAN_LITERAL) ? null : (BooleanLiteral) $;
  }

  /** Down-cast, if possible, to {@link ClassInstanceCreation}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static ClassInstanceCreation classInstanceCreation(final ASTNode $) {
    return !($ instanceof ClassInstanceCreation) ? null : (ClassInstanceCreation) $;
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

  /** @param ¢ JD
   * @return */
  public static CompilationUnit compilationUnit(final ASTNode ¢) {
    return ¢ == null ? null : (CompilationUnit) ¢;
  }

  /** Convert, is possible, an {@link ASTNode} to a
   * {@link ConditionalExpression}
   * @param $ result
   * @return argument, but down-casted to a {@link ConditionalExpression}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static ConditionalExpression conditionalExpression(final ASTNode $) {
    return !($ instanceof ConditionalExpression) ? null : (ConditionalExpression) $;
  }

  public static EnhancedForStatement enhancedFor(final ASTNode $) {
    return !iz.nodeTypeEquals($, ENHANCED_FOR_STATEMENT) ? null : (EnhancedForStatement) $;
  }

  /** Down-cast, if possible, to {@link EnumConstantDeclaration}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static EnumConstantDeclaration enumConstantDeclaration(final ASTNode $) {
    return !($ instanceof EnumConstantDeclaration) ? null : (EnumConstantDeclaration) $;
  }

  /** @param ¢ JD
   * @return */
  public static EnumDeclaration enumDeclaration(final ASTNode ¢) {
    return !(¢ instanceof EnumDeclaration) ? null : (EnumDeclaration) ¢;
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
    return !iz.nodeTypeEquals($, EXPRESSION_STATEMENT) ? null : (ExpressionStatement) $;
  }

  public static FieldDeclaration fieldDeclaration(final ASTNode ¢) {
    return !iz.nodeTypeEquals(¢, FIELD_DECLARATION) ? null : (FieldDeclaration) ¢;
  }

  /** Down-cast, if possible, to {@link ForStatement}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static ForStatement forStatement(final ASTNode $) {
    return !iz.nodeTypeEquals($, FOR_STATEMENT) ? null : (ForStatement) $;
  }

  /** Down-cast, if possible, to {@link IfStatement}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static IfStatement ifStatement(final ASTNode $) {
    return !iz.nodeTypeEquals($, IF_STATEMENT) ? null : (IfStatement) $;
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
    return !iz.nodeTypeEquals($, INSTANCEOF_EXPRESSION) ? null : (InstanceofExpression) $;
  }

  public static int int¢(final Object ¢) {
    return unbox((Integer) ¢);
  }

  public static LambdaExpression lambdaExpression(final ASTNode $) {
    return !iz.nodeTypeEquals($, LAMBDA_EXPRESSION) ? null : (LambdaExpression) $;
  }

  /** Convert, is possible, an {@link ASTNode} to a {@link MethodDeclaration}
   * @param $ result
   * @return argument, but down-casted to a {@link MethodDeclaration}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static MethodDeclaration methodDeclaration(final ASTNode $) {
    return $ == null ? null : eval(() -> ((MethodDeclaration) $)).when($ instanceof MethodDeclaration);
  }

  /** Down-cast, if possible, to {@link MethodInvocation}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static MethodInvocation methodInvocation(final ASTNode $) {
    return !($ instanceof MethodInvocation) ? null : (MethodInvocation) $;
  }

  /** Convert, is possible, an {@link ASTNode} to a {@link MethodRef}
   * @param ¢ ASTNode
   * @return argument, but down-casted to a {@link MethodRef}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static MethodRef methodRef(final ASTNode ¢) {
    return !iz.nodeTypeEquals(¢, MEMBER_REF) ? null : (MethodRef) ¢;
  }

  /** Down-cast, if possible, to {@link Modifier}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static Modifier modifier(final ASTNode $) {
    return !iz.modifier($) ? null : (Modifier) $;
  }

  public static Name name(final ASTNode ¢) {
    return ¢ instanceof Name ? (Name) ¢ : null;
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
    return !iz.numberLiteral($) ? null : (NumberLiteral) $;
  }

  /** Down-cast, if possible, to {@link ParenthesizedExpression}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static ParenthesizedExpression parenthesizedExpression(final Expression $) {
    return !iz.nodeTypeEquals($, PARENTHESIZED_EXPRESSION) ? null : (ParenthesizedExpression) $;
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
    return $ == null || !iz.nodeTypeEquals($, RETURN_STATEMENT) ? null : (ReturnStatement) $;
  }

  /** Convert, is possible, an {@link ASTNode} to a {@link SimpleName}
   * @param $ result
   * @return argument, but down-casted to a {@link SimpleName}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static SimpleName simpleName(final ASTNode $) {
    return eval(() -> (SimpleName) $).when($ instanceof SimpleName);
  }

  public static SimpleName simpleName(final PostfixExpression $) {
    return eval(() -> (SimpleName) $.getOperand()).when($.getOperand() instanceof SimpleName);
  }

  public static SimpleName simpleName(final PrefixExpression $) {
    return eval(() -> (SimpleName) $.getOperand()).when($.getOperand() instanceof SimpleName);
  }

  /** Down-cast, if possible, to {@link SingleMemberAnnotation}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static SingleMemberAnnotation singleMemberAnnotation(final Annotation $) {
    return !($ instanceof SingleMemberAnnotation) ? null : (SingleMemberAnnotation) $;
  }

  public static SingleVariableDeclaration singleVariableDeclaration(final ASTNode $) {
    return !iz.singleVariableDeclaration($) ? null : (SingleVariableDeclaration) $;
  }

  /** Down-cast, if possible, to {@link StringLiteral}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static StringLiteral stringLiteral(final ASTNode $) {
    return !iz.nodeTypeEquals($, STRING_LITERAL) ? null : (StringLiteral) $;
  }

  /** Convert, is possible, an {@link ASTNode} to a {@link ThrowStatement}
   * @param $ result
   * @return argument, but down-casted to a {@link ThrowStatement}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static ThrowStatement throwStatement(final ASTNode $) {
    return !iz.nodeTypeEquals($, THROW_STATEMENT) ? null : (ThrowStatement) $;
  }

  public static boolean true¢(@SuppressWarnings("unused") final int __) {
    return true;
  }

  /** @param ¢ JD
   * @return */
  public static TypeDeclaration typeDeclaration(final ASTNode ¢) {
    return !iz.typeDeclaration(¢) ? null : (TypeDeclaration) ¢;
  }

  /** Convert, if possible, an {@link Expression} to a
   * {@link VariableDeclarationExpression}
   * @param $ result
   * @return argument, but down-casted to a
   *         {@link VariableDeclarationExpression}, or <code><b>null</b></code>
   *         if no such down-cast is possible.. */
  public static VariableDeclarationExpression variableDeclarationExpression(final ASTNode $) {
    return !iz.nodeTypeEquals($, VARIABLE_DECLARATION_EXPRESSION) ? null : (VariableDeclarationExpression) $;
  }

  public static VariableDeclarationExpression variableDeclarationExpression(final ForStatement $) {
    return az.variableDeclarationExpression(findFirst.elementOf(step.initializers($)));
  }

  public static VariableDeclarationExpression variableDeclarationExpression(final VariableDeclarationStatement ¢) {
    if (¢ == null)
      return null;
    final VariableDeclarationExpression $ = ¢.getAST()
        .newVariableDeclarationExpression(duplicate.of(findFirst.elementOf(step.fragments(duplicate.of(¢)))));
    fragments($).addAll(nextFragmentsOf(¢));
    $.setType(duplicate.of(¢.getType()));
    extendedModifiers($).addAll(modifiersOf(¢));
    return $;
  }

  /** @param $
   * @return */
  public static VariableDeclarationFragment variableDeclrationFragment(final ASTNode $) {
    return !iz.variableDeclarationFragment($) ? null : (VariableDeclarationFragment) $;
  }

  public static VariableDeclarationStatement variableDeclrationStatement(final ASTNode $) {
    return !iz.nodeTypeEquals($, VARIABLE_DECLARATION_STATEMENT) ? null : (VariableDeclarationStatement) $;
  }

  /** Down-cast, if possible, to {@link WhileStatement}
   * @param $ result
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static WhileStatement whileStatement(final ASTNode $) {
    return !iz.whileStatement($) ? null : (WhileStatement) $;
  }

  /** Convert, is possible, an {@link ASTNode} to a {@link WildcardType}
   * @param $ result
   * @return argument, but down-casted to a {@link WildcardType}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static WildcardType wildcardType(final ASTNode $) {
    return !iz.nodeTypeEquals($, WILDCARD_TYPE) ? null : (WildcardType) $;
  }

  private static List<IExtendedModifier> modifiersOf(final VariableDeclarationStatement ¢) {
    final List<IExtendedModifier> $ = new ArrayList<>();
    duplicate.modifiers(step.extendedModifiers(¢), $);
    return $;
  }

  private static List<VariableDeclarationFragment> nextFragmentsOf(final VariableDeclarationStatement ¢) {
    final List<VariableDeclarationFragment> $ = new ArrayList<>();
    duplicate.into(step.fragments(¢), $);
    return chop($);
  }

  /** @param ¢ JD
   * @return */
  static PrimitiveType primitiveType(final Type ¢) {
    return ¢ == null ? null : (PrimitiveType) ¢;
  }

  /** @param ¢ JD
   * @return */
  public static CastExpression castExpression(final Expression ¢) {
    return ¢ == null || !iz.castExpression(¢) ? null : (CastExpression) ¢;
  }

  /** @param ¢ JD
   * @return */
  public static VariableDeclarationStatement variableDeclarationStatement(final ASTNode ¢) {
    return ¢ == null ? null : (VariableDeclarationStatement) ¢;
  }

  /** @param ¢ JD
   * @return */
  public static Type type(final ASTNode ¢) {
    return ¢ == null ? null : (Type) ¢;
  }
}
