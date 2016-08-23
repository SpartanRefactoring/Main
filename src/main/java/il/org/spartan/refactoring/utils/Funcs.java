package il.org.spartan.refactoring.utils;

import static il.org.spartan.idiomatic.*;
import static il.org.spartan.refactoring.utils.extract.*;
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
import il.org.spartan.utils.*;

/** Useful Functions */
public enum Funcs {
  ;
  @SuppressWarnings("serial") private static Map<Operator, Operator> conjugate = new HashMap<Operator, Operator>() {
    {
      put(GREATER, LESS);
      put(LESS, GREATER);
      put(GREATER_EQUALS, LESS_EQUALS);
      put(LESS_EQUALS, GREATER_EQUALS);
    }
  };
  static final PrefixExpression.Operator MINUS1 = PrefixExpression.Operator.MINUS;
  static final PrefixExpression.Operator PLUS1 = PrefixExpression.Operator.PLUS;
  static final InfixExpression.Operator MINUS2 = InfixExpression.Operator.MINUS;
  public static final InfixExpression.Operator PLUS2 = InfixExpression.Operator.PLUS;

  public static AbstractTypeDeclaration asAbstractTypeDeclaration(final ASTNode ¢) {
    return eval(() -> ((AbstractTypeDeclaration) ¢)).when(¢ instanceof AbstractTypeDeclaration);
  }

  /** Convert an {@link Expression} into {@link InfixExpression} whose operator
   * is either {@link org.eclipse.jdt.core.dom.InfixExpression.Operator#AND} or
   * {@link org.eclipse.jdt.core.dom.InfixExpression.Operator#OR}.
   * @param ¢ JD
   * @return parameter thus converted, or <code><b>null</b> if the conversion is
   *         not possible for it */
  public static InfixExpression asAndOrOr(final Expression ¢) {
    return !Is.infix(¢) || !Is.deMorgan(asInfixExpression(¢).getOperator()) ? null : asInfixExpression(¢);
  }

  /** Convert, is possible, an {@link ASTNode} to an {@link Assignment}
   * @param $ JD
   * @return argument, but down-casted to a {@link Assignment}, or
   *         <code><b>null</b></code> if the downcast is impossible. */
  public static Assignment asAssignment(final ASTNode $) {
    return !Is.is($, ASSIGNMENT) ? null : (Assignment) $;
  }

  /** Convert, is possible, an {@link ASTNode} to a {@link Block}
   * @param $ JD
   * @return argument, but down-casted to a {@link Block}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static Block asBlock(final ASTNode $) {
    return !Is.is($, BLOCK) ? null : (Block) $;
  }

  /** Down-cast, if possible, to {@link BooleanLiteral}
   * @param ¢ JD
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static BooleanLiteral asBooleanLiteral(final ASTNode $) {
    return !Is.is($, BOOLEAN_LITERAL) ? null : (BooleanLiteral) $;
  }

  /** Convert an {@link Expression} into {@link InfixExpression} whose operator
   * is one of the six comparison operators: <code><</code>, <code><=</code>,
   * <code>></code>, <code>>=</code>, <code>!=</code>, or <code>==</code>.
   * @param ¢ JD
   * @return parameter thus converted, or <code><b>null</b> if the conversion is
   *         not possible for it */
  public static InfixExpression asComparison(final Expression ¢) {
    return !(¢ instanceof InfixExpression) ? null : asComparison((InfixExpression) ¢);
  }

