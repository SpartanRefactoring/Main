package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @year 2016 */
public final class ReturnHoldsForAll extends NanoPatternTipper<Block> {
  private static final Set<UserDefinedTipper<Block>> tippers = new HashSet<UserDefinedTipper<Block>>() {
    static final long serialVersionUID = 1L;
    {
      add(statementsPattern("for($T $N : $X1) if($X2) return false; return true;", "return $X1.stream().allMatch($N -> !($X2));",
          "All matches pattern. Consolidate into one statement"));
    }
  };

  @Override public boolean canTip(final Block x) {
    return anyTips(tippers, x);
  }

  @Override public Tip pattern(final Block x) {
    return firstTip(tippers, x);
  }

  @Override public String category() {
    return Category.Return + "";
  }

  @Override public String description() {
    return "Return whether all elements in collection match predicate";
  }
}
