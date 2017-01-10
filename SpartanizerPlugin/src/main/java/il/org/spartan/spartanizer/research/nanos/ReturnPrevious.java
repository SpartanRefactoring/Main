package il.org.spartan.spartanizer.research.nanos;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

import static il.org.spartan.spartanizer.research.TipperFactory.statementsPattern;

import java.util.*;

/** @author Ori Marcovitch
 * @year 2016 */
public final class ReturnPrevious extends NanoPatternTipper<Block> {
  private static final List<UserDefinedTipper<Block>> tippers = new ArrayList<UserDefinedTipper<Block>>() {
    static final long serialVersionUID = 1L;
    {
      add(statementsPattern("$T $N2 = $N3; $N3 = $X; return $N2;", "return update($N3).with($X).getOld();", "Return Old"));
    }
  };

  @Override public boolean canTip(final Block x) {
    return anyTips(tippers, x);
  }

  @Override public Tip pattern(final Block x) {
    return firstTip(tippers, x);
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
