package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @year 2016 */
// TODO: Marco finish
public final class FindFirst extends NanoPatternTipper<EnhancedForStatement> {
  private static final UserDefinedTipper<EnhancedForStatement> tipper = TipperFactory.subBlockTipper("for($N1 $N2 : $X1) if($X2) return $N2;",
      "findFirstIn($X1).satisfying(($N2) -> $X2)", "FindFirst");

  @Override public String description(@SuppressWarnings("unused") final EnhancedForStatement __) {
    return tipper.description();
  }
  @Override public boolean canTip(final EnhancedForStatement x) {
    return tipper.canTip(x);
  }
  @Override public Tip tip(final EnhancedForStatement x) {
    Logger.logNP(x, getClass().getSimpleName());
    return tipper.tip(x);
  }
}
