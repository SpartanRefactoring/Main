package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;

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
    return ¢ == null ? null : ¢.arguments();
  }

  /** Expose the list of arguments in a {@link MethodInvocation}
   * @param ¢ JD
   * @return reference to the list of arguments in the argument */
  @SuppressWarnings("unchecked") public static List<Expression> arguments(final MethodInvocation ¢) {
    return ¢ == null ? null : ¢.arguments();
  }

  /** Expose the list of arguments in a {@link SuperMethodInvocation}
   * @param ¢ JD
   * @return reference to the list of arguments in the argument */
  @SuppressWarnings("unchecked") public static List<Expression> arguments(final SuperMethodInvocation ¢) {
    return ¢ == null ? null : ¢.arguments();
  }

  private static boolean balanced(final String s) {
    int $ = 0;
    for (final Integer ¢ : range.from(0).to(s.length()))
      if (s.charAt(¢.intValue()) == '<')
        ++$;
      else if (s.charAt(¢.intValue()) == '>')
        --$;
    return $ == 0;
  }

  public static Block body(final CatchClause ¢) {
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

  public static ASTNode body(final TryStatement ¢) {
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

  /** Expose the list of bodyDeclarations in an
   * {@link AnonymousClassDeclaration}
   * @param ¢ JD
   * @return reference to the list of bodyDeclarations in the argument */
  @SuppressWarnings("unchecked") public static List<BodyDeclaration> bodyDeclarations(final AnonymousClassDeclaration ¢) {
    return ¢ == null ? null : ¢.bodyDeclarations();
  }

  public static List<ConditionalExpression> branches(final ConditionalExpression ¢) {
    if (¢ == null)
      return null;
    ConditionalExpression s = ¢;
    final List<ConditionalExpression> $ = new ArrayList<>();
    $.add(s);
    for (; iz.conditionalExpression(elze(s));)
      $.add(s = az.conditionalExpression(elze(s)));
    return $;
  }

  /** Given an IfStatement of the form: <br>
   * if(a) <br>
   * <t> B1<br>
   * else if(b) <br>
   * <t> <t>B2<br>
   * else if(c)<br>
   * <t><t> B3<br>
   * ... <br>
   * else <br>
   * <t><t> Bn <br>
   * Retreives all If branches
   * @param ¢ JD
   * @return */
  public static List<IfStatement> branches(final IfStatement ¢) {
    if (¢ == null)
      return null;
    IfStatement s = ¢;
    final List<IfStatement> $ = new ArrayList<>();
    $.add(s);
    for (; iz.ifStatement(elze(s));)
      $.add(s = az.ifStatement(elze(s)));
    return $;
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

  /** FieldDeclarations of type
   * @param ¢ JD
   * @return */
  public static FieldDeclaration[] fieldDeclarations(final TypeDeclaration ¢) {
    return ¢ == null ? null : ¢.getFields();
  }

  /** FieldDeclarations names of type
   * @param ¢ JD
   * @return */
  public static List<String> fieldDeclarationsNames(final TypeDeclaration ¢) {
    return ¢ == null ? null : Stream.of(¢.getFields()).map(step::names).reduce(new ArrayList<>(), (x, y) -> {
      x.addAll(y);
      return x;
    });
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
    return ¢ != null ? ¢.fragments() : new ArrayList<>();
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
        : ((List<ImportDeclaration>) ¢.imports()).stream().map(x -> (!x.isStatic() ? "" : "static ") + x.getName() + (!x.isOnDemand() ? "" : ".*"))
            .collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked") public static List<ImportDeclaration> imports(final CompilationUnit ¢) {
    return ¢ == null ? null : ¢.imports();
  }

  /** Expose initializer contained in a {@link VariableDeclaration}
   * @param ¢ JD
   * @return initializer */
  public static Expression initializer(final VariableDeclaration ¢) {
    return ¢ == null ? null : ¢.getInitializer();
  }

  /** Expose the list of initializers contained in a {@link ForStatement}
   * @param ¢ JD
   * @return reference to the list of initializers contained in the argument */
  @SuppressWarnings("unchecked") public static List<Expression> initializers(final ForStatement ¢) {
    return ¢ == null ? null : ¢.initializers();
  }

  /** @param ¢ JD
   * @return */
  public static Javadoc javadoc(final MethodDeclaration ¢) {
    return ¢ == null ? null : ¢.getJavadoc();
  }

  /** @param ¢ JD
   * @return */
  public static SimpleName label(final ContinueStatement ¢) {
    return ¢ == null ? null : ¢.getLabel();
  }

  public static Expression lastElse(final ConditionalExpression ¢) {
    if (¢ == null)
      return null;
    ConditionalExpression $ = ¢;
    for (; iz.conditionalExpression(elze($));)
      $ = az.conditionalExpression(elze($));
    return elze($);
  }

  /** returns the else statement of the last if in an if else if else if else
   * sequence
   * @param ¢
   * @return */
  public static Statement lastElse(final IfStatement ¢) {
    if (¢ == null)
      return null;
    IfStatement $ = ¢;
    for (; iz.ifStatement(elze($));)
      $ = az.ifStatement(elze($));
    return elze($);
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
    final List<ASTNode> $ = new ArrayList<>();
    for (final Object s : ¢.structuralPropertiesForType()) {
      final Object child = ¢.getStructuralProperty((StructuralPropertyDescriptor) s);
      if (iz.astNode(child))
        $.add(az.astNode(child));
    }
    return $;
  }

  /** @param d JD
   * @return */
  private static List<String> methodNames(final AbstractTypeDeclaration d) {
    return d == null ? null : methods(d).stream().map(m -> identifier(name(m))).collect(Collectors.toList());
  }

  public static List<String> methodNames(final CompilationUnit u) {
    if (u == null)
      return null;
    final List<String> $ = new ArrayList<>();
    types(u).forEach(t -> $.addAll(methodNames(t)));
    return $;
  }

  /** @param ¢ JD
   * @return */
  public static List<MethodDeclaration> methods(final AnonymousClassDeclaration d) {
    final List<MethodDeclaration> $ = new ArrayList<>();
    for (final BodyDeclaration x : step.bodyDeclarations(d)) {
      final MethodDeclaration y = az.methodDeclaration(x);
      if (y != null)
        $.add(y);
    }
    return $;
  }

  /** @param ¢ JD
   * @return */
  @SuppressWarnings("unchecked") public static List<MethodDeclaration> methods(final AbstractTypeDeclaration ¢) {
    return ¢ == null ? null
        : iz.typeDeclaration(¢) ? Arrays.asList(az.typeDeclaration(¢).getMethods())
            : iz.enumDeclaration(¢) ? (List<MethodDeclaration>) az.enumDeclaration(¢).bodyDeclarations().stream()
                .filter(d -> iz.methodDeclaration(az.astNode(d))).collect(Collectors.toList()) : null;
  }

  /** get all methods
   * @param u JD
   * @return */
  public static List<MethodDeclaration> methods(final CompilationUnit u) {
    if (u == null)
      return null;
    final List<MethodDeclaration> $ = new ArrayList<>();
    types(u).forEach(t -> $.addAll(methods(t)));
    return $;
  }

  public static SimpleName name(final AbstractTypeDeclaration ¢) {
    return ¢ == null ? null : ¢.getName();
  }

  /** @param ¢ JD
   * @return */
  public static SimpleName name(final FieldAccess ¢) {
    return ¢ == null ? null : ¢.getName();
  }

  public static SimpleName name(final MethodDeclaration ¢) {
    return ¢ == null ? null : ¢.getName();
  }

  public static SimpleName name(final MethodInvocation ¢) {
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
    return d == null ? null : fragments(d).stream().map(x -> identifier(name(x))).collect(Collectors.toList());
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
   *         its correct type */
  @SuppressWarnings("unchecked") public static List<SingleVariableDeclaration> parameters(final MethodDeclaration ¢) {
    return ¢ == null ? null : ¢.parameters();
  }

  /** Expose the list of parameters names in a {@link MethodDeclaration}
   * @param d JD
   * @return */
  public static List<String> parametersNames(final MethodDeclaration d) {
    return d == null ? null : new ArrayList<>(step.parameters(d).stream().map(x -> x.getName() + "").collect(Collectors.toList()));
  }

  /** Expose the list of parameters types in a {@link MethodDeclaration}
   * @param ¢ JD
   * @return */
  public static List<Type> parametersTypes(final MethodDeclaration ¢) {
    return new ArrayList<>(step.parameters(¢).stream().map(step::type).collect(Collectors.toList()));
  }

  /** Shorthand for {@link ASTNode#getParent()}
   * @param ¢ JD
   * @return parent of the parameter */
  public static ASTNode parent(final ASTNode ¢) {
    return ¢ == null ? null : ¢.getParent();
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

  /** Returns the return type of the function
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

  /** Expose the list of sideEffects contained in a {@link Block}
   * @param ¢ JD
   * @return reference to the list of sideEffects contained in the argument */
  @SuppressWarnings("unchecked") public static List<Statement> statements(final Block ¢) {
    return ¢ == null ? null : ¢.statements();
  }

  public static List<Statement> statements(final MethodDeclaration ¢) {
    return ¢ == null ? null : statements(body(¢));
  }

  /** Expose the list of sideEffects contained in a {@link SwitchStatement}
   * @param ¢ JD
   * @return reference to the list of sideEffects contained in the argument */
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
      for (int idx = $.indexOf("extends"), openers = 0, ¢ = idx + 7;; ++¢) {
        if ($.charAt(¢) == ',' && openers <= 0) {
          $ = $.substring(0, idx) + $.substring(¢);
          break;
        }
        if ($.charAt(¢) == '<')
          ++openers;
        else if ($.charAt(¢) == '>') {
          --openers;
          if (openers == 0) {
            $ = $.substring(0, idx) + $.substring(¢ + 1);
            break;
          }
          if (openers < 0) {
            $ = $.substring(0, idx) + $.substring(¢);
            break;
          }
        }
      }
    $ = $.replaceAll("implements [^{]+", "").replaceAll("extends [^{]+", "");
    return findFirst.type(wizard.ast("class d{" + $.replaceAll("extends .+", "") + " x; }"));
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
  // public static Type type(final VariableDeclarationStatement ¢) {
  // return ¢ == null ? null : ¢.getType();
  // }
  @SuppressWarnings("unchecked") public static List<Type> typeArguments(final ParameterizedType ¢) {
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

  public static String typeString(final ASTNode ¢) {
    return ¢ == null ? "" : ¢.getClass().getSimpleName();
  }

  /** Expose the list of updaters contained in a {@link ForStatement}
   * @param ¢ JD
   * @return reference to the list of initializers contained in the argument */
  @SuppressWarnings("unchecked") public static List<Expression> updaters(final ForStatement ¢) {
    return ¢ == null ? null : ¢.updaters();
  }

  @SuppressWarnings("unchecked") public static List<MemberValuePair> values(final NormalAnnotation ¢) {
    return ¢ == null ? null : ¢.values();
  }
}
