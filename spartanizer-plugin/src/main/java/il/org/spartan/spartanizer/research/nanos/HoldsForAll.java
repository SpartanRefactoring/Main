package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.parent;

import org.eclipse.jdt.core.dom.EnhancedForStatement;

import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.research.nanos.common.BlockNanoPatternContainer;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternContainer;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.tipping.Tip;

/** {@link HoldsForAll} Nano Pattern - holds P(c) for all c in C
 * @author Ori Marcovitch
 * @since Jan 22, 2017 */
public final class HoldsForAll extends NanoPatternTipper<EnhancedForStatement> {
  private static final long serialVersionUID = -0x5D3D1EAB3B67FF5L;
  private static final BlockNanoPatternContainer tippers = new BlockNanoPatternContainer()
      .statementsPattern("for($T $N1 : $X1) if($X2) return false; return true;", "return $X1.stream().allMatch($N1 -> !($X2));",
          "All matches pattern. Consolidate into one statement")
      .statementsPattern("for($T $N1 : $X1) if($X2) $N2 = false;", "$N2 = $X1.stream().allMatch($N1 -> !($X2));",
          "All matches pattern.  Consolidate into one statement");
  private static final NanoPatternContainer<EnhancedForStatement> tippers2 = new NanoPatternContainer<EnhancedForStatement>()
      .add("for($T $N1 : $X1) if($X2) return false;", //
          "returnIf($X1.stream().allMatch($N1 -> !($X2)));", "All matches pattern. Consolidate into one statement")
      .add("for($T $N1 : $X1) if($X2) $N2 = false;", //
          "$N2 = $X1.stream().allMatch($N1 -> !($X2));", "All matches pattern. Consolidate into one statement");

  @Override public boolean canTip(final EnhancedForStatement x) {
    return tippers.canTip(az.block(parent(x)))//
        || tippers2.canTip(x);
  }
  @Override public Tip pattern(final EnhancedForStatement x) {
    return !tippers.canTip(az.block(parent(x))) ? tippers2.firstTip(x) : tippers.firstTip(az.block(parent(x)));
  }
  @Override public String description() {
    return "Return whether all elements in collection match predicate";
  }
  @Override public String tipperName() {
    return Aggregate.class.getSimpleName();
  }
}
