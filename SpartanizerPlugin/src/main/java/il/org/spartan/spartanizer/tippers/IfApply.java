package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace if(X) Y; with Y #when X
 * @author Ori Marcovitch
 * @year 2016 */
public final class IfApply extends NanoPatternTipper<IfStatement> implements TipperCategory.CommnoFactoring {
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "replace null coallescing ternary with ??";
  }

  @Override public boolean prerequisite(final IfStatement ¢) {
    return elze(¢) == null && !iz.block(step.then(¢));
  }
}
