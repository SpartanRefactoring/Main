package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace if(X) Y; with Y #when X
 * @author Ori Marcovitch
 * @year 2016 */
public final class IfApply extends NanoPatternTipper<IfStatement> implements Category.CommnoFactoring {
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "replace null coallescing ternary with ??";
  }

  @Override public boolean prerequisite(final IfStatement ¢) {
    return step.elze(¢) == null && !iz.block(step.then(¢));
  }
}
