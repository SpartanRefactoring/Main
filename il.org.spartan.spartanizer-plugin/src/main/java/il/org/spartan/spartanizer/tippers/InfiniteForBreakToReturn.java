package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** Convert Infinite loops with return sideEffects to shorter ones : toList
 * Convert {@code for(;true;) { doSomething(); if(done()) break; } return XX; }
 * to : {@code for(;true;) { doSomething(); if(done()) return XX; }}
 * @author Dor Ma'ayan
 * @since 2016-09-09 */
public final class InfiniteForBreakToReturn extends CarefulTipper<ForStatement>//
    implements Category.Shortcircuit {
  private static final long serialVersionUID = -0x7074B13F7F9E2909L;

  private static Statement handleIf(final IfStatement s, final ReturnStatement nextReturn) {
    return handleIf(then(s), elze(s), nextReturn);
  }
  private static Statement handleIf(final Statement then, final Statement elze, final ReturnStatement nextReturn) {
    if (iz.breakStatement(then))
      return then;
    if (iz.block(then)) {
      final Statement $ = handleBlock(az.block(then), nextReturn);
      if ($ != null)
        return $;
    }
    if (iz.ifStatement(then))
      return handleIf(then, nextReturn);
    if (elze == null)
      return null;
    if (iz.breakStatement(elze))
      return elze;
    if (!iz.block(elze))
      return iz.ifStatement(elze) ? null : handleIf(elze, nextReturn);
    final Statement $ = handleBlock(az.block(elze), nextReturn);
    return $ != null ? $ : iz.ifStatement(elze) ? null : handleIf(elze, nextReturn);
  }
  private static Statement make(final Statement s, final ReturnStatement nextReturn) {
    return iz.breakStatement(s) ? s //
        : iz.ifStatement(s) ? handleIf(s, nextReturn) //
            : iz.block(s) ? handleBlock(az.block(s), nextReturn) //
                : null;
  }
  private static Statement handleBlock(final Block b, final ReturnStatement nextReturn) {
    Statement $ = null;
    for (final Statement ¢ : statements(b)) {
      if (iz.ifStatement(¢))
        $ = handleIf(az.ifStatement(¢), nextReturn);
      if (iz.breakStatement(¢))
        return ¢;
    }
    return $;
  }
  private static Statement handleIf(final Statement s, final ReturnStatement nextReturn) {
    return handleIf(az.ifStatement(s), nextReturn);
  }
  @Override public String description() {
    return "Convert the break inside 'for(;;)' to 'return'";
  }
  @Override public String description(final ForStatement ¢) {
    return "Convert the break inside 'for(" + initializers(¢) + "; " + ¢.getExpression() + ";" + updaters(¢) + " to return";
  }
  private Tip make(final ForStatement s, final ReturnStatement nextReturn) {
    final Statement $ = make(body(s), nextReturn);
    return $ == null ? null : new Tip(description(), getClass(), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace($, nextReturn, g);
        r.remove(nextReturn, g);
      }
    }.spanning(nextReturn);
  }
  @Override public boolean prerequisite(final ForStatement ¢) {
    return ¢ != null && extract.nextReturn(¢) != null && !iz.finiteLoop(¢);
  }
  @Override public Tip tip(final ForStatement vor) {
    if (vor == null || iz.finiteLoop(vor))
      return null;
    final ReturnStatement $ = extract.nextReturn(vor);
    return $ == null ? null : make(vor, $);
  }
}
