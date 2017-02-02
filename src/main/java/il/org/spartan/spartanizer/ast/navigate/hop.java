package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.Utils.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** An empty <code><b>interface</b></code> for fluent programming. The name
 * should say it all: The name, followed by a dot, followed by a method name,
 * should read like a sentence phrase.
 * @author Yossi Gil
 * @since 2016 */
public interface hop {
  /** Provides a {@link Iterable} access to the ancestors of an {@link ASTNode},
   * including the node itself.
   * @param ¢ JD
   * @return an {@link Iterable} that traverses the ancestors of the ASTNode.
   *         Use case: Counting the number of Expressions among a given
   *         ASTNode's ancestors */
  static Iterable<ASTNode> ancestors(final ASTNode ¢) {
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

  /** @param root the node whose children we return
   * @return A list containing all the nodes in the given root'¢ sub tree */
  @Nullable
  static List<ASTNode> descendants(@Nullable final ASTNode root) {
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

  static VariableDeclarationFragment correspondingVariableDeclarationFragment(final VariableDeclarationStatement s, final SimpleName n) {
    return hop.correspondingVariableDeclarationFragment(step.fragments(s), n);
  }

  static VariableDeclarationFragment correspondingVariableDeclarationFragment(@NotNull final List<VariableDeclarationFragment> fs, final SimpleName ¢) {
    return fs.stream().filter(λ -> wizard.same(¢, λ.getName())).findFirst().orElse(null);
  }

  static String getEnclosingMethodName(final BodyDeclaration ¢) {
    final MethodDeclaration $ = yieldAncestors.untilClass(MethodDeclaration.class).from(¢);
    return $ == null ? null : $.getName() + "";
  }

  @Nullable
  static SimpleName lastComponent(@Nullable final Name ¢) {
    return ¢ == null ? null : ¢.isSimpleName() ? (SimpleName) ¢ : ¢.isQualifiedName() ? ((QualifiedName) ¢).getName() : null;
  }

  /** Find the last statement residing under a given {@link Statement}
   * @param ¢ JD
   * @return last statement residing under a given {@link Statement}, or
   *         <code><b>null</b></code> if not such sideEffects exists. */
  static ASTNode lastStatement(final Statement ¢) {
    return last(extract.statements(¢));
  }

  static Name name(@NotNull final Type ¢) {
    return ¢.isSimpleType() ? ((SimpleType) ¢).getName()
        : ¢.isNameQualifiedType() ? ((NameQualifiedType) ¢).getName() : ¢.isQualifiedType() ? ((QualifiedType) ¢).getName() : null;
  }

  /** Makes a list of all operands of an expression, comprising the left
   * operand, the right operand, followed by extra operands when they exist.
   * @param x JD
   * @return a list of all operands of an expression */
  @Nullable
  static List<Expression> operands(@Nullable final InfixExpression ¢) {
    if (¢ == null)
      return null;
    final List<Expression> $ = new ArrayList<>();
    $.add(left(¢));
    $.add(right(¢));
    if (¢.hasExtendedOperands())
      $.addAll(step.extendedOperands(¢));
    return $;
  }

  /** @param ¢ JD
   * @return converssion of {@link Statement} , which is previous to the
   *         firstLastStatement in the loop body. */
  static VariableDeclarationFragment precidingFragmentToLastExpression(final ForStatement ¢) {
    final ASTNode n = hop.lastStatement(copy.of(step.body(¢)));
    if (n == null)
      return null;
    final Statement current = az.statement(n);
    if (current == null)
      return null;
    final Statement previous = previousStatementInBody(current);
    if (previous == null)
      return null;
    final VariableDeclarationStatement $ = az.variableDeclrationStatement(previous);
    return $ == null ? null : findFirst.elementOf(step.fragments($));
  }

  /** @param ¢ JD
   * @return conversion of {@link Statement}, which is previous to the
   *         LastStatement in the loop body. */
  static VariableDeclarationFragment prevFragmentToLastExpression(final WhileStatement ¢) {
    final ASTNode n = hop.lastStatement(copy.of(step.body(¢)));
    if (n == null)
      return null;
    final Statement current = az.statement(n);
    if (current == null)
      return null;
    final Statement previous = previousStatementInBody(current);
    if (previous == null)
      return null;
    final VariableDeclarationStatement $ = az.variableDeclrationStatement(previous);
    return $ == null ? null : findFirst.elementOf(step.fragments($));
  }

  /** @param s current {@link Statement}.
   * @return the previous {@link Statement} in the parent {@link Block}. If
   *         parent is not {@link Block} return null, if n is first
   *         {@link Statement} also null. */
  static Statement previousStatementInBody(@NotNull final Statement s) {
    final Block b = az.block(s.getParent());
    if (b == null)
      return null;
    final List<Statement> $ = statements(b);
    return $.indexOf(s) < 1 ? null : $.get($.indexOf(s) - 1);
  }

  @Nullable
  static SimpleName simpleName(@NotNull final Type ¢) {
    return lastComponent(hop.name(¢));
  }
}
