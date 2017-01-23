package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert <code>
 * int a = 3;
 * while(Panic) {
 *    ++OS.is.in.danger;
 * }
 * </code> to <code>
 * for(int a = 3; Panic;) {
 *    ++OS.is.in.danger;
 * }
 * </code>
 * @author Alex Kopzon
 * @since 2016 */
public final class WhileToForInitializers extends ReplaceToNextStatementExclude<VariableDeclarationFragment>//
    implements TipperCategory.Unite {
  private static ForStatement buildForStatement(final VariableDeclarationFragment f, final WhileStatement ¢) {
    final ForStatement $ = ¢.getAST().newForStatement();
    $.setBody(copy.of(body(¢)));
    $.setExpression(pullInitializersFromExpression(copy.ofWhileExpression(¢), parent(f)));
    initializers($).add(Initializers(f));
    return $;
  }

  private static boolean fitting(final VariableDeclarationStatement s, final WhileStatement ¢) {
    return fragmentsUseFitting(s, ¢);
  }

  private static VariableDeclarationStatement fragmentParent(final VariableDeclarationFragment ¢) {
    return copy.of(az.variableDeclrationStatement(parent(¢)));
  }

  // TODO: now fitting returns true iff all fragments fitting. We
  // may want to be able to treat each fragment separately.
  private static boolean fragmentsUseFitting(final VariableDeclarationStatement vds, final WhileStatement s) {
    return step.fragments(vds).stream()
        .allMatch(¢ -> variableUsedInWhile(s, name(¢)) && Inliner.variableNotUsedAfterStatement(az.statement(s), ¢.getName()));
  }

  private static Expression Initializers(final VariableDeclarationFragment ¢) {
    return az.variableDeclarationExpression(fragmentParent(¢));
  }

  private static VariableDeclarationStatement parent(final VariableDeclarationFragment ¢) {
    return az.variableDeclrationStatement(step.parent(¢));
  }

  private static Expression pullInitializersFromExpression(final Expression from, final VariableDeclarationStatement s) {
    return iz.infix(from) ? ForToForInitializers.handleInfixCondition(copy.of(az.infixExpression(from)), s)
        : iz.assignment(from) ? ForToForInitializers.handleAssignmentCondition(az.assignment(from), s)
            : iz.parenthesizedExpression(from) ? ForToForInitializers.handleParenthesizedCondition(az.parenthesizedExpression(from), s) : from;
  }

  /** Determines whether a specific SimpleName was used in a
   * {@link ForStatement}.
   * @param s JD
   * @param n JD
   * @return <code><b>true</b></code> <em>iff</em> the SimpleName is used in a
   *         ForStatement's condition, updaters, or body. */
  private static boolean variableUsedInWhile(final WhileStatement s, final SimpleName n) {
    return !collect.usesOf(n).in(condition(s)).isEmpty() || !collect.usesOf(n).in(body(s)).isEmpty();
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Merge with subsequent 'while', making a 'for (" + ¢ + "; " + expression(az.whileStatement(extract.nextStatement(¢))) + ";)' loop";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g,
      final ExclusionManager exclude) {
    if (f == null || $ == null || nextStatement == null || exclude == null)
      return null;
    final VariableDeclarationStatement vds = parent(f);
    if (vds == null)
      return null;
    final WhileStatement s = az.whileStatement(nextStatement);
    if (s == null)
      return null;
    exclude.excludeAll(fragments(vds));
    if (!fitting(vds, s))
      return null;
    $.remove(vds, g);
    $.replace(s, buildForStatement(f, s), g);
    return $;
  }
}
