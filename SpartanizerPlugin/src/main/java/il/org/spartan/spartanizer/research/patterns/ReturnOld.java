package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @year 2016 */
public final class ReturnOld extends NanoPatternTipper<Block> {
  private static final UserDefinedTipper<Block> tipper = TipperFactory//
      .statementsPattern("$N1 $N2 = $N3; $N3 = $X; return $N2;", "return update($N3).with($X).getOld();", "Return Old");

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
