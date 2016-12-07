package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.utils.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-28 */
public enum extract {
  ;
  /** Retrieve all operands, including parenthesized ones, under an expression
   * @param x JD
   * @return a {@link List} of all operands to the parameter */
  public static List<Expression> allOperands(final InfixExpression ¢) {
    assert ¢ != null;
    return hop.operands(flatten.of(¢));
  }

  public static List<InfixExpression.Operator> allOperators(final InfixExpression ¢) {
    assert ¢ != null;
    final List<InfixExpression.Operator> $ = new ArrayList<>();
    extract.findOperators(¢, $);
    return $;
  }

  public static List<Annotation> annotations(final BodyDeclaration ¢) {
    return annotations(step.extendedModifiers(¢));
  }

  private static List<Annotation> annotations(final List<IExtendedModifier> ms) {
    final ArrayList<Annotation> $ = new ArrayList<>();
    for (final IExtendedModifier ¢ : ms) {
      final Annotation a = az.annotation(¢);
      if (a != null)
        $.add(a);
    }
    return $;
  }

  public static List<Annotation> annotations(final SingleVariableDeclaration ¢) {
    return annotations(step.extendedModifiers(¢));
  }

  public static List<Annotation> annotations(final VariableDeclarationStatement ¢) {
    return annotations(step.extendedModifiers(¢));
  }

  /** Determines whether a give {@link ASTNode} includes precisely one
   * {@link Statement}, and return this statement.
   * @param ¢ The node from which to return statement.
   * @return single return statement contained in the parameter, or
   *         <code><b>null</b></code> if no such value exists. */
  public static ReturnStatement asReturn(final ASTNode ¢) {
    return asReturn(singleStatement(¢));
  }

  public static ReturnStatement asReturn(final Statement ¢) {
    return az.returnStatement(¢);
  }

  /** @param ¢ a statement or block to extract the assignment from
   * @return null if the block contains more than one statement or if the
   *         statement is not an assignment or the assignment if it exists */
  public static Assignment assignment(final ASTNode ¢) {
    final ExpressionStatement $ = extract.expressionStatement(¢);
    return $ == null ? null : az.assignment($.getExpression());
  }

  public static String category(final ASTNode $) {
    switch ($.getNodeType()) {
      case ANNOTATION_TYPE_DECLARATION:
        return "@interface";
      case ANNOTATION_TYPE_MEMBER_DECLARATION:
        return "@interrface member";
      case ENUM_DECLARATION:
        return "enum";
      case ENUM_CONSTANT_DECLARATION:
        return "enum member";
      case FIELD_DECLARATION:
        return "field";
      case INITIALIZER:
        if (Modifier.isStatic(((Initializer) $).getModifiers()))
          return "static type initializer";
        return "type initializer";
      case METHOD_DECLARATION:
        final Statement body = body(az.methodDeclaration($));
        if (body == null)
          return "abstract";
        final List<Statement> ss = extract.statements(body);
        if (ss.isEmpty())
          return "empty";
        if (ss.size() == 1)
          return "singleton";
        return "method";
      case TYPE_DECLARATION:
        return category((TypeDeclaration) $);
      default:
        assert fault.unreachable() : fault.dump() //
            + "\n d = " + $ + "\n d.getClass() = " + $.getClass() //
            + "\n d.getNodeType() = " + $.getNodeType() //
            + fault.done();
        return $.getClass().getSimpleName();
    }
  }

  private static String category(final TypeDeclaration ¢) {
    final StringBuilder $ = new StringBuilder();
    if (!¢.isPackageMemberTypeDeclaration())
      $.append("internal ");
    if (¢.isMemberTypeDeclaration())
      $.append("member ");
    if (¢.isLocalTypeDeclaration())
      $.append("local ");
    $.append(!¢.isInterface() ? "class" : "interface");
    return $ + "";
  }

  /** Peels any parenthesis that may wrap an {@Link Expression}
   * @param $ JD
   * @return the parameter if not parenthesized, or the unparenthesized this
   *         version of it */
  public static Expression core(final Expression $) {
    return $ == null ? $ //
        : iz.nodeTypeEquals($, PARENTHESIZED_EXPRESSION) ? core(az.parenthesizedExpression($).getExpression()) //
            : iz.nodeTypeEquals($, PREFIX_EXPRESSION) ? core(az.prefixExpression($)) //
                : $;
  }

  public static Expression core(final PrefixExpression $) {
    return $.getOperator() != wizard.PLUS1 ? $ : core($.getOperand());
  }

