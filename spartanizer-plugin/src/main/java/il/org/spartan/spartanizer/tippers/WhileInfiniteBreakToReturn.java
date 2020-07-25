package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.body;
import static il.org.spartan.spartanizer.tippers.InfiniteForBreakToReturn.allLoopBreaks;

import java.util.List;

import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.remove;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.CarefulTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

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
    final ReturnStatement ret = extract.nextReturn(s);
    if (s == null || iz.finiteLoop(s) || ret == null)
      return null;
    final List<BreakStatement> $ = allLoopBreaks(body(s));
    return $.isEmpty() ? null : new Tip(description(s), getClass(), s.getExpression()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        $.forEach(λ->r.replace(λ, ret, g));
        remove.statement(ret, r, g);
      }
    }.spanning(ret);
  }
}
