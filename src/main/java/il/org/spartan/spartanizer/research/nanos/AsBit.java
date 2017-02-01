package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** x true ? 1 : 0 <br>
 * This is actually a casting from boolean to int
 * @author Ori Marcovitch
 * @since Dec 7, 2016 */
public class AsBit extends NanoPatternTipper<ConditionalExpression> {
  private static final List<UserDefinedTipper<ConditionalExpression>> tippers = new ArrayList<UserDefinedTipper<ConditionalExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X ? 1 : 0", "as.bit($X)", ""));
      add(patternTipper("$X ? 0 : 1", "as.bit(!($X))", ""));
    }
  };

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return anyTips(tippers, ¢);
  }

  @Nullable
  @Override public Tip pattern(final ConditionalExpression ¢) {
    return firstTipper(tippers, ¢).tip(¢);
  }

  @NotNull
  @Override public String description() {
    return "Casting a boolean into an int";
  }

  @NotNull
  @Override public String technicalName() {
    return "CastXFromBooleanToInt";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }

  @NotNull
  @Override public Category category() {
    return Category.Functional;
  }
}
