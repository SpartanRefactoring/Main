package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Eliminate conditional continue before last statement in a for loop toList
 * Issue #1014
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2017-01-04 */
public class EnhancedForEliminateConditionalContinue extends EagerTipper<EnhancedForStatement>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = -212967427070060695L;

  @Override  public String description( final EnhancedForStatement ¢) {
    return "Eliminate conditional continue before last statement in for about " + ¢.getExpression();
  }

  @Override @Nullable public Tip tip( final EnhancedForStatement ¢) {
    return ContinueInConditionalEliminateAux.actualReplacement(az.block(¢.getBody()), ¢, getClass());
  }
}
