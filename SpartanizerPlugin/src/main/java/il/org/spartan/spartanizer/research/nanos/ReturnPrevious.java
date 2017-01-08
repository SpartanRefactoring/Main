package il.org.spartan.spartanizer.research.nanos;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

import static il.org.spartan.spartanizer.research.TipperFactory.statementsPattern;

/** @author Ori Marcovitch
 * @year 2016 */
public final class ReturnPrevious extends NanoPatternTipper<Block> {
  private static final UserDefinedTipper<Block> tipper = statementsPattern("$T $N2 = $N3; $N3 = $X; return $N2;",
      "return update($N3).with($X).getOld();", "Return Old");

  @Override public boolean canTip(final Block x) {
    return tipper.canTip(x);
  }

  @Override public Tip pattern(final Block x) {
    return tipper.tip(x);
  }

  @Override public Category category() {
    return Category.Return;
  }

  @Override public String description() {
    return "Update field and return old value";
  }

  @Override public String technicalName() {
    return "AssignXWithYReturnPreviousX";
  }
}
