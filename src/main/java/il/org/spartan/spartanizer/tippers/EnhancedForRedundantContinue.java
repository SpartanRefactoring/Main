package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

/** like (@link ForRedundantContinue) but for enhanced for.
 * @author Kfir Marx
 * @since 2016-11-26 */
public class EnhancedForRedundantContinue extends CarefulTipper<EnhancedForStatement>//
    implements TipperCategory.Shortcircuit {
  @Override @NotNull public String description(final EnhancedForStatement ¢) {
    return "Prune redundant " + extract.lastStatement(¢);
  }

  @Override @NotNull public String description() {
    return "Prune redundant continue";
  }

  @Override @NotNull public Tip tip(@NotNull final EnhancedForStatement ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        remove(r, extract.lastStatement(¢), g);
      }
    };
  }

  @Override public boolean prerequisite(final EnhancedForStatement ¢) {
    return iz.continueStatement(extract.lastStatement(¢));
  }

  static void remove(@NotNull final ASTRewrite r, @NotNull final Statement s, final TextEditGroup g) {
    r.getListRewrite(parent(s), Block.STATEMENTS_PROPERTY).remove(s, g);
  }
}
