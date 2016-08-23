package il.org.spartan.refactoring.utils;

import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Restructure.*;
import static il.org.spartan.refactoring.utils.expose.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-28 */
public enum extract {
  ;
  /** Retrieve all operands, including parenthesized ones, under an expression
   * @param e JD
   * @return a {@link List} of all operands to the parameter */
  public static List<Expression> allOperands(final InfixExpression e) {
    return extract.operands(flatten(e));
  }

  /** Retrieves the ancestors of the ASTNode, via an Iterator.
   * @param ¢ JD
   * @return an {@link Iterable} that traverses the ancestors of the ASTNode.
   *         Use case: Counting the number of Expressions among a given
   *         ASTNode's ancestors */
  public static Iterable<ASTNode> ancestors(final ASTNode ¢) {
    return () -> new Iterator<ASTNode>() {
      ASTNode current = ¢;

      @Override public boolean hasNext() {
        return current != null;
      }

      @Override public ASTNode next() {
        final ASTNode $ = current;
        current = current.getParent();
        return $;
      }
    };
  }

  /** @param n a statement or block to extract the assignment from
   * @return null if the block contains more than one statement or if the
   *         statement is not an assignment or the assignment if it exists */
  public static Assignment assignment(final ASTNode n) {
    final ExpressionStatement e = extract.expressionStatement(n);
    return e == null ? null : asAssignment(e.getExpression());
  }

  public static CompilationUnit compilationUnit(final ASTNode ¢) {
    return (CompilationUnit) AncestorSearch.forType(COMPILATION_UNIT).from(¢);
  }

  public static InfixExpression.Operator operator(final InfixExpression e) {
    return e == null ? null : e.getOperator();
  }

  public static PrefixExpression.Operator operator(final PrefixExpression e) {
    return e == null ? null : e.getOperator();
  }

  public static PostfixExpression.Operator operator(final PostfixExpression e) {
    return e == null ? null : e.getOperator();
  }

  /** @param ¢ JD
   * @return ASTNode of the type if one of ¢'s parent ancestors is a container
   *         type and null otherwise */
  public static ASTNode containerType(final ASTNode ¢) {
    for (final ASTNode $ : ancestors(¢.getParent()))
      if (Is.is($, ANONYMOUS_CLASS_DECLARATION //
          , ANNOTATION_TYPE_DECLARATION //
          , ENUM_DECLARATION //
          , TYPE_DECLARATION //
          , ENUM_CONSTANT_DECLARATION //
      ))
        return $;
    return null;
  }

  /** Peels any parenthesis that may wrap an {@Link Expression}
   * @param $ JD
   * @return the parameter if not parenthesized, or the unparenthesized this
   *         version of it */
  public static Expression core(final Expression $) {
    return $ == null ? $ //
        : Is.is($, PARENTHESIZED_EXPRESSION) ? core(Funcs.asParenthesizedExpression($).getExpression()) //
            : Is.is($, PREFIX_EXPRESSION) ? core(asPrefixExpression($)) //
                : $;
  }

  public static Expression core(final PrefixExpression $) {
    return $.getOperator() != PLUS1 ? $ : core($.getOperand());
  }

