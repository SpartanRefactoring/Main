package il.org.spartan.spartanizer.research.nanos;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** {@link HoldsForAll} Nano Pattern - holds P(c) for all c in C
 * @author Ori Marcovitch
 * @since Jan 22, 2017 */
public final class HoldsForAll extends NanoPatternTipper<EnhancedForStatement> {
  private static final BlockNanoPatternContainer tippers = new BlockNanoPatternContainer() {
    static final long serialVersionUID = 1L;
    {
      statementsPattern("for($T $N1 : $X1) if($X2) return false; return true;", //
          "return $X1.stream().allMatch($N1 -> !($X2));", "All matches pattern. Consolidate into one statement");
      statementsPattern("for($T $N1 : $X1) if($X2) $N2 = false;", //
          "$N2 = $X1.stream().allMatch($N1 -> !($X2));", "All matches pattern. Consolidate into one statement");
    }
  };

  @Override public boolean canTip(final EnhancedForStatement x) {
    return tippers.anyTips(az.block(parent(x)));
  }

  @Override public Tip pattern(final EnhancedForStatement $) {
    return tippers.firstTip(az.block(parent($)));
  }

  @Override public Category category() {
    return Category.Iterative;
  }

  @Override public String description() {
    return "Return whether all elements in collection match predicate";
  }

  @Override public String example() {
    return tippers.firstPattern();
  }

  @Override public String symbolycReplacement() {
    return tippers.firstReplacement();
  }
}