  /** Computes the "Essence" of a statement, i.e., if a statement is essentially
   * a single, non-empty, non-block statement, possibly wrapped in brackets,
   * perhaps along with any number of empty sideEffects, then its Essence is
   * this single non-empty statement.
   * @param ¢ JD
   * @return Essence of the parameter, or <code><b>null</b></code>, if there are
   *         no non-empty sideEffects within the parameter. If, however there
   *         are multiple non-empty sideEffects inside the parameter then the
   *         parameter itself is returned. */
  public static Statement core(final Statement ¢) {
    final List<Statement> $ = extract.statements(¢);
    switch ($.size()) {
      case 0:
        return null;
      case 1:
        return first($);
      default:
        return ¢;
    }
  }

  /** Convert, is possible, an {@link ASTNode} to a {@link ExpressionStatement}
   * @param pattern a statement or a block to extract the expression statement
   *        from
   * @return expression statement if n is a block or an expression statement or
   *         null if it not an expression statement or if the block contains
   *         more than one statement */
  public static ExpressionStatement expressionStatement(final ASTNode ¢) {
    return ¢ == null ? null : az.expressionStatement(extract.singleStatement(¢));
  }

  public static void findOperators(final InfixExpression x, final List<InfixExpression.Operator> $) {
    if (x == null)
      return;
    $.add(x.getOperator());
    findOperators(az.infixExpression(x.getLeftOperand()), $);
    findOperators(az.infixExpression(x.getRightOperand()), $);
  }

  /** Extract list of fragments in a {@link Statement}.
   * @param ¢ JD
   * @return reference to the list of fragments in the argument */
  public static List<VariableDeclarationFragment> fragments(final Statement ¢) {
    final List<VariableDeclarationFragment> $ = new ArrayList<>();
    switch (¢.getNodeType()) {
      case BLOCK:
        return fragmentsInto((Block) ¢, $);
      case VARIABLE_DECLARATION_STATEMENT:
        $.addAll(step.fragments(az.variableDeclrationStatement(¢)));
        return $;
      default:
        return $;
    }
  }

  private static List<VariableDeclarationFragment> fragmentsInto(final Block b, final List<VariableDeclarationFragment> $) {
    step.statements(b).stream().filter(iz::variableDeclarationStatement).forEach(¢ -> extract.fragmentsInto(az.variableDeclrationStatement(¢), $));
    return $;
  }

  private static List<VariableDeclarationFragment> fragmentsInto(final VariableDeclarationStatement s, final List<VariableDeclarationFragment> $) {
    $.addAll(step.fragments(s));
    return $;
  }

  private static List<IfStatement> ifsInto(final Block b, final List<IfStatement> $) {
    for (final Statement ¢ : step.statements(b))
      ifsInto(¢, $);
    return $;
  }

  private static List<IfStatement> ifsInto(final Statement ¢, final List<IfStatement> $) {
    switch (¢.getNodeType()) {
      case IF_STATEMENT:
        $.add(az.ifStatement(¢));
        return $;
      case BLOCK:
        return ifsInto((Block) ¢, $);
      default:
        return $;
    }
  }

  /** Extract the single {@link ReturnStatement} embedded in a node.
   * @param pattern JD
   * @return single {@link IfStatement} embedded in the parameter or
   *         <code><b>null</b></code> if not such sideEffects exists. */
  public static IfStatement ifStatement(final ASTNode ¢) {
    return az.ifStatement(extract.singleStatement(¢));
  }

  /** Extract list of {@link IfStatement}s in a {@link Statement}.
   * @param ¢ JD
   * @return reference to the list of fragments in the argument */
  public static List<IfStatement> ifStatements(final Statement ¢) {
    final List<IfStatement> $ = new ArrayList<>();
    switch (¢.getNodeType()) {
      case BLOCK:
        return ifsInto((Block) ¢, $);
      case IF_STATEMENT:
        $.add((IfStatement) ¢);
        return $;
      default:
        return $;
    }
  }

  public static InfixExpression infixExpression(final ASTNode ¢) {
    final ExpressionStatement $ = expressionStatement(¢);
    return ¢ == null || $ == null ? null : az.infixExpression($.getExpression());
  }

  /** @param pattern JD
   * @return method invocation if it exists or null if it doesn't or if the
   *         block contains more than one statement */
  public static MethodInvocation methodInvocation(final ASTNode ¢) {
    return az.methodInvocation(extract.expressionStatement(¢).getExpression());
  }

  public static List<Modifier> modifiers(final BodyDeclaration d) {
    final ArrayList<Modifier> $ = new ArrayList<>();
    for (final IExtendedModifier ¢ : extendedModifiers(d)) {
      final Modifier a = az.modifier((ASTNode) ¢);
      if (a != null)
        $.add(a);
    }
    return $;
  }

