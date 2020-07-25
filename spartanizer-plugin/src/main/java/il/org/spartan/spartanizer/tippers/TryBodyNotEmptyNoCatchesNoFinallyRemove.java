package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.body;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.TryStatement;

import il.org.spartan.spartanizer.engine.nominal.Trivia;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** convert pattern <code>try {s} [ finally { <i>empty</i> }]</code>, {@code s}
 * not empty, to {@code {s}}.
 * @author Yossi Gil
 * @since 2017-01-19 */
public final class TryBodyNotEmptyNoCatchesNoFinallyRemove extends ReplaceCurrentNode<TryStatement>//
    implements Category.SyntacticBaggage {
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
