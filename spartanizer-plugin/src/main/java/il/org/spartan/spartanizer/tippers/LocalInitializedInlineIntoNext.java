package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.identifier;
import static il.org.spartan.spartanizer.ast.navigate.step.initializer;
import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static il.org.spartan.spartanizer.ast.navigate.step.nodeType;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;
import static il.org.spartan.spartanizer.ast.navigate.step.type;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.misc;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.descendants;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.engine.Inliner;
import il.org.spartan.spartanizer.tipping.GoToNextStatement;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Bool;

/** convert {@code
 * a = 3;
 * whatever(a);
 * } to {@code
 * whatever(3);
 * }
 * @author Ori Marcovitch
 * @since 2016-11-27 */
public final class LocalInitializedInlineIntoNext extends GoToNextStatement<VariableDeclarationFragment>//
    implements Category.Inlining {
  private static final long serialVersionUID = -0x32A5C56237F0DE7L;

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Inline variable " + name(¢) + " into next statement";
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g) {
    if (containsClassInstanceCreation(nextStatement)//
        || wizard.forbiddenOpOnPrimitive(f, nextStatement))
      return null;
    switch (nodeType(nextStatement)) {
      case ASTNode.DO_STATEMENT:
      case ASTNode.ENHANCED_FOR_STATEMENT:
      case ASTNode.FOR_STATEMENT:
      case ASTNode.RETURN_STATEMENT:
      case ASTNode.SYNCHRONIZED_STATEMENT:
      case ASTNode.TRY_STATEMENT:
      case ASTNode.WHILE_STATEMENT:
        return null;
      default:
        if (containsClassInstanceCreation(nextStatement))
          return null;
        if (containsLambda(nextStatement))
          return null;
    }
    final Expression initializer = initializer(f);
    if (initializer == null)
      return null;
    final Statement parent = az.statement(parent(f));
    if (parent == null//
        || iz.forStatement(parent))
      return null;
    final SimpleName n = peelIdentifier(nextStatement, identifier(name(f)));
    if (n == null//
        || anyFurtherUsage(parent, nextStatement, identifier(n))//
        || misc.leftSide(nextStatement, identifier(n))//
        || preOrPostfix(n))
      return null;
    Expression e = !iz.castExpression(initializer) && !iz.conditionalExpression(initializer) && !iz.castExpression(n.getParent()) ? initializer
        : subject.operand(initializer).parenthesis();
    final VariableDeclarationStatement pp = az.variableDeclarationStatement(parent);
    if (pp != null)
      e = Inliner.protect(e);
    if (pp == null//
        || fragments(pp).size() <= 1)
      $.remove(parent, g);
    else {
      if (nodeType(type(pp)) == ASTNode.ARRAY_TYPE)
        return null;
      final VariableDeclarationStatement pn = copy.of(pp);
      final List<VariableDeclarationFragment> l = fragments(pp);
      for (int ¢ = l.size() - 1; ¢ >= 0; --¢) {
        if (l.get(¢).equals(f)) {
          fragments(pn).remove(¢);
          break;
        }
        if (iz.containsName(name(f), initializer(l.get(¢))))
          return null;
      }
      $.replace(parent, pn, g);
    }
    $.replace(n, e, g);
    return $;
  }
  private static boolean containsLambda(final Statement nextStatement) {
    return !descendants.whoseClassIs(LambdaExpression.class).from(nextStatement).isEmpty();
  }
  private static boolean preOrPostfix(final SimpleName id) {
    final ASTNode $ = parent(id);
    return iz.prefixExpression($)//
        || iz.postfixExpression($);
  }
  private static boolean containsClassInstanceCreation(final Statement nextStatement) {
    return !descendants.whoseClassIs(ClassInstanceCreation.class).from(nextStatement).isEmpty();
  }
  private static boolean anyFurtherUsage(final Statement originalStatement, final Statement nextStatement, final String id) {
    final Bool $ = new Bool();
    final ASTNode parent = parent(nextStatement);
    parent.accept(new ASTVisitor(true) {
      @Override public boolean preVisit2(final ASTNode ¢) {
        if (parent.equals(¢))
          return true;
        if (!¢.equals(nextStatement)//
            && !¢.equals(originalStatement)//
            && iz.statement(¢)//
            && !occurencesOf(az.statement(¢), id).isEmpty())
          $.inner = true;
        return false;
      }
    });
    return $.inner;
  }
  private static SimpleName peelIdentifier(final Statement s, final String id) {
    final List<SimpleName> $ = occurencesOf(s, id);
    return $.size() != 1 ? null : the.firstOf($);
  }
  static List<SimpleName> occurencesOf(final ASTNode $, final String id) {
    return descendants.whoseClassIs(SimpleName.class).suchThat(λ -> identifier(λ).equals(id)).from($);
  }
}