  public static List<Modifier> modifiers(final SingleVariableDeclaration d) {
    final ArrayList<Modifier> $ = new ArrayList<>();
    for (final IExtendedModifier ¢ : extendedModifiers(d)) {
      final Modifier a = az.modifier((ASTNode) ¢);
      if (a != null)
        $.add(a);
    }
    return $;
  }

  public static List<Modifier> modifiers(final VariableDeclarationStatement s) {
    final ArrayList<Modifier> $ = new ArrayList<>();
    for (final IExtendedModifier ¢ : extendedModifiers(s)) {
      final Modifier a = az.modifier((ASTNode) ¢);
      if (a != null)
        $.add(a);
    }
    return $;
  }

  public static String name(final ASTNode ¢) {
    switch (¢.getNodeType()) {
      case ANNOTATION_TYPE_DECLARATION:
        return ((AnnotationTypeDeclaration) ¢).getName() + "";
      case ANNOTATION_TYPE_MEMBER_DECLARATION:
        return ((AnnotationTypeMemberDeclaration) ¢).getName() + "";
      case ENUM_DECLARATION:
        return ((EnumDeclaration) ¢).getName() + "";
      case ENUM_CONSTANT_DECLARATION:
        return ((EnumConstantDeclaration) ¢).getName() + "";
      case FIELD_DECLARATION:
        return separate.these(step.fragments((FieldDeclaration) ¢)).by("/");
      case INITIALIZER:
        return "initializer";
      case METHOD_DECLARATION:
        return ((MethodDeclaration) ¢).getName() + "";
      case TYPE_DECLARATION:
        return ((TypeDeclaration) ¢).getName() + "";
      default:
        assert fault.unreachable() : fault.dump() //
            + "\n d = " + ¢ + "\n d.getClass() = " + ¢.getClass() //
            + "\n d.getNodeType() = " + ¢.getNodeType() //
            + fault.done();
        return ¢.getClass().getSimpleName();
    }
  }

  @SuppressWarnings("boxing") private static Statement next(final Statement s, final List<Statement> ss) {
    for (final Integer $ : range.from(0).to(ss.size() - 1))
      if (ss.get($) == s)
        return ss.get($ + 1);
    return null;
  }

  /** Find the {@link Assignment} that follows a given node.
   * @param pattern JD
   * @return {@link Assignment} that follows the parameter, or
   *         <code><b>null</b></code> if not such value exists. */
  public static Assignment nextAssignment(final ASTNode ¢) {
    return assignment(extract.nextStatement(¢));
  }

  /** Find the {@link PrefixExpression} that follows a given node.
   * @param pattern JD
   * @return {@link Assignment} that follows the parameter, or
   *         <code><b>null</b></code> if not such value exists. */
  public static PrefixExpression nextPrefix(final ASTNode ¢) {
    return az.prefixExpression(az.expressionStatement(extract.nextStatement(¢)).getExpression());
  }

  /** Extract the {@link IfStatement} that immediately follows a given node
   * @param pattern JD
   * @return {@link IfStatement} that immediately follows the parameter, or
   *         <code><b>null</b></code>, if no such statement exists. */
  public static IfStatement nextIfStatement(final ASTNode ¢) {
    return az.ifStatement(extract.nextStatement(¢));
  }

  /** Extract the {@link ReturnStatement} that immediately follows a given node
   * @param pattern JD
   * @return {@link ReturnStatement} that immediately follows the parameter, or
   *         <code><b>null</b></code>, if no such statement exists. */
  public static ReturnStatement nextReturn(final ASTNode ¢) {
    return az.returnStatement(extract.nextStatement(¢));
  }

  /** Extract the {@link Statement} that immediately follows a given node.
   * @param pattern JD
   * @return {@link Statement} that immediately follows the parameter, or
   *         <code><b>null</b></code>, if no such statement exists. */
  public static Statement nextStatement(final ASTNode ¢) {
    return nextStatement(containingStatement(¢));
  }

  /** Extract the {@link Statement} that immediately follows a given statement
   * @param ¢ JD
   * @return {@link Statement} that immediately follows the parameter, or
   *         <code><b>null</b></code>, if no such statement exists. */
  public static Statement nextStatement(final Statement ¢) {
    if (¢ == null)
      return null;
    final Block $ = az.block(¢.getParent());
    return $ == null ? null : next(¢, extract.statements($));
  }

  public static Expression onlyArgument(final MethodInvocation ¢) {
    return onlyExpression(arguments(¢));
  }

  public static Expression onlyExpression(final List<Expression> $) {
    return core(onlyOne($));
  }

