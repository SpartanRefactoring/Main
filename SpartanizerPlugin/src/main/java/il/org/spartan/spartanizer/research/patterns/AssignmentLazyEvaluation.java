package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @year 2016 */
public final class AssignmentLazyEvaluation extends NanoPatternTipper<Assignment> {
  static Set<UserDefinedTipper<Assignment>> tippers = new HashSet<UserDefinedTipper<Assignment>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("$X1 = $X1 != null ? $X1 : $X2", "lazyEvaluatedTo($X1,$X2)", "lazy evaluation"));
      add(TipperFactory.patternTipper("$X1 = $X1 == null ? $X2 : $X1", "lazyEvaluatedTo($X1,$X2)", "lazy evaluation"));
      add(TipperFactory.patternTipper("$X1 = null != $X1 ? $X1 : $X2", "lazyEvaluatedTo($X1,$X2)", "lazy evaluation"));
      add(TipperFactory.patternTipper("$X1 = null == $X1 ? $X2 : $X1", "lazyEvaluatedTo($X1,$X2)", "lazy evaluation"));
    }
  };

  @Override public String description(@SuppressWarnings("unused") final Assignment __) {
    return "replace lazy evaluation with lazyEvaluatedTo($X1,$X2)";
  }

  @Override public boolean canTip(final Assignment x) {
    for (final UserDefinedTipper<Assignment> ¢ : tippers)
      if (¢.canTip(x))
        return true;
    return false;
  }

  @Override public Tip tip(final Assignment x) {
    Logger.logNP(x, getClass().getSimpleName());
    for (final UserDefinedTipper<Assignment> ¢ : tippers)
      if (¢.canTip(x))
        return ¢.tip(x);
    assert false;
    return null;
  }
}
