package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.statementsPattern;

/** @author Ori Marcovitch
 * @year 2016 */
public final class CachingPattern extends NanoPatternTipper<Block> {
  private static final UserDefinedTipper<Block> tipper = //
      statementsPattern("if($X1 == null)$X1 = $X2;return $X1;", //
          "return $X1!=null?$X1:($X1=$X2);", //
          "Caching pattern: rewrite as return of ternary");

  @Override public boolean canTip(final Block x) {
    return tipper.canTip(x);
  }

  @Override public Tip pattern(final Block x) {
    return tipper.tip(x);
  }
}
