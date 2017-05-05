package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code
 * if (x)
 *   throw b;
 * else
 *   throw c;
 * } into {@code
 * throw x ? b : c
 * }
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class IfThrowFooElseThrowBar extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.Ternarization {
  private static final long serialVersionUID = -0x629F5E257C7498C9L;

  @Override public String description(final IfStatement ¢) {
    return "Consolidate 'if' " + Trivia.gist(¢.getExpression()) + " into a single 'throw' statement";
  }
  @Override public boolean prerequisite(final IfStatement ¢) {
    return extract.throwExpression(then(¢)) != null && extract.throwExpression(elze(¢)) != null;
  }
  @Override public Statement replacement(final IfStatement ¢) {
    return make.throwOf(subject.pair(extract.throwExpression(then(¢)), extract.throwExpression(elze(¢))).toCondition(¢.getExpression()));
  }
}
