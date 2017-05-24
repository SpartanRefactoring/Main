package il.org.spartan.spartanizer.ast.navigate;

import static fluent.ly.idiomatic.*;
import static fluent.ly.lisp.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot com}
 * @since 2015-07-28 */
@SuppressWarnings("ClassWithTooManyMethods")
public enum extract {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  /** Retrieve all operands, including parenthesized ones, under an expression
   * @param x JD
   * @return a {@link List} of all operands to the parameter */
  public static List<Expression> allOperands(final InfixExpression ¢) {
    assert ¢ != null;
    return hop.operands(flatten.of(¢));
  }
  public static Collection<InfixExpression.Operator> allOperators(final InfixExpression ¢) {
    assert ¢ != null;
    final List<InfixExpression.Operator> $ = an.empty.list();
    extract.findOperators(¢, $);
    return $;
  }
  public static List<Annotation> annotations(final BodyDeclaration ¢) {
    return annotations(extendedModifiers(¢));
  }
  public static List<Annotation> annotations(final MethodDeclaration ¢) {
    return annotations(extendedModifiers(¢));
  }
  private static List<Annotation> annotations(final List<IExtendedModifier> ¢) {
    return ¢.stream().map(az::annotation).filter(Objects::nonNull).collect(toList());
  }
  public static List<Annotation> annotations(final SingleVariableDeclaration ¢) {
    return annotations(extendedModifiers(¢));
  }
  public static List<Annotation> annotations(final VariableDeclarationExpression ¢) {
    return extract.annotations(extendedModifiers(¢));
  }
  public static List<Annotation> annotations(final VariableDeclarationStatement ¢) {
    return annotations(extendedModifiers(¢));
  }
  public static int arity(final InfixExpression ¢) {
    return 2 + step.extendedOperands(¢).size();
  }
  /** Determines whether a give {@link ASTNode} includes precisely one
   * {@link Statement}, and return this statement.
   * @param ¢ The node from which to return statement.
   * @return single return statement contained in the parameter, or
   *         {@code null if no such value exists. */
  public static ReturnStatement asReturn(final ASTNode ¢) {
    return asReturn(singleStatement(¢));
  }
  private static ReturnStatement asReturn(final Statement ¢) {
    return az.returnStatement(¢);
  }
  /** @param ¢ a statement or block to extract the assignment from
   * @return null if the block contains more than one statement or if the
   *         statement is not an assignment or the assignment if it exists */
  public static Assignment assignment(final ASTNode ¢) {
    final ExpressionStatement $ = extract.expressionStatement(¢);
    return $ == null ? null : az.assignment($.getExpression());
  }
  public static Collection<ConditionalExpression> branches(final ConditionalExpression ¢) {
    if (¢ == null)
      return null;
    ConditionalExpression s = ¢;
    final Collection<ConditionalExpression> $ = an.empty.list();
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
   * Retrieves all If branches
   * @param ¢ JD
   * @return */
  public static Collection<IfStatement> branches(final IfStatement ¢) {
    if (¢ == null)
      return null;
    IfStatement s = ¢;
    final Collection<IfStatement> $ = an.empty.list();
    $.add(s);
    while (iz.ifStatement(elze(s)))
      $.add(s = az.ifStatement(elze(s)));
    return $;
  }
  public static List<SwitchCase> casesOnSameBranch(final SwitchStatement s, final SwitchCase c) {
    final List<Statement> ll = step.statements(s);
    final int ind = indexOf(ll, c);
    if (ind < 0)
      return null;
    final List<SwitchCase> $ = an.empty.list();
    $.add(c);
    for (int ¢ = ind + 1; ¢ < ll.size() && iz.switchCase(ll.get(¢)); ++¢)
      $.add(az.switchCase(ll.get(¢)));
    for (int ¢ = ind - 1; ¢ >= 0 && iz.switchCase(ll.get(¢)); --¢)
      $.add(az.switchCase(ll.get(¢)));
    return $;
  }
  public static String category(final ASTNode $) {
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
          return "static __ initializer";
        return "__ initializer";
      case METHOD_DECLARATION:
        final Statement body = body(az.methodDeclaration($));
        if (body == null)
          return "abstract";
        final List<Statement> ss = extract.statements(body);
        if (ss.isEmpty())
          return "empty method";
        if (ss.size() == 1)
          return "singleton method";
        return "method";
      case TYPE_DECLARATION:
        return category((TypeDeclaration) $);
      default:
        assert fault.unreachable() : fault.dump() + "\n d = " + $ + "\n d.getClass() = " + $.getClass() + "\n d.getNodeType() = " + $.getNodeType()
            + fault.done();
        return wizard.nodeName($);
    }
  }
  private static String category(final TypeDeclaration ¢) {
    final StringBuilder $ = new StringBuilder();
    $.append(!¢.isPackageMemberTypeDeclaration() ? "internal " //
        : ¢.isMemberTypeDeclaration() ? "member " //
            : !¢.isLocalTypeDeclaration() ? "" : "local ");
    $.append(!¢.isInterface() ? "class" : "interface");
    return $ + "";
  }
  /** Peels any parenthesis that may wrap an {@Link Expression}
   * @param $ JD
   * @return the parameter if not parenthesized, or the unparenthesized this
   *         version of it */
  public static Expression core(final Expression $) {
    return $ == null ? null //
        : iz.nodeTypeEquals($, PARENTHESIZED_EXPRESSION) ? core(az.parenthesizedExpression($).getExpression()) //
            : iz.nodeTypeEquals($, PREFIX_EXPRESSION) ? core(az.prefixExpression($)) //
                : $;
  }
  public static Expression core(final PrefixExpression $) {
    return $.getOperator() != op.PLUS1 ? $ : core($.getOperand());
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
    final List<Statement> $ = extract.statements(¢);
    switch ($.size()) {
      case 0:
        return null;
      case 1:
        return the.firstOf($);
      default:
        return ¢;
    }
  }
  /** @param n root node to search the identifier in it
   * @param x the identifier to search for
   * @return number of times x appears in n */
  public static int countNameInSubtree(final SimpleName n, final ASTNode x) {
    return n == null || x == null ? 0
        : descendants.whoseClassIs(SimpleName.class).suchThat(λ -> step.identifier(λ).equals(step.identifier(n))).inclusiveFrom(x).size();
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
  private static void findOperators(final InfixExpression x, final List<InfixExpression.Operator> $) {
    if (x == null)
      return;
    $.add(x.getOperator());
    findOperators(az.infixExpression(x.getLeftOperand()), $);
    findOperators(az.infixExpression(x.getRightOperand()), $);
  }
  /** extract list of fragments in a {@link Statement}.
   * @param ¢ JD
   * @return reference to the list of fragments in the argument */
  public static List<VariableDeclarationFragment> fragments(final Statement ¢) {
    final List<VariableDeclarationFragment> $ = an.empty.list();
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
    step.statements(b).stream().filter(iz::variableDeclarationStatement).forEach(λ -> extract.fragmentsInto(az.variableDeclrationStatement(λ), $));
    return $;
  }
  private static List<VariableDeclarationFragment> fragmentsInto(final VariableDeclarationStatement s, final List<VariableDeclarationFragment> $) {
    $.addAll(fragments(s));
    return $;
  }
  private static List<IfStatement> ifsInto(final Block b, final List<IfStatement> $) {
    step.statements(b).forEach(λ -> ifsInto(λ, $));
    return $;
  }
  private static List<IfStatement> ifsInto(final Statement ¢, final List<IfStatement> $) {
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
  /** extract the single {@link ReturnStatement} embedded in a node.
   * @param pattern JD
   * @return single {@link IfStatement} embedded in the parameter or
   *         {@code null if not such sideEffects exists. */
  public static IfStatement ifStatement(final ASTNode ¢) {
    return az.ifStatement(extract.singleStatement(¢));
  }
  /** extract list of {@link IfStatement}s in a {@link Statement}.
   * @param ¢ JD
   * @return reference to the list of fragments in the argument */
  public static Collection<IfStatement> ifStatements(final Statement ¢) {
    final List<IfStatement> $ = an.empty.list();
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
  /** @param ss list of statements
   * @param s statement to search for
   * @return index of s in l, or -1 if not contained */
  private static int indexOf(final List<Statement> ss, final Statement s) {
    return IntStream.range(0, ss.size()).filter(λ -> wizard.eq(s, ss.get(λ))).findFirst().orElse(-1);
  }
  public static InfixExpression infixExpression(final ASTNode ¢) {
    final ExpressionStatement $ = expressionStatement(¢);
    return ¢ == null || $ == null ? null : az.infixExpression($.getExpression());
  }
  public static Expression lastElse(final ConditionalExpression ¢) {
    if (¢ == null)
      return null;
    ConditionalExpression $ = ¢;
    while (iz.conditionalExpression(elze($)))
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
  public static MethodInvocation methodInvocation(final ASTNode ¢) {
    return az.methodInvocation(extract.expressionStatement(¢).getExpression());
  }
  public static Collection<Modifier> modifiers(final BodyDeclaration d) {
    return extendedModifiers(d).stream().map(λ -> az.modifier((ASTNode) λ)).filter(Objects::nonNull).collect(toList());
  }
  public static Collection<Modifier> modifiers(final TypeDeclaration d) {
    return extendedModifiers(d).stream().map(λ -> az.modifier((ASTNode) λ)).filter(Objects::nonNull).collect(toList());
  }
  public static Collection<Modifier> modifiers(final FieldDeclaration d) {
    return extendedModifiers(d).stream().map(λ -> az.modifier((ASTNode) λ)).filter(Objects::nonNull).collect(toList());
  }
  public static List<Modifier> modifiers(final SingleVariableDeclaration d) {
    return extendedModifiers(d).stream().map(λ -> az.modifier((ASTNode) λ)).filter(Objects::nonNull).collect(toList());
  }
  public static List<Modifier> modifiers(final VariableDeclarationStatement s) {
    return extendedModifiers(s).stream().map(λ -> az.modifier((ASTNode) λ)).filter(Objects::nonNull).collect(toList());
  }
  public static String name(final ASTNode ¢) {
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
  public static String leftName(final Name ¢) {
    return iz.simpleName(¢) ? az.simpleName(¢).getIdentifier() : iz.qualifiedName(¢) ? leftName(az.qualifiedName(¢).getQualifier()) : null;
  }
  public static String name(final Type ¢) {
    if (¢ == null)
      return null;
    switch (¢.getNodeType()) {
      case QUALIFIED_TYPE:
        return extract.name(az.qualifiedType(¢).getName());
      case PRIMITIVE_TYPE:
        return ¢ + "";
      case NAME_QUALIFIED_TYPE:
        return extract.name(az.nameQualifiedType(¢).getName());
      case SIMPLE_TYPE:
        return extract.name(az.simpleType(¢).getName());
      case WILDCARD_TYPE:
        return extract.name(az.wildcardType(¢).getBound());
      case ARRAY_TYPE:
        return extract.name(az.arrayType(¢).getElementType());
      case INTERSECTION_TYPE:
      case UNION_TYPE:
        return null;
      case PARAMETERIZED_TYPE:
        return extract.name(az.parameterizedType(¢).getType());
      default:
        assert fault.unreachable() : fault.dump() + "\n d = " + ¢ + "\n d.getClass() = " + ¢.getClass() + "\n d.getNodeType() = " + ¢.getNodeType()
            + fault.done();
        return null;
    }
  }
  public static String leftName(final Type ¢) {
    if (¢ == null)
      return null;
    switch (¢.getNodeType()) {
      case QUALIFIED_TYPE:
        return extract.leftName(az.qualifiedType(¢).getQualifier());
      case NAME_QUALIFIED_TYPE:
        return extract.leftName(az.nameQualifiedType(¢).getQualifier());
      default:
        return name(¢);
    }
  }
  @SuppressWarnings("boxing") private static Statement next(final Statement s, final List<Statement> ss) {
    return range.to(ss.size() - 1).stream().filter(λ -> ss.get(λ) == s).map(λ -> ss.get(λ + 1)).findFirst().orElse(null);
  }
  /** Find the {@link Assignment} that follows a given node.
   * @param pattern JD
   * @return {@link Assignment} that follows the parameter, or
   *         {@code null if not such value exists. */
  public static Assignment nextAssignment(final ASTNode ¢) {
    return assignment(extract.nextStatement(¢));
  }
  public static Collection<VariableDeclarationFragment> nextFragmentsOf(final VariableDeclarationStatement ¢) {
    final List<VariableDeclarationFragment> $ = an.empty.list();
    copy.into(fragments(¢), $);
    return chop($);
  }
  /** extract the {@link IfStatement} that immediately follows a given node
   * @param pattern JD
   * @return {@link IfStatement} that immediately follows the parameter, or
   *         {@code null, if no such statement exists. */
  public static IfStatement nextIfStatement(final ASTNode ¢) {
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
  public static ReturnStatement nextReturn(final ASTNode ¢) {
    return az.returnStatement(extract.nextStatement(¢));
  }
  /** extract the {@link Statement} that immediately follows a given node.
   * @param pattern JD
   * @return {@link Statement} that immediately follows the parameter, or
   *         {@code null, if no such statement exists. */
  public static Statement nextStatement(final ASTNode ¢) {
    return nextStatement(containing.statement(¢));
  }
  /** extract the {@link Statement} that immediately follows a given statement
   * @param ¢ JD
   * @return {@link Statement} that immediately follows the parameter, or
   *         {@code null, if no such statement exists. */
  private static Statement nextStatement(final Statement ¢) {
    if (¢ == null)
      return null;
    final Block $ = az.block(¢.getParent());
    return $ == null ? null : next(¢, extract.statements($));
  }
  /** extract the {@link Statement} that immediately follows a given SwitchCase
   * statement, inside the switch statement
   * @param ¢ JD
   * @return {@link Statement} that immediately follows the parameter, or
   *         {@code null, if no such statement exists. */
  public static Statement nextStatementInBlock(final SwitchCase ¢) {
    return next(¢, step.statements(parent(¢)));
  }
  public static Expression onlyArgument(final MethodInvocation ¢) {
    return onlyExpression(arguments(¢));
  }
  private static Expression onlyExpression(final List<Expression> $) {
    return core(the.onlyOneOf($));
  }
  public static SimpleName onlyName(final VariableDeclarationExpression ¢) {
    return step.fragments(¢).stream().filter(λ -> !iz.identifier("$", λ.getName())).map(VariableDeclaration::getName).findFirst().orElse(null);
  }
  public static SimpleName onlyName(final VariableDeclarationStatement ¢) {
    return the.onlyOneOf(fragments(¢)).getName();
  }
  /** Finds the expression returned by a return statement
   * @param pattern a node to extract an expression from
   * @return null if the statement is not an expression or return statement or
   *         the expression if they are */
  public static Expression returnExpression(final ASTNode ¢) {
    final ReturnStatement $ = returnStatement(¢);
    return $ == null ? null : $.getExpression();
  }
  /** extract the single {@link ReturnStatement} embedded in a node.
   * @param pattern JD
   * @return single {@link ReturnStatement} embedded in the parameter, and
   *         return it; {@code null if not such sideEffects
   *         exists. */
  public static ReturnStatement returnStatement(final ASTNode ¢) {
    return az.returnStatement(extract.singleStatement(¢));
  }
  public static SimpleName simpleName(final PostfixExpression $) {
    return eval(() -> (SimpleName) $.getOperand()).when($.getOperand() instanceof SimpleName);
  }
  public static SimpleName simpleName(final PrefixExpression $) {
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
    return the.onlyOneOf(extract.statements(¢));
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
  public static List<Statement> statements(final ASTNode ¢) {
    final List<Statement> $ = an.empty.list();
    return !(¢ instanceof Statement) ? $ : //
        extract.statementsInto((Statement) ¢, $);
  }
  private static List<Statement> statementsInto(final Block b, final List<Statement> $) {
    step.statements(b).forEach(λ -> statementsInto(λ, $));
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
  public static List<SwitchCase> switchCases(final SwitchStatement ¢) {
    return step.statements(¢).stream().filter(iz::switchCase).map(az::switchCase).collect(toList());
  }
  /** @param n a node to extract an expression from
   * @return null if the statement is not an expression or return statement or
   *         the expression if they are */
  public static Expression throwExpression(final ASTNode ¢) {
    final ThrowStatement $ = az.throwStatement(extract.singleStatement(¢));
    return $ == null ? null : $.getExpression();
  }
  /** extract the single {@link ThrowStatement} embedded in a node.
   * @param n JD
   * @return single {@link ThrowStatement} embedded in the parameter, and return
   *         it; {@code null if not such sideEffects exists. */
  public static ThrowStatement throwStatement(final ASTNode ¢) {
    return az.throwStatement(extract.singleStatement(¢));
  }
  public static List<SimpleName> names(final Name n) {
    if (n == null)
      return an.empty.list();
    if (iz.simpleName(n))
      return as.list(az.simpleName(n));
    final List<SimpleName> $ = new LinkedList<>();
    for (Name q = n; q != null; q = !iz.qualifiedName(q) ? null : az.qualifiedName(q).getQualifier())
      $.add(0, !iz.qualifiedName(q) ? az.simpleName(q) : az.qualifiedName(q).getName());
    return $;
  }
  public static List<String> identifiers(final Name n) {
    return names(n).stream().map(λ -> λ.getIdentifier()).collect(toList());
  }
  @SuppressWarnings("unchecked") public static List<SingleVariableDeclaration> methodArguments(MethodDeclaration n) {
    return n.parameters();
  }
}