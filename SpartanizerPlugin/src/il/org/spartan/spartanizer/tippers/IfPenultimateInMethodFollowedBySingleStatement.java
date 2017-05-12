package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert {@code if(a){f();return;}g();} into {@code if(a){f();return;}g();}
 * f(); } provided that this {@code if} statement is the last statement in a
 * method.
 * @author Yossi Gil
 * @since 2016 */
public final class IfPenultimateInMethodFollowedBySingleStatement extends GoToNextStatement<IfStatement>//
    implements TipperCategory.EarlyReturn {
  private static final long serialVersionUID = -0x7FE2E1D00BCC8C44L;

  @Override public String description(final IfStatement ¢) {
    return "Convert return into else in  if(" + ¢.getExpression() + ")";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final IfStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (elze(s) != null || !iz.lastInMethod(nextStatement))
      return null;
    final Statement then = then(s);
    final ReturnStatement deleteMe = az.returnStatement(hop.lastStatement(then));
    if (deleteMe == null || deleteMe.getExpression() != null)
      return null;
    $.replace(deleteMe, make.emptyStatement(deleteMe), g);
    remove.statement(nextStatement, $, g);
    final IfStatement newIf = copy.of(s);
    final Block block = az.block(then(newIf));
    if (block != null)
      lisp.removeLast(statements(block));
    else
      newIf.setThenStatement(make.emptyStatement(newIf));
    newIf.setElseStatement(copy.of(nextStatement));
    $.replace(s, newIf, g);
    remove.statement(nextStatement, $, g);
    return $;
  }
}
