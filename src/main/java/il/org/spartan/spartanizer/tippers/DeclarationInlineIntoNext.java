package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;
import static il.org.spartan.lisp.*;

/** convert
 *
 * <pre>
 * a = 3;
 * whatever(a);
 * </pre>
 *
 * to
 *
 * <pre>
 * whatever(3);
 * </pre>
 *
 * @author Ori Marcovitch
 * @since 2016-11-27 */
public final class DeclarationInlineIntoNext extends ReplaceToNextStatement<VariableDeclarationFragment> implements TipperCategory.Collapse {
  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Inline assignment to " + name(¢) + " into subsequent statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g) {
    final Statement parent = az.statement(f.getParent());
    if (parent == null || iz.forStatement(parent) || nextStatement == null || iz.forStatement(nextStatement) || iz.enhancedFor(nextStatement)
        || iz.conditionalExpression(initializer(f)) || iz.arrayInitializer(initializer(f)) || cannotInlineInto(nextStatement)
        || initializer(f) == null || DeclarationInitializerStatementTerminatingScope.isNotAllowedOpOnPrimitive(f, nextStatement)
        || iz.enhancedFor(nextStatement) && iz.simpleName(az.enhancedFor(nextStatement).getExpression())
            && !(az.simpleName(az.enhancedFor(nextStatement).getExpression()) + "").equals(f.getName() + "") && !iz.simpleName(f.getInitializer())
            && !iz.literal(f.getInitializer()))
      return null;
    final SimpleName id = peelIdentifier(nextStatement, identifier(name(f)));
    if (id == null || anyFurtherUsage(parent, nextStatement, identifier(id)) || leftSide(nextStatement, identifier(id)) || preOrPostfix(id))
      return null;
    $.remove(parent, g);
    $.replace(id, !iz.castExpression(initializer(f)) ? initializer(f) : subject.operand(initializer(f)).parenthesis(), g);
    return $;
  }

  private static boolean cannotInlineInto(final Statement nextStatement) {
    return iz.returnStatement(nextStatement) || iz.whileStatement(nextStatement) || iz.doStatement(nextStatement)
        || iz.synchronizedStatement(nextStatement) || iz.tryStatement(nextStatement) || containsClassInstanceCreation(nextStatement);
  }

  private static boolean preOrPostfix(final SimpleName id) {
    final ASTNode $ = parent(id);
    return iz.prefixExpression($) || iz.postfixExpression($);
  }

  private static boolean containsClassInstanceCreation(final Statement nextStatement) {
    return !searchDescendants.forClass(ClassInstanceCreation.class).from(nextStatement).isEmpty();
  }

  private static boolean anyFurtherUsage(final Statement originalStatement, final Statement nextStatement, final String id) {
    final Bool $ = new Bool();
    final ASTNode parent = nextStatement.getParent();
    parent.accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        if (parent.equals(¢))
          return true;
        if (!¢.equals(nextStatement) && !¢.equals(originalStatement) && iz.statement(¢) && !occurencesOf(az.statement(¢), id).isEmpty())
          $.inner = true;
        return false;
      }
    });
    return $.inner;
  }

  private static boolean leftSide(final Statement nextStatement, final String id) {
    final Bool $ = new Bool();
    nextStatement.accept(new ASTVisitor() {
      @Override public boolean visit(final Assignment ¢) {
        if (iz.simpleName(left(¢)) && identifier(az.simpleName(left(¢))).equals(id))
          $.inner = true;
        return true;
      }
    });
    return $.inner;
  }

  static SimpleName peelIdentifier(final Statement s, final String id) {
    final List<SimpleName> $ = occurencesOf(s, id);
    return $.size() != 1 ? null : first($);
  }

  static List<SimpleName> occurencesOf(final Statement s, final String id) {
    return searchDescendants.forClass(SimpleName.class).suchThat(x -> identifier(x).equals(id)).from(s);
  }
}