  /** Computes the "essence" of a statement, i.e., if a statement is essentially
   * a single, non-empty, non-block statement, possibly wrapped in brackets,
   * perhaps along with any number of empty statements, then its essence is this
   * single non-empty statement.
   * @param s JD
   * @return essence of the parameter, or <code><b>null</b></code>, if there are
   *         no non-empty statements within the parameter. If, however there are
   *         multiple non-empty statements inside the parameter then the
   *         parameter itself is returned. */
  public static Statement core(final Statement s) {
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

  /** @param n a node to extract an expression from
   * @return null if the statement is not an expression, nor a return statement,
   *         nor a throw statement. Otherwise, the expression in these. */
  public static Expression expression(final ASTNode n) {
    if (n == null)
      return null;
    switch (n.getNodeType()) {
      case ASTNode.EXPRESSION_STATEMENT:
        return expression((ExpressionStatement) n);
      case ASTNode.RETURN_STATEMENT:
        return expression((ReturnStatement) n);
      case ASTNode.THROW_STATEMENT:
        return expression((ThrowStatement) n);
      case ASTNode.CLASS_INSTANCE_CREATION:
        return expression((ClassInstanceCreation) n);
      case ASTNode.CAST_EXPRESSION:
        return expression((CastExpression) n);
      case ASTNode.METHOD_INVOCATION:
        return expression((MethodInvocation) n);
      case ASTNode.PARENTHESIZED_EXPRESSION:
        return expression((ParenthesizedExpression) n);
      case ASTNode.DO_STATEMENT:
        return expression((DoStatement) n);
      default:
        return null;
    }
  }

  public static Expression expression(final ClassInstanceCreation $) {
    return core($.getExpression());
  }

  public static Expression expression(final CastExpression $) {
    return core($.getExpression());
  }

  public static Expression expression(final DoStatement $) {
    return core($.getExpression());
  }

  public static Expression expression(final ExpressionStatement $) {
    return $ == null ? null : core($.getExpression());
  }

  public static Expression expression(final MethodInvocation $) {
    return core($.getExpression());
  }

  public static Expression expression(final ParenthesizedExpression $) {
    return core($.getExpression());
  }

  public static Expression expression(final ReturnStatement $) {
    return core($.getExpression());
  }

  public static Expression expression(final ThrowStatement $) {
    return core($.getExpression());
  }

  /** Convert, is possible, an {@link ASTNode} to a {@link ExpressionStatement}
   * @param n a statement or a block to extract the expression statement from
   * @return expression statement if n is a block or an expression statement or
   *         null if it not an expression statement or if the block contains
   *         more than one statement */
  public static ExpressionStatement expressionStatement(final ASTNode n) {
    return n == null ? null : asExpressionStatement(extract.singleStatement(n));
  }

  /** Search for a {@link PrefixExpression} in the tree rooted at an
   * {@link ASTNode}.
   * @param n JD
   * @return first {@link PrefixExpression} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  public static PostfixExpression findFirstPostfix(final ASTNode n) {
    final Wrapper<PostfixExpression> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final PostfixExpression e) {
        if ($.get() == null)
          $.set(e);
        return false;
      }
    });
    return $.get();
  }

  /** Search for an {@link IfStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param n JD
   * @return first {@link IfStatement} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  public static IfStatement firstIfStatement(final ASTNode n) {
    if (n == null)
      return null;
    final Wrapper<IfStatement> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final IfStatement s) {
        if ($.get() == null)
          $.set(s);
        return false;
      }
    });
    return $.get();
  }

  /** Search for an {@link MethodDeclaration} in the tree rooted at an
   * {@link ASTNode}.
   * @param n JD
   * @return first {@link IfStatement} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  public static MethodDeclaration firstMethodDeclaration(final ASTNode n) {
    final Wrapper<MethodDeclaration> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final MethodDeclaration d) {
        if ($.get() == null)
          $.set(d);
        return false;
      }
    });
    return $.get();
  }

  /** Find the first {@link InfixExpression} representing an addition, under a
   * given node, as found in the usual visitation order.
   * @param n JD
   * @return first {@link InfixExpression} representing an addition under the
   *         parameter given node, or <code><b>null</b></code> if no such value
   *         could be found. */
  public static InfixExpression firstPlus(final ASTNode n) {
    final Wrapper<InfixExpression> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final InfixExpression e) {
        if ($.get() != null)
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
    final Wrapper<Type> $ = new Wrapper<>();
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

  /** Return the first {@link VariableDeclarationFragment} encountered in a
   * visit of the tree rooted a the parameter.
   * @param n JD
   * @return first such node encountered in a visit of the tree rooted a the
   *         parameter, or <code><b>null</b></code> */
  public static VariableDeclarationFragment firstVariableDeclarationFragment(final ASTNode n) {
    if (n == null)
      return null;
    final Wrapper<VariableDeclarationFragment> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final VariableDeclarationFragment f) {
        if ($.get() == null)
          $.set(f);
        return false;
      }
    });
    return $.get();
  }

  /** Extract the single {@link ReturnStatement} embedded in a node.
   * @param n JD
   * @return single {@link IfStatement} embedded in the parameter or
   *         <code><b>null</b></code> if not such statements exists. */
  public static IfStatement ifStatement(final ASTNode n) {
    return asIfStatement(extract.singleStatement(n));
  }

  /** Find the last statement residing under a given {@link Statement}
   * @param s JD
   * @return last statement residing under a given {@link Statement}, or
   *         <code><b>null</b></code> if not such statements exists. */
  public static ASTNode lastStatement(final Statement s) {
    return last(statements(s));
  }

  /** Extract the {@link MethodDeclaration} that contains a given node.
   * @param n JD
   * @return inner most {@link MethodDeclaration} in which the parameter is
   *         nested, or <code><b>null</b></code>, if no such statement
   *         exists. */
  public static MethodDeclaration methodDeclaration(final ASTNode n) {
    for (ASTNode $ = n; $ != null; $ = $.getParent())
      if (Is.methodDeclaration($))
        return asMethodDeclaration($);
    return null;
  }

  /** @param n JD
   * @return method invocation if it exists or null if it doesn't or if the
   *         block contains more than one statement */
  public static MethodInvocation methodInvocation(final ASTNode n) {
    return asMethodInvocation(extract.expressionStatement(n).getExpression());
  }

  public static SimpleName name(final MethodInvocation i) {
    return i.getName();
  }

  public static SimpleName name(final SuperMethodInvocation i) {
    return i.getName();
  }

  /** Find the {@link Assignment} that follows a given node.
   * @param n JD
   * @return {@link Assignment} that follows the parameter, or
   *         <code><b>null</b></code> if not such value exists. */
  public static Assignment nextAssignment(final ASTNode n) {
    return extract.assignment(nextStatement(n));
  }

  /** Extract the {@link IfStatement} that immediately follows a given node
   * @param n JD
   * @return {@link IfStatement} that immediately follows the parameter, or
   *         <code><b>null</b></code>, if no such statement exists. */
  public static IfStatement nextIfStatement(final ASTNode n) {
    return asIfStatement(nextStatement(n));
  }

  /** Extract the {@link ReturnStatement} that immediately follows a given node
   * @param n JD
   * @return {@link ReturnStatement} that immediately follows the parameter, or
   *         <code><b>null</b></code>, if no such statement exists. */
  public static ReturnStatement nextReturn(final ASTNode n) {
    return asReturnStatement(nextStatement(n));
  }

  /** Extract the {@link Statement} that immediately follows a given node.
   * @param n JD
   * @return {@link Statement} that immediately follows the parameter, or
   *         <code><b>null</b></code>, if no such statement exists. */
  public static Statement nextStatement(final ASTNode n) {
    return nextStatement(extract.statement(n));
  }

  /** Extract the {@link Statement} that immediately follows a given statement
   * @param s JD
   * @return {@link Statement} that immediately follows the parameter, or
   *         <code><b>null</b></code>, if no such statement exists. */
  public static Statement nextStatement(final Statement s) {
    if (s == null)
      return null;
    final Block b = asBlock(s.getParent());
    return b == null ? null : next(s, extract.statements(b));
  }

  public static Expression onlyArgument(final MethodInvocation i) {
    return onlyExpression(arguments(i));
  }

  public static Expression onlyExpression(final List<Expression> $) {
    return core(onlyOne($));
  }

  public static Expression operand(final PostfixExpression ¢) {
    return ¢ == null ? null : core(¢.getOperand());
  }

  public static Expression operand(final PrefixExpression ¢) {
    return ¢ == null ? null : core(¢.getOperand());
  }

  /** Makes a list of all operands of an expression, comprising the left
   * operand, the right operand, followed by extra operands when they exist.
   * @param e JD
   * @return a list of all operands of an expression */
  public static List<Expression> operands(final InfixExpression e) {
    if (e == null)
      return null;
    final List<Expression> $ = new ArrayList<>();
    $.add(left(e));
    $.add(right(e));
    if (e.hasExtendedOperands())
      $.addAll(expose.extendedOperands(e));
    return $;
  }

  /** Finds the expression returned by a return statement
   * @param n a node to extract an expression from
   * @return null if the statement is not an expression or return statement or
   *         the expression if they are */
  public static Expression returnExpression(final ASTNode n) {
    final ReturnStatement $ = returnStatement(n);
    return $ == null ? null : $.getExpression();
  }

  /** Extract the single {@link ReturnStatement} embedded in a node.
   * @param n JD
   * @return single {@link ReturnStatement} embedded in the parameter, and
   *         return it; <code><b>null</b></code> if not such statements
   *         exists. */
  public static ReturnStatement returnStatement(final ASTNode n) {
    return asReturnStatement(extract.singleStatement(n));
  }

  /** Finds the single statement in the <code><b>else</b></code> branch of an
   * {@link IfStatement}
   * @param s JD
   * @return single statement in the <code><b>else</b></code> branch of the
   *         parameter, or <code><b>null</b></code>, if no such statement
   *         exists. */
  public static Statement singleElse(final IfStatement s) {
    return extract.singleStatement(elze(s));
  }

  /** @param n JD
   * @return if b is a block with just 1 statement it returns that statement, if
   *         b is statement it returns b and if b is null it returns a null */
  public static Statement singleStatement(final ASTNode n) {
    return onlyOne(extract.statements(n));
  }

  /** Finds the single statement in the "then" branch of an {@link IfStatement}
   * @param s JD
   * @return single statement in the "then" branch of the parameter, or
   *         <code><b>null</b></code>, if no such statement exists. */
  public static Statement singleThen(final IfStatement s) {
    return extract.singleStatement(then(s));
  }

  /** Extract the {@link Statement} that contains a given node.
   * @param n JD
   * @return inner most {@link Statement} in which the parameter is nested, or
   *         <code><b>null</b></code>, if no such statement exists. */
  public static Statement statement(final ASTNode n) {
    for (ASTNode $ = n; $ != null; $ = $.getParent())
      if (Is.statement($))
        return asStatement($);
    return null;
  }

  /** Extract the list of non-empty statements embedded in node (nesting within
   * control structure such as <code><b>if</b></code> are not removed.)
   * @param n JD
   * @return list of such statements. */
  public static List<Statement> statements(final ASTNode n) {
    final List<Statement> $ = new ArrayList<>();
    return n == null || !(n instanceof Statement) ? $ : //
        extract.statementsInto((Statement) n, $);
  }

  /** @param n a node to extract an expression from
   * @return null if the statement is not an expression or return statement or
   *         the expression if they are */
  public static Expression throwExpression(final ASTNode n) {
    final ThrowStatement $ = asThrowStatement(extract.singleStatement(n));
    return $ == null ? null : $.getExpression();
  }

  /** Extract the single {@link ThrowStatement} embedded in a node.
   * @param n JD
   * @return single {@link ThrowStatement} embedded in the parameter, and return
   *         it; <code><b>null</b></code> if not such statements exists. */
  public static ThrowStatement throwStatement(final ASTNode n) {
    return asThrowStatement(extract.singleStatement(n));
  }

  public static Type type(final CastExpression e) {
    return e.getType();
  }

  private static Statement next(final Statement s, final List<Statement> ss) {
    for (int i = 0; i < ss.size() - 1; ++i)
      if (ss.get(i) == s)
        return ss.get(i + 1);
    return null;
  }

  private static List<Statement> statementsInto(final Block b, final List<Statement> $) {
    for (final Statement s : expose.statements(b))
      extract.statementsInto(s, $);
    return $;
  }

  private static List<Statement> statementsInto(final Statement ¢, final List<Statement> $) {
    switch (¢.getNodeType()) {
      case EMPTY_STATEMENT:
        return $;
      case BLOCK:
        return extract.statementsInto((Block) ¢, $);
      default:
        $.add(¢);
        return $;
    }
  }
}
