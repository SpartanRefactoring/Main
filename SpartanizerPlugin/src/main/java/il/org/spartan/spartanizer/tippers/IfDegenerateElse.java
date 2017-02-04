package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** convert {@code if (x) return b; else {} } into {@code
 * if (x)
 *   return b;
 * }
 * @author Yossi Gil
 * @since 2015-08-01 */
public final class IfDegenerateElse extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.SyntacticBaggage {
  static boolean degenerateElse(final IfStatement ¢) {
    return elze(¢) != null && iz.vacuousElse(¢);
  }

  @Override @NotNull public String description(@NotNull final IfStatement ¢) {
    return "Remove vacuous 'else' branch of 'if(" + trivia.gist(¢.getExpression() + "") + ")...'";
  }

  @Override public boolean prerequisite(@Nullable final IfStatement ¢) {
    return ¢ != null && then(¢) != null && degenerateElse(¢);
  }

  @Override @NotNull public Statement replacement(final IfStatement ¢) {
    final IfStatement $ = copy.of(¢);
    $.setElseStatement(null);
    return !iz.blockRequiredInReplacement(¢, $) ? $ : subject.statement($).toBlock();
  }
}