  /** Convert, is possible, an {@link ASTNode} to a
   * {@link ConditionalExpression}
   * @param ¢ JD
   * @return argument, but down-casted to a {@link ConditionalExpression}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static ConditionalExpression asConditionalExpression(final ASTNode ¢) {
    return !(¢ instanceof ConditionalExpression) ? null : (ConditionalExpression) ¢;
  }

  /** Down-cast, if possible, to {@link ConditionalExpression}
   * @param ¢ JD
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static ConditionalExpression asConditionalExpression(final Expression ¢) {
    return !(¢ instanceof ConditionalExpression) ? null : (ConditionalExpression) ¢;
  }

  /** Down-cast, if possible, to {@link Expression}
   * @param ¢ JD
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static Expression asExpression(final ASTNode ¢) {
    return !(¢ instanceof Expression) ? null : (Expression) ¢;
  }

  /** Down-cast, if possible, to {@link ExpressionStatement}
   * @param ¢ JD
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static ExpressionStatement asExpressionStatement(final ASTNode ¢) {
    return !(¢ instanceof ExpressionStatement) ? null : (ExpressionStatement) ¢;
  }

  /** Down-cast, if possible, to {@link IfStatement}
   * @param $ JD
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static IfStatement asIfStatement(final ASTNode $) {
    return !Is.is($, IF_STATEMENT) ? null : (IfStatement) $;
  }

  /** Down-cast, if possible, to {@link InfixExpression}
   * @param ¢ JD
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static InfixExpression asInfixExpression(final ASTNode ¢) {
    return !(¢ instanceof InfixExpression) ? null : (InfixExpression) ¢;
  }

  /** Convert, is possible, an {@link ASTNode} to a {@link MethodDeclaration}
   * @param $ JD
   * @return argument, but down-casted to a {@link MethodDeclaration}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static MethodDeclaration asMethodDeclaration(final ASTNode $) {
    return eval(() -> ((MethodDeclaration) $)).when($ instanceof MethodDeclaration);
  }

  /** Down-cast, if possible, to {@link MethodInvocation}
   * @param ¢ JD
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static MethodInvocation asMethodInvocation(final Expression ¢) {
    return !(¢ instanceof MethodInvocation) ? null : (MethodInvocation) ¢;
  }

  /** Convert an {@link Expression} into a {@link PrefixExpression} whose
   * operator is <code>!</code>,
   * @param ¢ JD
   * @return parameter thus converted, or <code><b>null</b> if the conversion is
   *         not possible for it */
  public static PrefixExpression asNot(final Expression ¢) {
    return !(¢ instanceof PrefixExpression) ? null : asNot(asPrefixExpression(¢));
  }

  /** Down-cast, if possible, to {@link NumberLiteral}
   * @param ¢ JD
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static NumberLiteral asNumberLiteral(final ASTNode ¢) {
    return !Is.isNumberLiteral(¢) ? null : (NumberLiteral) ¢;
  }

  public static ParenthesizedExpression asParenthesizedExpression(final Expression $) {
    return !Is.is($, PARENTHESIZED_EXPRESSION) ? null : (ParenthesizedExpression) $;
  }

  /** Down-cast, if possible, to {@link InfixExpression}
   * @param ¢ JD
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static PostfixExpression asPostfixExpression(final ASTNode ¢) {
    return eval(() -> (PostfixExpression) ¢).when(¢ instanceof PostfixExpression);
  }

  /** Down-cast, if possible, to {@link PrefixExpression}
   * @param ¢ JD
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static PrefixExpression asPrefixExpression(final ASTNode ¢) {
    return eval(() -> (PrefixExpression) ¢).when(¢ instanceof PrefixExpression);
  }

  /** Determines whether a give {@link ASTNode} includes precisely one
   * {@link Statement}, and return this statement.
   * @param ¢ The node from which to return statement.
   * @return single return statement contained in the parameter, or
   *         <code><b>null</b></code> if no such value exists. */
  public static ReturnStatement asReturn(final ASTNode ¢) {
    return asReturn(singleStatement(¢));
  }

  /** Down-cast, if possible, to {@link ReturnStatement}
   * @param $ JD
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static ReturnStatement asReturnStatement(final ASTNode $) {
    return !Is.is($, RETURN_STATEMENT) ? null : (ReturnStatement) $;
  }

  /** Convert, is possible, an {@link ASTNode} to a {@link SimpleName}
   * @param $ JD
   * @return argument, but down-casted to a {@link SimpleName}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static SimpleName asSimpleName(final ASTNode $) {
    return eval(() -> (SimpleName) $).when($ instanceof SimpleName);
  }

  /** Down-cast, if possible, to {@link Statement}
   * @param ¢ JD
   * @return parameter down-casted to the returned type, or
   *         <code><b>null</b></code> if no such down-casting is possible. */
  public static Statement asStatement(final ASTNode ¢) {
    return !Is.isStatement(¢) ? null : (Statement) ¢;
  }

  /** Obtain a condensed textual representation of an {@link ASTNode}
   * @param ¢ JD
   * @return textual representation of the parameter, */
  public static String asString(final ASTNode ¢) {
    return removeWhites(body(¢));
  }

  public static StringLiteral asStringLiteral(final ASTNode ¢) {
    return !Is.is(¢, STRING_LITERAL) ? null : (StringLiteral) ¢;
  }

