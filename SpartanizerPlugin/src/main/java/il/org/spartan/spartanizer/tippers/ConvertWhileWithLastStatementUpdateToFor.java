package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Dor Ma'ayan
 * @since 2016-09-23 */
public class ConvertWhileWithLastStatementUpdateToFor extends ReplaceCurrentNode<WhileStatement> implements Kind.Collapse {
  @SuppressWarnings("unchecked") private static ForStatement buildForWhithoutLastStatement(final ForStatement $, final WhileStatement s) {
    $.setExpression(dupWhileExpression(s));
    $.updaters().add(dupWhileLastStatement(s));
    hop.removeLastStatement(dupWhileBody(s));
    $.setBody(hop.removeLastStatement(dupWhileBody(s)));
    return $;
  }

  private static Statement dupWhileBody(final WhileStatement s) {
    return duplicate.of(s.getBody());
  }

  private static Expression dupWhileExpression(final WhileStatement s) {
    return duplicate.of(s.getExpression());
  }

  private static Expression dupWhileLastStatement(final WhileStatement s) {
    return duplicate.of(az.expressionStatement(lastStatement(s)).getExpression());
  }

  private static ASTNode lastStatement(final WhileStatement s) {
    return hop.lastStatement(s.getBody());
  }

  private static boolean lastStatementIsUpdate(final WhileStatement s) {
    return iz.assignment(lastStatement(s)) || iz.incrementOrDecrement(lastStatement(s)) || iz.expressionStatement(lastStatement(s));
  }

  @Override public String description(final WhileStatement n) {
    return "Convert the while about '(" + n.getExpression() + ")' to a traditional for(;;)";
  }

  @Override public boolean prerequisite(final WhileStatement ¢) {
    return ¢ != null && !iz.containsContinueStatement(¢.getBody());
  }

  @Override public ASTNode replacement(final WhileStatement s) {
    if (lastStatementIsUpdate(s)) {
      final ForStatement $ = s.getAST().newForStatement();
      return buildForWhithoutLastStatement($, s);
    }
    return null;
  }
}
