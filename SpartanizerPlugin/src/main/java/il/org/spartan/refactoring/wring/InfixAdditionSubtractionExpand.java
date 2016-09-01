package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.java.*;

/** expand additive terms, e.g., convert <code>a-(b+c)</code> to /**
 * code>a-b-c</code>
 * @author Yossi Gil
 * @since 2016 */
public final class InfixAdditionSubtractionExpand extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.Canonicalization {
  @Override Expression replacement(final InfixExpression e) {
    if (TermsCollector.isLeafTerm(e))
      return null;
    final Expression $ = TermsExpander.simplify(e);
    return !wizard.same($, e) ? $ : null;
  }

  @Override String description(final InfixExpression e) {
    return "Expand additive terms in " + e;
  }
}
