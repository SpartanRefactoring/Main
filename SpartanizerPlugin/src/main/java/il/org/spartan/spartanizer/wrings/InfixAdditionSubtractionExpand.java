package il.org.spartan.spartanizer.wrings;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** expand additive terms, e.g., convert <code>a-(b+c)</code> to /**
 * code>a-b-c</code>
 * @author Yossi Gil
 * @since 2016 */
public final class InfixAdditionSubtractionExpand extends ReplaceCurrentNode<InfixExpression> implements Kind.Idiomatic {
  @Override public String description(final InfixExpression ¢) {
    return "Expand additive terms in " + ¢;
  }

  @Override public Expression replacement(final InfixExpression x) {
    if (TermsCollector.isLeafTerm(x))
      return null;
    final Expression $ = TermsExpander.simplify(x);
    return !wizard.same($, x) ? $ : null;
  }
}
