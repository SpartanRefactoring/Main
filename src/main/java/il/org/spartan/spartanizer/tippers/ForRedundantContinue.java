package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** removes continue in for loop if it's last statement in the loop.
 * @author Doron Meshulam
 * @since 2016-11-26 */
public class ForRedundantContinue extends CarefulTipper<ForStatement>//
    implements TipperCategory.Shortcircuit {
  private static final long serialVersionUID = 0x1DA2D2D1173F3165L;

  @Override @NotNull public String description(final ForStatement ¢) {
    return "Prune redundant " + extract.lastStatement(¢);
  }

  @Override @NotNull public String description() {
    return "Prune redundant continue";
  }

  @Override @Nullable public Tip tip(@NotNull final ForStatement ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        @Nullable final Block b = az.block(body(¢));
        if (b == null)
          r.replace(extract.lastStatement(¢), make.emptyStatement(¢), g);
        else {
          // TODO: Doron Meshulam: use list rewrite (search for code that does
          // that) --yg
          statements(b).remove(extract.lastStatement(¢));
          r.replace(b, copy.of(b), g);
        }
      }
    };
  }

  @Override public boolean prerequisite(@NotNull final ForStatement ¢) {
    final Statement s = extract.lastStatement(¢);
    if (!iz.continueStatement(s))
      return false;
    @NotNull final SimpleName $ = label(az.continueStatement(s));
    return $ == null || iz.labeledStatement(parent(¢)) && $.getIdentifier().equals(((LabeledStatement) ¢.getParent()).getLabel().getIdentifier());
  }
}