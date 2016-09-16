package il.org.spartan.spartanizer.ast;

import static il.org.spartan.spartanizer.ast.wizard.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;

/** An empty <code><b>interface</b></code> for fluent programming. The name
 * should say it all: The name, followed by a dot, followed by a method name,
 * should read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-28 */
public interface findFirst {
  /** Search for an {@link AssertStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param n JD
   * @return first {@link AssertStatement} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  static AssertStatement assertStatement(final ASTNode ¢) {
    return findFirstClass(AssertStatement.class, ¢);
  }

  static <N extends ASTNode> N findFirstClass(final Class<N> c, final ASTNode n) {
    if (n == null)
      return null;
    final Wrapper<N> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @SuppressWarnings("unchecked") @Override public boolean preVisit2(final ASTNode ¢) {
        if ($.get() != null)
          return false;
        if (¢.getClass() != c && !c.isAssignableFrom(¢.getClass()))
          return true;
        $.set((N) ¢);
        assert $.get() == ¢;
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
  static MethodDeclaration firstMethodDeclaration(final ASTNode ¢) {
    return findFirstClass(MethodDeclaration.class, ¢);
  }

  /** Find the first {@link InfixExpression} representing an addition, under a
   * given node, as found in the usual visitation order.
   * @param n JD
   * @return first {@link InfixExpression} representing an addition under the
   *         parameter given node, or <code><b>null</b></code> if no such value
   *         could be found. */
  static InfixExpression firstPlus(final ASTNode n) {
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

  static Type firstType(final Statement ¢) {
    return findFirstClass(Type.class, ¢);
  }

  /** Search for an {@link ForStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param n JD
   * @return first {@link ForStatement} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  static ForStatement forStatement(final ASTNode ¢) {
    return findFirstClass(ForStatement.class, ¢);
  }

  /** Search for an {@link IfStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param n JD
   * @return first {@link IfStatement} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  static IfStatement ifStatement(final ASTNode ¢) {
    return findFirstClass(IfStatement.class, ¢);
  }

  /** Search for a {@link PrefixExpression} in the tree rooted at an
   * {@link ASTNode}.
   * @param n JD
   * @return first {@link PrefixExpression} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  static PostfixExpression postfixExpression(final ASTNode ¢) {
    return findFirstClass(PostfixExpression.class, ¢);
  }

  static ThrowStatement throwStatement(final ASTNode ¢) {
    return findFirstClass(ThrowStatement.class, ¢);
  }

  /** Return the first {@link VariableDeclarationFragment} encountered in a
   * visit of the tree rooted a the parameter.
   * @param n JD
   * @return first such node encountered in a visit of the tree rooted a the
   *         parameter, or <code><b>null</b></code> */
  static VariableDeclarationFragment variableDeclarationFragment(final ASTNode ¢) {
    return findFirstClass(VariableDeclarationFragment.class, ¢);
  }

  /** Search for an {@link WhileStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param n JD
   * @return first {@link WhileStatement} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  static WhileStatement whileStatement(final ASTNode ¢) {
    return findFirstClass(WhileStatement.class, ¢);
  }
}
