package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Eliminate conditional continue before last statement in a for loop toList
 * Issue #1014
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-01-04 */
public class EnhancedForEliminateConditionalContinue extends EagerTipper<EnhancedForStatement>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = -212967427070060695L;

  @Override @NotNull public String description(@NotNull final EnhancedForStatement ¢) {
    return "Eliminate conditional continue before last statement in for about " + ¢.getExpression();
  }

  @Override @Nullable public Tip tip(@NotNull final EnhancedForStatement ¢) {
    return EliminateConditionalContinueAux.actualReplacement(az.block(¢.getBody()), ¢, getClass());
  }
}
