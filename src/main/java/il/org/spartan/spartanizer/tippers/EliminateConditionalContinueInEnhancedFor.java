package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Eliminate conditional continue before last statement in a for loop </br>
 * Issue #1014
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-01-04 */
public class EliminateConditionalContinueInEnhancedFor extends EagerTipper<EnhancedForStatement>//
    implements TipperCategory.SyntacticBaggage {
  @NotNull
  @Override public String description(@SuppressWarnings("unused") final EnhancedForStatement __) {
    return "Eliminate conditional continue before last statement in the for loop";
  }

  @Nullable
  @Override public Tip tip(@NotNull final EnhancedForStatement ¢) {
    return EliminateConditionalContinueAux.actualReplacement(az.block(¢.getBody()), ¢, getClass());
  }
}
