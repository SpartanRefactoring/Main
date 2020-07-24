package il.org.spartan.spartanizer.ast.navigate;

import static fluent.ly.the.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import an.*;
import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** An empty {@code interface} for fluent programming. The name should say it
 * all: The name, followed by a dot, followed by a method name, should read like
 * a sentence phrase.
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
    return () -> new Iterator<>() {
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
  static VariableDeclarationFragment correspondingVariableDeclarationFragment(final List<VariableDeclarationFragment> fs, final SimpleName ¢) {
    return fs.stream().filter(λ -> wizard.eq(¢, λ.getName())).findFirst().orElse(null);
  }
  static VariableDeclarationFragment correspondingVariableDeclarationFragment(final VariableDeclarationStatement s, final SimpleName n) {
    return hop.correspondingVariableDeclarationFragment(step.fragments(s), n);
  }
  /** @param root the node whose children we return
   * @return A list containing all the nodes in the given root'¢ sub tree */
  static List<ASTNode> descendants(final ASTNode root) {
    if (root == null)
      return null;
    final List<ASTNode> $ = an.empty.list();
    root.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        $.add(¢);
      }
    });
    $.remove(0);
    return $;
  }
  static String getEnclosingMethodName(final BodyDeclaration ¢) {
    final MethodDeclaration $ = yieldAncestors.untilClass(MethodDeclaration.class).from(¢);
    return $ == null ? null : $.getName() + "";
  }
  static SimpleName lastName(final Name ¢) {
    return ¢ == null ? null : ¢.isSimpleName() ? (SimpleName) ¢ : ¢.isQualifiedName() ? ((QualifiedName) ¢).getName() : null;
  }
  /** Find the last statement residing under a given {@link Statement}
   * @param ¢ JD
   * @return last statement residing under a given {@link Statement}, or
   *         {@code null if not such sideEffects exists. */
  static Statement lastStatement(final Statement ¢) {
    return last(extract.statements(¢));
  }
  static Name name(final Type ¢) {
    return ¢.isSimpleType() ? ((SimpleType) ¢).getName()
        : ¢.isNameQualifiedType() ? ((NameQualifiedType) ¢).getName() : ¢.isQualifiedType() ? ((QualifiedType) ¢).getName() : null;
  }
  /** Makes a list of all operands of an expression, comprising the left
   * operand, the right operand, followed by extra operands when they exist.
   * @param x JD
   * @return a list of all operands of an expression */
  static List<Expression> operands(final InfixExpression ¢) {
    if (¢ == null)
      return null;
    final List<Expression> $ = as.list(left(¢), right(¢));
    if (¢.hasExtendedOperands())
      $.addAll(extendedOperands(¢));
    return $;
  }
  static Expression origin(final Assignment a) {
    Assignment ¢ = a;
    for (Expression $ = ¢;; $ = from(¢)) {
      ¢ = az.assignment($);
      if (¢ == null || ¢.getOperator() != ASSIGN)
        return $;
    }
  }
  /** [[SuppressWarningsSpartan]] - see #1246 */
  static VariableDeclarationFragment penultimate(final Statement body) {
    final ASTNode n = hop.lastStatement(copy.of(body));
    final Statement $ = !(n instanceof Statement) ? null : (Statement) n;
    return n == null || $ == null ? null : previous($);
  }
  /** @param ¢ JD
   * @return conversion of {@link Statement}, which is previous to the
   *         LastStatement in the loop body. */
  static VariableDeclarationFragment penultimate(final WhileStatement $) {
    return penultimate(body($));
  }
  /** @param ¢ JD
   * @return conversion of {@link Statement} , which is previous to the
   *         firstLastStatement in the loop body. */
  static VariableDeclarationFragment penultimateFragment(final ForStatement ¢) {
    return penultimate(body(¢));
  }
  static VariableDeclarationFragment previous(final Statement ¢) {
    return the.firstOf(fragments(az.variableDeclrationStatement(previousStatementInBody(¢))));
  }
  static SimpleName simpleName(final Type ¢) {
    return lastName(hop.name(¢));
  }
  static List<Statement> subsequentStatements(final Statement ¢) {
    return parent(¢) instanceof SwitchStatement ? the.rest(¢, step.statements((SwitchStatement) parent(¢)))
        : parent(¢) instanceof Block ? the.rest(¢, step.statements((Block) parent(¢))) //
            : empty.list();
  }
}
