package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.Utils.*;
import static il.org.spartan.idiomatic.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.range.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot com}
 * @since 2015-07-28 */
@SuppressWarnings("ClassWithTooManyMethods")
public enum extract {
  ;
  /** Retrieve all operands, including parenthesized ones, under an expression
   * @param x JD
   * @return a {@link List} of all operands to the parameter */
  @Nullable public static List<Expression> allOperands(@NotNull final InfixExpression ¢) {
    assert ¢ != null;
    return hop.operands(flatten.of(¢));
  }

  @NotNull public static Collection<InfixExpression.Operator> allOperators(@NotNull final InfixExpression ¢) {
    assert ¢ != null;
    @NotNull final List<InfixExpression.Operator> $ = new ArrayList<>();
    extract.findOperators(¢, $);
    return $;
  }

  @NotNull public static List<Annotation> annotations(final BodyDeclaration ¢) {
    return annotations(extendedModifiers(¢));
  }

  @NotNull public static Iterable<Annotation> annotations(final SingleVariableDeclaration ¢) {
    return annotations(extendedModifiers(¢));
  }

  @NotNull public static List<Annotation> annotations(final VariableDeclarationStatement ¢) {
    return annotations(extendedModifiers(¢));
  }

  /** Determines whether a give {@link ASTNode} includes precisely one
   * {@link Statement}, and return this statement.
   * @param ¢ The node from which to return statement.
   * @return single return statement contained in the parameter, or
   *         {@code null if no such value exists. */
  @Nullable public static ReturnStatement asReturn(final ASTNode ¢) {
    return asReturn(singleStatement(¢));
  }

  /** @param ¢ a statement or block to extract the assignment from
   * @return null if the block contains more than one statement or if the
   *         statement is not an assignment or the assignment if it exists */
  public static Assignment assignment(final ASTNode ¢) {
    @Nullable final ExpressionStatement $ = extract.expressionStatement(¢);
    return $ == null ? null : az.assignment($.getExpression());
  }

