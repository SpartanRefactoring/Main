package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.tipping.*;

/** *
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public final class ReturnPrevious extends NanoPatternTipper<ReturnStatement> {
  private static final long serialVersionUID = 0x4F4329ED9362E46BL;
  private static final List<UserDefinedTipper<Block>> tippers = as
      .list(statementsPattern("$T $N2 = $N3; $N3 = $X; return $N2;", "return update($N3).with($X).getOld();", "Return Old"));

  @Override public boolean canTip(final ReturnStatement x) {
    return anyTips(tippers, az.block(parent(x)));
  }
  @Override public Tip pattern(final ReturnStatement $) {
    return firstTip(tippers, az.block(parent($)));
  }
  @Override public Category category() {
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
