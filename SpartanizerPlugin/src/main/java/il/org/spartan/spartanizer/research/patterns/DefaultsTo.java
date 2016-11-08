package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** Replace X != null ? X : Y with X ?? Y <br>
 * replace X == null ? Y : X with X ?? Y <br>
 * replace null == X ? Y : X with X ?? Y <br>
 * replace null != X ? X : Y with X ?? Y <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class DefaultsTo extends NanoPatternTipper<ConditionalExpression> {
  List<UserDefinedTipper<ConditionalExpression>> tippers = new ArrayList<UserDefinedTipper<ConditionalExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.tipper("$X1 != null ? $X1 : $X2", "default¢($X1).to($X2)", ""));
      add(TipperFactory.tipper("$X1 == null ? $X2 : $X1", "default¢($X1).to($X2)", ""));
      add(TipperFactory.tipper("null != $X1 ? $X1 : $X2", "default¢($X1).to($X2)", ""));
      add(TipperFactory.tipper("null == $X1 ? $X2 : $X1", "default¢($X1).to($X2)", ""));
    }
  };

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "defaulsTo pattern";
  }
  @Override public boolean canTip(final ConditionalExpression x) {
    for (final UserDefinedTipper<ConditionalExpression> ¢ : tippers)
      if (¢.canTip(x))
        return true;
    return false;
  }
  @Override public Tip tip(final ConditionalExpression x) {
    Logger.logNP(x, "defaultsTo");
    for (final UserDefinedTipper<ConditionalExpression> ¢ : tippers)
      if (¢.canTip(x))
        return ¢.tip(x);
    assert false;
    return null;
  }
}
