package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** {@link HoldsForAll} Nano Pattern - holds P(c) for all c in C
 * @author Ori Marcovitch
 * @since Jan 22, 2017 */
public final class HoldsForAll extends NanoPatternTipper<EnhancedForStatement> {
  private static final long serialVersionUID = -419909996243222517L;
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

  @Nullable @Override public Tip pattern(final EnhancedForStatement x) {
    return !tippers.canTip(az.block(parent(x))) ? tippers2.firstTip(x) : tippers.firstTip(az.block(parent(x)));
  }

  @NotNull @Override public Category category() {
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

  @Override public String nanoName() {
    return Aggregate.class.getSimpleName();
  }
}
