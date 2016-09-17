package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** /** convert
 *
 * <pre>
 * if (x)
 *   return b;
 * else {
 * }
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
public final class IfDegenerateElse extends ReplaceCurrentNode<IfStatement> implements Kind.NOP {
  static boolean degenerateElse(final IfStatement ¢) {
    return elze(¢) != null && iz.vacuousElse(¢);
  }

  @Override public boolean demandsToSuggestButPerhapsCant(final IfStatement ¢) {
    return ¢ != null && then(¢) != null && degenerateElse(¢);
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Remove vacuous 'else' branch";
  }

  @Override public Statement replacement(final IfStatement s) {
    final IfStatement $ = duplicate.of(s);
    $.setElseStatement(null);
    return !iz.blockRequiredInReplacement(s, $) ? $ : subject.statement($).toBlock();
  }
}
