package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** convert {@code switch (x) { case a: case b: x = 5; break; case c: default:
 * case d: break; } switch (x) { case a: case b: x = 5; break; default: } } into
 * {@code
* (some commands)
 * } . Tested in {@link Issue0880}
 * @author Yuval Simon
 * @since 2016-11-27 */
public class RemoveRedundantSwitchCases extends CarefulTipper<SwitchCase>//
    implements TipperCategory.SyntacticBaggage {
  @Override @NotNull public Tip tip(@NotNull final SwitchCase n, @Nullable final ExclusionManager exclude) {
    final SwitchCase $ = az.switchCase(extract.nextStatementInside(n));
    if (exclude != null)
      exclude.excludeAll(extract.casesOnSameBranch(az.switchStatement(n.getParent()), n));
    return new Tip(description(n), n, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        r.remove($.isDefault() ? n : $, g);
      }
    };
  }

  @Override protected boolean prerequisite(@NotNull final SwitchCase n) {
    final SwitchCase $ = az.switchCase(extract.nextStatementInside(n));
    return $ != null && ($.isDefault() || n.isDefault());
  }

  @Override @NotNull @SuppressWarnings("unused") public String description(final SwitchCase n) {
    return "remove redundant switch case";
  }
}