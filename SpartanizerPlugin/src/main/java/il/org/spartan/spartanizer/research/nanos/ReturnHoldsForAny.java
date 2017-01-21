package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-05 */
public final class ReturnHoldsForAny extends NanoPatternTipper<Block> {
  private static final List<UserDefinedTipper<Block>> tippers = new ArrayList<UserDefinedTipper<Block>>() {
    static final long serialVersionUID = 1L;
    {
      add(statementsPattern("for($T $N : $X1) if($X2) return true; return false;", "return $X1.stream().anyMatch($N -> $X2);",
          "Any matches pattern. Consolidate into one statement"));
    }
  };

  @Override public boolean canTip(final Block x) {
    return anyTips(tippers, x);
  }

  @Override public Tip pattern(final Block x) {
    return firstTip(tippers, x);
  }

  @Override public Category category() {
    return Category.Iterative;
  }

  @Override public String description() {
    return "Return whether any elements in collection match predicate";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