  public static SimpleName onlyName(final VariableDeclarationExpression ¢) {
    for (final VariableDeclarationFragment $ : step.fragments(¢))
      if (!iz.identifier("$", $.getName()))
        return $.getName();
    return null;
  }

  public static SimpleName onlyName(final VariableDeclarationStatement ¢) {
    return lisp.onlyOne(fragments(¢)).getName();
  }

  /** Finds the expression returned by a return statement
   * @param pattern a node to extract an expression from
   * @return null if the statement is not an expression or return statement or
   *         the expression if they are */
  public static Expression returnExpression(final ASTNode ¢) {
    final ReturnStatement $ = returnStatement(¢);
    return $ == null ? null : $.getExpression();
  }

  /** Extract the single {@link ReturnStatement} embedded in a node.
   * @param pattern JD
   * @return single {@link ReturnStatement} embedded in the parameter, and
   *         return it; <code><b>null</b></code> if not such sideEffects
   *         exists. */
  public static ReturnStatement returnStatement(final ASTNode ¢) {
    return az.returnStatement(extract.singleStatement(¢));
  }

  /** Finds the single statement in the <code><b>else</b></code> branch of an
   * {@link IfStatement}
   * @param subject JD
   * @return single statement in the <code><b>else</b></code> branch of the
   *         parameter, or <code><b>null</b></code>, if no such statement
   *         exists. */
  public static Statement singleElse(final IfStatement ¢) {
    return extract.singleStatement(elze(¢));
  }

  /** @param pattern JD
   * @return if b is a block with just 1 statement it returns that statement, if
   *         b is statement it returns b and if b is null it returns a null */
  public static Statement singleStatement(final ASTNode ¢) {
    return onlyOne(extract.statements(¢));
  }

  /** Finds the single statement in the "then" branch of an {@link IfStatement}
   * @param subject JD
   * @return single statement in the "then" branch of the parameter, or
   *         <code><b>null</b></code>, if no such statement exists. */
  public static Statement singleThen(final IfStatement ¢) {
    return extract.singleStatement(then(¢));
  }

  /** Extract the {@link Statement} that contains a given node.
   * @param pattern JD
   * @return inner most {@link Statement} in which the parameter is nested, or
   *         <code><b>null</b></code>, if no such statement exists. */
  public static Statement containingStatement(final ASTNode ¢) {
    for (ASTNode $ = ¢; $ != null; $ = $.getParent())
      if (iz.statement($))
        return az.statement($);
    return null;
  }

  /** Extract the list of non-empty statements embedded in node (nesting within
   * control structure such as <code><b>if</b></code> are not removed.)
   * @param pattern JD
   * @return list of such sideEffects. */
  public static List<Statement> statements(final ASTNode ¢) {
    final List<Statement> $ = new ArrayList<>();
    return ¢ == null || !(¢ instanceof Statement) ? $ : //
        extract.statementsInto((Statement) ¢, $);
  }

  private static List<Statement> statementsInto(final Block b, final List<Statement> $) {
    for (final Statement ¢ : step.statements(b))
      statementsInto(¢, $);
    return $;
  }

  private static List<Statement> statementsInto(final Statement ¢, final List<Statement> $) {
    switch (¢.getNodeType()) {
      case EMPTY_STATEMENT:
        return $;
      case BLOCK:
        return statementsInto((Block) ¢, $);
      default:
        $.add(¢);
        return $;
    }
  }

  /** @param n a node to extract an expression from
   * @return null if the statement is not an expression or return statement or
   *         the expression if they are */
  public static Expression throwExpression(final ASTNode ¢) {
    final ThrowStatement $ = az.throwStatement(extract.singleStatement(¢));
    return $ == null ? null : $.getExpression();
  }

  /** Extract the single {@link ThrowStatement} embedded in a node.
   * @param n JD
   * @return single {@link ThrowStatement} embedded in the parameter, and return
   *         it; <code><b>null</b></code> if not such sideEffects exists. */
  public static ThrowStatement throwStatement(final ASTNode ¢) {
    return az.throwStatement(extract.singleStatement(¢));
  }

  public static Type type(final ASTNode $) {
    switch ($.getNodeType()) {
      case VARIABLE_DECLARATION_EXPRESSION:
        return az.variableDeclarationExpression($).getType();
      case SINGLE_VARIABLE_DECLARATION:
        return az.singleVariableDeclaration($).getType();
      case VARIABLE_DECLARATION_STATEMENT:
        return az.variableDeclrationStatement($).getType();
      case VARIABLE_DECLARATION_FRAGMENT:
        return type(az.variableDeclrationFragment($).getParent());
      default:
        return null;
    }
  }
}