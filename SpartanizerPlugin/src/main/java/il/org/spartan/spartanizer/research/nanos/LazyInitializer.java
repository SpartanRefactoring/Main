package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/**  * @year 2016 
 @author Ori Marcovitch
 * @since Jan 8, 2017
 */

public final class LazyInitializer extends NanoPatternTipper<Assignment> {
  private static final List<UserDefinedTipper<Assignment>> tippers = new ArrayList<UserDefinedTipper<Assignment>>() {
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

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}

