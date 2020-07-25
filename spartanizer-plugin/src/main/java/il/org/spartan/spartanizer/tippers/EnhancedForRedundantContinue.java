package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.remove;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.CarefulTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** like (@link ForRedundantContinue) but for enhanced for.
 * @author Kfir Marx
 * @since 2016-11-26 */
public class EnhancedForRedundantContinue extends CarefulTipper<EnhancedForStatement>//
    implements Category.Shortcircuit {
  private static final long serialVersionUID = -0x4DA66AFD24883D75L;

  @Override public String description(final EnhancedForStatement ¢) {
    return "Prune redundant " + extract.lastStatement(¢);
  }
  @Override public String description() {
    return "Prune redundant continue";
  }
  @Override public Tip tip(final EnhancedForStatement ¢) {
    return new Tip(description(¢), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        remove.statement(extract.lastStatement(¢), r, g);
      }
    };
  }
  @Override public boolean prerequisite(final EnhancedForStatement ¢) {
    return iz.continueStatement(extract.lastStatement(¢));
  }
}
