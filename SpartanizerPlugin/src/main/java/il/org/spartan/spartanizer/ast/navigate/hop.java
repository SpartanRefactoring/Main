package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.Utils.last;
import static il.org.spartan.utils.lisp2.rest;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;

/** An empty {@code interface} for fluent programming. The name should say it
 * all: The name, followed by a dot, followed by a method name, should read like
 * a sentence phrase.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
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
  static List<ASTNode> descendants(final ASTNode root) {
    if (root == null)
      return null;
    final List<ASTNode> $ = new ArrayList<>();
    root.accept(new ASTVisitor(true) {
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

  static VariableDeclarationFragment correspondingVariableDeclarationFragment(final List<VariableDeclarationFragment> fs, final SimpleName ¢) {
    return fs.stream().filter(λ -> wizard.same(¢, λ.getName())).findFirst().orElse(null);
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

  /** @param ¢ JD
   * @return conversion of {@link Statement} , which is previous to the
   *         firstLastStatement in the loop body. */
  static VariableDeclarationFragment penultimateFragment(final ForStatement ¢) {
    return penultimate(body(¢));
  }

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

  static VariableDeclarationFragment previous(final Statement ¢) {
    return first(fragments(az.variableDeclrationStatement(previousStatementInBody(¢))));
  }

  static SimpleName simpleName(final Type ¢) {
    return lastName(hop.name(¢));
  }

  static List<Statement> subsequentStatements(final Statement s) {
    if (parent(s) instanceof SwitchStatement)
      return rest(s, step.statements((SwitchStatement) parent(s)));
    if (parent(s) instanceof Block)
      return rest(s, step.statements((Block) parent(s)));
    return monitor.bug("Weird type", s, parent(s));
  }

  static ListRewrite statementsRewriter(final ASTRewrite r, final Statement s) {
    if (parent(s) instanceof SwitchStatement)
      return r.getListRewrite(parent(s), SwitchStatement.STATEMENTS_PROPERTY);
    if (parent(s) instanceof Block)
      return r.getListRewrite(parent(s), Block.STATEMENTS_PROPERTY);
    return monitor.bug("Weird type", s, parent(s));
  }
}
