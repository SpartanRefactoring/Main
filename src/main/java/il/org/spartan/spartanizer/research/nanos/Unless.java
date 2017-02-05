package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** X false ? Y : null
 * @author Ori Marcovitch
 * @since Dec 13, 2016 */
public final class Unless extends NanoPatternTipper<ConditionalExpression> {
  private static final List<UserDefinedTipper<ConditionalExpression>> tippers = new ArrayList<UserDefinedTipper<ConditionalExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X1 ? $D : $X2", "unless($X1).eval(() -> $X2).defaultTo($D)", "Go fluent: Unless pattern"));
      add(patternTipper("$X1  ? $X2 : $D", "unless(!$X1).eval(() -> $X2).defaultTo($D)", "Go fluent: Unless pattern"));
    }
  };
  private static final List<NanoPatternTipper<ConditionalExpression>> rivals = new ArrayList<NanoPatternTipper<ConditionalExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(new DefaultsTo());
      add(new SafeReference());
    }
  };

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return anyTips(tippers, ¢) && nonTips(rivals, ¢);
  }

  @Override @Nullable public Tip pattern(final ConditionalExpression ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String description() {
    return "Evaluate an expression unless some condition is satisfied";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }

  @Override @NotNull public Category category() {
    return Category.Safety;
  }
}
