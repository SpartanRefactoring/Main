package il.org.spartan.spartanizer.research.patterns;

import java.util.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.leonidas.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Ori Marcovitch
 * @year 2016 */
public final class AssignmentLazyEvaluation extends NanoPatternTipper<Assignment> {
  private static final List<UserDefinedTipper<Assignment>> tippers = new ArrayList<>();

  public AssignmentLazyEvaluation() {
    if (tippers.size() == 4)
      return;
    tippers.add(TipperFactory.tipper("$X1 = $X1 != null ? $X1 : $X2", "lazyEvaluatedTo($X1,$X2)", "lazy evaluation"));
    tippers.add(TipperFactory.tipper("$X1 = $X1 == null ? $X2 : $X1", "lazyEvaluatedTo($X1,$X2)", "lazy evaluation"));
    tippers.add(TipperFactory.tipper("$X1 = null != $X1 ? $X1 : $X2", "lazyEvaluatedTo($X1,$X2)", "lazy evaluation"));
    tippers.add(TipperFactory.tipper("$X1 = null == $X1 ? $X2 : $X1", "lazyEvaluatedTo($X1,$X2)", "lazy evaluation"));
  }

  @Override public String description(@SuppressWarnings("unused") final Assignment __) {
    return "replace lazy evaluation with lazyEvaluatedTo($X1,$X2)";
  }

  @Override public boolean prerequisite(final Assignment x) {
    for (UserDefinedTipper<Assignment> ¢ : tippers)
      if (¢.canTip(x))
        return true;
    return false;
  }

  @Override public Tip tip(final Assignment x) throws TipperFailure {
    for (UserDefinedTipper<Assignment> ¢ : tippers)
      if (¢.canTip(x))
        return ¢.tip(x);
    assert false;
    return null;
  }
}
