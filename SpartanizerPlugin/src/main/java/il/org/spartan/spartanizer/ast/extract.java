package il.org.spartan.spartanizer.ast;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.assemble.*;

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
  public static List<Expression> allOperands(final InfixExpression x) {
    assert x != null;
    return hop.operands(flatten.of(x));
  }

  public static List<InfixExpression.Operator> allOperators(final InfixExpression x) {
    assert x != null;
    final List<InfixExpression.Operator> $ = new ArrayList<>();
    findOperators(x, $);
    return $;
  }

  public static List<Annotation> annotations(final BodyDeclaration d) {
    final ArrayList<Annotation> $ = new ArrayList<>();
    for (final IExtendedModifier ¢ : step.modifiers(d)) {
      final Annotation a = az.annotation(¢);
      if (a != null)
        $.add(a);
    }
    return $;
  }

  public static List<Annotation> annotations(final SingleVariableDeclaration d) {
    final ArrayList<Annotation> $ = new ArrayList<>();
    for (final IExtendedModifier ¢ : step.modifiers(d)) {
      final Annotation a = az.annotation(¢);
      if (a != null)
        $.add(a);
    }
    return $;
  }

  public static List<Annotation> annotations(final VariableDeclarationStatement s) {
    final ArrayList<Annotation> $ = new ArrayList<>();
    for (final IExtendedModifier ¢ : step.modifiers(s)) {
      final Annotation a = az.annotation(¢);
      if (a != null)
        $.add(a);
    }
    return $;
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

  /** @param n a statement or block to extract the assignment from
   * @return null if the block contains more than one statement or if the
   *         statement is not an assignment or the assignment if it exists */
  public static Assignment assignment(final ASTNode n) {
    final ExpressionStatement e = extract.expressionStatement(n);
    return e == null ? null : az.assignment(e.getExpression());
  }

  /** Peels any parenthesis that may wrap an {@Link Expression}
   * @param $ JD
   * @return the parameter if not parenthesized, or the unparenthesized this
   *         version of it */
  public static Expression core(final Expression $) {
    return $ == null ? $ //
        : iz.is($, PARENTHESIZED_EXPRESSION) ? core(az.parenthesizedExpression($).getExpression()) //
            : iz.is($, PREFIX_EXPRESSION) ? core(az.prefixExpression($)) //
                : $;
  }

  public static Expression core(final PrefixExpression $) {
    return $.getOperator() != wizard.PLUS1 ? $ : core($.getOperand());
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
        return first(ss);
      default:
        return s;
    }
  }

  /** Convert, is possible, an {@link ASTNode} to a {@link ExpressionStatement}
   * @param n a statement or a block to extract the expression statement from
   * @return expression statement if n is a block or an expression statement or
   *         null if it not an expression statement or if the block contains
   *         more than one statement */
  public static ExpressionStatement expressionStatement(final ASTNode n) {
    return n == null ? null : az.expressionStatement(extract.singleStatement(n));
  }

  /** Search for a {@link PrefixExpression} in the tree rooted at an
   * {@link ASTNode}.
   * @param n JD
   * @return first {@link PrefixExpression} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  public static PostfixExpression findFirstPostfix(final ASTNode n) {
    final Wrapper<PostfixExpression> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final PostfixExpression x) {
        if ($.get() == null)
          $.set(x);
        return false;
      }
    });
    return $.get();
  }

  /** Search for an {@link AssertStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param n JD
   * @return first {@link AssertStatement} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  public static AssertStatement firstAssertStatement(final ASTNode n) {
    if (n == null)
      return null;
    final Wrapper<AssertStatement> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final AssertStatement s) {
        if ($.get() == null)
          $.set(s);
        return false;
      }
    });
    return $.get();
  }

  /** Search for an {@link ForStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param n JD
   * @return first {@link ForStatement} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  public static ForStatement firstForStatement(final ASTNode n) {
    if (n == null)
      return null;
    final Wrapper<ForStatement> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final ForStatement s) {
        if ($.get() == null)
          $.set(s);
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
      @Override public boolean visit(final InfixExpression x) {
        if ($.get() != null)
          return false;
        if (x.getOperator() != InfixExpression.Operator.PLUS)
          return true;
        $.set(x);
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

  /** Search for an {@link WhileStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param n JD
   * @return first {@link WhileStatement} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  public static WhileStatement firstWhileStatement(final ASTNode n) {
    if (n == null)
      return null;
    final Wrapper<WhileStatement> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final WhileStatement s) {
        if ($.get() == null)
          $.set(s);
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
    return az.ifStatement(extract.singleStatement(n));
  }

  /** @param n JD
   * @return method invocation if it exists or null if it doesn't or if the
   *         block contains more than one statement */
  public static MethodInvocation methodInvocation(final ASTNode n) {
    return az.methodInvocation(extract.expressionStatement(n).getExpression());
  }

  public static List<Modifier> modifiers(final BodyDeclaration d) {
    final ArrayList<Modifier> $ = new ArrayList<>();
    for (final IExtendedModifier ¢ : step.modifiers(d)) {
      final Modifier a = az.modifier((ASTNode) ¢);
      if (a != null)
        $.add(a);
    }
    return $;
  }

  public static List<Modifier> modifiers(final SingleVariableDeclaration d) {
    final ArrayList<Modifier> $ = new ArrayList<>();
    for (final IExtendedModifier ¢ : step.modifiers(d)) {
      final Modifier a = az.modifier((ASTNode) ¢);
      if (a != null)
        $.add(a);
    }
    return $;
  }

  public static List<Modifier> modifiers(final VariableDeclarationStatement s) {
    final ArrayList<Modifier> $ = new ArrayList<>();
    for (final IExtendedModifier ¢ : step.modifiers(s)) {
      final Modifier a = az.modifier((ASTNode) ¢);
      if (a != null)
        $.add(a);
    }
    return $;
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
    return az.ifStatement(nextStatement(n));
  }

  /** Extract the {@link ReturnStatement} that immediately follows a given node
   * @param n JD
   * @return {@link ReturnStatement} that immediately follows the parameter, or
   *         <code><b>null</b></code>, if no such statement exists. */
  public static ReturnStatement nextReturn(final ASTNode n) {
    return az.returnStatement(nextStatement(n));
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
    final Block b = az.block(s.getParent());
    return b == null ? null : next(s, extract.statements(b));
  }

  public static Expression onlyArgument(final MethodInvocation i) {
    return onlyExpression(arguments(i));
  }

  public static Expression onlyExpression(final List<Expression> $) {
    return core(lisp.onlyOne($));
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
    return az.returnStatement(extract.singleStatement(n));
  }

  /** Finds the single statement in the <code><b>else</b></code> branch of an
   * {@link IfStatement}
   * @param s JD
   * @return single statement in the <code><b>else</b></code> branch of the
   *         parameter, or <code><b>null</b></code>, if no such statement
   *         exists. */
  public static Statement singleElse(final IfStatement s) {
    return extract.singleStatement(step.elze(s));
  }

  /** @param n JD
   * @return if b is a block with just 1 statement it returns that statement, if
   *         b is statement it returns b and if b is null it returns a null */
  public static Statement singleStatement(final ASTNode n) {
    return lisp.onlyOne(extract.statements(n));
  }

  /** Finds the single statement in the "then" branch of an {@link IfStatement}
   * @param s JD
   * @return single statement in the "then" branch of the parameter, or
   *         <code><b>null</b></code>, if no such statement exists. */
  public static Statement singleThen(final IfStatement s) {
    return extract.singleStatement(step.then(s));
  }

  /** Extract the {@link Statement} that contains a given node.
   * @param n JD
   * @return inner most {@link Statement} in which the parameter is nested, or
   *         <code><b>null</b></code>, if no such statement exists. */
  public static Statement statement(final ASTNode n) {
    for (ASTNode $ = n; $ != null; $ = $.getParent())
      if (iz.statement($))
        return az.asStatement($);
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
    final ThrowStatement $ = az.throwStatement(extract.singleStatement(n));
    return $ == null ? null : $.getExpression();
  }

  /** Extract the single {@link ThrowStatement} embedded in a node.
   * @param n JD
   * @return single {@link ThrowStatement} embedded in the parameter, and return
   *         it; <code><b>null</b></code> if not such statements exists. */
  public static ThrowStatement throwStatement(final ASTNode n) {
    return az.throwStatement(extract.singleStatement(n));
  }

  private static void findOperators(final InfixExpression x, final List<InfixExpression.Operator> $) {
    if (x == null)
      return;
    $.add(x.getOperator());
    findOperators(az.infixExpression(x.getLeftOperand()), $);
    findOperators(az.infixExpression(x.getRightOperand()), $);
  }

  private static Statement next(final Statement s, final List<Statement> ss) {
    for (int i = 0; i < ss.size() - 1; ++i)
      if (ss.get(i) == s)
        return ss.get(i + 1);
    return null;
  }

  private static List<Statement> statementsInto(final Block b, final List<Statement> $) {
    for (final Statement s : step.statements(b))
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
