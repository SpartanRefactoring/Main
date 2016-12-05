package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.*;

/** An empty <code><b>interface</b></code> for fluent programming. The name
 * should say it all: The name, followed by a dot, followed by a method name,
 * should read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-28 */
public interface findFirst {
  /** Search for an {@link AssertStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param pattern JD
   * @return first {@link AssertStatement} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  static AssertStatement assertStatement(final ASTNode ¢) {
    return instanceOf(AssertStatement.class, ¢);
  }

  /** Find the first {@link ConditionalExpression}, under a given node, as found
   * in the usual visitation order.
   * @param n First node to visit
   * @return first {@link ConditionalExpression} representing an addition under
   *         the parameter given node, or <code><b>null</b></code> if no such
   *         value could be found. */
  static ConditionalExpression conditionalExpression(final ASTNode n) {
    final Wrapper<ConditionalExpression> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final ConditionalExpression ¢) {
        if ($.get() != null)
          return false;
        $.set(¢);
        return false;
      }
    });
    return $.get();
  }

  static <E> E elementOf(final List<E> ¢) {
    return first(¢);
  }

  /** Search for an {@link Expression} in the tree rooted at an {@link ASTNode}.
   * @param pattern JD
   * @return first {@link Expression} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  static Expression expression(final ASTNode ¢) {
    return findFirst.instanceOf(Expression.class, ¢);
  }

  /** @param ¢ JD
   * @return */
  static Type type(final ASTNode ¢) {
    return instanceOf(Type.class, ¢);
  }

  /** @param ¢ JD
   * @return */
  static TypeDeclaration typeDeclaration(final ASTNode ¢) {
    return instanceOf(TypeDeclaration.class, ¢);
  }

  /** @param ¢ JD
   * @return */
  static AbstractTypeDeclaration abstractTypeDeclaration(final ASTNode ¢) {
    return instanceOf(AbstractTypeDeclaration.class, ¢);
  }

  /** Search for an {@link ForStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param pattern JD
   * @return first {@link ForStatement} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  static ForStatement forStatement(final ASTNode ¢) {
    return instanceOf(ForStatement.class, ¢);
  }

  /** Search for an {@link IfStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param pattern JD
   * @return first {@link IfStatement} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  static IfStatement ifStatement(final ASTNode ¢) {
    return instanceOf(IfStatement.class, ¢);
  }

  /** Find the first {@link InfixExpression} representing an addition, under a
   * given node, as found in the usual visitation order.
   * @param n JD
   * @return first {@link InfixExpression} representing an addition under the
   *         parameter given node, or <code><b>null</b></code> if no such value
   *         could be found. */
  static InfixExpression infixPlus(final ASTNode n) {
    final Wrapper<InfixExpression> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final InfixExpression ¢) {
        if ($.get() != null)
          return false;
        if (¢.getOperator() != PLUS2)
          return true;
        $.set(¢);
        return false;
      }
    });
    return $.get();
  }

  static <N extends ASTNode> N instanceOf(final Class<N> c, final ASTNode n) {
    if (n == null)
      return null;
    final Wrapper<ASTNode> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        if ($.get() != null)
          return false;
        if (¢.getClass() != c && !c.isAssignableFrom(¢.getClass()))
          return true;
        $.set(¢);
        assert $.get() == ¢;
        return false;
      }
    });
    @SuppressWarnings("unchecked") final N $$ = (N) $.get();
    return $$;
  }

  /** Search for an {@link MethodDeclaration} in the tree rooted at an
   * {@link ASTNode}.
   * @param pattern JD
   * @return first {@link MethodDeclaration} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  static MethodDeclaration methodDeclaration(final ASTNode ¢) {
    return instanceOf(MethodDeclaration.class, ¢);
  }

  /** Search for a {@link PrefixExpression} in the tree rooted at an
   * {@link ASTNode}.
   * @param pattern JD
   * @return first {@link PrefixExpression} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  static PostfixExpression postfixExpression(final ASTNode ¢) {
    return instanceOf(PostfixExpression.class, ¢);
  }

  static ThrowStatement throwStatement(final ASTNode ¢) {
    return instanceOf(ThrowStatement.class, ¢);
  }

  /** Return the first {@link VariableDeclarationFragment} encountered in a
   * visit of the tree rooted a the parameter.
   * @param pattern JD
   * @return first such node encountered in a visit of the tree rooted a the
   *         parameter, or <code><b>null</b></code> */
  static VariableDeclarationFragment variableDeclarationFragment(final ASTNode ¢) {
    return instanceOf(VariableDeclarationFragment.class, ¢);
  }

  /** Search for an {@link WhileStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param pattern JD
   * @return first {@link WhileStatement} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  static WhileStatement whileStatement(final ASTNode ¢) {
    return instanceOf(WhileStatement.class, ¢);
  }

  /** @param ¢ JD
   * @return */
  static ASTNode statement(final ASTNode ¢) {
    return instanceOf(Statement.class, ¢);
  }

  /** @param ¢ JD
   * @return */
  static ASTNode enhancedForStatement(ASTNode ¢) {
    return instanceOf(EnhancedForStatement.class, ¢);
  }

  /** @param ¢ JD
   * @return */
  static ASTNode returnStatement(ASTNode ¢) {
    return instanceOf(ReturnStatement.class, ¢);
  }

  /** @param ¢ JD
   * @return */
  static ASTNode castExpression(ASTNode ¢) {
    return instanceOf(CastExpression.class, ¢);
  }

  /** @param ¢ JD
   * @return */
  static SuperMethodInvocation superMethodDeclaration(ASTNode ¢) {
    return instanceOf(SuperMethodInvocation.class, ¢);
  }
}
