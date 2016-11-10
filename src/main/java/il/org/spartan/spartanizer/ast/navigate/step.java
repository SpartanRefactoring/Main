package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;

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
  public static Statement body(final ForStatement ¢) {
    return ¢.getBody();
  }
  public static Block body(final MethodDeclaration ¢) {
    return ¢.getBody();
  }
  public static Statement body(final WhileStatement ¢) {
    return ¢.getBody();
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
  /** Expose the loop condition contained in a {@link ForStatement}
   * @param ¢ JD
   * @return reference to the list of initializers contained in the argument */
  public static Expression condition(final ForStatement ¢) {
    return ¢.getExpression();
  }
  public static Expression condition(final WhileStatement ¢) {
    return ¢.getExpression();
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
    switch (¢.getNodeType()) {
      case ASTNode.EXPRESSION_STATEMENT:
        return expression((ExpressionStatement) ¢);
      case ASTNode.WHILE_STATEMENT:
        return expression((WhileStatement) ¢);
      case ASTNode.FOR_STATEMENT:
        return expression((ForStatement) ¢);
      case ASTNode.RETURN_STATEMENT:
        return expression((ReturnStatement) ¢);
      case ASTNode.THROW_STATEMENT:
        return expression((ThrowStatement) ¢);
      case ASTNode.CLASS_INSTANCE_CREATION:
        return expression((ClassInstanceCreation) ¢);
      case ASTNode.CAST_EXPRESSION:
        return expression((CastExpression) ¢);
      case ASTNode.ENHANCED_FOR_STATEMENT:
        return az.enhancedFor(¢).getExpression();
      case ASTNode.METHOD_INVOCATION:
        return receiver((MethodInvocation) ¢);
      case ASTNode.PARENTHESIZED_EXPRESSION:
        return expression((ParenthesizedExpression) ¢);
      case ASTNode.DO_STATEMENT:
        return expression((DoStatement) ¢);
      case ASTNode.IF_STATEMENT:
        return expression((IfStatement) ¢);
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
  public static Expression expression(final ForStatement ¢) {
    return ¢.getExpression();
  }
  public static Expression expression(final IfStatement $) {
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
  public static Expression expression(final WhileStatement ¢) {
    return ¢.getExpression();
  }
  @SuppressWarnings("unchecked") public static List<Expression> expressions(final ArrayInitializer ¢) {
    return ¢.expressions();
  }
  @SuppressWarnings("unchecked") public static List<IExtendedModifier> extendedModifiers(final BodyDeclaration ¢) {
    return ¢.modifiers();
  }
  @SuppressWarnings("unchecked") public static List<IExtendedModifier> extendedModifiers(final SingleVariableDeclaration ¢) {
    return ¢.modifiers();
  }
  @SuppressWarnings("unchecked") public static List<IExtendedModifier> extendedModifiers(final VariableDeclarationExpression ¢) {
    assert ¢ != null;
    return ¢.modifiers();
  }
  @SuppressWarnings("unchecked") public static List<IExtendedModifier> extendedModifiers(final VariableDeclarationStatement ¢) {
    return ¢.modifiers();
  }
  /** Expose the list of extended operands in an {@link InfixExpression}
   * @param ¢ JD
   * @return reference to the list of extended operands contained in the
   *         parameter */
  @SuppressWarnings("unchecked") public static List<Expression> extendedOperands(final InfixExpression ¢) {
    return ¢.extendedOperands();
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
    return ¢ == null ? null : Arrays.asList(¢.getFields()).stream().map(x -> names(x)).reduce(new ArrayList<>(), (x, y) -> {
      x.addAll(y);
      return x;
    });
  }
  /** @param d
   * @return */
  private static List<String> names(final FieldDeclaration d) {
    return d == null ? null : fragments(d).stream().map(x -> identifier(name(x))).collect(Collectors.toList());
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
  /** Shorthand for {@link Assignment#getRightHandSide()}
   * @param ¢ JD
   * @return right operand of the parameter */
  public static Expression from(final Assignment ¢) {
    return ¢.getRightHandSide();
  }
  public static String identifier(final MethodInvocation ¢) {
    return identifier(name(¢));
  }
  public static String identifier(final SimpleName ¢) {
    return ¢.getIdentifier();
  }
  @SuppressWarnings("unchecked") public static List<ImportDeclaration> importDeclarations(final CompilationUnit ¢) {
    return ¢.imports();
  }
  @SuppressWarnings("unchecked") public static List<String> importDeclarationsNames(final CompilationUnit ¢) {
    return ((List<ImportDeclaration>) ¢.imports()).stream().map(x -> (!x.isStatic() ? "" : "static ") + x.getName() + (!x.isOnDemand() ? "" : ".*"))
        .collect(Collectors.toList());
  }
  /** Expose the list of initializers contained in a {@link ForStatement}
   * @param ¢ JD
   * @return reference to the list of initializers contained in the argument */
  @SuppressWarnings("unchecked") public static List<Expression> initializers(final ForStatement ¢) {
    return ¢.initializers();
  }
  /** Shorthand for {@link Assignment#getLeftHandSide()}
   * @param ¢ JD
   * @return left side of the assignment */
  public static Expression left(final Assignment ¢) {
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
  /** Expose the list of parameters names in a {@link MethodDeclaration}
   * @param d JD
   * @return */
  public static List<String> parametersNames(final MethodDeclaration d) {
    return new ArrayList<>(step.parameters(d).stream().map(x -> x.getName() + "").collect(Collectors.toList()));
  }
  /** Expose the list of parameters types in a {@link MethodDeclaration}
   * @param d JD
   * @return */
  public static List<Type> parametersTypes(final MethodDeclaration d) {
    return new ArrayList<>(step.parameters(d).stream().map(x -> step.type(x)).collect(Collectors.toList()));
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
  /** Expose the list of sideEffects contained in a {@link Block}
   * @param ¢ JD
   * @return reference to the list of sideEffects contained in the argument */
  @SuppressWarnings("unchecked") public static List<Statement> statements(final Block ¢) {
    return ¢.statements();
  }
  /** Expose the list of sideEffects contained in a {@link SwitchStatement}
   * @param ¢ JD
   * @return reference to the list of sideEffects contained in the argument */
  @SuppressWarnings("unchecked") public static List<Statement> statements(final SwitchStatement ¢) {
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
  /** Shorthand for {@link Assignment#getLeftHandSide()}
   * @param ¢ JD
   * @return left operand of the parameter */
  public static Expression to(final Assignment ¢) {
    return ¢.getLeftHandSide();
  }
  /** Shorthand for {@link NumberLiteral#getToken()}
   * @param ¢ JD
   * @return the token representing the number */
  public static String token(final NumberLiteral ¢) {
    return ¢.getToken();
  }
  /** Shorthand for {@link CastExpression#getType()}
   * @param ¢ JD
   * @return the Type of the {@link castExpression} */
  public static Type type(final CastExpression ¢) {
    return ¢.getType();
  }
  /** Shorthand for {@link InstanceofExpression#getRightOperand()}
   * @param ¢ JD
   * @return the Type of the right operand */
  public static Type type(final InstanceofExpression ¢) {
    return ¢.getRightOperand();
  }
  /** Shorthand for {@link ClassInstanceCreation#getType()}
   * @param ¢ JD
   * @return the Type of the {@link ClassInstanceCreation} */
  public static Type type(final ClassInstanceCreation ¢) {
    return ¢.getType();
  }
  /** Shorthand for {@link VariableDeclarationExpression#getType()}
   * @param ¢ JD
   * @return the Type of the {@link VariableDeclarationExpression} */
  public static Type type(final VariableDeclarationExpression ¢) {
    return ¢.getType();
  }
  @SuppressWarnings("unchecked") public static List<Type> typeArguments(final ParameterizedType ¢) {
    return ¢.typeArguments();
  }
  /** @param ¢ JD
   * @return types in ¢ */
  @SuppressWarnings("unchecked") public static List<AbstractTypeDeclaration> types(final CompilationUnit ¢) {
    return ¢.types();
  }
  /** Expose the list of updaters contained in a {@link ForStatement}
   * @param ¢ JD
   * @return reference to the list of initializers contained in the argument */
  @SuppressWarnings("unchecked") public static List<Expression> updaters(final ForStatement ¢) {
    return ¢.updaters();
  }
  @SuppressWarnings("unchecked") public static List<MemberValuePair> values(final NormalAnnotation ¢) {
    return ¢.values();
  }
  /** @param ¢ JD
   * @return */
  @SuppressWarnings("unchecked") public static List<MethodDeclaration> methods(final AbstractTypeDeclaration ¢) {
    return ¢ == null ? null
        : iz.typeDeclaration(¢) ? Arrays.asList(az.typeDeclaration(¢).getMethods())
            : iz.enumDeclaration(¢) ? (List<MethodDeclaration>) az.enumDeclaration(¢).bodyDeclarations().stream()
                .filter(d -> iz.methodDeclaration((ASTNode) d)).collect(Collectors.toList()) : null;
  }
  /** @param p JD
   * @return */
  public static Type type(final SingleVariableDeclaration ¢) {
    return ¢ == null ? null : ¢.getType();
  }
  /** @param ¢ JD
   * @return */
  public static SimpleName name(final VariableDeclarationFragment ¢) {
    return ¢ == null ? null : ¢.getName();
  }
  /** @param ¢ JD
   * @return */
  public static Type type(final FieldDeclaration ¢) {
    return ¢ == null ? null : ¢.getType();
  }
  /** @param ¢
   * @return */
  public static SimpleName name(final SingleVariableDeclaration ¢) {
    return ¢ == null ? null : ¢.getName();
  }
  /** @param ¢ JD
   * @return */
  public static Type type(final VariableDeclarationStatement ¢) {
    return ¢ == null ? null : ¢.getType();
  }
  /** @param ¢ JD
   * @return */
  public static Type type(final VariableDeclarationFragment ¢) {
    return ¢ == null || ¢.getParent() == null ? null : type(az.variableDeclarationStatement(¢.getParent()));
  }
  /** A little hack to get Type out of TypeDeclaration.
   * @param d JD
   * @return */
  public static Type type(final AbstractTypeDeclaration d) {
    if (d == null)
      return null;
    final String typeType = iz.typeDeclaration(d) ? "class" : iz.enumDeclaration(d) ? "enum" : "annotation";
    String name = (d + "").substring((d + "").indexOf(typeType));
    name = name.substring(name.indexOf(typeType) + typeType.length(), name.indexOf("{"));
    while (name.contains("extends") && !balanced(name.substring(0, name.indexOf("extends"))))
      for (int idx = name.indexOf("extends"), openers = 0, ¢ = idx + 7;; ++¢) {
        if (name.charAt(¢) == ',' && openers <= 0) {
          name = name.substring(0, idx) + name.substring(¢);
          break;
        }
        if (name.charAt(¢) == '<')
          ++openers;
        else if (name.charAt(¢) == '>') {
          --openers;
          if (openers == 0) {
            name = name.substring(0, idx) + name.substring(¢ + 1);
            break;
          }
          if (openers < 0) {
            name = name.substring(0, idx) + name.substring(¢);
            break;
          }
        }
      }
    name = name.replaceAll("implements [^{]+", "").replaceAll("extends [^{]+", "");
    return findFirst.type(wizard.ast("class d{" + name.replaceAll("extends .+", "") + " x; }"));
  }
  private static boolean balanced(final String s) {
    int openers = 0;
    for (int ¢ = 0; ¢ < s.length(); ++¢)
      if (s.charAt(¢) == '<')
        ++openers;
      else if (s.charAt(¢) == '>')
        --openers;
    return openers == 0;
  }
  /** @param ¢
   * @return */
  public static PackageDeclaration packageDeclaration(final CompilationUnit ¢) {
    return ¢ == null ? null : ¢.getPackage();
  }
  /** @param ¢
   * @return */
  public static SimpleName name(final MethodDeclaration ¢) {
    return ¢ == null ? null : ¢.getName();
  }
}
