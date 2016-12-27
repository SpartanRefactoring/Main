package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Ori Marcovitch
 * @year 2016 */
public final class BlockInlineStatementIntoNext extends CarefulTipper<Block> implements TipperCategory.Inlining {
  private static final UserDefinedTipper<Block> tipper = TipperFactory.patternTipper("$X = $X.$N1($A1); $X = $X.$N2($A2);",
      "$X = $X.$N1($A1).$N2($A2);", "inline statement into next one");

  @Override public String description(@SuppressWarnings("unused") final Block __) {
    return tipper.description();
  }

  @Override public boolean prerequisite(final Block x) {
    return tipper.canTip(x);
  }

  @Override public Tip tip(final Block x) {
    return tipper.tip(x);
  }
}
