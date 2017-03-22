package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

/** convert pattern <code>try {s} [ finally { <i>empty</i> }]</code>, {@code s}
 * not empty, to {@code {s}}.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-19 */
public final class TryBodyEmptyNoCatchesNoFinallyEliminate extends RemovingTipper<TryStatement>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = -8358212771965020073L;

  @Override public boolean prerequisite(@NotNull final TryStatement ¢) {
    final Block $ = ¢.getFinally();
    return statements(body(¢)).isEmpty() && ¢.catchClauses().isEmpty() && ($ == null || statements($).isEmpty());
  }

  @NotNull @Override public String description(@NotNull final TryStatement ¢) {
    return "Eliminate this no-op try block " + trivia.gist(¢.getBody());
  }
}
