package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.java.*;

/** expand additive terms, e.g., convert <code>a-(b+c)</code> to /**
 * code>a-b-c</code>
 * @author Yossi Gil
 * @since 2016 */
public final class InfixAdditionSubtractionExpand extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.Canonicalization {
  @Override String description(final InfixExpression x) {
    return "Expand additive terms in " + x;
  }

  @Override Expression replacement(final InfixExpression x) {
    if (TermsCollector.isLeafTerm(x))
      return null;
    final Expression $ = TermsExpander.simplify(x);
    return !wizard.same($, x) ? $ : null;
  }
}
