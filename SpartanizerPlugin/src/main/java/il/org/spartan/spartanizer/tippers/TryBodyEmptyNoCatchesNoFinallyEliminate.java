package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert pattern <code>try {s} [ finally { <i>empty</i> }]</code>, {@code s}
 * not empty, to {@code {s}}.
 * @author Yossi Gil
 * @since 2017-01-19 */
public final class TryBodyEmptyNoCatchesNoFinallyEliminate extends RemovingTipper<TryStatement>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = -0x73FE563B7E1BA7A9L;

  @Override public boolean prerequisite(final TryStatement ¢) {
    final Block $ = ¢.getFinally();
    return statements(body(¢)).isEmpty() && ¢.catchClauses().isEmpty() && ($ == null || statements($).isEmpty());
  }
  @Override public String description(final TryStatement ¢) {
    return "Eliminate this no-__ try block " + Trivia.gist(¢.getBody());
  }
}
