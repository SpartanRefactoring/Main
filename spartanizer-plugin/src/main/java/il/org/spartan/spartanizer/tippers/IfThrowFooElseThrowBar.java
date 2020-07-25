package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.then;

import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;

import il.org.spartan.spartanizer.ast.factory.cons;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.engine.nominal.Trivia;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

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
    implements Category.Ternarization {
  private static final long serialVersionUID = -0x629F5E257C7498C9L;

  @Override public String description(final IfStatement ¢) {
    return "Consolidate 'if' " + Trivia.gist(¢.getExpression()) + " into a single 'throw' statement";
  }
  @Override public boolean prerequisite(final IfStatement ¢) {
    return extract.throwExpression(then(¢)) != null && extract.throwExpression(elze(¢)) != null;
  }
  @Override public Statement replacement(final IfStatement ¢) {
    return cons.throwOf(subject.pair(extract.throwExpression(then(¢)), extract.throwExpression(elze(¢))).toCondition(¢.getExpression()));
  }
}
