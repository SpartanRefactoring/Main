package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** Replace X == null ? null : X.Y with X?.Y <br>
 * replace X != null ? X.Y : null with X?.Y <br>
 * replace null == X ? null : X.Y with X?.Y <br>
 * replace null != X ? X.Y : null with X?.Y <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class SafeReference extends NanoPatternTipper<ConditionalExpression> {
  static Set<UserDefinedTipper<ConditionalExpression>> tippers = new HashSet<UserDefinedTipper<ConditionalExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.tipper("$X1 == null ? null : $X1.$X2", "NullConditional($X1,$X2)", "null Conditional"));
      add(TipperFactory.tipper("$X1 != null ? $X1.$X2 : null", "NullConditional($X1,$X2)", "null Conditional"));
      add(TipperFactory.tipper("null == $X1 ? null : $X1.$X2", "NullConditional($X1,$X2)", "null Conditional"));
      add(TipperFactory.tipper("null != $X1 ? $X1.$X2 : null", "NullConditional($X1,$X2)", "null Conditional"));
    }
  };

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "replace null conditionl ternary with ?.";
  }
  @Override public boolean canTip(final ConditionalExpression x) {
    for (final UserDefinedTipper<ConditionalExpression> ¢ : tippers)
      if (¢.canTip(x))
        return true;
    return false;
  }
  @Override public Tip tip(final ConditionalExpression x) {
    Logger.logNP(x, "safeReference");
    for (final UserDefinedTipper<ConditionalExpression> ¢ : tippers)
      if (¢.canTip(x))
        return ¢.tip(x);
    assert false;
    return null;
  }
}
