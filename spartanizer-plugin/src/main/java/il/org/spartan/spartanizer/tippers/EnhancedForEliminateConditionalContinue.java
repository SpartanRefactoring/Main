package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.EnhancedForStatement;

import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.tipping.EagerTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Eliminate conditional continue before last statement in a for loop toList
 * Issue #1014
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2017-01-04 */
public class EnhancedForEliminateConditionalContinue extends EagerTipper<EnhancedForStatement>//
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = -0x2F49CBF1F3D6897L;

  @Override public String description(final EnhancedForStatement ¢) {
    return "Eliminate conditional continue before last statement in for about " + ¢.getExpression();
  }
  @Override public Tip tip(final EnhancedForStatement ¢) {
    return ContinueInConditionalEliminateAux.actualReplacement(az.block(¢.getBody()), ¢, myClass());
  }
}
