package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;

/** convert
 *
 * <pre>
 * if (x) ; else {a;}
 * </pre>
 *
 * into
 *
 * <pre>
 * if (!x)
 * a;
 * </pre>
 *
 * .
 * @author Yossi Gil
 * @since 2015-08-26 */
public final class IfEmptyThen extends Wring.ReplaceCurrentNode<IfStatement> implements Kind.Canonicalization {
  @Override String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Invert conditional and remove vacuous 'then' branch";
  }

  @Override Statement replacement(final IfStatement s) {
    final IfStatement $ = subject.pair(step.elze(s), null).toNot(s.getExpression());
    return !iz.blockRequiredInReplacement(s, $) ? $ : subject.statement($).toBlock();
  }

  @Override boolean claims(final IfStatement s) {
    return s != null && iz.vacuousThen(s) && !iz.vacuousElse(s);
  }
}
