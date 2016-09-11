package il.org.spartan.spartanizer.wring;
import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** reorder comparisons so that the specific value is placed on the right.
 * Specific value means a literal, or any of the two keywords
 *
 * <pre>
 * <b>this</b>
 * </pre>
 *
 * or
 *
 * <pre>
 * <b>null</b>
 * </pre>
 *
 * .
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class InfixComparisonSpecific extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.Canonicalization {
  private static final specificity specifity = new specificity();

  @Override public boolean scopeIncludes(final InfixExpression x) {
    return !x.hasExtendedOperands() && iz.comparison(x) && (specificity.defined(left(x)) || specificity.defined(right(x)));
  }

  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Exchange left and right operands of comparison";
  }

  @Override boolean eligible(final InfixExpression x) {
    return specifity.compare(left(x), right(x)) < 0;
  }

  @Override Expression replacement(final InfixExpression x) {
    return make.conjugate(x);
  }
}
