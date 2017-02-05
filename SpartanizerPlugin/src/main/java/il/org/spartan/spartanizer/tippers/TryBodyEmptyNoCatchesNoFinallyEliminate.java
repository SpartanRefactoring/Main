package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;


/** convert pattern <code>try {s} [ finally { <i>empty</i> }]</code>, {@code s}
 * not empty, to {@code {s}}.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-19 */
public final class TryBodyEmptyNoCatchesNoFinallyEliminate extends RemovingTipper<TryStatement>//
    implements TipperCategory.SyntacticBaggage {
  @Override public boolean prerequisite( final TryStatement ¢) {
    return statements(body(¢)).isEmpty() && ¢.catchClauses().isEmpty() && (¢.getFinally() == null || statements(¢.getFinally()).isEmpty());
  }

  @Override  public String description( final TryStatement ¢) {
    return "Eliminate this no-op try block " + trivia.gist(¢.getBody());
  }
}
