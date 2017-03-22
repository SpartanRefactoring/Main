package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code if (x) return b; else {} } into {@code
 * if (x)
 *   return b;
 * }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-01 */
public final class IfDegenerateElse extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 7639783379834141872L;

  static boolean degenerateElse(final IfStatement ¢) {
    return elze(¢) != null && iz.vacuousElse(¢);
  }

  @NotNull @Override public String description(@NotNull final IfStatement ¢) {
    return "Remove vacuous 'else' branch of 'if(" + trivia.gist(¢.getExpression() + "") + ")...'";
  }

  @Override public boolean prerequisite(@Nullable final IfStatement ¢) {
    return ¢ != null && then(¢) != null && degenerateElse(¢);
  }

  @Override public Statement replacement(final IfStatement ¢) {
    final IfStatement $ = copy.of(¢);
    $.setElseStatement(null);
    return !iz.blockRequiredInReplacement(¢, $) ? $ : subject.statement($).toBlock();
  }
}
