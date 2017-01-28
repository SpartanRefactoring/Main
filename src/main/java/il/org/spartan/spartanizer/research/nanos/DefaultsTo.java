package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** This is the ?? operator
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public final class DefaultsTo extends NanoPatternTipper<ConditionalExpression> {
  private static final List<UserDefinedTipper<ConditionalExpression>> tippers = new ArrayList<UserDefinedTipper<ConditionalExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X1 != null ? $X1 : $X2", "default¢($X1).to($X2)", "dfault pattern: Go fluent"));
      add(patternTipper("$X1 == null ? $X2 : $X1", "default¢($X1).to($X2)", "dfault pattern: Go fluent"));
      add(patternTipper("null != $X1 ? $X1 : $X2", "default¢($X1).to($X2)", "dfault pattern: Go fluent"));
      add(patternTipper("null == $X1 ? $X2 : $X1", "default¢($X1).to($X2)", "dfault pattern: Go fluent"));
    }
  };

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final ConditionalExpression ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String description() {
    return "Evaluate expression, if null- replace with default value";
  }

  @Override public String technicalName() {
    return "IfX₁IsNullX₁ElseX₂";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }

  @Override public NanoPatternTipper.Category category() {
    return Category.Default;
  }
}
