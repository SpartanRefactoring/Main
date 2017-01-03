package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** We would like to...<br>
 * Replace X == null ? null : X.Y with X?.Y <br>
 * replace X != null ? X.Y : null with X?.Y <br>
 * replace null == X ? null : X.Y with X?.Y <br>
 * replace null != X ? X.Y : null with X?.Y <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class SafeReference extends NanoPatternTipper<ConditionalExpression> {
  private static final Set<UserDefinedTipper<ConditionalExpression>> tippers = new HashSet<UserDefinedTipper<ConditionalExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$N == null ? null : $N.$N2", "safe($N).get(()->$N.$N2)", "safe reference"));
      add(patternTipper("$N != null ? $N.$N2 : null", "safe($N).get(()->$N.$N2)", "safe reference"));
      add(patternTipper("null == $N ? null : $N.$N2", "safe($N).get(()->$N.$N2)", "safe reference"));
      add(patternTipper("null != $N ? $N.$N2 : null", "safe($N).get(()->$N.$N2)", "safe reference"));
    }
  };

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final ConditionalExpression ¢) {
    return firstTip(tippers, ¢);
  }
}
