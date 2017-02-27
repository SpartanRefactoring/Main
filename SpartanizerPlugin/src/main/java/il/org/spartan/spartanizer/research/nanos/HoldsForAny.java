package il.org.spartan.spartanizer.research.nanos;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** {@link HoldsForAny} Nano Pattern - holds P(c) for any c in C
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-05 */
public final class HoldsForAny extends NanoPatternTipper<EnhancedForStatement> {
  private static final long serialVersionUID = 3787670358656343399L;
  private static final String description = "Any matches pattern. Consolidate into one statement";
  private static final BlockNanoPatternContainer tippers = new BlockNanoPatternContainer() {
    @SuppressWarnings("hiding")
    static final long serialVersionUID = 1L;
    {
      statementsPattern("for($T $N1 : $X1) if($X2) return true; return false;", //
          "return $X1.stream().anyMatch($N1 -> $X2);", description);
      statementsPattern("for($T $N1 : $X1) if($X2) $N2 = true;", //
          "$N2 = $X1.stream().anyMatch($N1 -> $X2);", description);
    }
  };
  private static final NanoPatternContainer<EnhancedForStatement> tippers2 = new NanoPatternContainer<EnhancedForStatement>() {
    @SuppressWarnings("hiding")
    static final long serialVersionUID = 1L;
    {
      patternTipper("for($T $N1 : $X1) if($X2) return true;", //
          "returnIf($X1.stream().anyMatch($N1 -> $X2));", "All matches pattern. Consolidate into one statement");
      patternTipper("for($T $N1 : $X1) if($X2) $N2 = true;", //
          "$N2 = $X1.stream().anyMatch($N1 -> $X2);", "All matches pattern. Consolidate into one statement");
    }
  };

  @Override public boolean canTip(final EnhancedForStatement x) {
    return tippers.canTip(az.block(parent(x)))//
        || tippers2.canTip(x);
  }

  @Override public Tip pattern(final EnhancedForStatement x) {
    return !tippers.canTip(az.block(parent(x))) ? tippers2.firstTip(x) : tippers.firstTip(az.block(parent(x)));
  }

  @Override public Category category() {
    return Category.Iterative;
  }

  @Override public String description() {
    return "Return whether any elements in collection match predicate";
  }

  @Override public String example() {
    return tippers.firstPattern();
  }

  @Override public String symbolycReplacement() {
    return tippers.firstReplacement();
  }
}
