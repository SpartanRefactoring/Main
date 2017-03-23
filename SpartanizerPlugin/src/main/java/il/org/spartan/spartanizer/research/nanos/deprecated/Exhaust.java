package il.org.spartan.spartanizer.research.nanos.deprecated;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Exhaust an iterator but do nothing with elements
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-01 */
public class Exhaust extends NanoPatternTipper<WhileStatement> {
  private static final long serialVersionUID = -4404219080361481179L;
  private static final Collection<UserDefinedTipper<WhileStatement>> tippers = as.list( //
      patternTipper("while ($X) {}", "exhaust(()->$X);", "Exhaust pattern: conevrt to fluent API")) //
  ;

  @Override public boolean canTip(final WhileStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override @Nullable public Tip pattern(final WhileStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @Override @NotNull public Category category() {
    return Category.Iterative;
  }

  @Override @NotNull public String description() {
    return "Move an Iterable to its end using getNext() != null";
  }
}
