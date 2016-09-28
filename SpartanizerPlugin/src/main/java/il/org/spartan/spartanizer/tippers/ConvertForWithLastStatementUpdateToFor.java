package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Alex Kopzon
 * @since 2016-09-23 */
public class ConvertForWithLastStatementUpdateToFor extends ReplaceCurrentNode<ForStatement> implements TipperCategory.Collapse {
  @SuppressWarnings("unchecked") private static ForStatement buildForWhithoutLastStatement(final ForStatement $, final ForStatement s) {
    $.setExpression(dupWhileExpression(s));
    $.updaters().add(dupWhileLastStatement(s));
    // minus.LastStatement(dupWhileBody(s));
    $.setBody(minus.LastStatement(dupWhileBody(s)));
    return $;
  }

  private static Statement dupWhileBody(final ForStatement ¢) {
    return duplicate.of(¢.getBody());
  }

  private static Expression dupWhileExpression(final ForStatement ¢) {
    return duplicate.of(¢.getExpression());
  }

  private static Expression dupWhileLastStatement(final ForStatement ¢) {
    return duplicate.of(az.expressionStatement(lastStatement(¢)).getExpression());
  }

  private static ASTNode lastStatement(final ForStatement ¢) {
    return hop.lastStatement(¢.getBody());
  }

  private static boolean fitting(final ForStatement ¢) {
    return iz.assignment(lastStatement(¢)) || iz.incrementOrDecrement(lastStatement(¢)) || iz.expressionStatement(lastStatement(¢)) || !iz.containsContinueStatement(¢.getBody());
  }

  @Override public String description(final ForStatement ¢) {
    return "Convert the while about '(" + ¢.getExpression() + ")' to a traditional for(;;)";
  }

  @Override public boolean prerequisite(final ForStatement ¢) {
    return ¢ != null && !iz.containsContinueStatement(¢.getBody());
  }
  
  @Override public ASTNode replacement(final ForStatement ¢) {
    return !fitting(¢) ? null : buildForWhithoutLastStatement(¢.getAST().newForStatement(), ¢);
  }
}
