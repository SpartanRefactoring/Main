package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @year 2016 */
public final class CachingPattern extends NanoPatternTipper<Block> {
  private static final UserDefinedTipper<Block> tipper = TipperFactory//
      .statementsPattern("if($X1 == null)$X1 = $X2;return $X1;", //
          "return $X1!=null?$X1:($X1=$X2);", //
          "Caching nano: rewrite as return of ternary");

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
