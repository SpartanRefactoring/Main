package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

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
public final class IfDegenerateElse extends Wring.ReplaceCurrentNode<IfStatement> implements Kind.Canonicalization {
  static boolean degenerateElse(final IfStatement s) {
    return step.elze(s) != null && iz.vacuousElse(s);
  }

  @Override String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Remove vacuous 'else' branch";
  }

  @Override Statement replacement(final IfStatement s) {
    final IfStatement $ = duplicate.of(s);
    $.setElseStatement(null);
    return !iz.blockRequiredInReplacement(s, $) ? $ : subject.statement($).toBlock();
  }

  @Override boolean scopeIncludes(final IfStatement s) {
    return s != null && step.then(s) != null && degenerateElse(s);
  }
}
