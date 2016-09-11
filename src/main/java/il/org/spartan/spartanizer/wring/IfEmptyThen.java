package il.org.spartan.spartanizer.wring;
import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

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
    final IfStatement $ = subject.pair(elze(s), null).toNot(s.getExpression());
    return !iz.blockRequiredInReplacement(s, $) ? $ : subject.statement($).toBlock();
  }

  @Override boolean scopeIncludes(final IfStatement s) {
    return iz.vacuousThen(s) && !iz.vacuousElse(s);
  }
}
