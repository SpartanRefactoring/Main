package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** An empty {@code interface} for fluent programming. The name should say it
 * all: The name, followed by a dot, followed by a method name, should read like
 * a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-28 */
public interface findFirst {
  /** @param ¢ JD
   * @return */
  static AbstractTypeDeclaration abstractTypeDeclaration(final ASTNode ¢) {
    return instanceOf(AbstractTypeDeclaration.class).in(¢);
  }
  /** Search for an {@link AssertStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param pattern JD
   * @return first {@link AssertStatement} found in an {@link ASTNode n}, or
   *         {@code null if there is no such statement. */
  static AssertStatement assertStatement(final ASTNode ¢) {
    return instanceOf(AssertStatement.class).in(¢);
  }
  /** @param ¢ JD
   * @return */
  static ASTNode assignment(final ASTNode ¢) {
    return instanceOf(Assignment.class).in(¢);
  }
  /** @param ¢ JD
   * @return */
  static ASTNode castExpression(final ASTNode ¢) {
    return instanceOf(CastExpression.class).in(¢);
  }
  /** Find the first {@link ConditionalExpression}, under a given node, as found
   * in the usual visitation order.
   * @param n First node to visit
   * @return first {@link ConditionalExpression} representing an addition under
   *         the parameter given node, or {@code null if no such
   *         value could be found. */
  static ConditionalExpression conditionalExpression(final ASTNode n) {
    final Wrapper<ConditionalExpression> ret = new Wrapper<>();
    // noinspection SameReturnValue
    n.accept(new ASTVisitor(true) {
      @Override public boolean visit(final ConditionalExpression ¢) {
        if (ret.get() != null)
          return false;
        ret.set(¢);
        return false;
      }
    });
    return ret.get();
  }
  /** @param ¢ JD
   * @return */
  static ASTNode enhancedForStatement(final ASTNode ¢) {
    return instanceOf(EnhancedForStatement.class).in(¢);
  }
  /** Search for an {@link Expression} in the tree rooted at an {@link ASTNode}.
   * @param pattern JD
   * @return first {@link Expression} found in an {@link ASTNode n}, or
   *         {@code null if there is no such statement. */
  static Expression expression(final ASTNode ¢) {
    return findFirst.instanceOf(Expression.class).in(¢);
  }
  /** Search for an {@link ForStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param pattern JD
   * @return first {@link ForStatement} found in an {@link ASTNode n}, or
   *         {@code null if there is no such statement. */
  static ForStatement forStatement(final ASTNode ¢) {
    return instanceOf(ForStatement.class).in(¢);
  }
  /** Search for an {@link IfStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param pattern JD
   * @return first {@link IfStatement} found in an {@link ASTNode n}, or
   *         {@code null if there is no such statement. */
  static IfStatement ifStatement(final ASTNode ¢) {
    return instanceOf(IfStatement.class).in(¢);
  }
  /** Find the first {@link InfixExpression} representing an addition, under a
   * given node, as found in the usual visitation order.
   * @param n JD
   * @return first {@link InfixExpression} representing an addition under the
   *         parameter given node, or {@code null if no such value
   *         could be found. */
  static InfixExpression infixPlus(final ASTNode n) {
    final Wrapper<InfixExpression> ret = new Wrapper<>();
    n.accept(new ASTVisitor(true) {
      @Override public boolean visit(final InfixExpression ¢) {
        if (ret.get() != null)
          return false;
        if (¢.getOperator() != op.PLUS2)
          return true;
        ret.set(¢);
        return false;
      }
    });
    return ret.get();
  }

  @FunctionalInterface
  interface In<N extends ASTNode> {
    N in(ASTNode n);
  }

  static <N extends ASTNode> In<N> instanceOf(final Class<N> c) {
    return n -> {
      if (n == null)
        return null;
      final Wrapper<N> ret = new Wrapper<>();
      n.accept(new ASTVisitor(true) {
        @Override @SuppressWarnings("unchecked") public boolean preVisit2(final ASTNode ¢) {
          if (ret.get() != null)
            return false;
          if (¢.getClass() != c && !c.isAssignableFrom(¢.getClass()))
            return true;
          ret.set((N) ¢);
          assert ret.get() == ¢;
          return false;
        }
      });
      return ret.get();
    };
  }
  /** @param ¢ JD
   * @return */
  static TypeDeclaration typeDeclaration(final ASTNode ¢) {
    return instanceOf(TypeDeclaration.class).in(¢);
  }
  /** Return the first {@link VariableDeclarationFragment} encountered in a
   * visit of the tree rooted a the parameter.
   * @param pattern JD
   * @return first such node encountered in a visit of the tree rooted a the
   *         parameter, or {@code null */
  static VariableDeclarationFragment variableDeclarationFragment(final ASTNode ¢) {
    return instanceOf(VariableDeclarationFragment.class).in(¢);
  }
  /** Search for an {@link WhileStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param pattern JD
   * @return first {@link WhileStatement} found in an {@link ASTNode n}, or
   *         {@code null if there is no such statement. */
  static WhileStatement whileStatement(final ASTNode ¢) {
    return instanceOf(WhileStatement.class).in(¢);
  }
  /** @param ¢ JD
   * @return */
  static ExpressionStatement expressionStatement(final ASTNode ¢) {
    return instanceOf(ExpressionStatement.class).in(¢);
  }
  /** @param ¢ JD
   * @return */
  static Block block(final ASTNode ¢) {
    return instanceOf(Block.class).in(¢);
  }
  /** @param ¢ JD
   * @return */
  static InfixExpression infixExpression(final ASTNode ¢) {
    return instanceOf(InfixExpression.class).in(¢);
  }
  /** @param ¢ JD
   * @return */
  static TryStatement tryStatement(final ASTNode ¢) {
    return instanceOf(TryStatement.class).in(¢);
  }
  /** @param ¢ JD
   * @return */
  static BooleanLiteral booleanLiteral(final ASTNode ¢) {
    return instanceOf(BooleanLiteral.class).in(¢);
  }
  /** @param ¢ JD
   * @return */
  static FieldAccess fieldAccess(final ASTNode ¢) {
    return instanceOf(FieldAccess.class).in(¢);
  }
  static Name name(final ASTNode ¢) {
    return instanceOf(Name.class).in(¢);
  }
  static VariableDeclarationStatement variableDeclarationStatement(final ASTNode ¢) {
    return instanceOf(VariableDeclarationStatement.class).in(¢);
  }
  static ConditionalExpression conditionalArgument(final MethodInvocation ¢) {
    return arguments(¢).stream().filter(iz::conditionalExpression).map(az::conditionalExpression).findFirst().orElse(null);
  }
  static ASTNode statement(final ASTNode ¢) {
    return instanceOf(Statement.class).in(¢);
  }
  static ASTNode returnStatement(final ASTNode ¢) {
    return instanceOf(ReturnStatement.class).in(¢);
  }
}
