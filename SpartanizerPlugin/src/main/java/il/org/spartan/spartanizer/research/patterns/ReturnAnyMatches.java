package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @year 2016 */
public final class ReturnAnyMatches extends NanoPatternTipper<Block> {
  private static final UserDefinedTipper<Block> tipper = TipperFactory//
      .statementsPattern("for($N1 $N2 : $X1) if($X2) return true; return false;", "return anyIn($X1).matches($N2 -> $X2);",
          "Any matches pattern. Consolidate into one statement");

  @Override public String description(@SuppressWarnings("unused") final Block __) {
    return tipper.description();
  }

  @Override public boolean canTip(final Block x) {
    return tipper.canTip(x);
  }

  @Override public Tip tip(final Block x) {
    Logger.logNP(x, getClass().getSimpleName());
    return tipper.tip(x);
  }
}
