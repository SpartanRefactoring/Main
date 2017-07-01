package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

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

  @Override public String description() {
    return "Convert the break inside 'for(;;)' to 'return'";
  }
  @Override public String description(final ForStatement ¢) {
    return "Convert the break inside 'for(" + initializers(¢) + "; " + ¢.getExpression() + ";" + updaters(¢) + " to return";
  }
  private Tip make(final ForStatement s, final ReturnStatement nextReturn) {
    final List<BreakStatement> $ = allLoopBreaks(body(s));
    return $.isEmpty() ? null : new Tip(description(), getClass(), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        $.forEach(λ->r.replace(λ, nextReturn, g));
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
  static List<BreakStatement> allLoopBreaks(Statement s) {
    List<BreakStatement> $ = an.empty.list();
    s.accept(new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("unused") ForStatement node) {
        return false;
      }
      @Override public boolean visit(@SuppressWarnings("unused") DoStatement node) {
        return false;
      }
      @Override public boolean visit(@SuppressWarnings("unused") WhileStatement node) {
        return false;
      }
      @Override public boolean visit(@SuppressWarnings("unused") EnhancedForStatement node) {
        return false;
      }
      @Override public boolean visit(BreakStatement node) {
        return $.add(node);
      }
    });
    return $;
  }
}
