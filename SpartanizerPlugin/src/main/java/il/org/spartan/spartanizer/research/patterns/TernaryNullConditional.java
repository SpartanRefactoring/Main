package il.org.spartan.spartanizer.research.patterns;

import java.util.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.leonidas.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace X == null ? null : X.Y with X?.Y <br>
 * replace X != null ? X.Y : null with X?.Y <br>
 * replace null == X ? null : X.Y with X?.Y <br>
 * replace null != X ? X.Y : null with X?.Y <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class TernaryNullConditional extends NanoPatternTipper<ConditionalExpression> implements TipperCategory.Nanos {
  private static final List<UserDefinedTipper<ConditionalExpression>> tippers = new ArrayList<>();

  public TernaryNullConditional() {
    if (tippers.size() == 4)
      return;
    tippers.add(TipperFactory.tipper("$X1 == null ? null : $X1.$X2", "NullConditional($X1,$X2)", "null Conditional"));
    tippers.add(TipperFactory.tipper("$X1 != null ? $X1.$X2 : null", "NullConditional($X1,$X2)", "null Conditional"));
    tippers.add(TipperFactory.tipper("null == $X1 ? null : $X1.$X2", "NullConditional($X1,$X2)", "null Conditional"));
    tippers.add(TipperFactory.tipper("null != $X1 ? $X1.$X2 : null", "NullConditional($X1,$X2)", "null Conditional"));
  }

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "replace null conditionl ternary with ?.";
  }

  @Override public boolean prerequisite(final ConditionalExpression x) {
    for (final UserDefinedTipper<ConditionalExpression> ¢ : tippers)
      if (¢.canTip(x))
        return true;
    return false;
  }

  @Override public Tip tip(final ConditionalExpression x) throws TipperFailure {
    for (final UserDefinedTipper<ConditionalExpression> ¢ : tippers)
      if (¢.canTip(x))
        return ¢.tip(x);
    return null;
  }
}
