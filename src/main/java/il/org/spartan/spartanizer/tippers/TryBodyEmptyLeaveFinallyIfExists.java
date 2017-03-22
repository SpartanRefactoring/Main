package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

/** Replace this pattern {@code try {} catch(..) {a;} ... finally {x;}} with
 * {@code {x;}}, or nothing, if there is nothing in {@code x;}
 * @author Sapir Bismot
 * @since 2016-11-21 */
public final class TryBodyEmptyLeaveFinallyIfExists extends CarefulTipper<TryStatement>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 339602734857194942L;

  @Override public boolean prerequisite(@NotNull final TryStatement ¢) {
    final Block $ = ¢.getBody();
    return $ != null && statements($).isEmpty();
  }

  @NotNull @Override public Tip tip(@NotNull final TryStatement s) {
    return new Tip(description(s), s, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        final Block finallyBlock = s.getFinally();
        if (finallyBlock == null || statements(finallyBlock).isEmpty())
          r.remove(s, g);
        else
          r.replace(s, finallyBlock, g);
      }
    };
  }

  @Override public String description(@SuppressWarnings("unused") final TryStatement __) {
    return "Prune empty try block";
  }
}
