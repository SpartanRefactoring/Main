package il.org.spartan.spartanizer.ast;

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
  static AssertStatement assertStatement(final ASTNode n) {
    if (n == null)
      return null;
    final Wrapper<AssertStatement> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final AssertStatement ¢) {
        if ($.get() == null)
          $.set(¢);
        return false;
      }
    });
    return $.get();
  }

  /** Search for a {@link PrefixExpression} in the tree rooted at an
   * {@link ASTNode}.
   * @param n JD
   * @return first {@link PrefixExpression} found in an {@link ASTNode n}, or
   *         <code><b>null</b> if there is no such statement. */
  static PostfixExpression postfixExpression(final ASTNode n) {
    final Wrapper<PostfixExpression> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final PostfixExpression ¢) {
        if ($.get() == null)
          $.set(¢);
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
  static ForStatement forStatement(final ASTNode n) {
    if (n == null)
      return null;
    final Wrapper<ForStatement> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final ForStatement ¢) {
        if ($.get() == null)
          $.set(¢);
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
  static IfStatement ifStatement(final ASTNode n) {
    if (n == null)
      return null;
    final Wrapper<IfStatement> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final IfStatement ¢) {
        if ($.get() == null)
          $.set(¢);
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
  static MethodDeclaration firstMethodDeclaration(final ASTNode n) {
    final Wrapper<MethodDeclaration> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final MethodDeclaration ¢) {
        if ($.get() == null)
          $.set(¢);
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
  static InfixExpression firstPlus(final ASTNode n) {
    final Wrapper<InfixExpression> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final InfixExpression ¢) {
        if ($.get() != null)
          return false;
        if (¢.getOperator() != InfixExpression.Operator.PLUS)
          return true;
        $.set(¢);
        return false;
      }
    });
    return $.get();
  }

  static Type firstType(final Statement s) {
    final Wrapper<Type> $ = new Wrapper<>();
    s.accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        if (!(¢ instanceof Type))
          return true;
        $.set((Type) ¢);
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
  static VariableDeclarationFragment variableDeclarationFragment(final ASTNode n) {
    if (n == null)
      return null;
    final Wrapper<VariableDeclarationFragment> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final VariableDeclarationFragment ¢) {
        if ($.get() == null)
          $.set(¢);
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
  static WhileStatement whileStatement(final ASTNode n) {
    if (n == null)
      return null;
    final Wrapper<WhileStatement> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final WhileStatement ¢) {
        if ($.get() == null)
          $.set(¢);
        return false;
      }
    });
    return $.get();
  }

  static ThrowStatement throwStatement(ASTNode ¢) {
    return findFirstClass(ThrowStatement.class, ¢);
  }

  static <N extends ASTNode> N findFirstClass(Class<N> c, ASTNode n) {
    if (n == null)
      return null;
    final Wrapper<N> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @SuppressWarnings("unchecked") @Override public boolean preVisit2(final ASTNode ¢) {
        if (¢.getClass() != c)
          return true;
        $.set((N) ¢);
        return true;
      }
    });
    return $.get();
  }
}
