package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.java.*;

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
    return !x.hasExtendedOperands() && iz.comparison(x) && (specificity.defined(step.left(x)) || specificity.defined(step.right(x)));
  }

  @Override boolean canMake(final InfixExpression e) {
    return specifity.compare(left(e), right(e)) < 0;
  }

  @Override boolean eligible(final InfixExpression x) {
    return specifity.compare(step.left(x), step.right(x)) < 0;
  }

  @Override public boolean claims(final InfixExpression e) {
    return !e.hasExtendedOperands() && iz . comparison(e) && (Specificity.defined(left(e)) || Specificity.defined(right(e)));
  }
}
