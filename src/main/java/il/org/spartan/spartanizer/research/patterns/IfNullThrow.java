package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** if(X == null) throw Exception;
 * @author Ori Marcovitch
 * @year 2016 */
public final class IfNullThrow extends NanoPatternTipper<IfStatement> {
  private static final UserDefinedTipper<IfStatement> tipper = TipperFactory.patternTipper("if($X == null) throw $X2;",
      "If.Null($X).throwz(() -> {$X2;});", "Grumpy pattern");

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Grumpy pattern";
  }

  @Override public boolean canTip(final IfStatement ¢) {
    return tipper.canTip(¢);
  }

  @Override public Tip tip(final IfStatement ¢) {
    Logger.logNP(¢, getClass().getSimpleName());
    return tipper.tip(¢);
  }
}
