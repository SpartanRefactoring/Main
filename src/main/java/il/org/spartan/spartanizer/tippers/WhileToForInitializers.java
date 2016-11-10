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
public final class WhileToForInitializers extends ReplaceToNextStatementExclude<VariableDeclarationFragment> implements TipperCategory.Collapse {
  private static ForStatement buildForStatement(final VariableDeclarationFragment f, final WhileStatement ¢) {
    final ForStatement $ = ¢.getAST().newForStatement();
    $.setBody(duplicate.of(body(¢)));
    $.setExpression(pullInitializersFromExpression(dupWhileExpression(¢), parent(f)));
    initializers($).add(Initializers(f));
    return $;
  }
  private static Expression dupWhileExpression(final WhileStatement ¢) {
    return duplicate.of(expression(¢));
  }
  private static boolean fitting(final VariableDeclarationStatement s, final WhileStatement ¢) {
    return fragmentsUseFitting(s, ¢);
  }
  private static VariableDeclarationStatement fragmentParent(final VariableDeclarationFragment ¢) {
    return duplicate.of(az.variableDeclrationStatement(¢.getParent()));
  }
  // TODO: now fitting returns true iff all fragments fitting. We
  // may want to be able to treat each fragment separately.
  private static boolean fragmentsUseFitting(final VariableDeclarationStatement vds, final WhileStatement s) {
    for (final VariableDeclarationFragment ¢ : step.fragments(vds))
      if (!variableUsedInWhile(s, ¢.getName()) || !iz.variableNotUsedAfterStatement(az.statement(s), ¢.getName()))
        return false;
    return true;
  }
  private static Expression Initializers(final VariableDeclarationFragment ¢) {
    return az.variableDeclarationExpression(fragmentParent(¢));
  }
  private static VariableDeclarationStatement parent(final VariableDeclarationFragment ¢) {
    return az.variableDeclrationStatement(¢.getParent());
  }
  private static Expression pullInitializersFromExpression(final Expression from, final VariableDeclarationStatement s) {
    return iz.infix(from) ? ForToForInitializers.handleInfixCondition(duplicate.of(az.infixExpression(from)), s)
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
    return !Collect.usesOf(n).in(step.condition(s)).isEmpty() || !Collect.usesOf(n).in(step.body(s)).isEmpty();
  }
  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Merge with subsequent 'while', making a 'for (" + ¢ + "; " + expression(az.whileStatement(extract.nextStatement(¢))) + ";)' loop";
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g,
      final ExclusionManager exclude) {
    if (f == null || r == null || nextStatement == null || exclude == null)
      return null;
    final VariableDeclarationStatement vds = parent(f);
    if (vds == null)
      return null;
    final WhileStatement s = az.whileStatement(nextStatement);
    if (s == null)
      return null;
    exclude.excludeAll(step.fragments(vds));
    if (!fitting(vds, s))
      return null;
    r.remove(vds, g);
    r.replace(s, buildForStatement(f, s), g);
    return r;
  }
}
