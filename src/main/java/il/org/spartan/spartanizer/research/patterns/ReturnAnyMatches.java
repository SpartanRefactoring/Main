package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-05 */
public final class ReturnAnyMatches extends NanoPatternTipper<Block> {
  private static final Set<UserDefinedTipper<Block>> tippers = new HashSet<UserDefinedTipper<Block>>() {
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

  @Override public String category() {
    return Category.Quantifier + "";
  }

  @Override public String description() {
    return "Return whether any elements in collection match predicate";
  }
}
