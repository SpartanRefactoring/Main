package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.leonidas.*;
import il.org.spartan.spartanizer.tipping.*;

/** if(X == null) throw Exception;
 * @author Ori Marcovitch
 * @year 2016 */
public final class IfNullThrow extends NanoPatternTipper<IfStatement> {
  private static final UserDefinedTipper<IfStatement> tipper = TipperFactory.tipper("if($X == null) throw new $N();", "ExplodeOnNullWith($N, $X)",
      "");

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Grumpy pattern";
  }

  @Override public boolean prerequisite(final IfStatement ¢) {
    return tipper.canTip(¢);
  }

  @Override public Tip tip(final IfStatement ¢) throws TipperFailure {
    return tipper.tip(¢);
    // if (¢ == null)
    // throw new $N("Cannot get the toString of a null identity");
  }
}