  @Nullable public static Collection<ConditionalExpression> branches(@Nullable final ConditionalExpression ¢) {
    if (¢ == null)
      return null;
    @Nullable ConditionalExpression s = ¢;
    @NotNull final Collection<ConditionalExpression> $ = new ArrayList<>();
    $.add(s);
    while (iz.conditionalExpression(elze(s)))
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
  @Nullable public static Collection<IfStatement> branches(@Nullable final IfStatement ¢) {
    if (¢ == null)
      return null;
    @Nullable IfStatement s = ¢;
    @NotNull final Collection<IfStatement> $ = new ArrayList<>();
    $.add(s);
    while (iz.ifStatement(elze(s)))
      $.add(s = az.ifStatement(elze(s)));
    return $;
  }

  @Nullable public static List<String> usedNames(final Expression x) {
    return new ExpressionBottomUp<List<String>>() {
      @NotNull @Override public List<String> reduce() {
        return new ArrayList<>();
      }

      @Nullable @Override public List<String> reduce(@Nullable final List<String> ss1, @Nullable final List<String> ss2) {
        if (ss1 == null && ss2 == null)
          return new ArrayList<>();
        if (ss1 == null)
          return ss2;
        if (ss2 == null)
          return ss1;
        ss1.addAll(ss2);
        return ss1;
      }

      @NotNull @Override protected List<String> map(@NotNull final SimpleName ¢) {
        final String $ = ¢.getIdentifier();
        return guessName.of($) != guessName.METHOD_OR_VARIABLE ? reduce() : as.list($);
      }

      @NotNull @Override protected List<String> map(@SuppressWarnings("unused") final ThisExpression ¢) {
        return reduce();
      }
    }.map(x);
  }

  public static List<SwitchCase> casesOnSameBranch(final SwitchStatement s, @NotNull final SwitchCase c) {
    @NotNull final List<Statement> ll = step.statements(s);
    final int ind = indexOf(ll, c);
    if (ind < 0)
      return null;
    @NotNull final List<SwitchCase> $ = new ArrayList<>();
    $.add(c);
    for (int ¢ = ind + 1; ¢ < ll.size() && iz.switchCase(ll.get(¢)); ++¢)
      $.add(az.switchCase(ll.get(¢)));
    for (int ¢ = ind - 1; ¢ >= 0 && iz.switchCase(ll.get(¢)); --¢)
      $.add(az.switchCase(ll.get(¢)));
    return $;
  }

  @NotNull public static String category(@NotNull final ASTNode $) {
    switch ($.getNodeType()) {
      case ANNOTATION_TYPE_DECLARATION:
        return "@interface";
      case ANNOTATION_TYPE_MEMBER_DECLARATION:
        return "@interrface member";
      case ENUM_CONSTANT_DECLARATION:
        return "enum member";
      case ENUM_DECLARATION:
        return "enum";
      case FIELD_DECLARATION:
        return "field";
      case INITIALIZER:
        if (Modifier.isStatic(((BodyDeclaration) $).getModifiers()))
          return "static type initializer";
        return "type initializer";
      case METHOD_DECLARATION:
        @NotNull final Statement body = body(az.methodDeclaration($));
        if (body == null)
          return "abstract";
        @NotNull final List<Statement> ss = extract.statements(body);
        if (ss.isEmpty())
          return "empty";
        if (ss.size() == 1)
          return "singleton";
        return "method";
      case TYPE_DECLARATION:
        return category((TypeDeclaration) $);
      default:
        assert fault.unreachable() : fault.dump() + "\n d = " + $ + "\n d.getClass() = " + $.getClass() + "\n d.getNodeType() = " + $.getNodeType()
            + fault.done();
        return wizard.nodeName($);
    }
  }

  /** extract the {@link Statement} that contains a given node.
   * @param pattern JD
   * @return inner most {@link Statement} in which the parameter is nested, or
   *         {@code null, if no such statement exists. */
  public static Statement containingStatement(final ASTNode ¢) {
    for (ASTNode $ = ¢; $ != null; $ = parent($))
      if (iz.statement($))
        return az.statement($);
    return null;
  }

  /** Peels any parenthesis that may wrap an {@Link Expression}
   * @param $ JD
   * @return the parameter if not parenthesized, or the unparenthesized this
   *         version of it */
  @Nullable public static Expression core(@Nullable final Expression $) {
    return $ == null ? null //
        : iz.nodeTypeEquals($, PARENTHESIZED_EXPRESSION) ? core(az.parenthesizedExpression($).getExpression()) //
            : iz.nodeTypeEquals($, PREFIX_EXPRESSION) ? core(az.prefixExpression($)) //
                : $;
  }

  @NotNull public static Expression core(@NotNull final PrefixExpression $) {
    return $.getOperator() != wizard.PLUS1 ? $ : core($.getOperand());
  }

  /** Computes the "Essence" of a statement, i.e., if a statement is essentially
   * a single, non-empty, non-block statement, possibly wrapped in brackets,
   * perhaps along with any number of empty sideEffects, then its Essence is
   * this single non-empty statement.
   * @param ¢ JD
   * @return Essence of the parameter, or {@code null, if there are
   *         no non-empty sideEffects within the parameter. If, however there
   *         are multiple non-empty sideEffects inside the parameter then the
   *         parameter itself is returned. */
  public static Statement core(final Statement ¢) {
    @NotNull final List<Statement> $ = extract.statements(¢);
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
  @Nullable public static ExpressionStatement expressionStatement(@Nullable final ASTNode ¢) {
    return ¢ == null ? null : az.expressionStatement(extract.singleStatement(¢));
  }

  /** extract list of fragments in a {@link Statement}.
   * @param ¢ JD
   * @return reference to the list of fragments in the argument */
  @NotNull public static List<VariableDeclarationFragment> fragments(@NotNull final Statement ¢) {
    @NotNull final List<VariableDeclarationFragment> $ = new ArrayList<>();
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

  /** extract the single {@link ReturnStatement} embedded in a node.
   * @param pattern JD
   * @return single {@link IfStatement} embedded in the parameter or
   *         {@code null if not such sideEffects exists. */
  @Nullable public static IfStatement ifStatement(final ASTNode ¢) {
    return az.ifStatement(extract.singleStatement(¢));
  }

  /** extract list of {@link IfStatement}s in a {@link Statement}.
   * @param ¢ JD
   * @return reference to the list of fragments in the argument */
  @NotNull public static Collection<IfStatement> ifStatements(@NotNull final Statement ¢) {
    @NotNull final List<IfStatement> $ = new ArrayList<>();
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

  public static InfixExpression infixExpression(@Nullable final ASTNode ¢) {
    @Nullable final ExpressionStatement $ = expressionStatement(¢);
    return ¢ == null || $ == null ? null : az.infixExpression($.getExpression());
  }

  @Nullable public static Expression lastElse(@Nullable final ConditionalExpression ¢) {
    if (¢ == null)
      return null;
    @Nullable ConditionalExpression $ = ¢;
    while (iz.conditionalExpression(elze($)))
      $ = az.conditionalExpression(elze($));
    return elze($);
  }

  /** returns the else statement of the last if in an if else if else if else
   * sequence
   * @param ¢
   * @return */
  @Nullable public static Statement lastElse(@Nullable final IfStatement ¢) {
    if (¢ == null)
      return null;
    @Nullable IfStatement $ = ¢;
    while (iz.ifStatement(elze($)))
      $ = az.ifStatement(elze($));
    return elze($);
  }

  public static Statement lastStatement(final EnhancedForStatement ¢) {
    return lastStatement(body(¢));
  }

  public static Statement lastStatement(final ForStatement ¢) {
    return lastStatement(body(¢));
  }

  public static Statement lastStatement(final Statement ¢) {
    return !iz.block(¢) ? ¢ : hop.lastStatement(az.block(¢));
  }

  /** @param pattern JD
   * @return method invocation if it exists or null if it doesn't or if the
   *         block contains more than one statement */
  @Nullable public static MethodInvocation methodInvocation(final ASTNode ¢) {
    return az.methodInvocation(extract.expressionStatement(¢).getExpression());
  }

  public static Collection<Modifier> modifiers(final BodyDeclaration d) {
    return extendedModifiers(d).stream().map(λ -> az.modifier((ASTNode) λ)).filter(Objects::nonNull).collect(toList());
  }

  public static List<Modifier> modifiers(final SingleVariableDeclaration d) {
    return extendedModifiers(d).stream().map(λ -> az.modifier((ASTNode) λ)).filter(Objects::nonNull).collect(toList());
  }

  public static List<Modifier> modifiers(final VariableDeclarationStatement s) {
    return extendedModifiers(s).stream().map(λ -> az.modifier((ASTNode) λ)).filter(Objects::nonNull).collect(toList());
  }

  public static String name(@NotNull final ASTNode ¢) {
    switch (¢.getNodeType()) {
      case INITIALIZER:
        return "initializer";
      case FIELD_DECLARATION:
        return separate.these(step.fragments((FieldDeclaration) ¢)).by("/");
      case ANNOTATION_TYPE_MEMBER_DECLARATION:
        return ((AnnotationTypeMemberDeclaration) ¢).getName() + "";
      case ENUM_CONSTANT_DECLARATION:
        return ((EnumConstantDeclaration) ¢).getName() + "";
      case METHOD_DECLARATION:
        return ((MethodDeclaration) ¢).getName() + "";
      case ANNOTATION_TYPE_DECLARATION:
      case ENUM_DECLARATION:
      case TYPE_DECLARATION:
        return ((AbstractTypeDeclaration) ¢).getName() + "";
      default:
        assert fault.unreachable() : fault.dump() + "\n d = " + ¢ + "\n d.getClass() = " + ¢.getClass() + "\n d.getNodeType() = " + ¢.getNodeType()
            + fault.done();
        return wizard.nodeName(¢);
    }
  }

  public static String name(final Name ¢) {
    return iz.simpleName(¢) ? az.simpleName(¢).getIdentifier() : iz.qualifiedName(¢) ? az.qualifiedName(¢).getName().getIdentifier() : null;
  }

  /** Find the {@link Assignment} that follows a given node.
   * @param pattern JD
   * @return {@link Assignment} that follows the parameter, or
   *         {@code null if not such value exists. */
  @Nullable public static Assignment nextAssignment(final ASTNode ¢) {
    return assignment(extract.nextStatement(¢));
  }

  public static Collection<VariableDeclarationFragment> nextFragmentsOf(@NotNull final VariableDeclarationStatement ¢) {
    @NotNull final List<VariableDeclarationFragment> $ = new ArrayList<>();
    copy.into(fragments(¢), $);
    return chop($);
  }

  /** extract the {@link IfStatement} that immediately follows a given node
   * @param pattern JD
   * @return {@link IfStatement} that immediately follows the parameter, or
   *         {@code null, if no such statement exists. */
  @Nullable public static IfStatement nextIfStatement(final ASTNode ¢) {
    return az.ifStatement(extract.nextStatement(¢));
  }

  /** Find the {@link PrefixExpression} that follows a given node.
   * @param pattern JD
   * @return {@link Assignment} that follows the parameter, or
   *         {@code null if not such value exists. */
  public static PrefixExpression nextPrefix(final ASTNode ¢) {
    return az.prefixExpression(expression(az.expressionStatement(extract.nextStatement(¢))));
  }

  /** extract the {@link ReturnStatement} that immediately follows a given node
   * @param pattern JD
   * @return {@link ReturnStatement} that immediately follows the parameter, or
   *         {@code null, if no such statement exists. */
  @Nullable public static ReturnStatement nextReturn(final ASTNode ¢) {
    return az.returnStatement(extract.nextStatement(¢));
  }

  /** extract the {@link Statement} that immediately follows a given node.
   * @param pattern JD
   * @return {@link Statement} that immediately follows the parameter, or
   *         {@code null, if no such statement exists. */
  @Nullable public static Statement nextStatement(final ASTNode ¢) {
    return nextStatement(containingStatement(¢));
  }

  /** extract the {@link Statement} that immediately follows a given SwitchCase
   * statement, inside the switch statement
   * @param ¢ JD
   * @return {@link Statement} that immediately follows the parameter, or
   *         {@code null, if no such statement exists. */
  @Nullable public static Statement nextStatementInside(@Nullable final SwitchCase ¢) {
    if (¢ == null)
      return null;
    @Nullable final SwitchStatement $ = az.switchStatement(¢.getParent());
    return $ == null ? null : next(¢, step.statements($));
  }

  public static Expression onlyArgument(final MethodInvocation ¢) {
    return onlyExpression(arguments(¢));
  }

  public static SimpleName onlyName(final VariableDeclarationExpression ¢) {
    return step.fragments(¢).stream().filter(λ -> !iz.identifier("$", λ.getName())).map(VariableDeclaration::getName).findFirst().orElse(null);
  }

  public static SimpleName onlyName(@NotNull final VariableDeclarationStatement ¢) {
    return lisp.onlyOne(fragments(¢)).getName();
  }

  /** Finds the expression returned by a return statement
   * @param pattern a node to extract an expression from
   * @return null if the statement is not an expression or return statement or
   *         the expression if they are */
  public static Expression returnExpression(final ASTNode ¢) {
    @Nullable final ReturnStatement $ = returnStatement(¢);
    return $ == null ? null : $.getExpression();
  }

  /** extract the single {@link ReturnStatement} embedded in a node.
   * @param pattern JD
   * @return single {@link ReturnStatement} embedded in the parameter, and
   *         return it; {@code null if not such sideEffects
   *         exists. */
  @Nullable public static ReturnStatement returnStatement(final ASTNode ¢) {
    return az.returnStatement(extract.singleStatement(¢));
  }

  public static SimpleName simpleName(@NotNull final PostfixExpression $) {
    return eval(() -> (SimpleName) $.getOperand()).when($.getOperand() instanceof SimpleName);
  }

  public static SimpleName simpleName(@NotNull final PrefixExpression $) {
    return eval(() -> (SimpleName) $.getOperand()).when($.getOperand() instanceof SimpleName);
  }

  /** Finds the single statement in the {@code else} branch of an
   * {@link IfStatement}
   * @param subject JD
   * @return single statement in the {@code else} branch of the parameter, or
   *         {@code null, if no such statement
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
   *         {@code null, if no such statement exists. */
  public static Statement singleThen(final IfStatement ¢) {
    return extract.singleStatement(then(¢));
  }

  /** extract the list of non-empty statements embedded in node (nesting within
   * control structure such as {@code if} are not removed.)
   * @param pattern JD
   * @return list of such sideEffects. */
  @NotNull public static List<Statement> statements(final ASTNode ¢) {
    @NotNull final List<Statement> $ = new ArrayList<>();
    return !(¢ instanceof Statement) ? $ : //
        extract.statementsInto((Statement) ¢, $);
  }

  public static List<SwitchCase> switchCases(final SwitchStatement ¢) {
    return step.statements(¢).stream().filter(iz::switchCase).map(az::switchCase).collect(toList());
  }

  /** @param n a node to extract an expression from
   * @return null if the statement is not an expression or return statement or
   *         the expression if they are */
  public static Expression throwExpression(final ASTNode ¢) {
    @Nullable final ThrowStatement $ = az.throwStatement(extract.singleStatement(¢));
    return $ == null ? null : $.getExpression();
  }

  /** extract the single {@link ThrowStatement} embedded in a node.
   * @param n JD
   * @return single {@link ThrowStatement} embedded in the parameter, and return
   *         it; {@code null if not such sideEffects exists. */
  @Nullable public static ThrowStatement throwStatement(final ASTNode ¢) {
    return az.throwStatement(extract.singleStatement(¢));
  }

  @NotNull public static List<ASTNode> updatedVariables(final Expression x) {
    @Nullable final List<ASTNode> $ = new ExpressionBottomUp<List<ASTNode>>() {
      @NotNull @Override public List<ASTNode> reduce() {
        return new LinkedList<>();
      }

      @Nullable @Override public List<ASTNode> reduce(@Nullable final List<ASTNode> l1, @Nullable final List<ASTNode> l2) {
        if (l1 == null)
          return l2;
        if (l2 == null)
          return l1;
        l1.addAll(l2);
        return l1;
      }

      @Nullable @Override protected List<ASTNode> map(final Assignment ¢) {
        return reduce(list(¢), super.map(¢));
      }

      @Override protected List<ASTNode> map(final PostfixExpression ¢) {
        return reduce(Collections.singletonList(step.expression(¢)), super.map(¢));
      }

      @Nullable @Override protected List<ASTNode> map(@NotNull final PrefixExpression ¢) {
        return reduce(!updating(¢) ? reduce() : atomic(¢.getOperand()), super.map(¢));
      }

      @NotNull List<ASTNode> atomic(final Expression ¢) {
        return Collections.singletonList(¢);
      }

      @NotNull List<ASTNode> list(final ASTNode ¢) {
        return new ArrayList<>(Collections.singletonList(¢));
      }

      boolean updating(@NotNull final PrefixExpression ¢) {
        return in(¢.getOperator(), INCREMENT, DECREMENT);
      }
    }.map(x);
    return $ != null ? $ : new ArrayList<>();
  }

  @NotNull private static List<Annotation> annotations(@NotNull final Iterable<IExtendedModifier> ms) {
    @NotNull final List<Annotation> $ = new ArrayList<>();
    for (final IExtendedModifier ¢ : ms) {
      @Nullable final Annotation a = az.annotation(¢);
      if (a != null)
        $.add(a);
    }
    return $;
  }

  @Nullable private static ReturnStatement asReturn(final Statement ¢) {
    return az.returnStatement(¢);
  }

  @NotNull private static String category(@NotNull final TypeDeclaration ¢) {
    @NotNull final StringBuilder $ = new StringBuilder();
    $.append(!¢.isPackageMemberTypeDeclaration() ? "internal " //
        : ¢.isMemberTypeDeclaration() ? "member " //
            : !¢.isLocalTypeDeclaration() ? "" : "local ");
    $.append(!¢.isInterface() ? "class" : "interface");
    return $ + "";
  }

  private static void findOperators(@Nullable final InfixExpression x, @NotNull final List<InfixExpression.Operator> $) {
    if (x == null)
      return;
    $.add(x.getOperator());
    findOperators(az.infixExpression(x.getLeftOperand()), $);
    findOperators(az.infixExpression(x.getRightOperand()), $);
  }

  @NotNull private static List<VariableDeclarationFragment> fragmentsInto(final Block b, @NotNull final List<VariableDeclarationFragment> $) {
    step.statements(b).stream().filter(iz::variableDeclarationStatement).forEach(λ -> extract.fragmentsInto(az.variableDeclrationStatement(λ), $));
    return $;
  }

  @NotNull private static List<VariableDeclarationFragment> fragmentsInto(@NotNull final VariableDeclarationStatement s,
      @NotNull final List<VariableDeclarationFragment> $) {
    $.addAll(fragments(s));
    return $;
  }

  @NotNull private static List<IfStatement> ifsInto(final Block b, @NotNull final List<IfStatement> $) {
    step.statements(b).forEach(λ -> ifsInto(λ, $));
    return $;
  }

  @NotNull private static List<IfStatement> ifsInto(@NotNull final Statement ¢, @NotNull final List<IfStatement> $) {
    switch (¢.getNodeType()) {
      case BLOCK:
        return ifsInto((Block) ¢, $);
      case IF_STATEMENT:
        $.add(az.ifStatement(¢));
        return $;
      default:
        return $;
    }
  }

  /** @param ss list of statements
   * @param s statement to search for
   * @return index of s in l, or -1 if not contained */
  private static int indexOf(@NotNull final List<Statement> ss, @NotNull final Statement s) {
    for (int $ = 0; $ < ss.size(); ++$)
      if (wizard.same(s, ss.get($)))
        return $;
    return -1;
  }

  @SuppressWarnings("boxing") private static Statement next(final Statement s, @NotNull final List<Statement> ss) {
    return range.to(ss.size() - 1).stream().filter(λ -> ss.get(λ) == s).map(λ -> ss.get(λ + 1)).findFirst().orElse(null);
  }

  /** extract the {@link Statement} that immediately follows a given statement
   * @param ¢ JD
   * @return {@link Statement} that immediately follows the parameter, or
   *         {@code null, if no such statement exists. */
  @Nullable private static Statement nextStatement(@Nullable final Statement ¢) {
    if (¢ == null)
      return null;
    @Nullable final Block $ = az.block(¢.getParent());
    return $ == null ? null : next(¢, extract.statements($));
  }

  private static Expression onlyExpression(final List<Expression> $) {
    return core(onlyOne($));
  }

  @NotNull private static List<Statement> statementsInto(final Block b, @NotNull final List<Statement> $) {
    step.statements(b).forEach(λ -> statementsInto(λ, $));
    return $;
  }

  @NotNull private static List<Statement> statementsInto(@NotNull final Statement ¢, @NotNull final List<Statement> $) {
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

  public static int arity(final InfixExpression ¢) {
    return 2 + step.extendedOperands(¢).size();
  }
}