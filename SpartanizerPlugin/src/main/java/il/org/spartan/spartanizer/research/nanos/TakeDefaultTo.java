package il.org.spartan.spartanizer.research.nanos;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** X == null ? Y : Z
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-01 */
public final class TakeDefaultTo extends NanoPatternTipper<ConditionalExpression> {
  private static final long serialVersionUID = -2244844927280034691L;
  private static final NanoPatternContainer<ConditionalExpression> tippers = new NanoPatternContainer<ConditionalExpression>()
      .add("$X1 != null ? $X2 : $X3", "take($X2).default¢($X1).to($X3)", "takeDfaultTo pattern: Go fluent") //
      .add("$X1 == null ? $X3 : $X2", "take($X2).default¢($X1).to($X3)", "takeDfaultTo pattern: Go fluent") //
      .add("null != $X1 ? $X3 : $X2", "take($X2).default¢($X1).to($X3)", "takeDfaultTo pattern: Go fluent") //
      .add("null == $X1 ? $X3 : $X2", "take($X2).default¢($X1).to($X3)", "takeDfaultTo pattern: Go fluent") //
  ;
  static final DefaultsTo rival = new DefaultsTo();

  @Override public boolean canTip(@NotNull final ConditionalExpression ¢) {
    return tippers.canTip(¢)//
        && rival.cantTip(¢);
  }

  @Nullable @Override public Fragment pattern(@NotNull final ConditionalExpression ¢) {
    return tippers.firstTip(¢);
  }

  @Override public String description() {
    return "Evaluate expression, if null take some expression, otherwise take another";
  }

  @Override public String nanoName() {
    return "QuestionQuestion";
  }
}
