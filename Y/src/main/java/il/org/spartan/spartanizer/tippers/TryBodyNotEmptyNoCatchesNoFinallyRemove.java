package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert pattern <code>try {s} [ finally { <i>empty</i> }]</code>, {@code s}
 * not empty, to {@code {s}}.
 * @author Yossi Gil
 * @since 2017-01-19 */
public final class TryBodyNotEmptyNoCatchesNoFinallyRemove extends ReplaceCurrentNode<TryStatement>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 0x6F78D0F285FBC384L;

  @Override public boolean prerequisite(final TryStatement ¢) {
    return !statements(body(¢)).isEmpty() && ¢.resources().isEmpty() && ¢.catchClauses().isEmpty()
        && (¢.getFinally() == null || statements(¢.getFinally()).isEmpty());
  }

  @Override public ASTNode replacement(final TryStatement ¢) {
    return ¢.getBody();
  }

  @Override public String description(final TryStatement ¢) {
    return "Remove the do-nothing try wrap around block " + Trivia.gist(¢.getBody());
  }
}