  /** Convert, is possible, an {@link ASTNode} to a
   * {@link ConditionalExpression}
   * @param ¢ JD
   * @return argument, but down-casted to a {@link ConditionalExpression}, or
   *         <code><b>null</b></code> if no such down-cast is possible.. */
  public static ThrowStatement asThrowStatement(final ASTNode ¢) {
    return !(¢ instanceof ThrowStatement) ? null : (ThrowStatement) ¢;
  }

  /** Convert, if possible, an {@link Expression} to a
   * {@link VariableDeclarationExpression}
   * @param ¢ JD
   * @return argument, but down-casted to a
   *         {@link VariableDeclarationExpression}, or <code><b>null</b></code>
   *         if no such down-cast is possible.. */
  public static VariableDeclarationExpression asVariableDeclarationExpression(final Expression ¢) {
    return ¢.getNodeType() != VARIABLE_DECLARATION_EXPRESSION ? null : (VariableDeclarationExpression) ¢;
  }

  public static <T> List<T> chop(final List<T> ts) {
    if (ts.isEmpty())
      return null;
    ts.remove(0);
    return ts;
  }

  /** @param root the node whose children we return
   * @return A list containing all the nodes in the given root'¢ sub tree */
  public static List<ASTNode> collectDescendants(final ASTNode root) {
    if (root == null)
      return null;
    final List<ASTNode> $ = new ArrayList<>();
    root.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        $.add(¢);
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
    for (final Assignment.Operator ¢ : os)
      if (¢ == null || ¢ != cmpTo)
        return false;
    return true;
  }

  /** Makes an opposite operator from a given one, which keeps its logical
   * operation after the node swapping. ¢.¢. "&" is commutative, therefore no
   * change needed. "<" isn'¢ commutative, but it has its opposite: ">=".
   * @param ¢ The operator to flip
   * @return correspond operator - ¢.¢. "<=" will become ">", "+" will stay
   *         "+". */
  public static Operator conjugate(final Operator ¢) {
    return !conjugate.containsKey(¢) ? ¢ : conjugate.get(¢);
  }

  /** @param ns unknown number of nodes to check
   * @return true if one of the nodes is an Expression Statement of type Post or
   *         Pre Expression with ++ or -- operator. false if none of them are or
   *         if the given parameter is null. */
  public static boolean containIncOrDecExp(final ASTNode... ns) {
    if (ns == null)
      return false;
    for (final ASTNode ¢ : ns)
      if (¢ != null && Is.isIncrementOrDecrement(¢))
        return true;
    return false;
  }

  public static int countNegations(final InfixExpression e) {
    return countNegations(extract.operands(e));
  }

