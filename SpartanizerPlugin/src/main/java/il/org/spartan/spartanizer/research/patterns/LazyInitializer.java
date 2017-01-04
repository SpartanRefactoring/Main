package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** @author Ori Marcovitch
 * @year 2016 */
public final class LazyInitializer extends NanoPatternTipper<Assignment> {
  private static final Set<UserDefinedTipper<Assignment>> tippers = new HashSet<UserDefinedTipper<Assignment>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X1 = default¢($X1).to($X2)", "lazyInitialize($X1).with(()->$X2)", "lazy evaluation"));
    }
  };

  @Override public boolean canTip(final Assignment x) {
    return anyTips(tippers, x);
  }

  @Override public Tip pattern(final Assignment x) {
    return firstTip(tippers, x);
  }

  @Override public String description() {
    return "An variable initialization which executes only if the value is not yet initialized";
  }
}
