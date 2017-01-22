package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** @author Ori Marcovitch
 * @year 2016 */
public final class HoldsForAll extends NanoPatternTipper<EnhancedForStatement> {
  private static final List<UserDefinedTipper<Block>> tippers = new ArrayList<UserDefinedTipper<Block>>() {
    static final long serialVersionUID = 1L;
    {
      add(statementsPattern("for($T $N : $X1) if($X2) return false; return true;", "return $X1.stream().allMatch($N -> !($X2));",
          "All matches pattern. Consolidate into one statement"));
    }
  };

  @Override public boolean canTip(final EnhancedForStatement x) {
    return anyTips(tippers, az.block(parent(x)));
  }

  @Override public Tip pattern(final EnhancedForStatement $) {
    return firstTip(tippers, az.block(parent($)));
  }

  @Override public Category category() {
    return Category.Iterative;
  }

  @Override public String description() {
    return "Return whether all elements in collection match predicate";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
