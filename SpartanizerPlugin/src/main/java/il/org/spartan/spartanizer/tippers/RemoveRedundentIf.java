package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Simplify if statements as much as possible (or remove them or parts of them)
 * if and only if </br>
 * it doesn'tipper have any side-effect.
 * @author Dor Ma'ayan
 * @since 2016-09-26 */
public class RemoveRedundentIf extends ReplaceCurrentNode<IfStatement> implements TipperCategory.Collapse {
  @Override public String description(final IfStatement ¢) {
    return "remove :" + ¢;
  }

  @Override public ASTNode replacement(final IfStatement s) {
    if (s == null)
      return null;
    final boolean condition = !haz.sideEffects(s.getExpression());
    final boolean then = RemoveRedundent.checkBlock(s.getThenStatement());
    final boolean elze = RemoveRedundent.checkBlock(s.getElseStatement());
    return condition && then && (elze || s.getElseStatement() == null) ? s.getAST().newBlock()
        : !condition || !then || elze || s.getElseStatement() == null ? null
            : subject.pair(duplicate.of(s.getElseStatement()), null).toNot(duplicate.of(s.getExpression()));
  }
}
