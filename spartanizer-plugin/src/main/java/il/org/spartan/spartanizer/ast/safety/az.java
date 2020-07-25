package il.org.spartan.spartanizer.ast.safety;

import static fluent.ly.idiomatic.eval;
import static il.org.spartan.spartanizer.ast.navigate.step.extendedModifiers;
import static il.org.spartan.spartanizer.ast.navigate.step.initializers;
import static il.org.spartan.spartanizer.ast.navigate.step.operand;
import static il.org.spartan.spartanizer.ast.navigate.step.operator;
import static org.eclipse.jdt.core.dom.ASTNode.ANNOTATION_TYPE_MEMBER_DECLARATION;
import static org.eclipse.jdt.core.dom.ASTNode.ARRAY_ACCESS;
import static org.eclipse.jdt.core.dom.ASTNode.ARRAY_INITIALIZER;
import static org.eclipse.jdt.core.dom.ASTNode.ARRAY_TYPE;
import static org.eclipse.jdt.core.dom.ASTNode.ASSIGNMENT;
import static org.eclipse.jdt.core.dom.ASTNode.BLOCK;
import static org.eclipse.jdt.core.dom.ASTNode.BOOLEAN_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.BREAK_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.DO_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.EMPTY_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.ENHANCED_FOR_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.EXPRESSION_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.FIELD_DECLARATION;
import static org.eclipse.jdt.core.dom.ASTNode.FOR_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.IF_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.INFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.INSTANCEOF_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.LAMBDA_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.MEMBER_REF;
import static org.eclipse.jdt.core.dom.ASTNode.PARENTHESIZED_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.RETURN_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.STRING_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.SWITCH_CASE;
import static org.eclipse.jdt.core.dom.ASTNode.SWITCH_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.THROW_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.TYPE_DECLARATION_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.VARIABLE_DECLARATION_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.VARIABLE_DECLARATION_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.WILDCARD_TYPE;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.NOT;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.IntersectionType;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;

