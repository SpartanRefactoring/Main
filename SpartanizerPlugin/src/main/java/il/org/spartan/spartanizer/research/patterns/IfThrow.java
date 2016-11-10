package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** if(X == null) throw Exception;
 * @author Ori Marcovitch
 * @year 2016 */
public final class IfThrow extends NanoPatternTipper<IfStatement> {
  private static final UserDefinedTipper<IfStatement> nullTipper = TipperFactory.tipper("if($X == null) throw $X2;", "", "");
  private static final UserDefinedTipper<IfStatement> tipper = TipperFactory.tipper("if($X) throw $X2;", "explodeWith($X2).when($X);",
      "IfThrow pattern. Go fluent!");

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "";
  }
  @Override public boolean canTip(final IfStatement ¢) {
    return tipper.canTip(¢) && nullTipper.cantTip(¢);
  }
  @Override public Tip tip(final IfStatement ¢) {
    Logger.logNP(¢, getClass().getSimpleName());
    return tipper.tip(¢);
  }
}
