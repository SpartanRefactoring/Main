package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;
import static il.org.spartan.spartanizer.ast.navigate.step.then;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.lisp;
import il.org.spartan.spartanizer.ast.factory.atomic;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.remove;
import il.org.spartan.spartanizer.ast.navigate.hop;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.GoToNextStatement;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Convert {@code if(a){f();return;}g();} into {@code if(a){f();return;}g();}
 * f(); } provided that this {@code if} statement is the last statement in a
 * method.
 * @author Yossi Gil
 * @since 2016 */
public final class IfPenultimateInMethodFollowedBySingleStatement extends GoToNextStatement<IfStatement>//
    implements Category.EarlyReturn {
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
    $.replace(deleteMe, atomic.emptyStatement(deleteMe), g);
    remove.statement(nextStatement, $, g);
    final IfStatement newIf = copy.of(s);
    final Block block = az.block(then(newIf));
    if (block != null)
      lisp.removeLast(statements(block));
    else
      newIf.setThenStatement(atomic.emptyStatement(newIf));
    newIf.setElseStatement(copy.of(nextStatement));
    $.replace(s, newIf, g);
    remove.statement(nextStatement, $, g);
    return $;
  }
}
