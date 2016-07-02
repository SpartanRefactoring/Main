package il.org.spartan.refactoring.utils;

import static il.org.spartan.Utils.*;
import static il.org.spartan.idiomatic.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Restructure.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import il.org.spartan.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/**
 * An empty <code><b>enum</b></code> for fluent programming. The name should say
 * it all: The name, followed by a dot, followed by a method name, should read
 * like a sentence phrase.
 *
 * @author Yossi Gil
 * @since 2015-07-28
 */
public enum extract {
  ;
  /**
   * Retrieve all operands, including parenthesized ones, under an expression
   *
   * @param e JD
   * @return a {@link List} of all operands to the parameter
   */
  public static List<Expression> allOperands(final InfixExpression e) {
    return extract.operands(flatten(e));
  }
  /**
   * @param n a statement or block to extract the assignment from
   * @return null if the block contains more than one statement or if the
   *         statement is not an assignment or the assignment if it exists
   */
  public static Assignment assignment(final ASTNode n) {
    return asAssignment(expression(extract.expressionStatement(n)));
  }
  /**
   * @param e
   * @return
   */
  private static Expression expression(final ExpressionStatement e) {
    return e == null ? null : e.getExpression();
  }
  /**
   * Find the "core" of a given {@link Expression}, by peeling of any
   * parenthesis that may wrap it.
   *
   * @param $ JD
   * @return the parameter itself, if not parenthesized, or the result of
   *         applying this function (@link {@link #getClass()}) to whatever is
   *         wrapped in these parenthesis.
   */
  public static Expression core(final Expression $) {
    return $ == null || $.getNodeType() != PARENTHESIZED_EXPRESSION ? $ : core(((ParenthesizedExpression) $).getExpression());
  }
  /**
   * Computes the "essence" of a statement, i.e., if a statement is essentially
   * a single, non-empty, non-block statement, possibly wrapped in brackets,
   * perhaps along with any number of empty statements, then its essence is this
   * single non-empty statement.
   *
   * @param s JD
   * @return the essence of the parameter, or <code><b>null</b></code>, if there
   *         are no non-empty statements within the parameter. If, however there
   *         are multiple non-empty statements inside the parameter then the
   *         parameter itself is returned.
   */
  public static Statement core(final Statement s) {
    if (Scalpel.isInaccessible(s))
      return s;
    final List<Statement> ss = extract.statements(s);
    switch (ss.size()) {
      case 0:
        return null;
      case 1:
        return ss.get(0);
      default:
        return s;
    }
  }
  /**
   * @param n a node to extract an expression from
   * @return null if the statement is not an expression, nor a return statement,
   *         nor a throw statement. Otherwise, the expression in these.
   */
  public static Expression expression(final ASTNode n) {
    if (n == null)
      return null;
    switch (n.getNodeType()) {
      case ASTNode.EXPRESSION_STATEMENT:
        return ((ExpressionStatement) n).getExpression();
      case ASTNode.RETURN_STATEMENT:
        return ((ReturnStatement) n).getExpression();
      case ASTNode.THROW_STATEMENT:
        return ((ThrowStatement) n).getExpression();
      default:
        return null;
    }
  }
  /**
   * Convert, is possible, an {@link ASTNode} to a {@link ExpressionStatement}
   *
   * @param n a statement or a block to extract the expression statement from
   * @return the expression statement if n is a block or an expression statement
   *         or null if it not an expression statement or if the block contains
   *         more than one statement
   */
  public static ExpressionStatement expressionStatement(final ASTNode n) {
    return n == null ? null : asExpressionStatement(extract.singleStatement(n));
  }
  /**
   * Search for a {@link PrefixExpression} in the tree rooted at an
   * {@link ASTNode}.
   *
   * @param n JD
   * @return the first {@link PrefixExpression} found in an {@link ASTNode n},
   *         or <code><b>null</b> if there is no such statement.
   */
  public static PostfixExpression findFirstPostfix(final ASTNode n) {
    final maybe<PostfixExpression> $ = maybe.no();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final PostfixExpression ¢) {
        if ($.missing())
          $.set(¢);
        return false;
      }
    });
    return $.get();
  }
  /**
   * Search for an {@link IfStatement} in the tree rooted at an {@link ASTNode}.
   *
   * @param n JD
   * @return the first {@link IfStatement} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement.
   */
  public static IfStatement firstIfStatement(final ASTNode n) {
    if (n == null)
      return null;
    final maybe<IfStatement> $ = maybe.no();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final IfStatement s) {
        if ($.missing())
          $.set(s);
        return false;
      }
    });
    return $.get();
  }
  /**
   * Search for an {@link MethodDeclaration} in the tree rooted at an
   * {@link ASTNode}.
   *
   * @param n JD
   * @return the first {@link IfStatement} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement.
   */
  public static MethodDeclaration firstMethodDeclaration(final ASTNode n) {
    final maybe<MethodDeclaration> $ = maybe.no();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final MethodDeclaration d) {
        if ($.missing())
          $.set(d);
        return false;
      }
    });
    return $.get();
  }
  /**
   * Find the first {@link InfixExpression} representing an addition, under a
   * given node, as found in the usual visitation order.
   *
   * @param n JD
   * @return the first {@link InfixExpression} representing an addition under
   *         the parameter given node, or <code><b>null</b></code> if no such
   *         value could be found.
   */
  public static InfixExpression firstPlus(final ASTNode n) {
    final maybe<InfixExpression> $ = maybe.no();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final InfixExpression e) {
        if ($.present())
          return false;
        if (e.getOperator() != InfixExpression.Operator.PLUS)
          return true;
        $.set(e);
        return false;
      }
    });
    return $.get();
  }
  public static Type firstType(final Statement s) {
    final maybe<Type> $ = maybe.no();
    s.accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode n) {
        if (!(n instanceof Type))
          return true;
        $.set((Type) n);
        return false;
      }
    });
    return $.get();
  }
  /**
   * Return the first {@link VariableDeclarationFragment} encountered in a visit
   * of the tree rooted a the parameter.
   *
   * @param n JD
   * @return the first such node encountered in a visit of the tree rooted a the
   *         parameter, or <code><b>null</b></code>
   */
  public static VariableDeclarationFragment firstVariableDeclarationFragment(final ASTNode n) {
    if (n == null)
      return null;
    final maybe<VariableDeclarationFragment> $ = maybe.no();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final VariableDeclarationFragment f) {
        if ($.missing())
          $.set(f);
        return false;
      }
    });
    return $.get();
  }
  /**
   * Extract the single {@link ReturnStatement} embedded in a node.
   *
   * @param n JD
   * @return the single {@link IfStatement} embedded in the parameter or
   *         <code><b>null</b></code> if not such statements exists.
   */
  public static IfStatement ifStatement(final ASTNode n) {
    return asIfStatement(extract.singleStatement(n));
  }
  /**
   * Find the last statement residing under a given {@link Statement}
   *
   * @param s JD
   * @return the last statement residing under a given {@link Statement}, or
   *         <code><b>null</b></code> if not such statements exists.
   */
  public static ASTNode lastStatement(final Statement s) {
    return last(statements(s));
  }
  /**
   * Extract the {@link MethodDeclaration} that contains a given node.
   *
   * @param n JD
   * @return the inner most {@link MethodDeclaration} in which the parameter is
   *         nested, or <code><b>null</b></code>, if no such statement exists.
   */
  public static MethodDeclaration methodDeclaration(final ASTNode n) {
    for (ASTNode $ = n; $ != null; $ = $.getParent())
      if (Is.methodDeclaration($))
        return asMethodDeclaration($);
    return null;
  }
  /**
   * @param n JD
   * @return the method invocation if it exists or null if it doesn't or if the
   *         block contains more than one statement
   */
  public static MethodInvocation methodInvocation(final ASTNode n) {
    return asMethodInvocation(extract.expressionStatement(n).getExpression());
  }
  /**
   * Find the {@link Assignment} that follows a given node.
   *
   * @param n JD
   * @return the {@link Assignment} that follows the parameter, or
   *         <code><b>null</b></code> if not such value exists.
   */
  public static Assignment nextAssignment(final ASTNode n) {
    return extract.assignment(nextStatement(n));
  }
  /**
   * Extract the {@link IfStatement} that immediately follows a given node
   *
   * @param n JD
   * @return the {@link IfStatement} that immediately follows the parameter, or
   *         <code><b>null</b></code>, if no such statement exists.
   */
  public static IfStatement nextIfStatement(final ASTNode n) {
    return asIfStatement(nextStatement(n));
  }
  /**
   * Extract the {@link ReturnStatement} that immediately follows a given node
   *
   * @param n JD
   * @return the {@link ReturnStatement} that immediately follows the parameter,
   *         or <code><b>null</b></code>, if no such statement exists.
   */
  public static ReturnStatement nextReturn(final ASTNode n) {
    return asReturnStatement(nextStatement(n));
  }
  /**
   * Extract the {@link Statement} that immediately follows a given node.
   *
   * @param n JD
   * @return the {@link Statement} that immediately follows the parameter, or
   *         <code><b>null</b></code>, if no such statement exists.
   */
  public static Statement nextStatement(final ASTNode n) {
    return nextStatement(extract.statement(n));
  }
  /**
   * Extract the {@link Statement} that immediately follows a given statement
   *
   * @param s JD
   * @return the {@link Statement} that immediately follows the parameter, or
   *         <code><b>null</b></code>, if no such statement exists.
   */
  public static Statement nextStatement(final Statement s) {
    if (s == null)
      return null;
    final Block b = asBlock(s.getParent());
    return unless(b == null).eval(() -> next(s, extract.statements(b)));
  }
  /**
   * Makes a list of all operands of an expression, comprising the left operand,
   * the right operand, followed by extra operands when they exist.
   *
   * @param e JD
   * @return a list of all operands of an expression
   */
  public static List<Expression> operands(final InfixExpression e) {
    if (e == null)
      return null;
    final List<Expression> $ = new ArrayList<>();
    $.add(left(e));
    $.add(right(e));
    if (e.hasExtendedOperands()) {
      @SuppressWarnings("unchecked") final List<Expression> extendedOperands = e.extendedOperands();
      $.addAll(extendedOperands);
    }
    return $;
  }
  /**
   * Finds the expression returned by a return statement
   *
   * @param n a node to extract an expression from
   * @return null if the statement is not an expression or return statement or
   *         the expression if they are
   */
  public static Expression returnExpression(final ASTNode n) {
    return extract.expression(extract.returnStatement(n));
  }
  /**
   * @param $
   * @return
   */
  private static Expression expression(final ReturnStatement $) {
    return $ == null ? null : $.getExpression();
  }
  /**
   * Extract the single {@link ReturnStatement} embedded in a node.
   *
   * @param n JD
   * @return the single {@link ReturnStatement} embedded in the parameter, and
   *         return it; <code><b>null</b></code> if not such statements exists.
   */
  public static ReturnStatement returnStatement(final ASTNode n) {
    return asReturnStatement(extract.singleStatement(n));
  }
  /**
   * Finds the single statement in the <code><b>else</b></code> branch of an
   * {@link IfStatement}
   *
   * @param s JD
   * @return the single statement in the <code><b>else</b></code> branch of the
   *         parameter, or <code><b>null</b></code>, if no such statement
   *         exists.
   */
  public static Statement singleElse(final IfStatement s) {
    return extract.singleStatement(elze(s));
  }
  /**
   * @param n JD
   * @return if b is a block with just 1 statement it returns that statement, if
   *         b is statement it returns b and if b is null it returns a null
   */
  public static Statement singleStatement(final ASTNode n) {
    final List<Statement> $ = extract.statements(n);
    return unless($.size() != 1).eval(() -> $.get(0));
  }
  /**
   * Finds the single statement in the "then" branch of an {@link IfStatement}
   *
   * @param s JD
   * @return the single statement in the "then" branch of the parameter, or
   *         <code><b>null</b></code>, if no such statement exists.
   */
  public static Statement singleThen(final IfStatement s) {
    return extract.singleStatement(then(s));
  }
  /**
   * Extract the {@link Statement} that contains a given node.
   *
   * @param n JD
   * @return the inner most {@link Statement} in which the parameter is nested,
   *         or <code><b>null</b></code>, if no such statement exists.
   */
  public static Statement statement(final ASTNode n) {
    for (ASTNode $ = n; $ != null; $ = $.getParent())
      if (Is.statement($))
        return asStatement($);
    return null;
  }
  /**
   * Extract the list of non-empty statements embedded in node (nesting within
   * control structure such as <code><b>if</b></code> are not removed.)
   *
   * @param n JD
   * @return the list of such statements.
   */
  public static List<Statement> statements(final ASTNode n) {
    final List<Statement> $ = new ArrayList<>();
    return n == null || !(n instanceof Statement) ? $ : extract.statementsInto((Statement) n, $);
  }
  /**
   * @param n a node to extract an expression from
   * @return null if the statement is not an expression or return statement or
   *         the expression if they are
   */
  public static Expression throwExpression(final ASTNode n) {
    final ThrowStatement $ = asThrowStatement(extract.singleStatement(n));
    return $ == null ? null : $.getExpression();
  }
  /**
   * Extract the single {@link ThrowStatement} embedded in a node.
   *
   * @param n JD
   * @return the single {@link ThrowStatement} embedded in the parameter, and
   *         return it; <code><b>null</b></code> if not such statements exists.
   */
  public static ThrowStatement throwStatement(final ASTNode n) {
    return asThrowStatement(extract.singleStatement(n));
  }
  private static Statement next(final Statement s, final List<Statement> ss) {
    for (int i = 0; i < ss.size() - 1; ++i)
      if (ss.get(i) == s)
        return ss.get(i + 1);
    return null;
  }
  private static List<Statement> statementsInto(final Block b, final List<Statement> $) {
    for (final Object statement : b.statements())
      extract.statementsInto((Statement) statement, $);
    return $;
  }
  private static List<Statement> statementsInto(final Statement s, final List<Statement> $) {
    switch (s.getNodeType()) {
      case EMPTY_STATEMENT:
        return $;
      case BLOCK:
        return extract.statementsInto((Block) s, $);
      default:
        $.add(s);
        return $;
    }
  }
}
