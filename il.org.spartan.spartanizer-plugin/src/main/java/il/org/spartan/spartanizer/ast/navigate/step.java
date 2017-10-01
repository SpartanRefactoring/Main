package il.org.spartan.spartanizer.ast.navigate;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import an.*;
import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum step {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  /** Expose the list of arguments in a {@link ClassInstanceCreation}
   * @param ¢ JD
   * @return reference to the list of arguments in the argument */
  @SuppressWarnings("unchecked") public static List<Expression> arguments(final ClassInstanceCreation ¢) {
    return ¢ == null ? null : ¢.arguments();
  }
  @SuppressWarnings("unchecked") public static Iterable<Expression> arguments(final ConstructorInvocation ¢) {
    return ¢ == null ? null : ¢.arguments();
  }
  /** Expose the list of arguments in a {@link MethodInvocation}
   * @param ¢ JD
   * @return reference to the list of arguments in the argument */
  @SuppressWarnings("unchecked") public static List<Expression> arguments(final MethodInvocation ¢) {
    return ¢ == null ? null : ¢.arguments();
  }
  @SuppressWarnings("unchecked") public static Iterable<Expression> arguments(final SuperConstructorInvocation ¢) {
    return ¢.arguments();
  }
  /** Expose the list of arguments in a {@link SuperMethodInvocation}
   * @param ¢ JD
   * @return reference to the list of arguments in the argument */
  @SuppressWarnings("unchecked") public static List<Expression> arguments(final SuperMethodInvocation ¢) {
    return ¢ == null ? null : ¢.arguments();
  }
  public static int arity(final MethodDeclaration ¢) {
    return ¢ == null ? -1 : parameters(¢) == null ? 0 : parameters(¢).size();
  }
  private static boolean balanced(final CharSequence s) {
    int $ = 0;
    for (final Integer ¢ : range.from(0).to(s.length()))
      if (s.charAt(¢.intValue()) == '<')
        ++$;
      else if (s.charAt(¢.intValue()) == '>')
        --$;
    return $ == 0;
  }
  public static Statement body(final ASTNode ¢) {
    return body(az.statement(¢));
  }
  public static Statement body(final Statement ¢) {
    return iz.forStatement(¢) ? az.forStatement(¢).getBody()
        : iz.whileStatement(¢) ? az.whileStatement(¢).getBody()
            : iz.enhancedFor(¢) ? az.enhancedFor(¢).getBody()
                : iz.doStatement(¢) ? az.doStatement(¢).getBody()
                    : iz.switchStatement(¢) || !iz.synchronizedStatement(¢) ? null : az.synchronizedStatement(¢).getBody();
  }
  public static Block body(final CatchClause ¢) {
    return ¢ == null ? null : ¢.getBody();
  }
  public static Statement body(final DoStatement ¢) {
    return ¢ == null ? null : ¢.getBody();
  }
  public static Statement body(final EnhancedForStatement ¢) {
    return ¢ == null ? null : ¢.getBody();
  }
  public static Statement body(final ForStatement ¢) {
    return ¢ == null ? null : ¢.getBody();
  }
  /** @param ¢ JD
   * @return */
  public static Block body(final LambdaExpression ¢) {
    return ¢ == null ? null : az.block(¢.getBody());
  }
  public static Block body(final MethodDeclaration ¢) {
    return ¢ == null ? null : ¢.getBody();
  }
  /** @param ¢ JD
   * @return */
  private static Block body(final SynchronizedStatement ¢) {
    return ¢ == null ? null : ¢.getBody();
  }
  public static Block body(final TryStatement ¢) {
    return ¢ == null ? null : ¢.getBody();
  }
  public static Statement body(final WhileStatement ¢) {
    return ¢ == null ? null : ¢.getBody();
  }
  /** Expose the list of bodyDeclarations in an {@link AbstractTypeDeclaration}
   * @param ¢ JD
   * @return reference to the list of bodyDeclarations in the argument */
  @SuppressWarnings("unchecked") public static List<BodyDeclaration> bodyDeclarations(final AbstractTypeDeclaration ¢) {
    return ¢ == null ? null : ¢.bodyDeclarations();
  }
  /** Expose the list of bodyDeclarations in an {@link AbstractTypeDeclaration}
   * @param ¢ JD
   * @return reference to the list of bodyDeclarations in the argument */
  @SuppressWarnings("unchecked") public static List<BodyDeclaration> bodyDeclarations(final AnnotationTypeDeclaration ¢) {
    return ¢ == null ? null : ¢.bodyDeclarations();
  }
  /** Expose the list of bodyDeclarations in an {@link AnonymousClassDeclaration}
   * @param ¢ JD
   * @return reference to the list of bodyDeclarations in the argument */
  @SuppressWarnings("unchecked") public static List<BodyDeclaration> bodyDeclarations(final AnonymousClassDeclaration ¢) {
    return ¢ == null ? null : ¢.bodyDeclarations();
  }
  /** Expose the list of catchClauses in a {@link TryStatement}
   * @param ¢ JD
   * @return reference to the list of catchClauses in the argument */
  @SuppressWarnings("unchecked") public static List<CatchClause> catchClauses(final TryStatement ¢) {
    return ¢ == null ? null : ¢.catchClauses();
  }
  /** Expose the loop condition contained in a {@link ForStatement}
   * @param ¢ JD
   * @return reference to the list of initializers contained in the argument */
  public static Expression condition(final ForStatement ¢) {
    return ¢ == null ? null : ¢.getExpression();
  }
  public static Expression condition(final WhileStatement ¢) {
    return ¢ == null ? null : ¢.getExpression();
  }
  /** @param ¢ JD
   * @return */
  public static List<MethodDeclaration> constructors(final AbstractTypeDeclaration ¢) {
    return bodyDeclarations(¢).stream() //
        .map(az::methodDeclaration) //
        .filter(Objects::nonNull) //
        .filter(MethodDeclaration::isConstructor) //
        .collect(toList())//
    ;
  }
  public static Collection<MethodDeclaration> constructors(final ASTNode ¢) {
    return members.of(¢).stream() //
        .map(az::methodDeclaration) //
        .filter(iz::constructor) //
        .filter(Objects::nonNull) //
        .collect(toList()) //
    ;
  }
  @SuppressWarnings("unchecked") public static List<Expression> dimensions(final ArrayCreation ¢) {
    return ¢ == null ? null : ¢.dimensions();
  }
  /** Shorthand for {@link ConditionalExpression#getElseExpression()}
   * @param ¢ JD
   * @return else part of the parameter */
  public static Expression elze(final ConditionalExpression ¢) {
    return ¢ == null ? null : ¢.getElseExpression();
  }
  /** Shorthand for {@link IfStatement#getElseStatement}
   * @param ¢ JD
   * @return else statement of the parameter */
  public static Statement elze(final IfStatement ¢) {
    return ¢ == null ? null : ¢.getElseStatement();
  }
  @SuppressWarnings("unchecked") public static List<EnumConstantDeclaration> enumConstants(final EnumDeclaration ¢) {
    return ¢ == null ? null : ¢.enumConstants();
  }
  public static SingleVariableDeclaration exception(final CatchClause ¢) {
    return ¢ == null ? null : ¢.getException();
  }
  /** @param n a node to extract an expression from
   * @return null if the statement is not an expression, nor a return statement,
   *         nor a throw statement. Otherwise, the expression in these. */
  public static Expression expression(final ASTNode ¢) {
    if (¢ == null)
      return null;
    switch (¢.getNodeType()) {
      case ASTNode.CAST_EXPRESSION:
        return expression((CastExpression) ¢);
      case ASTNode.CLASS_INSTANCE_CREATION:
        return expression((ClassInstanceCreation) ¢);
      case ASTNode.CONDITIONAL_EXPRESSION:
        return expression((ConditionalExpression) ¢);
      case ASTNode.DO_STATEMENT:
        return expression((DoStatement) ¢);
      case ASTNode.ENHANCED_FOR_STATEMENT:
        return az.enhancedFor(¢).getExpression();
      case ASTNode.EXPRESSION_STATEMENT:
        return expression((ExpressionStatement) ¢);
      case ASTNode.FOR_STATEMENT:
        return expression((ForStatement) ¢);
      case ASTNode.IF_STATEMENT:
        return expression((IfStatement) ¢);
      case ASTNode.METHOD_INVOCATION:
        return receiver((MethodInvocation) ¢);
      case ASTNode.PARENTHESIZED_EXPRESSION:
        return expression((ParenthesizedExpression) ¢);
      case ASTNode.RETURN_STATEMENT:
        return expression((ReturnStatement) ¢);
      case ASTNode.SWITCH_CASE:
        return expression((SwitchCase) ¢);
      case ASTNode.SWITCH_STATEMENT:
        return expression((SwitchStatement) ¢);
      case ASTNode.THROW_STATEMENT:
        return expression((ThrowStatement) ¢);
      case ASTNode.WHILE_STATEMENT:
        return expression((WhileStatement) ¢);
      case ASTNode.PREFIX_EXPRESSION:
        return operand((PrefixExpression) ¢);
      case ASTNode.POSTFIX_EXPRESSION:
        return operand((PostfixExpression) ¢);
      default:
        return null;
    }
  }
  public static Expression expression(final CastExpression $) {
    return $ == null ? null : extract.core($.getExpression());
  }
  public static Expression expression(final ClassInstanceCreation $) {
    return $ == null ? null : extract.core($.getExpression());
  }
  public static Expression expression(final ConditionalExpression ¢) {
    return ¢ == null ? null : extract.core(¢.getExpression());
  }
  public static Expression expression(final DoStatement $) {
    return $ == null ? null : extract.core($.getExpression());
  }
  public static Expression expression(final ExpressionStatement $) {
    return $ == null ? null : extract.core($.getExpression());
  }
  public static Expression expression(final FieldAccess ¢) {
    return ¢ == null ? null : ¢.getExpression();
  }
  public static Expression expression(final ForStatement ¢) {
    return ¢ == null ? null : ¢.getExpression();
  }
  public static Expression expression(final IfStatement $) {
    return $ == null ? null : extract.core($.getExpression());
  }
  public static Expression expression(final MethodInvocation ¢) {
    return ¢ == null ? null : ¢.getExpression();
  }
  public static Expression expression(final ParenthesizedExpression $) {
    return $ == null ? null : $.getExpression();
  }
  public static Expression expression(final ReturnStatement $) {
    return $ == null ? null : extract.core($.getExpression());
  }
  public static Expression expression(final SwitchCase ¢) {
    return ¢ == null ? null : ¢.getExpression();
  }
  public static Expression expression(final SwitchStatement ¢) {
    return ¢ == null ? null : ¢.getExpression();
  }
  public static Expression expression(final ThrowStatement $) {
    return $ == null ? null : extract.core($.getExpression());
  }
  public static Expression expression(final WhileStatement ¢) {
    return ¢ == null ? null : ¢.getExpression();
  }
  @SuppressWarnings("unchecked") public static List<Expression> expressions(final ArrayInitializer ¢) {
    return ¢ == null ? null : ¢.expressions();
  }
  @SuppressWarnings("unchecked") public static List<IExtendedModifier> extendedModifiers(final BodyDeclaration ¢) {
    return ¢ == null ? null : ¢.modifiers();
  }
  @SuppressWarnings("unchecked") public static List<IExtendedModifier> extendedModifiers(final SingleVariableDeclaration ¢) {
    return ¢ == null ? null : ¢.modifiers();
  }
  @SuppressWarnings("unchecked") public static List<IExtendedModifier> extendedModifiers(final VariableDeclarationExpression ¢) {
    assert ¢ != null;
    return ¢.modifiers();
  }
  @SuppressWarnings("unchecked") public static List<IExtendedModifier> extendedModifiers(final VariableDeclarationStatement ¢) {
    return ¢ == null ? null : ¢.modifiers();
  }
  /** Expose the list of extended operands in an {@link InfixExpression}
   * @param ¢ JD
   * @return reference to the list of extended operands contained in the
   *         parameter */
  @SuppressWarnings("unchecked") public static List<Expression> extendedOperands(final InfixExpression ¢) {
    return ¢ == null ? null : ¢.extendedOperands();
  }
  /** FieldDeclarations of __
   * @param ¢ JD
   * @return */
  public static FieldDeclaration[] fieldDeclarations(final TypeDeclaration ¢) {
    return ¢ == null ? null : ¢.getFields();
  }
  /** FieldDeclarations names of __
   * @param ¢ JD
   * @return */
  public static Collection<String> fieldDeclarationsNames(final TypeDeclaration ¢) {
    return ¢ == null ? null : Stream.of(¢.getFields()).map(step::names).reduce(an.empty.list(), (x, y) -> {
      x.addAll(y);
      return x;
    });
  }
  public static FieldDeclaration[] fields(final TypeDeclaration ¢) {
    return ¢ == null ? null : ¢.getFields();
  }
  /** Expose the list of fragments in a {@link FieldDeclaration}
   * @param ¢ JD
   * @return reference to the list of fragments in the argument */
  @SuppressWarnings("unchecked") public static List<VariableDeclarationFragment> fragments(final FieldDeclaration ¢) {
    return ¢ == null ? null : ¢.fragments();
  }
  @SuppressWarnings("unchecked") public static List<IDocElement> fragments(final TagElement ¢) {
    return ¢ == null ? null : ¢.fragments();
  }
  /** Expose the list of fragments in a {@link VariableDeclarationExpression}
   * @param ¢ JD
   * @return reference to the list of fragments in the argument */
  @SuppressWarnings("unchecked") public static List<VariableDeclarationFragment> fragments(final VariableDeclarationExpression ¢) {
    return ¢ == null ? empty.list() : ¢.fragments();
  }
  /** Expose the list of fragments in a {@link VariableDeclarationStatement}
   * @param ¢ JD
   * @return reference to the list of fragments in the argument */
  @SuppressWarnings("unchecked") public static List<VariableDeclarationFragment> fragments(final VariableDeclarationStatement ¢) {
    return ¢ == null ? null : ¢.fragments();
  }
  /** Shorthand for {@link Assignment#getRightHandSide()}
   * @param ¢ JD
   * @return right operand of the parameter */
  public static Expression from(final Assignment ¢) {
    return ¢ == null ? null : right(¢);
  }
  public static String identifier(final AnnotationTypeDeclaration ¢) {
    return ¢.getName() + "";
  }
  public static String identifier(final MethodDeclaration ¢) {
    return identifier(name(¢));
  }
  public static String identifier(final MethodInvocation ¢) {
    return identifier(name(¢));
  }
  public static String identifier(final Name ¢) {
    return ¢ == null ? null : iz.simpleName(¢) ? az.simpleName(¢).getIdentifier() : az.qualifiedName(¢).getFullyQualifiedName();
  }
  public static String identifier(final QualifiedName ¢) {
    return ¢ == null ? null : ¢.getFullyQualifiedName();
  }
  public static String identifier(final SimpleName ¢) {
    return ¢ == null ? null : ¢.getIdentifier();
  }
  public static String identifier(final SingleVariableDeclaration ¢) {
    return identifier(name(¢));
  }
  public static String identifier(final VariableDeclarationFragment ¢) {
    return ¢ == null ? null : identifier(¢.getName());
  }
  @SuppressWarnings("unchecked") public static List<ImportDeclaration> importDeclarations(final CompilationUnit ¢) {
    return ¢ == null ? null : ¢.imports();
  }
  @SuppressWarnings("unchecked") public static List<String> importDeclarationsNames(final CompilationUnit ¢) {
    return ¢ == null ? null
        : ((List<ImportDeclaration>) ¢.imports()).stream().map(λ -> (!λ.isStatic() ? "" : "static ") + λ.getName() + (!λ.isOnDemand() ? "" : ".*"))
            .collect(toList());
  }
  @SuppressWarnings("unchecked") public static Collection<ImportDeclaration> imports(final CompilationUnit ¢) {
    return ¢ == null ? null : ¢.imports();
  }
  /** Expose initializer contained in a {@link VariableDeclaration}
   * @param ¢ JD
   * @return initializer */
  public static Expression initializer(final VariableDeclaration ¢) {
    return ¢ == null ? null : ¢.getInitializer();
  }
  public static Collection<Initializer> initializers(final ASTNode ¢) {
    return members.of(¢).stream().map(az::initializer).filter(Objects::nonNull).collect(toList());
  }
  /** Expose the list of initializers contained in a {@link ForStatement}
   * @param ¢ JD
   * @return reference to the list of initializers contained in the argument */
  @SuppressWarnings("unchecked") public static List<Expression> initializers(final ForStatement ¢) {
    return ¢ == null ? null : ¢.initializers();
  }
  public static List<Initializer> initializersClass(final ASTNode ¢) {
    return initializers(¢).stream().filter(iz::static¢).collect(toList());
  }
  public static Collection<Initializer> initializersInstance(final ASTNode n) {
    return initializers(n).stream().filter(λ -> !iz.static¢(λ)).collect(toList());
  }
  public static Javadoc javadoc(final BodyDeclaration ¢) {
    return ¢ == null ? null : ¢.getJavadoc();
  }
  public static SimpleName label(final BreakStatement ¢) {
    return ¢ == null ? null : ¢.getLabel();
  }
  public static SimpleName label(final ContinueStatement ¢) {
    return ¢ == null ? null : ¢.getLabel();
  }
  /** Shorthand for {@link Assignment#getLeftHandSide()}
   * @param ¢ JD
   * @return left side of the assignment */
  public static Expression left(final Assignment ¢) {
    return ¢ == null ? null : ¢.getLeftHandSide();
  }
  /** Shorthand for {@link InfixExpression#getLeftOperand()}
   * @param ¢ JD
   * @return left operand of the parameter */
  public static Expression left(final InfixExpression ¢) {
    return ¢ == null ? null : ¢.getLeftOperand();
  }
  /** Shorthand for {@link InstanceofExpression#getLeftOperand()}
   * @param ¢ JD
   * @return left operand of the parameter */
  public static Expression left(final InstanceofExpression ¢) {
    return ¢ == null ? null : ¢.getLeftOperand();
  }
  public static List<ASTNode> marchingList(final ASTNode ¢) {
    final List<ASTNode> $ = an.empty.list();
    for (final Object s : ¢.structuralPropertiesForType()) {
      final Object child = ¢.getStructuralProperty((StructuralPropertyDescriptor) s);
      if (iz.astNode(child))
        $.add(az.astNode(child));
    }
    return $;
  }
  /** @param ¢ JD
   * @return */
  private static Collection<String> methodNames(final AbstractTypeDeclaration ¢) {
    return ¢ == null ? null : methods(¢).stream().map(step::name).map(step::identifier).collect(toList());
  }
  public static Collection<String> methodNames(final CompilationUnit u) {
    if (u == null)
      return null;
    final List<String> $ = an.empty.list();
    types(u).forEach(λ -> $.addAll(methodNames(λ)));
    return $;
  }
  /** @param ¢ JD
   * @return */
  @SuppressWarnings("unchecked") public static List<MethodDeclaration> methods(final AbstractTypeDeclaration ¢) {
    return ¢ == null ? null
        : iz.typeDeclaration(¢) ? as.list(az.typeDeclaration(¢).getMethods())
            : iz.enumDeclaration(¢)
                ? (List<MethodDeclaration>) az.enumDeclaration(¢).bodyDeclarations().stream().filter(λ -> iz.methodDeclaration(az.astNode(λ)))
                    .collect(toList())
                : null;
  }
  /** @param ¢ JD
   * @return */
  public static List<MethodDeclaration> methods(final AnonymousClassDeclaration ¢) {
    return step.bodyDeclarations(¢).stream().map(az::methodDeclaration).filter(Objects::nonNull).collect(toList());
  }
  /** get all methods
   * @param u JD
   * @return */
  public static List<MethodDeclaration> methods(final CompilationUnit u) {
    if (u == null)
      return null;
    final List<MethodDeclaration> $ = an.empty.list();
    types(u).forEach(λ -> $.addAll(methods(λ)));
    return $;
  }
  public static Collection<?> modifiers(final FieldDeclaration ¢) {
    return ¢ == null ? null : ¢.modifiers();
  }
  public static List<?> modifiers(final MethodDeclaration ¢) {
    return ¢ == null ? null : ¢.modifiers();
  }
  public static List<?> modifiers(final VariableDeclarationStatement ¢) {
    return ¢ == null ? null : ¢.modifiers();
  }
  public static SimpleName name(final AbstractTypeDeclaration ¢) {
    return ¢ == null ? null : ¢.getName();
  }
  public static SimpleName name(final AnnotationTypeMemberDeclaration ¢) {
    return ¢ == null ? null : ¢.getName();
  }
  public static SimpleName name(final EnumConstantDeclaration ¢) {
    return ¢ == null ? null : ¢.getName();
  }
  /** @param ¢ JD
   * @return */
  public static SimpleName name(final FieldAccess ¢) {
    return ¢ == null ? null : ¢.getName();
  }
  public static Name name(final ImportDeclaration ¢) {
    return ¢ == null ? null : ¢.getName();
  }
  public static SimpleName name(final MethodDeclaration ¢) {
    return ¢ == null ? null : ¢.getName();
  }
  public static SimpleName name(final MethodInvocation ¢) {
    return ¢ == null ? null : ¢.getName();
  }
  public static SimpleName name(final QualifiedName ¢) {
    return ¢ == null ? null : ¢.getName();
  }
  public static Name name(final SimpleType ¢) {
    return ¢ == null ? null : ¢.getName();
  }
  public static SimpleName name(final SingleVariableDeclaration ¢) {
    return ¢ == null ? null : ¢.getName();
  }
  public static SimpleName name(final SuperMethodInvocation ¢) {
    return ¢ == null ? null : ¢.getName();
  }
  /** @param ¢ JD
   * @return */
  public static SimpleName name(final VariableDeclaration ¢) {
    return ¢ == null ? null : ¢.getName();
  }
  private static List<String> names(final FieldDeclaration d) {
    return d == null ? null : fragments(d).stream().map(λ -> identifier(name(λ))).collect(toList());
  }
  public static int nodeType(final ASTNode ¢) {
    return ¢ == null ? 0 : ¢.getNodeType();
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
  public static PackageDeclaration packageDeclaration(final CompilationUnit ¢) {
    return ¢ == null ? null : ¢.getPackage();
  }
  @SuppressWarnings("unchecked") public static List<? extends VariableDeclaration> parameters(final LambdaExpression ¢) {
    return ¢ == null ? null : ¢.parameters();
  }
  /** Expose the list of parameters in a {@link MethodDeclaration}
   * @param ¢ JD
   * @return result of method {@link MethodDeclaration#parameters} downcasted to
   *         its correct __ */
  @SuppressWarnings("unchecked") public static List<SingleVariableDeclaration> parameters(final MethodDeclaration ¢) {
    return ¢ == null ? null : ¢.parameters();
  }
  /** Expose the list of parameters names in a {@link MethodDeclaration}
   * @param d JD
   * @return */
  public static List<String> parametersNames(final MethodDeclaration d) {
    return d == null ? null : step.parameters(d).stream().map(λ -> λ.getName() + "").collect(toList());
  }
  /** Expose the list of parameters types in a {@link MethodDeclaration}
   * @param ¢ JD
   * @return */
  public static List<Type> parametersTypes(final MethodDeclaration ¢) {
    return step.parameters(¢).stream().map(step::type).collect(toList());
  }
  /** Shorthand for {@link ASTNode#getParent()}
   * @param ¢ JD
   * @return parent of the parameter */
  public static ASTNode parent(final ASTNode ¢) {
    return ¢ == null ? null : ¢.getParent();
  }
  /** @param ¢ current {@link Statement}.
   * @return the previous {@link Statement} in the parent {@link Block}. If parent
   *         is not {@link Block} return null, if n is first {@link Statement}
   *         also null. */
  public static Statement previousStatementInBody(final Statement ¢) {
    return the.previous(¢, statements(az.block(parent(¢))));
  }
  public static Expression receiver(final MethodInvocation ¢) {
    return ¢ == null ? null : extract.core(¢.getExpression());
  }
  /** Expose the list of resources contained in a {@link TryStatement}
   * @param ¢ JD
   * @return reference to the list of resources contained in the argument */
  @SuppressWarnings("unchecked") public static List<VariableDeclarationExpression> resources(final TryStatement ¢) {
    return ¢ == null ? null : ¢.resources();
  }
  /** Returns the return __ of the function
   * @param ¢ JD
   * @return */
  public static Type returnType(final MethodDeclaration ¢) {
    return ¢ == null ? null : ¢.getReturnType2();
  }
  /** Shorthand for {@link Assignment#getRightHandSide()}
   * @param ¢ JD
   * @return right side of the assignment */
  public static Expression right(final Assignment ¢) {
    return ¢ == null ? null : ¢.getRightHandSide();
  }
  /** Shorthand for {@link CastExpression#getExpression()}
   * @param ¢ JD
   * @return right operand of the parameter */
  public static Expression right(final CastExpression ¢) {
    return ¢ == null ? null : ¢.getExpression();
  }
  /** Shorthand for {@link InfixExpression#getRightOperand()}
   * @param ¢ JD
   * @return right operand of the parameter */
  public static Expression right(final InfixExpression ¢) {
    return ¢ == null ? null : ¢.getRightOperand();
  }
  public static List<Statement> statements(final ASTNode ¢) {
    return iz.block(¢) ? statements(az.block(¢)) : iz.switchStatement(¢) ? statements(az.switchStatement(¢)) : null;
  }
  /** Expose the list of sideEffects contained in a {@link Block}
   * @param ¢ JD
   * @return reference to the list of sideEffects contained in the argument */
  @SuppressWarnings("unchecked") public static List<Statement> statements(final Block ¢) {
    return ¢ == null ? null : ¢.statements();
  }
  public static List<Statement> statements(final LambdaExpression ¢) {
    return statements(body(¢));
  }
  public static List<Statement> statements(final MethodDeclaration ¢) {
    return ¢ == null ? null : statements(body(¢));
  }
  /** Expose the list of sideEffects contained in a {@link SwitchStatement}
   * @param ¢ JD
   * @return reference to the list of statements contained in the argument */
  @SuppressWarnings("unchecked") public static List<Statement> statements(final SwitchStatement ¢) {
    return ¢ == null ? null : ¢.statements();
  }
  /** @param ¢ JD
   * @return */
  public static List<Statement> statements(final SynchronizedStatement ¢) {
    return ¢ == null ? null : statements(body(¢));
  }
  @SuppressWarnings("unchecked") public static List<TagElement> tags(final Javadoc ¢) {
    return ¢ == null ? null : ¢.tags();
  }
  /** Shorthand for {@link ConditionalExpression#getThenExpression()}
   * @param ¢ JD
   * @return then part of the parameter */
  public static Expression then(final ConditionalExpression ¢) {
    return ¢ == null ? null : ¢.getThenExpression();
  }
  /** Shorthand for {@link IfStatement#getThenStatement}
   * @param ¢ JD
   * @return then statement of the parameter */
  public static Statement then(final IfStatement ¢) {
    return ¢ == null ? null : ¢.getThenStatement();
  }
  /** Shorthand for {@link Assignment#getLeftHandSide()}
   * @param ¢ JD
   * @return left operand of the parameter */
  public static Expression to(final Assignment ¢) {
    return ¢ == null ? null : left(¢);
  }
  /** Shorthand for {@link NumberLiteral#getToken()}
   * @param ¢ JD
   * @return the token representing the number */
  public static String token(final NumberLiteral ¢) {
    return ¢ == null ? null : ¢.getToken();
  }
  /** A little hack to get Type out of TypeDeclaration.
   * @param d JD
   * @return */
  public static Type type(final AbstractTypeDeclaration d) {
    if (d == null)
      return null;
    String typeType = iz.typeDeclaration(d) ? "class" : iz.enumDeclaration(d) ? "enum" : "annotation";
    if (!(d + "").contains(typeType) && iz.typeDeclaration(d))
      typeType = "interface";
    String $ = (d + "").substring((d + "").indexOf(typeType));
    $ = $.substring($.indexOf(typeType) + typeType.length(), $.indexOf("{"));
    while ($.contains("extends") && !balanced($.substring(0, $.indexOf("extends"))))
      for (int i = $.indexOf("extends"), openers = 0, ¢ = i + 7;; ++¢) {
        if ($.charAt(¢) == ',' && openers <= 0) {
          $ = $.substring(0, i) + $.substring(¢);
          break;
        }
        if ($.charAt(¢) == '<')
          ++openers;
        else if ($.charAt(¢) == '>') {
          --openers;
          if (openers == 0) {
            $ = $.substring(0, i) + $.substring(¢ + 1);
            break;
          }
          if (openers < 0) {
            $ = $.substring(0, i) + $.substring(¢);
            break;
          }
        }
      }
    $ = $.replaceAll("implements [^{]+", "").replaceAll("extends [^{]+", "");
    return findFirst.instanceOf(Type.class).in(make.ast("class d{" + $.replaceAll("extends .+", "") + " x; }"));
  }
  /** Shorthand for {@link CastExpression#getType()}
   * @param ¢ JD
   * @return the Type of the {@link castExpression} */
  public static Type type(final CastExpression ¢) {
    return ¢ == null ? null : ¢.getType();
  }
  /** Shorthand for {@link ClassInstanceCreation#getType()}
   * @param ¢ JD
   * @return the Type of the {@link ClassInstanceCreation} */
  public static Type type(final ClassInstanceCreation ¢) {
    return ¢ == null ? null : ¢.getType();
  }
  /** @param ¢ JD
   * @return */
  public static Type type(final FieldDeclaration ¢) {
    return ¢ == null ? null : ¢.getType();
  }
  /** Shorthand for {@link InstanceofExpression#getRightOperand()}
   * @param ¢ JD
   * @return the Type of the right operand */
  public static Type type(final InstanceofExpression ¢) {
    return ¢ == null ? null : ¢.getRightOperand();
  }
  public static Type type(final ParameterizedType ¢) {
    return ¢ == null ? null : ¢.getType();
  }
  /** @param p JD
   * @return */
  public static Type type(final SingleVariableDeclaration ¢) {
    return ¢ == null ? null : ¢.getType();
  }
  /** Shorthand for {@link VariableDeclarationExpression#getType()}
   * @param ¢ JD
   * @return the Type of the {@link VariableDeclarationExpression} */
  public static Type type(final VariableDeclarationExpression ¢) {
    return ¢ == null ? null : ¢.getType();
  }
  /** @param ¢ JD
   * @return */
  public static Type type(final VariableDeclarationFragment ¢) {
    return ¢ == null || ¢.getParent() == null ? null : type(az.variableDeclarationStatement(¢.getParent()));
  }
  public static Type type(final VariableDeclarationStatement ¢) {
    return ¢ == null ? null : ¢.getType();
  }
  /** @param ¢ JD
   * @return */
  @SuppressWarnings("unchecked")
  // public static Type __(final VariableDeclarationStatement ¢) {
  // return ¢ == null ? null : ¢.getType();
  // }
  public static List<Type> typeArguments(final ParameterizedType ¢) {
    return ¢ == null ? null : ¢.typeArguments();
  }
  @SuppressWarnings("unchecked") public static List<Type> typeBounds(final TypeParameter ¢) {
    return ¢ == null ? null : ¢.typeBounds();
  }
  public static Name typeName(final Annotation ¢) {
    return ¢ == null ? null : ¢.getTypeName();
  }
  /** @param ¢ JD
   * @return types in ¢ */
  @SuppressWarnings("unchecked") public static List<AbstractTypeDeclaration> types(final CompilationUnit ¢) {
    return ¢ == null ? null : ¢.types();
  }
  @SuppressWarnings("unchecked") public static List<Type> types(final UnionType ¢) {
    return ¢ == null ? null : ¢.types();
  }
  /** Expose the list of updaters contained in a {@link ForStatement}
   * @param ¢ JD
   * @return reference to the list of initializers contained in the argument */
  @SuppressWarnings("unchecked") public static List<Expression> updaters(final ForStatement ¢) {
    return ¢ == null ? null : ¢.updaters();
  }
  public static Expression value(final SingleMemberAnnotation ¢) {
    return ¢ == null ? null : ¢.getValue();
  }
  @SuppressWarnings("unchecked") public static List<MemberValuePair> values(final NormalAnnotation ¢) {
    return ¢ == null ? null : ¢.values();
  }
  public static Expression condition(DoStatement ¢) {
    return ¢ == null ? null : ¢.getExpression();
  }
}
