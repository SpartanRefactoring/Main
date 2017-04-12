package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** {@link HoldsForAny} Nano Pattern - holds P(c) for any c in C
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-05 */
public final class HoldsForAny extends NanoPatternTipper<EnhancedForStatement> {
  private static final long serialVersionUID = 0x3490821F8DFCDD67L;
  private static final String description = "Any matches pattern. Consolidate into one statement";
  private static final BlockNanoPatternContainer tippers = new BlockNanoPatternContainer()
      .statementsPattern("for($T $N1 : $X1) if($X2) return true; return false;", //
          "return $X1.stream().anyMatch($N1 -> $X2);", description)
      .statementsPattern("for($T $N1 : $X1) if($X2) $N2 = true;", //
          "$N2 = $X1.stream().anyMatch($N1 -> $X2);", description);
  private static final NanoPatternContainer<EnhancedForStatement> tippers2 = new NanoPatternContainer<EnhancedForStatement>()
      .add("for($T $N1 : $X1) if($X2) return true;", //
          "returnIf($X1.stream().anyMatch($N1 -> $X2));", "All matches pattern. Consolidate into one statement")
      .add("for($T $N1 : $X1) if($X2) $N2 = true;", //
          "$N2 = $X1.stream().anyMatch($N1 -> $X2);", "All matches pattern. Consolidate into one statement");

  @Override public boolean canTip(final EnhancedForStatement x) {
    return tippers.canTip(az.block(parent(x)))//
        || tippers2.canTip(x);
  }

  @Override public Tip pattern(final EnhancedForStatement x) {
    return !tippers.canTip(az.block(parent(x))) ? tippers2.firstTip(x) : tippers.firstTip(az.block(parent(x)));
  }

  @Override public String description() {
    return "Return whether any elements in collection match predicate";
  }

  @Override public String tipperName() {
    return Aggregate.class.getSimpleName();
  }
}
