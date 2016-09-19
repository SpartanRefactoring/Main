package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

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
public final class InfixComparisonSpecific extends ReplaceCurrentNode<InfixExpression> implements Kind.Idiomatic {
  private static final specificity specifity = new specificity();



  @Override public boolean canSuggest(final InfixExpression ¢) {
    return specifity.compare(left(¢), right(¢)) < 0 && !¢.hasExtendedOperands() && iz.comparison(¢) && (specificity.defined(left(¢)) || specificity.defined(right(¢)));
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Exchange left and right operands of comparison";
  }

  @Override public Expression replacement(final InfixExpression ¢) {
    return make.conjugate(¢);
  }
}
