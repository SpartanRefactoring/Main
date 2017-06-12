package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** Convert Infinite loops with return sideEffects to shorter ones : toList
 * Convert {@code while (true) { doSomething(); if(done()) break; } return XX; }
 * to : {@code while (true) { doSomething(); if(done()) return XX; } }
 * @author Dor Ma'ayan
 * @since 2016-09-09 */
public final class WhileInfiniteBreakToReturn extends CarefulTipper<WhileStatement>//
    implements Category.Loops {
  private static final long serialVersionUID = -0x565FA66D15A7881BL;

  private static Statement handleBlock(final Block body, final ReturnStatement nextReturn) {
    Statement $ = null;
    for (final Statement ¢ : statements(body)) {
      if (iz.ifStatement(¢))
        $ = handleIf(az.ifStatement(¢), nextReturn);
      if (iz.breakStatement(¢)) {
        $ = ¢;
        break;
      }
    }
    return $;
  }
  private static Statement handleIf(final IfStatement s, final ReturnStatement nextReturn) {
    final IfStatement ifStatement = az.ifStatement(s);
    if (ifStatement == null)
      return null;
    final Statement then = ifStatement.getThenStatement(), elze = ifStatement.getElseStatement();
    if (then == null)
      return null;
    if (iz.breakStatement(then))
      return then;
    if (iz.block(then)) {
      final Statement $ = handleBlock((Block) then, nextReturn);
      if ($ != null)
        return $;
    }
    if (iz.ifStatement(then))
      return handleIf(az.ifStatement(then), nextReturn);
    if (elze == null)
      return null;
    if (iz.breakStatement(elze))
      return elze;
    if (!iz.block(elze))
      return !iz.ifStatement(elze) ? null : handleIf(az.ifStatement(elze), nextReturn);
    final Statement $ = handleBlock((Block) elze, nextReturn);
    return $ != null ? $ : !iz.ifStatement(elze) ? null : handleIf(az.ifStatement(elze), nextReturn);
  }
  @Override public String description() {
    return "Convert the break inside 'while()' loop to 'return'";
  }
  @Override public String description(final WhileStatement ¢) {
    return "Convert the break inside 'while(" + ¢.getExpression() + ")' to return";
  }
  @Override public boolean prerequisite(final WhileStatement ¢) {
    return ¢ != null && extract.nextReturn(¢) != null && !iz.finiteLoop(¢);
  }
  @Override public Tip tip(final WhileStatement s) {
    final ReturnStatement nextReturn = extract.nextReturn(s);
    if (s == null || iz.finiteLoop(s) || nextReturn == null)
      return null;
    final Statement body = body(s), //
        $ = iz.ifStatement(body) ? handleIf(az.ifStatement(body), nextReturn) //
            : iz.block(body) ? handleBlock(az.block(body), nextReturn) //
                : iz.breakStatement(body) ? body : null;
    return $ == null ? null : new Tip(description(s), getClass(), s.getExpression()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace($, nextReturn, g);
        remove.statement(nextReturn, r, g);
      }
    }.spanning(nextReturn);
  }
}
