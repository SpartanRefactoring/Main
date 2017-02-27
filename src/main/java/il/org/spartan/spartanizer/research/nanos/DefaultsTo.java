package il.org.spartan.spartanizer.research.nanos;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** This is the ?? operator
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public final class DefaultsTo extends NanoPatternTipper<ConditionalExpression> {
  private static final long serialVersionUID = -7580396559433880409L;
  private static final NanoPatternContainer<ConditionalExpression> tippers = new NanoPatternContainer<ConditionalExpression>() {
    @SuppressWarnings("hiding") static final long serialVersionUID = 1L;
    {
      patternTipper("$X1 != null ? $X1 : $X2", "default¢($X1).to($X2)", "dfault pattern: Go fluent");
      patternTipper("$X1 == null ? $X2 : $X1", "default¢($X1).to($X2)", "dfault pattern: Go fluent");
      patternTipper("null != $X1 ? $X1 : $X2", "default¢($X1).to($X2)", "dfault pattern: Go fluent");
      patternTipper("null == $X1 ? $X2 : $X1", "default¢($X1).to($X2)", "dfault pattern: Go fluent");
    }
  };

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return tippers.canTip(¢);
  }

  @Override public Tip pattern(final ConditionalExpression ¢) {
    return tippers.firstTip(¢);
  }

  @Override public String description() {
    return "Evaluate expression, if null- replace with default value";
  }

  @Override public String technicalName() {
    return "IfX₁IsNullX₁ElseX₂";
  }

  @Override public String example() {
    return tippers.firstPattern();
  }

  @Override public String symbolycReplacement() {
    return tippers.firstReplacement();
  }

  @Override public NanoPatternTipper.Category category() {
    return Category.Default;
  }
}
