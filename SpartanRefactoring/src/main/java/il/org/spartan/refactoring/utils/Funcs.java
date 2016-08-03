package il.org.spartan.refactoring.utils;

import static il.org.spartan.idiomatic.*;
import static il.org.spartan.utils.Utils.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.refactoring.wring.*;

/** Useful Functions */
public enum Funcs {
  ;
  private static Map<Operator, Operator> conjugate = makeConjugates();

  public static AbstractTypeDeclaration asAbstractTypeDeclaration(final ASTNode n) {
    return eval(() -> ((AbstractTypeDeclaration) n)).when(n instanceof AbstractTypeDeclaration);
  }
  /** Convert an {@link Expression} into {@link InfixExpression} whose operator
   * is either {@link org.eclipse.jdt.core.dom.InfixExpression.Operator#AND} or
   * {@link org.eclipse.jdt.core.dom.InfixExpression.Operator#OR}.
   * @param e JD
   * @return the parameter thus converted, or <code><b>null</b> if the
   *         conversion is not possible for it */
  public static InfixExpression asAndOrOr(final Expression e) {
    return !Is.infix(e) || !Is.deMorgan(asInfixExpression(e).getOperator()) ? null : asInfixExpression(e);
  }
  /** Convert, is possible, an {@link ASTNode} to an {@link Assignment}
   * @param $ JD
   * @return the argument, but down-casted to a {@link Assignment}, or
   *         <code><b>null</b></code> if the downcast is impossible. */
  public static Assignment asAssignment(final ASTNode $) {
    return !is($, ASSIGNMENT) ? null : (Assignment) $;
  }
  private static int asBit(final boolean b) {
    return b ? 1 : 0;
  }
  /** Convert, is possible, an {@link ASTNode} to a {@link Block}
   * @param $ JD
   * @return the argument, but down-casted to a {@link Block}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static Block asBlock(final ASTNode $) {
    return !is($, BLOCK) ? null : (Block) $;
  }
  /** Down-cast, if possible, to {@link BooleanLiteral}
   * @param e JD
   * @return the parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static BooleanLiteral asBooleanLiteral(final Expression e) {
    return !(e instanceof BooleanLiteral) ? null : (BooleanLiteral) e;
  }
  /** Convert an {@link Expression} into {@link InfixExpression} whose operator
   * is one of the six comparison operators: <code><</code>, <code><=</code>,
   * <code>></code>, <code>>=</code>, <code>!=</code>, or <code>==</code>.
   * @param e JD
   * @return the parameter thus converted, or <code><b>null</b> if the
   *         conversion is not possible for it */
  public static InfixExpression asComparison(final Expression e) {
    return !(e instanceof InfixExpression) ? null : asComparison((InfixExpression) e);
  }
  private static InfixExpression asComparison(final InfixExpression e) {
    return in(e.getOperator(), //
        GREATER, //
        GREATER_EQUALS, //
        LESS, //
        LESS_EQUALS, //
        EQUALS, //
        NOT_EQUALS //
    ) ? e : null;
  }
  /** Convert, is possible, an {@link ASTNode} to a
   * {@link ConditionalExpression}
   * @param n JD
   * @return the argument, but down-casted to a {@link ConditionalExpression},
   *         or <code><b>null</b></code> if no such down-cast is possible.. */
  public static ConditionalExpression asConditionalExpression(final ASTNode n) {
    return !(n instanceof ConditionalExpression) ? null : (ConditionalExpression) n;
  }
  /** Down-cast, if possible, to {@link ConditionalExpression}
   * @param e JD
   * @return the parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static ConditionalExpression asConditionalExpression(final Expression e) {
    return !(e instanceof ConditionalExpression) ? null : (ConditionalExpression) e;
  }
  /** Down-cast, if possible, to {@link Expression}
   * @param n JD
   * @return the parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static Expression asExpression(final ASTNode n) {
    return !(n instanceof Expression) ? null : (Expression) n;
  }
  /** Down-cast, if possible, to {@link ExpressionStatement}
   * @param n JD
   * @return the parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static ExpressionStatement asExpressionStatement(final ASTNode n) {
    return !(n instanceof ExpressionStatement) ? null : (ExpressionStatement) n;
  }
  /** Down-cast, if possible, to {@link IfStatement}
   * @param $ JD
   * @return the parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static IfStatement asIfStatement(final ASTNode $) {
    return !is($, IF_STATEMENT) ? null : (IfStatement) $;
  }
  /** Down-cast, if possible, to {@link InfixExpression}
   * @param n JD
   * @return the parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static InfixExpression asInfixExpression(final ASTNode n) {
    return !(n instanceof InfixExpression) ? null : (InfixExpression) n;
  }
  /** Convert, is possible, an {@link ASTNode} to a {@link MethodDeclaration}
   * @param $ JD
   * @return the argument, but down-casted to a {@link MethodDeclaration}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static MethodDeclaration asMethodDeclaration(final ASTNode $) {
    return eval(() -> ((MethodDeclaration) $)).when($ instanceof MethodDeclaration);
  }
  /** Down-cast, if possible, to {@link MethodInvocation}
   * @param e JD
   * @return the parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static MethodInvocation asMethodInvocation(final Expression e) {
    return !(e instanceof MethodInvocation) ? null : (MethodInvocation) e;
  }
  /** Convert an {@link Expression} into a {@link PrefixExpression} whose
   * operator is <code>!</code>,
   * @param e JD
   * @return the parameter thus converted, or <code><b>null</b> if the
   *         conversion is not possible for it */
  public static PrefixExpression asNot(final Expression e) {
    return !(e instanceof PrefixExpression) ? null : asNot(asPrefixExpression(e));
  }
  static PrefixExpression asNot(final PrefixExpression e) {
    return NOT.equals(e.getOperator()) ? e : null;
  }
  /** Down-cast, if possible, to {@link InfixExpression}
   * @param n JD
   * @return the parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static PostfixExpression asPostfixExpression(final ASTNode n) {
    return eval(() -> (PostfixExpression) n).when(n instanceof PostfixExpression);
  }
  /** Down-cast, if possible, to {@link PrefixExpression}
   * @param n JD
   * @return the parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static PrefixExpression asPrefixExpression(final ASTNode n) {
    return eval(() -> (PrefixExpression) n).when(n instanceof PrefixExpression);
  }
  /** @param n The node from which to return statement.
   * @return null if it is not possible to extract the return statement. */
  public static ReturnStatement asReturn(final ASTNode n) {
    if (n == null)
      return null;
    switch (n.getNodeType()) {
      case ASTNode.BLOCK:
        return asReturn((Block) n);
      case ASTNode.RETURN_STATEMENT:
        return (ReturnStatement) n;
      default:
        return null;
    }
  }
  static ReturnStatement asReturn(final Block b) {
    return b.statements().size() != 1 ? null : Funcs.asReturn((Statement) b.statements().get(0));
  }
  /** Down-cast, if possible, to {@link ReturnStatement}
   * @param $ JD
   * @return the parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static ReturnStatement asReturnStatement(final ASTNode $) {
    return !is($, RETURN_STATEMENT) ? null : (ReturnStatement) $;
  }
  /** Convert, is possible, an {@link ASTNode} to a {@link SimpleName}
   * @param $ JD
   * @return the argument, but down-casted to a {@link SimpleName}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static SimpleName asSimpleName(final ASTNode $) {
    return eval(() -> (SimpleName) $).when($ instanceof SimpleName);
  }
  /** Down-cast, if possible, to {@link Statement}
   * @param n JD
   * @return the parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static Statement asStatement(final ASTNode n) {
    return !isStatement(n) ? null : (Statement) n;
  }
  /** Obtain a condensed textual representation of an {@link ASTNode}
   * @param n JD
   * @return the textual representation of the parameter, */
  public static String asString(final ASTNode n) {
    return removeWhites(n.toString());
  }
  /** Convert, is possible, an {@link ASTNode} to a
   * {@link ConditionalExpression}
   * @param n JD
   * @return the argument, but down-casted to a {@link ConditionalExpression},
   *         or <code><b>null</b></code> if no such down-cast is possible.. */
  public static ThrowStatement asThrowStatement(final ASTNode n) {
    return !(n instanceof ThrowStatement) ? null : (ThrowStatement) n;
  }
  public static VariableDeclarationExpression asVariableDeclarationExpression(final Expression e) {
    return e.getNodeType() != VARIABLE_DECLARATION_EXPRESSION ? null : (VariableDeclarationExpression) e;
  }
  /** @param root the node whose children we return
   * @return A list containing all the nodes in the given root's sub tree */
  public static List<ASTNode> collectDescendants(final ASTNode root) {
    if (root == null)
      return null;
    final List<ASTNode> $ = new ArrayList<>();
    root.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode n) {
        $.add(n);
      }
    });
    $.remove(0);
    return $;
  }
  /** the function checks if all the given assignments have the same left hand
   * side(variable) and operator
   * @param base The assignment to compare all others to
   * @param as The assignments to compare
   * @return true if all assignments has the same left hand side and operator as
   *         the first one or false otherwise */
  public static boolean compatible(final Assignment base, final Assignment... as) {
    if (hasNulls(base, as))
      return false;
    for (final Assignment a : as)
      if (incompatible(base, a))
        return false;
    return true;
  }
  /** @param cmpTo the assignment operator to compare all to
   * @param os A unknown number of assignments operators
   * @return true if all the operator are the same or false otherwise */
  public static boolean compatibleOps(final Assignment.Operator cmpTo, final Assignment.Operator... os) {
    if (hasNulls(cmpTo, os))
      return false;
    for (final Assignment.Operator o : os)
      if (o == null || o != cmpTo)
        return false;
    return true;
  }
  /** @param ns unknown number of nodes to check
   * @return true if one of the nodes is an Expression Statement of type Post or
   *         Pre Expression with ++ or -- operator. false if none of them are or
   *         if the given parameter is null. */
  public static boolean containIncOrDecExp(final ASTNode... ns) {
    if (ns == null)
      return false;
    for (final ASTNode n : ns)
      if (n != null && Is.isNodeIncOrDecExp(n))
        return true;
    return false;
  }
  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param n JD
   * @return a duplicate of the parameter, downcasted to the returned type. */
  @SuppressWarnings("unchecked") public static <N extends ASTNode> N duplicate(final N n) {
    return (N) copySubtree(n.getAST(), n);
  }
  /** Shorthand for {@link ConditionalExpression#getElseExpression()}
   * @param e JD
   * @return the else part of the parameter */
  public static Expression elze(final ConditionalExpression e) {
    return e.getElseExpression();
  }
  /** Shorthand for {@link IfStatement#getElseStatement}
   * @param s JD
   * @return the else statement of the parameter */
  public static Statement elze(final IfStatement s) {
    return s.getElseStatement();
  }
  private static Expression find(final boolean b, final List<Expression> es) {
    for (final Expression $ : es)
      if (Is.booleanLiteral($) && b == asBooleanLiteral($).booleanValue())
        return $;
    return null;
  }
  /** Swap the order of the left and right operands to an expression, changing
   * the operator if necessary.
   * @param e JD
   * @return a newly created expression with its operands thus swapped.
   * @throws IllegalArgumentException when the parameter has extra operands.
   * @see InfixExpression#hasExtendedOperands */
  public static InfixExpression flip(final InfixExpression e) {
    if (e.hasExtendedOperands())
      throw new IllegalArgumentException(e + ": flipping undefined for an expression with extra operands ");
    return Subject.pair(right(e), left(e)).to(flip(e.getOperator()));
  }
  /** Makes an opposite operator from a given one, which keeps its logical
   * operation after the node swapping. e.g. "&" is commutative, therefore no
   * change needed. "<" isn't commutative, but it has its opposite: ">=".
   * @param o The operator to flip
   * @return The correspond operator - e.g. "<=" will become ">", "+" will stay
   *         "+". */
  public static Operator flip(final Operator o) {
    return !conjugate.containsKey(o) ? o : conjugate.get(o);
  }
  /** @param n the node from which to extract the proper fragment
   * @param name the name by which to look for the fragment
   * @return the fragment if such with the given name exists or null otherwise
   *         (or if s or name are null) */
  // TODO this seems a bug
  public static VariableDeclarationFragment getDefinition(final ASTNode n, final Expression name) {
    return hasNulls(n, name) || n.getNodeType() != VARIABLE_DECLARATION_STATEMENT || name.getNodeType() != SIMPLE_NAME ? null
        : getDefinition((VariableDeclarationStatement) n, (SimpleName) name);
  }
  private static VariableDeclarationFragment getDefinition(final VariableDeclarationStatement s, final SimpleName n) {
    return getVarDeclFrag(expose.fragments(s), n);
  }
  private static VariableDeclarationFragment getVarDeclFrag(final List<VariableDeclarationFragment> fs, final SimpleName n) {
    for (final VariableDeclarationFragment $ : fs)
      if (same(n, $.getName()))
        return $;
    return null;
  }
  public static boolean incompatible(final Assignment a1, final Assignment a2) {
    return hasNulls(a1, a2) || !compatibleOps(a1.getOperator(), a2.getOperator()) || !same(left(a1), left(a2));
  }
  /** Determine if an integer can be found in a list of values
   * @param candidate what to search for
   * @param is where to search
   * @return true if the the item is found in the list */
  @SafeVarargs public static boolean intIsIn(final int candidate, final int... is) {
    for (final int i : is)
      if (i == candidate)
        return true;
    return false;
  }
  static boolean is(final ASTNode n, final int... types) {
    return n != null && intIsIn(n.getNodeType(), types);
  }
  public static boolean isAbstractTypeDeclaration(final ASTNode n) {
    return n != null && n instanceof AbstractTypeDeclaration;
  }
  public static boolean isAnonymousClassDeclaration(final ASTNode n) {
    return is(n, ANONYMOUS_CLASS_DECLARATION);
  }
  /** Determine whether a node is a return statement
   * @param n node to check
   * @return true if the given node is a block statement */
  public static boolean isBlock(final ASTNode n) {
    return is(n, BLOCK);
  }
  /** @param n node to check
   * @return true if the given node is a boolean or null literal or false
   *         otherwise */
  public static boolean isBoolOrNull(final ASTNode n) {
    return is(n, BOOLEAN_LITERAL, NULL_LITERAL);
  }
  public static boolean isEnumDeclaration(final BodyDeclaration ¢) {
    return is(¢, ENUM_DECLARATION);
  }
  /** Determined if a node is an "expression statement"
   * @param n node to check
   * @return true if the given node is expression statement */
  public static boolean isExpressionStatement(final ASTNode n) {
    return is(n, EXPRESSION_STATEMENT);
  }
  /** Determine whether a declaration is final or not
   * @param d JD
   * @return true if declaration is final */
  public static boolean isFinal(final BodyDeclaration d) {
    return (Modifier.FINAL & d.getModifiers()) != 0;
  }
  /** Determine whether a variable declaration is final or not
   * @param s JD
   * @return true if the variable is declared as final */
  public static boolean isFinal(final VariableDeclarationStatement s) {
    return (Modifier.FINAL & s.getModifiers()) != 0;
  }
  /** @param n JD
   * @return true if the given node is an infix expression or false otherwise */
  public static boolean isInfix(final ASTNode n) {
    return is(n, INFIX_EXPRESSION);
  }
  public static boolean isInterface(final ASTNode n) {
    return is(n, TYPE_DECLARATION) && ((TypeDeclaration) n).isInterface();
  }
  public static boolean isMethodDeclaration(final ASTNode $) {
    return is($, METHOD_DECLARATION);
  }
  /** @param n node to check
   * @return true if the given node is a method invocation or false otherwise */
  public static boolean isMethodInvocation(final ASTNode n) {
    return is(n, METHOD_INVOCATION);
  }
  /** @param n node to check
   * @return true if node is an Expression Statement of type Post or Pre
   *         Expression with ++ or -- operator false if node is not an
   *         Expression Statement or its a Post or Pre fix expression that its
   *         operator is not ++ or -- */
  public static boolean isNodeIncOrDecExp(final ASTNode n) {
    switch (n.getNodeType()) {
      case EXPRESSION_STATEMENT:
        return isNodeIncOrDecExp(((ExpressionStatement) n).getExpression());
      case POSTFIX_EXPRESSION:
        return in(((PostfixExpression) n).getOperator(), PostfixExpression.Operator.INCREMENT, PostfixExpression.Operator.DECREMENT);
      case PREFIX_EXPRESSION:
        return in(asPrefixExpression(n).getOperator(), PrefixExpression.Operator.INCREMENT, PrefixExpression.Operator.DECREMENT);
      default:
        return false;
    }
  }
  /** @param a the assignment who's operator we want to check
   * @return true is the assignment's operator is assign */
  public static boolean isOpAssign(final Assignment a) {
    return a != null && a.getOperator() == Assignment.Operator.ASSIGN;
  }
  /** Determine whether a declaration is private
   * @param d JD
   * @return true if declaration is private */
  public static boolean isPrivate(final BodyDeclaration d) {
    return (Modifier.PRIVATE & d.getModifiers()) != 0;
  }
  public static boolean isSimpleName(final ASTNode $) {
    return is($, SIMPLE_NAME);
  }
  public static boolean isStatement(final ASTNode n) {
    return n instanceof Statement;
  }
  /** Determine whether a declaration is static or not
   * @param d JD
   * @return true if declaration is static */
  public static boolean isStatic(final BodyDeclaration d) {
    return (Modifier.STATIC & d.getModifiers()) != 0;
  }
  /** @param n node to check
   * @return true if the given node is a variable declaration statement or false
   *         otherwise */
  public static boolean isVariableDeclarationStatement(final ASTNode n) {
    return is(n, VARIABLE_DECLARATION_STATEMENT);
  }
  /** Shorthand for {@link Assignment#getLeftHandSide()}
   * @param a JD
   * @return the left operand of the parameter */
  public static Expression left(final Assignment a) {
    return a.getLeftHandSide();
  }
  /** Shorthand for {@link InfixExpression#getLeftOperand()}
   * @param e JD
   * @return the left operand of the parameter */
  public static Expression left(final InfixExpression e) {
    return e.getLeftOperand();
  }
  /** Shorthand for {@link InstanceofExpression#getLeftOperand()}
   * @param e JD
   * @return the left operand of the parameter */
  public static Expression left(final InstanceofExpression e) {
    return e.getLeftOperand();
  }
  /** @param e JD
   * @return the parameter, but logically negated and simplified */
  public static Expression logicalNot(final Expression e) {
    final PrefixExpression $ = Subject.operand(e).to(NOT);
    final Expression $$ = PrefixNotPushdown.simplifyNot($);
    return $$ == null ? $ : $$;
  }
  private static Map<Operator, Operator> makeConjugates() {
    final Map<Operator, Operator> $ = new HashMap<>();
    $.put(GREATER, LESS);
    $.put(LESS, GREATER);
    $.put(GREATER_EQUALS, LESS_EQUALS);
    $.put(LESS_EQUALS, GREATER_EQUALS);
    return $;
  }
  /** @param e the expression to return in the return statement
   * @return the new return statement */
  public static ThrowStatement makeThrowStatement(final Expression e) {
    return Subject.operand(e).toThrow();
  }
  public static int negationLevel(final Expression e) {
    return e instanceof PrefixExpression ? negationLevel((PrefixExpression) e)
        : e instanceof ParenthesizedExpression ? negationLevel(((ParenthesizedExpression) e).getExpression())
            : asBit(e instanceof NumberLiteral && ((NumberLiteral) e).getToken().startsWith("-"));
  }
  private static int negationLevel(final PrefixExpression e) {
    return asBit(e.getOperator() == PrefixExpression.Operator.MINUS) + negationLevel(e.getOperand());
  }
  /** Retrieve next item in a list
   * @param i an index of specific item in a list
   * @param ts the indexed list
   * @return the following item in the list, if such such an item exists,
   *         otherwise, the last node */
  public static <T> T next(final int i, final List<T> ts) {
    return !inRange(i + 1, ts) ? last(ts) : ts.get(i + 1);
  }
  /** Shorthand for {@link ASTNode#getParent()}
   * @param n JD
   * @return the parent of the parameter */
  public static ASTNode parent(final ASTNode n) {
    return n.getParent();
  }
  public static ASTParser parser(final int kind) {
    final ASTParser $ = ASTParser.newParser(ASTParser.K_COMPILATION_UNIT);
    $.setKind(kind);
    $.setResolveBindings(false);
    final Map<String, String> options = JavaCore.getOptions();
    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8); // or newer
    // version
    $.setCompilerOptions(options);
    return $;
  }
  public static Expression peelNegation(final Expression $) {
    return //
    $ instanceof PrefixExpression ? peelNegation((PrefixExpression) $) //
        : $ instanceof ParenthesizedExpression ? peelNegation(((ParenthesizedExpression) $).getExpression()) //
            : $ instanceof NumberLiteral ? peelNegation((NumberLiteral) $) : $;
  }
  private static Expression peelNegation(final NumberLiteral $) {
    return !$.getToken().startsWith("-") ? $ : $.getAST().newNumberLiteral($.getToken().substring(1));
  }
  private static Expression peelNegation(final PrefixExpression $) {
    return $.getOperator() != PrefixExpression.Operator.MINUS ? $ : peelNegation($.getOperand());
  }
  /** Retrieve previous item in a list
   * @param i an index of specific item in a list
   * @param ts the indexed list
   * @return the previous item in the list, if such an item exists, otherwise,
   *         the last node */
  public static <T> T prev(final int i, final List<T> ts) {
    return ts.get(i < 1 ? 0 : i - 1);
  }
  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param n JD
   * @param t JD
   * @return a duplicate of the parameter, downcasted to the returned type.
   * @see ASTNode#copySubtree
   * @see ASTRewrite */
  @SuppressWarnings("unchecked") public static <N extends ASTNode> N rebase(final N n, final AST t) {
    return (N) copySubtree(t, n);
  }
  /** As elze but returns the last else statement in "if - else if - ... - else"
   * statement
   * @param s
   * @return last nested else statement */
  public static Statement recursiveElze(final IfStatement s) {
    Statement $ = s.getElseStatement();
    while ($ instanceof IfStatement)
      $ = ((IfStatement) $).getElseStatement();
    return $;
  }
  /** Remove all occurrences of a boolean literal from a list of
   * {@link Expression}s
   * @param b JD
   * @param es JD */
  public static void removeAll(final boolean b, final List<Expression> es) {
    for (;;) {
      final Expression e = find(b, es);
      if (e == null)
        return;
      es.remove(e);
    }
  }
  /** Shorthand for {@link Assignment#getRightHandSide()}
   * @param a JD
   * @return the left operand of the parameter */
  public static Expression right(final Assignment a) {
    return a.getRightHandSide();
  }
  /** Shorthand for {@link CastExpression#getExpression()}
   * @param e JD
   * @return the right operand of the parameter */
  public static Expression right(final CastExpression e) {
    return e.getExpression();
  }
  /** Shorthand for {@link InfixExpression#getRightOperand()}
   * @param e JD
   * @return the right operand of the parameter */
  public static Expression right(final InfixExpression e) {
    return e.getRightOperand();
  }
  /** Determine whether two nodes are the same, in the sense that their textual
   * representations is identical.
   * <p>
   * Each of the parameters may be <code><b>null</b></code>; a
   * <code><b>null</b></code> is only equal to< code><b>null</b></code>
   * @param n1 JD
   * @param n2 JD
   * @return <code><b>true</b></code> if the parameters are the same. */
  public static boolean same(final ASTNode n1, final ASTNode n2) {
    return n1 == n2 || n1 != null && n2 != null && n1.getNodeType() == n2.getNodeType() && n1.toString().equals(n2.toString());
  }
  /** String wise comparison of all the given SimpleNames
   * @param e a string to compare all names to
   * @param ames SimplesNames to compare by their string value to cmpTo
   * @return true if all names are the same (string wise) or false otherwise */
  public static boolean same(final Expression e, final Expression... es) {
    for (final Expression x : es)
      if (!Funcs.same(e, x))
        return false;
    return true;
  }
  /** Determine whether two lists of nodes are the same, in the sense that their
   * textual representations is identical.
   * @param ns1 first list to compare
   * @param ns2 second list to compare
   * @return are the lists equal string-wise */
  public static <T extends ASTNode> boolean same(final List<T> ns1, final List<T> ns2) {
    if (ns1 == ns2)
      return true;
    if (ns1.size() != ns2.size())
      return false;
    for (int i = 0; i < ns1.size(); ++i)
      if (!same(ns1.get(i), ns2.get(i)))
        return false;
    return true;
  }
  /** Shorthand for {@link ConditionalExpression#getThenExpression()}
   * @param e JD
   * @return the then part of the parameter */
  public static Expression then(final ConditionalExpression e) {
    return e.getThenExpression();
  }
  /** Shorthand for {@link IfStatement#getThenStatement}
   * @param s JD
   * @return the then statement of the parameter */
  public static Statement then(final IfStatement s) {
    return s.getThenStatement();
  }
}
