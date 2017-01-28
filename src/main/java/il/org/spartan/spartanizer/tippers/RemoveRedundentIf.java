package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Simplify if statements as much as possible (or remove them or parts of them)
 * if and only if </br>
 * it doesn'tipper have any side-effect.
 * @author Dor Ma'ayan
 * @since 2016-09-26 */
public class RemoveRedundentIf extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.EmptyCycles {
  @Override public String description(final IfStatement ¢) {
    return "Remove :" + ¢;
  }

  @Override public ASTNode replacement(final IfStatement s) {
    if (s == null)
      return null;
    final boolean $ = sideEffects.free(s.getExpression()), then = sideEffects.free(then(s)), elze = sideEffects.free(elze(s));
    return $ && then && (elze || s.getElseStatement() == null) ? s.getAST().newBlock()
        : $ && then && !elze && s.getElseStatement() != null ? subject.pair(copy.of(s.getElseStatement()), null).toNot(copy.of(s.getExpression()))
            : null;
  }
}
