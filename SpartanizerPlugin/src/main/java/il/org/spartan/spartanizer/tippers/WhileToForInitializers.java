package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert <code>
 * int a = 3;
 * while(Panic) {
 *    ++OS.is.in.denger;
 * }
 * </code> to <code>
 * for(int a = 3; Panic;) {
 *    ++OS.is.in.denger;
 * }
 * </code>
 * @author Alex Kopzon
 * @since 2016 */
public final class WhileToForInitializers extends ReplaceToNextStatementExclude<VariableDeclarationFragment> implements TipperCategory.Collapse {
  private static ForStatement buildForStatement(final VariableDeclarationFragment f, final WhileStatement ¢) {
    final ForStatement $ = ¢.getAST().newForStatement();
    $.setBody(duplicate.of(body(¢)));
    $.setExpression(pullInitializersFromExpression(dupWhileExpression(¢), parent(f)));
    step.initializers($).add(Initializers(f));
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

  // TODO: Alex and Dan, now fitting returns true iff all fragments fitting. We
  // may want to change it.
  private static boolean fragmentsUseFitting(final VariableDeclarationStatement ¢, final WhileStatement s) {
    for (final VariableDeclarationFragment f : step.fragments(¢))
      if (!variableUsedInWhile(s, f.getName()) || !iz.variableNotUsedAfterStatement(az.asStatement(s), f.getName()))
        return false;
    return true;
  }

  /** XXX: This is a bug of auto-laconize [[SuppressWarningsSpartan]] */
  private static Expression handleInfix(final InfixExpression from, final VariableDeclarationStatement s) {
    final List<Expression> operands = hop.operands(from);
    for (final Expression ¢ : operands)
      if (iz.parenthesizeExpression(¢) && iz.assignment(az.parenthesizedExpression(¢).getExpression())) {
        final Assignment a = az.assignment(az.parenthesizedExpression(¢).getExpression());
        final SimpleName var = az.simpleName(step.left(a));
        for (final VariableDeclarationFragment f : step.fragments(s))
          if ((f.getName() + "").equals(var + "")) {
            f.setInitializer(duplicate.of(step.right(a)));
            operands.set(operands.indexOf(¢), ¢.getAST().newSimpleName(var + ""));
          }
      }
    return subject.append(subject.pair(operands.get(0), operands.get(1)).to(from.getOperator()), minus.firstElem(minus.firstElem(operands)));
  }

  private static Expression Initializers(final VariableDeclarationFragment ¢) {
    return az.variableDeclarationExpression(fragmentParent(¢));
  }

  private static VariableDeclarationStatement parent(final VariableDeclarationFragment ¢) {
    return az.variableDeclrationStatement(¢.getParent());
  }

  /** Pulls matching initializers from forExpression, and pushes it to the
   * declarationStatement which is previous to the for loop.
   * @param from JD (already duplicated)
   * @param to is the list that will contain the pulled out initializations from
   *        the given expression.
   * @return expression to the new for loop, without the initializers. */
  private static Expression pullInitializersFromExpression(final Expression from, final VariableDeclarationStatement s) {
    return !haz.sideEffects(from) || !iz.infix(from) ? from : handleInfix(duplicate.of(az.infixExpression(from)), s);
  }

  /** Determines whether a specific SimpleName was used in a
   * {@link ForStatement}.
   * @param s JD
   * @param n JD
   * @return true <b>iff</b> the SimpleName is used in a ForStatement's
   *         condition, updaters, or body. */
  private static boolean variableUsedInWhile(final WhileStatement s, final SimpleName n) {
    return !Collect.usesOf(n).in(step.condition(s)).isEmpty() || !Collect.usesOf(n).in(step.body(s)).isEmpty();
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Merge with subequent 'while', making a for (" + ¢ + "; " + expression(az.whileStatement(extract.nextStatement(¢))) + "loop";
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
