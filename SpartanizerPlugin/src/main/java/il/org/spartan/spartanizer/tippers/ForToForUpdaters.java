package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Alex Kopzon
 * @since 2016-09-23 */
public class ForToForUpdaters extends ReplaceCurrentNode<ForStatement> implements TipperCategory.Collapse {
  @SuppressWarnings("unchecked") private static ForStatement buildForWhithoutFirstLastStatement(final ForStatement $) {
    $.updaters().add(dupWhileStatement($));
    $.setBody(minus.firstLastStatement(dupForBody($)));
    return $;
  }

  private static Statement dupForBody(final ForStatement ¢) {
    return duplicate.of(¢.getBody());
  }

  private static Expression dupWhileStatement(final ForStatement ¢) {
    return duplicate.of(az.expressionStatement(firstLastStatement(¢)).getExpression());
  }

  private static ASTNode firstLastStatement(final ForStatement ¢) {
    return hop.firstLastStatement(¢.getBody());
  }

  private static boolean fitting(final ForStatement ¢) {
    final ForRenameInitializerToCent renameInitializerTipper = new ForRenameInitializerToCent();
    final DeclarationInitializerStatementTerminatingScope inliningTipper = new DeclarationInitializerStatementTerminatingScope();
    if (renameInitializerTipper.canTip(forExpression(¢)) || inliningTipper.canTip(prevToFirstLastExpressionFragment(¢)))
      return false;
    final boolean $ = ¢ != null && (iz.assignment(lastStatement(¢)) || iz.incrementOrDecrement(lastStatement(¢)) || haz.sideEffects(lastStatement(¢)))
        && !iz.containsContinueStatement(¢.getBody());
    return $;
  }

  private static VariableDeclarationExpression forExpression(final ForStatement ¢) {
    final Expression e = findFirst.elementOf(step.initializers(¢));
    final VariableDeclarationExpression $ = az.variableDeclarationExpression(e);
    return $;
  }

  private static Statement lastStatement(final ForStatement ¢) {
    return az.asStatement(hop.lastStatement(¢.getBody()));
  }

  /** @param ¢ JD
   * @return converssion of {@link Statement}, which is previous to the
   *         firstLastStatement in the loop body. */
  private static VariableDeclarationFragment prevToFirstLastExpressionFragment(final ForStatement ¢) {
    final ASTNode n = hop.firstLastStatement(¢.getBody());
    if (n == null)
      return null;
    final Statement current = az.asStatement(n);
    if (current == null)
      return null;
    final Statement previous = hop.previousStatementInBody(current);
    if (previous == null)
      return null;
    final VariableDeclarationStatement vds = az.variableDeclrationStatement(previous);
    return vds == null ? null : findFirst.elementOf(step.fragments(vds));
  }

  @Override public String description(final ForStatement ¢) {
    return "Convert the while about '(" + ¢.getExpression() + ")' to a traditional for(;;)";
  }

  @Override public boolean prerequisite(final ForStatement ¢) {
    return ¢ != null && !iz.containsContinueStatement(¢.getBody());
  }

  @Override public ASTNode replacement(final ForStatement ¢) {
    return !fitting(¢) ? null : buildForWhithoutFirstLastStatement(duplicate.of(¢));
  }
}
