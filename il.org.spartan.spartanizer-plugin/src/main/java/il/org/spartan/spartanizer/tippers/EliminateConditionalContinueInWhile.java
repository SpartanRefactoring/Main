package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** Eliminate conditional continue before last statement in a while loop toList
 * Issue #1014
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2017-01-04 */
public class EliminateConditionalContinueInWhile extends EagerTipper<WhileStatement>//
    implements Category.Shortcircuit {
  private static final long serialVersionUID = -0x1EB9C077BFCB4C9AL;

  @Override public String description(@SuppressWarnings("unused") final WhileStatement __) {
    return "Eliminate conditional continue before last statement in the for loop";
  }
  @Override public Tip tip(final WhileStatement ¢) {
    return ContinueInConditionalEliminateAux.actualReplacement(az.block(¢.getBody()), ¢, myClass());
  }
}