import fluent.ly.as;
import fluent.ly.lisp;
import fluent.ly.the;
import fluent.ly.unbox;
import il.org.spartan.spartanizer.ast.factory.copy;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
@SuppressWarnings("ClassWithTooManyMethods")
public enum az {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  /** A fluent API to parse numeric literals, including provisions for unary
   * minus.
   * @author Yossi Gil */
  public interface throwing {
    static double double¢(final Expression ¢) throws NumberFormatException {
      assert iz.pseudoNumber(¢);
      return !iz.longType(¢) ? !iz.prefixExpression(¢) ? double¢(token(¢)) : -double¢(token(¢))
          : iz.numberLiteral(¢) ? double¢(lisp.chopLast(token(az.numberLiteral(¢)))) : -double¢(lisp.chopLast(token(az.prefixExpression(¢))));
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
      return iz.prefixExpression(¢) && iz.intType(¢) ? long¢(token(¢))
          : !iz.numberLiteral(¢) ? -long¢(lisp.chopLast(token(¢))) : long¢(iz.intType(¢) ? token(¢) : lisp.chopLast(token(¢)));
    }
    static long long¢(final String token) throws NumberFormatException {
      return Long.parseLong(token);
    }
    static NumberLiteral negativeLiteral(final Expression ¢) {
      return throwing.negativeLiteral(prefixExpression(¢));
    }
    static NumberLiteral negativeLiteral(final PrefixExpression ¢) {
      return operator(¢) != il.org.spartan.spartanizer.ast.navigate.op.MINUS1 || !iz.numericLiteral(operand(¢)) ? null : numberLiteral(operand(¢));
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
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static AbstractTypeDeclaration abstractTypeDeclaration(final ASTNode $) {
    return eval(() -> ((AbstractTypeDeclaration) $)).when($ instanceof AbstractTypeDeclaration);
  }
  /** Down-cast, if possible, to {@link Annotation}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static Annotation annotation(final IExtendedModifier $) {
    return !iz.annotation($) ? null : (Annotation) $;
  }
  public static AnnotationTypeDeclaration annotationTypeDeclration(final ASTNode $) {
    return !iz.annotationTypeDeclaration($) ? null : (AnnotationTypeDeclaration) $;
  }
  public static AnnotationTypeMemberDeclaration annotationTypeMemberDeclaration(final BodyDeclaration ¢) {
    return !iz.nodeTypeEquals(¢, ANNOTATION_TYPE_MEMBER_DECLARATION) ? null : (AnnotationTypeMemberDeclaration) ¢;
  }
  public static AnonymousClassDeclaration anonymousClassDeclaration(final ASTNode $) {
    return !iz.anonymousClassDeclaration($) ? null : (AnonymousClassDeclaration) $;
  }
  static ArrayAccess arrayAccess(final Expression ¢) {
    return !iz.nodeTypeEquals(¢, ARRAY_ACCESS) ? null : (ArrayAccess) ¢;
  }
  /** Down-cast, if possible, to {@link ArrayInitializer}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static ArrayInitializer arrayInitializer(final Expression $) {
    return !iz.nodeTypeEquals($, ARRAY_INITIALIZER) ? null : (ArrayInitializer) $;
  }
  public static ArrayType arrayType(final Type ¢) {
    return !iz.nodeTypeEquals(¢, ARRAY_TYPE) ? null : (ArrayType) ¢;
  }
  /** Convert, is possible, an {@link ASTNode} to an {@link Assignment}
   * @param $ result
   * @return argument, but down-casted to a {@link Assignment}, or
   *         {@code null} if the downcast is impossible. */
  public static Assignment assignment(final ASTNode $) {
    return !iz.nodeTypeEquals($, ASSIGNMENT) ? null : (Assignment) $;
  }
  /** Convert, is possible, an {@link ASTNode} to an {@link AssertStatement}
   * @param $ result
   * @return argument, but down-casted to a {@link AssertStatement}, or
   *         {@code null} if the downcast is impossible. */
  public static AssertStatement assertStatement(final ASTNode $) {
    return !iz.assertStatement(az.statement($)) ? null : (AssertStatement) $;
  }
  /** Convert, if possible, an {@link Object} to a {@link ASTNode}
   * @param $ result
   * @return argument, but down-casted to a {@link ASTNode}, or
   *         {@code null} if no such down-cast is possible.. */
  public static ASTNode astNode(final Object $) {
    return !iz.astNode($) ? null : (ASTNode) $;
  }
  /** Converts a boolean into a bit value
   * @param $ result
   * @return 1 if the parameter is {@code true}, 0 if it is {@code false} */
  @SuppressWarnings("BooleanParameter") public static int bit(final boolean $) {
    return as.bit($);
  }
  /** Convert, is possible, an {@link ASTNode} to a {@link Block}
   * @param $ result
   * @return argument, but down-casted to a {@link Block}, or
   *         {@code null} if no such down-cast is possible.. */
  public static Block block(final ASTNode $) {
    return !iz.nodeTypeEquals($, BLOCK) ? null : (Block) $;
  }
  public static BodyDeclaration bodyDeclaration(final ASTNode ¢) {
    return !(¢ instanceof BodyDeclaration) ? null : (BodyDeclaration) ¢;
  }
  /** Down-cast, if possible, to {@link BooleanLiteral}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static BooleanLiteral booleanLiteral(final ASTNode $) {
    return !iz.nodeTypeEquals($, BOOLEAN_LITERAL) ? null : (BooleanLiteral) $;
  }
  public static BreakStatement breakStatement(final Statement $) {
    return !iz.nodeTypeEquals($, BREAK_STATEMENT) ? null : (BreakStatement) $;
  }
  /** @param ¢ JD
   */
  public static CastExpression castExpression(final Expression ¢) {
    return ¢ == null || !iz.castExpression(¢) ? null : (CastExpression) ¢;
  }
  public static CatchClause catchClause(final ASTNode $) {
    return !iz.catchClause($) ? null : (CatchClause) $;
  }
  /** Down-cast, if possible, to {@link ClassInstanceCreation}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static ClassInstanceCreation classInstanceCreation(final ASTNode $) {
    return !($ instanceof ClassInstanceCreation) ? null : (ClassInstanceCreation) $;
  }
  /** Convert an {@link Expression} into {@link InfixExpression} whose operator is
   * one of the six comparison operators: {@code <}, {@code <=}, {@code >},
   * {@code >=}, {@code !=}, or {@code ==}.
   * @param $ result
   * @return parameter thus converted, or {@code null</b> if the conversion is
   *         not possible for it */
  public static InfixExpression comparison(final Expression $) {
    return !($ instanceof InfixExpression) ? null : az.comparison((InfixExpression) $);
  }
  private static InfixExpression comparison(final InfixExpression $) {
    return iz.comparison($) ? $ : null;
  }
  /** @param ¢ JD
   */
  public static CompilationUnit compilationUnit(final ASTNode ¢) {
    return ¢ == null ? null : (CompilationUnit) ¢;
  }
  /** Convert, is possible, an {@link ASTNode} to a {@link ConditionalExpression}
   * @param $ result
   * @return argument, but down-casted to a {@link ConditionalExpression}, or
   *         {@code null} if no such down-cast is possible.. */
  public static ConditionalExpression conditionalExpression(final ASTNode $) {
    return !($ instanceof ConditionalExpression) ? null : (ConditionalExpression) $;
  }
  /** @param ¢ JD
   */
  public static ContinueStatement continueStatement(final ASTNode ¢) {
    return ¢ == null || !iz.continueStatement(¢) ? null : (ContinueStatement) ¢;
  }
  public static DoStatement doStatement(final ASTNode $) {
    return !iz.nodeTypeEquals($, DO_STATEMENT) ? null : (DoStatement) $;
  }
  public static EmptyStatement emptyStatement(final Statement $) {
    return !iz.nodeTypeEquals($, EMPTY_STATEMENT) ? null : (EmptyStatement) $;
  }
  public static EnhancedForStatement enhancedFor(final ASTNode $) {
    return !iz.nodeTypeEquals($, ENHANCED_FOR_STATEMENT) ? null : (EnhancedForStatement) $;
  }
  /** Down-cast, if possible, to {@link EnumConstantDeclaration}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static EnumConstantDeclaration enumConstantDeclaration(final ASTNode $) {
    return !($ instanceof EnumConstantDeclaration) ? null : (EnumConstantDeclaration) $;
  }
  /** @param ¢ JD
   */
  public static EnumDeclaration enumDeclaration(final ASTNode ¢) {
    return !(¢ instanceof EnumDeclaration) ? null : (EnumDeclaration) ¢;
  }
  /** Down-cast, if possible, to {@link Expression}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static Expression expression(final ASTNode $) {
    return !($ instanceof Expression) ? null : (Expression) $;
  }
  /** Down-cast, if possible, to {@link ExpressionStatement}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static ExpressionStatement expressionStatement(final ASTNode $) {
    return !iz.nodeTypeEquals($, EXPRESSION_STATEMENT) ? null : (ExpressionStatement) $;
  }
  public static ExpressionStatement expressionStatement(final ExpressionStatement $) {
    return $;
  }
  /** @param ¢ JD
   */
  public static FieldAccess fieldAccess(final ASTNode ¢) {
    return ¢ == null || !iz.fieldAccess(¢) ? null : (FieldAccess) ¢;
  }
  public static FieldDeclaration fieldDeclaration(final ASTNode ¢) {
    return !iz.nodeTypeEquals(¢, FIELD_DECLARATION) ? null : (FieldDeclaration) ¢;
  }
  /** Down-cast, if possible, to {@link ForStatement}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static ForStatement forStatement(final ASTNode $) {
    return !iz.nodeTypeEquals($, FOR_STATEMENT) ? null : (ForStatement) $;
  }
  /** Down-cast, if possible, to {@link IfStatement}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static IfStatement ifStatement(final ASTNode $) {
    return !iz.nodeTypeEquals($, IF_STATEMENT) ? null : (IfStatement) $;
  }
  /** Down-cast, if possible, to {@link InfixExpression}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static InfixExpression infixExpression(final ASTNode $) {
    return !iz.nodeTypeEquals($, INFIX_EXPRESSION) ? null : (InfixExpression) $;
  }
  public static Initializer initializer(final ASTNode $) {
    return eval(() -> ((Initializer) $)).when($ instanceof Initializer);
  }
  /** Down-cast, if possible, to {@link InstanceofExpression}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static InstanceofExpression instanceofExpression(final Expression $) {
    return !iz.nodeTypeEquals($, INSTANCEOF_EXPRESSION) ? null : (InstanceofExpression) $;
  }
  public static int int¢(final Object ¢) {
    return unbox.it((Integer) ¢);
  }
  public static LambdaExpression lambdaExpression(final ASTNode $) {
    return !iz.nodeTypeEquals($, LAMBDA_EXPRESSION) ? null : (LambdaExpression) $;
  }
  /** Convert, is possible, an {@link ASTNode} to a {@link MethodDeclaration}
   * @param $ result
   * @return argument, but down-casted to a {@link MethodDeclaration}, or
   *         {@code null} if no such down-cast is possible.. */
  public static MethodDeclaration methodDeclaration(final ASTNode $) {
    return $ == null ? null : eval(() -> ((MethodDeclaration) $)).when($ instanceof MethodDeclaration);
  }
  /** Down-cast, if possible, to {@link MethodInvocation}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static MethodInvocation methodInvocation(final ASTNode $) {
    return !($ instanceof MethodInvocation) ? null : (MethodInvocation) $;
  }
  /** Convert, is possible, an {@link ASTNode} to a {@link MethodRef}
   * @param ¢ ASTNode
   * @return argument, but down-casted to a {@link MethodRef}, or
   *         {@code null} if no such down-cast is possible.. */
  public static MethodRef methodRef(final ASTNode ¢) {
    return !iz.nodeTypeEquals(¢, MEMBER_REF) ? null : (MethodRef) ¢;
  }
  /** Down-cast, if possible, to {@link Modifier}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static Modifier modifier(final ASTNode $) {
    return !iz.modifier($) ? null : (Modifier) $;
  }
  public static Collection<IExtendedModifier> modifiersOf(final VariableDeclarationStatement ¢) {
    final Collection<IExtendedModifier> $ = an.empty.list();
    copy.modifiers(extendedModifiers(¢), $);
    return $;
  }
  public static Name name(final ASTNode ¢) {
    return ¢ instanceof Name ? (Name) ¢ : null;
  }
  public static ImportDeclaration importDeclaration(final Object ¢) {
    return ¢ instanceof ImportDeclaration ? (ImportDeclaration) ¢ : null;
  }
  /** Down-cast, if possible, to {@link NormalAnnotation}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static NormalAnnotation normalAnnotation(final Annotation $) {
    return !($ instanceof NormalAnnotation) ? null : (NormalAnnotation) $;
  }
  /** Convert an {@link Expression} into a {@link PrefixExpression} whose operator
   * is {@code !},
   * @param $ result
   * @return parameter thus converted, or {@code null</b> if the conversion is
   *         not possible for it */
  public static PrefixExpression not(final Expression $) {
    return !($ instanceof PrefixExpression) ? null : not(prefixExpression($));
  }
  public static PrefixExpression not(final PrefixExpression $) {
    return $ != null && $.getOperator() == NOT ? $ : null;
  }
  /** Down-cast, if possible, to {@link NumberLiteral}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static NumberLiteral numberLiteral(final ASTNode $) {
    return !iz.numberLiteral($) ? null : (NumberLiteral) $;
  }
  public static ParameterizedType parameterizedType(final ASTNode ¢) {
    return !iz.parameterizedType(¢) ? null : (ParameterizedType) ¢;
  }
  public static SimpleType simpleType(final ASTNode ¢) {
    return !(¢ instanceof SimpleType) ? null : (SimpleType) ¢;
  }
  public static QualifiedType qualifiedType(final ASTNode ¢) {
    return !(¢ instanceof QualifiedType) ? null : (QualifiedType) ¢;
  }
  public static NameQualifiedType nameQualifiedType(final ASTNode ¢) {
    return !(¢ instanceof NameQualifiedType) ? null : (NameQualifiedType) ¢;
  }
  /** Down-cast, if possible, to {@link ParenthesizedExpression}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static ParenthesizedExpression parenthesizedExpression(final Expression $) {
    return !iz.nodeTypeEquals($, PARENTHESIZED_EXPRESSION) ? null : (ParenthesizedExpression) $;
  }
  /** Down-cast, if possible, to {@link InfixExpression}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static PostfixExpression postfixExpression(final ASTNode $) {
    return eval(() -> (PostfixExpression) $).when($ instanceof PostfixExpression);
  }
  /** Down-cast, if possible, to {@link PrefixExpression}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static PrefixExpression prefixExpression(final ASTNode $) {
    return eval(() -> (PrefixExpression) $).when($ instanceof PrefixExpression);
  }
  /** @param ¢ JD
   */
  static PrimitiveType primitiveType(final Type ¢) {
    return ¢ == null || !iz.primitiveType(¢) ? null : (PrimitiveType) ¢;
  }
  /** @param ¢ JD
   */
  public static QualifiedName qualifiedName(final ASTNode ¢) {
    return ¢ == null || !iz.qualifiedName(¢) ? null : (QualifiedName) ¢;
  }
  /** Down-cast, if possible, to {@link ReturnStatement}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static ReturnStatement returnStatement(final ASTNode $) {
    return $ == null || !iz.nodeTypeEquals($, RETURN_STATEMENT) ? null : (ReturnStatement) $;
  }
  public static Statement sequencer(final Statement ¢) {
    return iz.sequencer(¢) ? ¢ : null;
  }
  /** Convert an {@link Expression} into {@link InfixExpression} whose operator is
   * either {@link InfixExpression.Operator#AND} or
   * {@link InfixExpression.Operator#OR}.
   * @param $ result
   * @return parameter thus converted, or {@code null</b> if the conversion is
   *         not possible for it */
  public static InfixExpression shortcircuit(final Expression $) {
    return !iz.infixExpression($) || !iz.deMorgan(infixExpression($).getOperator()) ? null : infixExpression($);
  }
  /** Convert, is possible, an {@link ASTNode} to a {@link SimpleName}
   * @param $ result
   * @return argument, but down-casted to a {@link SimpleName}, or
   *         {@code null} if no such down-cast is possible.. */
  public static SimpleName simpleName(final ASTNode $) {
    return eval(() -> (SimpleName) $).when($ instanceof SimpleName);
  }
  /** Down-cast, if possible, to {@link SingleMemberAnnotation}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static SingleMemberAnnotation singleMemberAnnotation(final Annotation $) {
    return !($ instanceof SingleMemberAnnotation) ? null : (SingleMemberAnnotation) $;
  }
  public static SingleVariableDeclaration singleVariableDeclaration(final ASTNode $) {
    return !($ instanceof SingleVariableDeclaration) ? null : (SingleVariableDeclaration) $;
  }
  /** Down-cast, if possible, to {@link Statement}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static Statement statement(final ASTNode $) {
    return !iz.statement($) ? null : (Statement) $;
  }
  public static <N> Stream<N> stream(final Iterable<N> ¢) {
    return StreamSupport.stream(¢.spliterator(), false);
  }
  /** Down-cast, if possible, to {@link StringLiteral}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static StringLiteral stringLiteral(final ASTNode $) {
    return !iz.nodeTypeEquals($, STRING_LITERAL) ? null : (StringLiteral) $;
  }
  public static SuperConstructorInvocation superConstructorInvocation(final ASTNode ¢) {
    return !iz.superConstructorInvocation(¢) ? null : (SuperConstructorInvocation) ¢;
  }
  /** @param ¢ JD
   */
  public static SuperMethodInvocation superMethodInvocation(final Expression ¢) {
    return ¢ == null || !iz.superMethodInvocation(¢) ? null : (SuperMethodInvocation) ¢;
  }
  public static SwitchCase switchCase(final ASTNode $) {
    return !iz.nodeTypeEquals($, SWITCH_CASE) ? null : (SwitchCase) $;
  }
  public static SwitchStatement switchStatement(final ASTNode $) {
    return !iz.nodeTypeEquals($, SWITCH_STATEMENT) ? null : (SwitchStatement) $;
  }
  /** @param ¢ JD
   */
  public static SynchronizedStatement synchronizedStatement(final ASTNode ¢) {
    return ¢ == null || !iz.synchronizedStatement(¢) ? null : (SynchronizedStatement) ¢;
  }
  /** Convert, is possible, an {@link ASTNode} to a {@link ThrowStatement}
   * @param $ result
   * @return argument, but down-casted to a {@link ThrowStatement}, or
   *         {@code null} if no such down-cast is possible.. */
  public static ThrowStatement throwStatement(final ASTNode $) {
    return !iz.nodeTypeEquals($, THROW_STATEMENT) ? null : (ThrowStatement) $;
  }
  public static boolean true¢(@SuppressWarnings("unused") final int __) {
    return true;
  }
  public static TryStatement tryStatement(final ASTNode $) {
    return eval(() -> (TryStatement) $).when($ instanceof TryStatement);
  }
  /** @param ¢ JD
   */
  public static Type type(final ASTNode ¢) {
    return ¢ == null || !iz.type(¢) ? null : (Type) ¢;
  }
  /** @param ¢ JD
   */
  public static TypeDeclaration typeDeclaration(final ASTNode ¢) {
    return !iz.typeDeclaration(¢) ? null : (TypeDeclaration) ¢;
  }
  public static TypeDeclarationStatement typeDeclarationStatement(final Statement $) {
    return !iz.nodeTypeEquals($, TYPE_DECLARATION_STATEMENT) ? null : (TypeDeclarationStatement) $;
  }
  /** Down-cast, if possible, to {@link UnionType}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static UnionType UnionType(final ASTNode $) {
    return !($ instanceof UnionType) ? null : (UnionType) $;
  }
  /** Down-cast, if possible, to {@link IntersectionType}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static IntersectionType intersectionType(final ASTNode $) {
    return !($ instanceof IntersectionType) ? null : (IntersectionType) $;
  }
  /** Convert, if possible, an {@link Expression} to a
   * {@link VariableDeclarationExpression}
   * @param $ result
   * @return argument, but down-casted to a {@link VariableDeclarationExpression},
   *         or {@code null
   *         if no such down-cast is possible.. */
  public static VariableDeclarationExpression variableDeclarationExpression(final ASTNode $) {
    return !iz.nodeTypeEquals($, VARIABLE_DECLARATION_EXPRESSION) ? null : (VariableDeclarationExpression) $;
  }
  public static VariableDeclarationExpression variableDeclarationExpression(final ForStatement $) {
    return az.variableDeclarationExpression(the.firstOf(initializers($)));
  }
  /** @param ¢ JD
   */
  public static VariableDeclarationStatement variableDeclarationStatement(final ASTNode ¢) {
    return ¢ == null || !iz.variableDeclarationStatement(¢) ? null : (VariableDeclarationStatement) ¢;
  }
  public static VariableDeclarationFragment variableDeclrationFragment(final ASTNode $) {
    return !iz.variableDeclarationFragment($) ? null : (VariableDeclarationFragment) $;
  }
  public static VariableDeclarationStatement variableDeclrationStatement(final ASTNode $) {
    return !iz.nodeTypeEquals($, VARIABLE_DECLARATION_STATEMENT) ? null : (VariableDeclarationStatement) $;
  }
  /** Down-cast, if possible, to {@link WhileStatement}
   * @param $ result
   * @return parameter down-casted to the returned __, or
   *         {@code null} if no such down-casting is possible. */
  public static WhileStatement whileStatement(final ASTNode $) {
    return !iz.whileStatement($) ? null : (WhileStatement) $;
  }
  /** Convert, is possible, an {@link ASTNode} to a {@link WildcardType}
   * @param $ result
   * @return argument, but down-casted to a {@link WildcardType}, or
   *         {@code null} if no such down-cast is possible.. */
  public static WildcardType wildcardType(final ASTNode $) {
    return !iz.nodeTypeEquals($, WILDCARD_TYPE) ? null : (WildcardType) $;
  }
  public static ASTNode breakStatement(ASTNode $) {
    return !($ instanceof BreakStatement) ? null : (BreakStatement) $;
  }
}
