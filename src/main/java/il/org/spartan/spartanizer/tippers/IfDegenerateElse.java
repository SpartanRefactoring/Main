package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert
 *
 * <pre>
 * if (x)
 *   return b;
 * else {}
 * </pre>
 *
 * into
 *
 * <pre>
 * if (x)
 *   return b;
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-08-01 */
public final class IfDegenerateElse extends ReplaceCurrentNode<IfStatement> implements TipperCategory.InVain {
  static boolean degenerateElse(final IfStatement ¢) {
    return elze(¢) != null && iz.vacuousElse(¢);
  }

  @Override public String description(final IfStatement ¢) {
    return "Remove vacuous 'else' branch of 'if(" + ¢.getExpression() + ")...'";
  }

  @Override public boolean prerequisite(final IfStatement ¢) {
    return ¢ != null && then(¢) != null && degenerateElse(¢);
  }

  @Override public Statement replacement(final IfStatement ¢) {
    final IfStatement $ = duplicate.of(¢);
    $.setElseStatement(null);
    return !iz.blockRequiredInReplacement(¢, $) ? $ : subject.statement($).toBlock();
  }
}
