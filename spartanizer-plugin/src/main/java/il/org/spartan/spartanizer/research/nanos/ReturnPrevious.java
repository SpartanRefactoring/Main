package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.parent;
import static il.org.spartan.spartanizer.research.TipperFactory.statementsPattern;

import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ReturnStatement;

import fluent.ly.as;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.research.UserDefinedTipper;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.tipping.Tip;

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