  public static int countNegations(final List<Expression> es) {
    int $ = 0;
    for (final Expression e : es)
      $ += negationLevel(e);
    return $;
  }

  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param ¢ JD
   * @return a duplicate of the parameter, downcasted to the returned type. */
  @SuppressWarnings("unchecked") public static <¢ extends ASTNode> ¢ duplicate(final ¢ ¢) {
    return (¢) copySubtree(¢.getAST(), ¢);
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

  public static <T> T first(final List<T> ts) {
    return ts == null || ts.isEmpty() ? null : ts.get(0);
  }

  /** Swap the order of the left and right operands to an expression, changing
   * the operator if necessary.
   * @param ¢ JD
   * @return a newly created expression with its operands thus swapped.
   * @throws IllegalArgumentException when the parameter has extra operands.
   * @see InfixExpression#hasExtendedOperands */
  public static InfixExpression flip(final InfixExpression ¢) {
    if (¢.hasExtendedOperands())
      throw new IllegalArgumentException(¢ + ": flipping undefined for an expression with extra operands ");
    return subject.pair(right(¢), left(¢)).to(conjugate(¢.getOperator()));
  }

  /** @param n the node from which to extract the proper fragment
   * @param e the name by which to look for the fragment
   * @return fragment if such with the given name exists or null otherwise (or
   *         if ¢ or name are null) */
  // TODO this seems a bug
  public static VariableDeclarationFragment getDefinition(final ASTNode n, final Expression e) {
    return hasNulls(n, e) || n.getNodeType() != VARIABLE_DECLARATION_STATEMENT || e.getNodeType() != SIMPLE_NAME ? null
        : getDefinition((VariableDeclarationStatement) n, (SimpleName) e);
  }

  public static boolean incompatible(final Assignment a1, final Assignment a2) {
    return hasNulls(a1, a2) || !compatibleOps(a1.getOperator(), a2.getOperator()) || !same(left(a1), left(a2));
  }

  /** Determine if an integer can be found in a list of values
   * @param candidate what to search for
   * @param is where to search
   * @return true if the the item is found in the list */
  @SafeVarargs public static boolean intIsIn(final int candidate, final int... is) {
    for (final int ¢ : is)
      if (¢ == candidate)
        return true;
    return false;
  }

  /** Shorthand for {@link Assignment#getLeftHandSide()}
   * @param a JD
   * @return left operand of the parameter */
  public static Expression left(final Assignment a) {
    return a.getLeftHandSide();
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

  /** @param ¢ JD
   * @return parameter, but logically negated and simplified */
  public static Expression logicalNot(final Expression ¢) {
    final PrefixExpression $ = subject.operand(¢).to(NOT);
    final Expression $$ = PrefixNotPushdown.simplifyNot($);
    return $$ == null ? $ : $$;
  }

  /** @param ¢ the expression to return in the return statement
   * @return new return statement */
  public static ThrowStatement makeThrowStatement(final Expression ¢) {
    return subject.operand(¢).toThrow();
  }

  public static int negationLevel(final Expression ¢) {
    return Is.is(¢, PREFIX_EXPRESSION) ? negationLevel((PrefixExpression) ¢)
        : Is.is(¢, PARENTHESIZED_EXPRESSION) ? negationLevel(expression(¢))
            : asBit(Is.isNumberLiteral(¢) && asNumberLiteral(¢).getToken().startsWith("-"));
  }

  /** Create a new {@link SimpleName} instance at the AST of the parameter
   * @param n JD
   * @param newName the name that the returned value shall bear
   * @return a new {@link SimpleName} instance at the AST of the parameter */
  public static SimpleName newSimpleName(final ASTNode n, final String newName) {
    return n.getAST().newSimpleName(newName);
  }

  /** Retrieve next item in a list
   * @param i an index of specific item in a list
   * @param ts the indexed list
   * @return following item in the list, if such such an item exists, otherwise,
   *         the last node */
  public static <T> T next(final int i, final List<T> ts) {
    return !inRange(i + 1, ts) ? last(ts) : ts.get(i + 1);
  }

  public static <T> T onlyOne(final List<T> ts) {
    return ts == null || ts.size() != 1 ? null : ts.get(0);
  }

  /** Shorthand for {@link ASTNode#getParent()}
   * @param ¢ JD
   * @return parent of the parameter */
  public static ASTNode parent(final ASTNode ¢) {
    return ¢.getParent();
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
        : $ instanceof ParenthesizedExpression ? peelNegation(core($)) //
            : $ instanceof NumberLiteral ? peelNegation((NumberLiteral) $) //
                : $;
  }

  /** Retrieve previous item in a list
   * @param i an index of specific item in a list
   * @param ts the indexed list
   * @return previous item in the list, if such an item exists, otherwise, the
   *         last node */
  public static <T> T prev(final int i, final List<T> ts) {
    return ts.get(i < 1 ? 0 : i - 1);
  }

  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param ¢ JD
   * @param ¢ JD
   * @return a duplicate of the parameter, downcasted to the returned type.
   * @see ASTNode#copySubtree
   * @see ASTRewrite */
  @SuppressWarnings("unchecked") public static <N extends ASTNode> N rebase(final N n, final AST t) {
    return (N) copySubtree(t, n);
  }

  /** As {@link #elze(ConditionalExpression)} but returns the last else
   * statement in "if - else if - ... - else" statement
   * @param ¢ JD
   * @return last nested else statement */
  public static Statement recursiveElze(final IfStatement ¢) {
    Statement $ = ¢.getElseStatement();
    while ($ instanceof IfStatement)
      $ = ((IfStatement) $).getElseStatement();
    return $;
  }

  /** Remove all occurrences of a boolean literal from a list of
   * {@link Expression}¢
   * @param ¢ JD
   * @param es JD */
  public static void removeAll(final boolean b, final List<Expression> es) {
    for (;;) {
      final Expression ¢ = find(b, es);
      if (¢ == null)
        return;
      es.remove(¢);
    }
  }

  public static <T> Iterable<T> rest(final Iterable<T> ts) {
    return () -> {
      return new Iterator<T>() {
        final Iterator<T> $ = ts.iterator();
        {
          $.next();
        }

        @Override public boolean hasNext() {
          return $.hasNext();
        }

        @Override public T next() {
          return $.next();
        }
      };
    };
  }

  public static <T> Iterable<T> rest2(final Iterable<T> ts) {
    return rest(rest(ts));
  }

  /** Shorthand for {@link Assignment#getRightHandSide()}
   * @param ¢ JD
   * @return left operand of the parameter */
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

  /** Determine whether two nodes are the same, in the sense that their textual
   * representations is identical. <¢> Each of the parameters may be
   * <code><b>null</b></code>; a <code><b>null</b></code> is only equal to<
   * code><b>null</b></code>
   * @param n1 JD
   * @param n2 JD
   * @return <code><b>true</b></code> if the parameters are the same. */
  public static boolean same(final ASTNode n1, final ASTNode n2) {
    return n1 == n2 || n1 != null && n2 != null && n1.getNodeType() == n2.getNodeType() && body(n1).equals(body(n2));
  }

  private static String body(final ASTNode ¢) {
    return Utils.gist(¢.toString());
  }

  /** String wise comparison of all the given SimpleNames
   * @param ¢ string to compare all names to
   * @param es SimplesNames to compare by their string value to cmpTo
   * @return true if all names are the same (string wise) or false otherwise */
  public static boolean same(final Expression e, final Expression... es) {
    for (final Expression ¢ : es)
      if (!Funcs.same(¢, e))
        return false;
    return true;
  }

  /** Determine whether two lists of nodes are the same, in the sense that their
   * textual representations is identical.
   * @param ns1 first list to compare
   * @param ns2 second list to compare
   * @return are the lists equal string-wise */
  public static <¢ extends ASTNode> boolean same(final List<¢> ns1, final List<¢> ns2) {
    if (ns1 == ns2)
      return true;
    if (ns1.size() != ns2.size())
      return false;
    for (int ¢ = 0; ¢ < ns1.size(); ++¢)
      if (!same(ns1.get(¢), ns2.get(¢)))
        return false;
    return true;
  }

  public static <T> T second(final List<T> ts) {
    return ts == null || ts.size() < 2 ? null : ts.get(1);
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

  static PrefixExpression asNot(final PrefixExpression ¢) {
    return NOT.equals(¢.getOperator()) ? ¢ : null;
  }

  private static int asBit(final boolean ¢) {
    return ¢ ? 1 : 0;
  }

  private static InfixExpression asComparison(final InfixExpression ¢) {
    return in(¢.getOperator(), //
        GREATER, //
        GREATER_EQUALS, //
        LESS, //
        LESS_EQUALS, //
        EQUALS, //
        NOT_EQUALS //
    ) ? ¢ : null;
  }

  /** Find the first matching expression to the given boolean (b).
   * @param b JD,
   * @param es JD
   * @return first expression from the given list (es) whose boolean value
   *         matches to the given boolean (b). */
  private static Expression find(final boolean b, final List<Expression> es) {
    for (final Expression $ : es)
      if (Is.booleanLiteral($) && b == asBooleanLiteral($).booleanValue())
        return $;
    return null;
  }

  private static VariableDeclarationFragment getDefinition(final VariableDeclarationStatement s, final SimpleName n) {
    return getVarDeclFrag(expose.fragments(s), n);
  }

  private static VariableDeclarationFragment getVarDeclFrag(final List<VariableDeclarationFragment> fs, final SimpleName ¢) {
    for (final VariableDeclarationFragment $ : fs)
      if (same(¢, $.getName()))
        return $;
    return null;
  }

  private static int negationLevel(final PrefixExpression ¢) {
    return asBit(¢.getOperator() == PrefixExpression.Operator.MINUS) + negationLevel(¢.getOperand());
  }

  private static Expression peelNegation(final NumberLiteral $) {
    return !$.getToken().startsWith("-") ? $ : $.getAST().newNumberLiteral($.getToken().substring(1));
  }

  private static Expression peelNegation(final PrefixExpression $) {
    final org.eclipse.jdt.core.dom.PrefixExpression.Operator o = $.getOperator();
    return o != MINUS1 && o != PLUS1 ? $ : peelNegation($.getOperand());
  }
}
