package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.statementsPattern;

/** @author Ori Marcovitch
 * @year 2016 */
public final class ReturnOld extends NanoPatternTipper<Block> {
  private static final UserDefinedTipper<Block> tipper = statementsPattern("$T $N2 = $N3; $N3 = $X; return $N2;",
      "return update($N3).with($X).getOld();", "Return Old");

  @Override public boolean canTip(final Block x) {
    return tipper.canTip(x);
  }

  @Override public Tip pattern(final Block x) {
    return tipper.tip(x);
  }

  @Override public String category() {
    return Category.Return + "";
  }
}
