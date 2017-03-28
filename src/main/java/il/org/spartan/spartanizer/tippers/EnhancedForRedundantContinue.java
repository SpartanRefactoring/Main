package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** like (@link ForRedundantContinue) but for enhanced for.
 * @author Kfir Marx
 * @since 2016-11-26 */
public class EnhancedForRedundantContinue extends CarefulTipper<EnhancedForStatement>//
    implements TipperCategory.Shortcircuit {
  private static final long serialVersionUID = -5595277222526598517L;

  @Override public String description(final EnhancedForStatement ¢) {
    return "Prune redundant " + extract.lastStatement(¢);
  }

  @Override public String description() {
    return "Prune redundant continue";
  }

  @Override public Tip tip(final EnhancedForStatement ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        trick.remove(r, extract.lastStatement(¢), g);
      }
    };
  }

  @Override public boolean prerequisite(final EnhancedForStatement ¢) {
    return iz.continueStatement(extract.lastStatement(¢));
  }
}
