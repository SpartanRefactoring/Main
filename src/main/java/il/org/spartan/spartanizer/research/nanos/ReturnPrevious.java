package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** *
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public final class ReturnPrevious extends NanoPatternTipper<ReturnStatement> {
  private static final long serialVersionUID = 5711454852792640619L;
  private static final List<UserDefinedTipper<Block>> tippers = as
      .list(statementsPattern("$T $N2 = $N3; $N3 = $X; return $N2;", "return update($N3).with($X).getOld();", "Return Old"));

  @Override public boolean canTip(final ReturnStatement x) {
    return anyTips(tippers, az.block(parent(x)));
  }

  @Override @Nullable public Tip pattern(final ReturnStatement $) {
    return firstTip(tippers, az.block(parent($)));
  }

  @Override @NotNull public Category category() {
    return Category.Field;
  }

  @Override public String description() {
    return "Update field and return old value";
  }

  @Override public String technicalName() {
    return "AssignXWithYReturnPreviousX";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
