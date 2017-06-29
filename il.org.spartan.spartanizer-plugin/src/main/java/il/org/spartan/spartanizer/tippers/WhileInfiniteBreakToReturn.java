package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.spartanizer.tippers.InfiniteForBreakToReturn.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
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
    final List<BreakStatement> $ = allLoopBreaks(body(s));
    return $.isEmpty() ? null : new Tip(description(s), getClass(), s.getExpression()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        $.forEach(λ->r.replace(λ, nextReturn, g));
        remove.statement(nextReturn, r, g);
      }
    }.spanning(nextReturn);
  }
}
