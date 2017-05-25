package il.org.spartan.spartanizer.research.nanos;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** X == null ? Y : Z
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-01 */
public final class QuestionQuestion extends NanoPatternTipper<ConditionalExpression> {
  private static final long serialVersionUID = -0x1F274A91525D3383L;
  private static final NanoPatternContainer<ConditionalExpression> tippers = new NanoPatternContainer<ConditionalExpression>()
      .add("$X1 != null ? $X2 : $X3", "take($X2).default¢($X1).to($X3)", "takeDfaultTo pattern: Go fluent") //
      .add("$X1 == null ? $X3 : $X2", "take($X2).default¢($X1).to($X3)", "takeDfaultTo pattern: Go fluent") //
      .add("null != $X1 ? $X3 : $X2", "take($X2).default¢($X1).to($X3)", "takeDfaultTo pattern: Go fluent") //
      .add("null == $X1 ? $X3 : $X2", "take($X2).default¢($X1).to($X3)", "takeDfaultTo pattern: Go fluent") //
  ;
  static final DefaultsTo rival = new DefaultsTo();

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return tippers.canTip(¢)//
        && rival.cantTip(¢);
  }
  @Override public Tip pattern(final ConditionalExpression ¢) {
    return tippers.firstTip(¢);
  }
  @Override public String description() {
    return "Evaluate expression, if null take some expression, otherwise take another";
  }
}
