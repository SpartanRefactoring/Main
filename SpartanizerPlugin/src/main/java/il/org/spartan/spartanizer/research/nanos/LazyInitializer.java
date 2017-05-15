package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** *
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public final class LazyInitializer extends NanoPatternTipper<Assignment> {
  private static final long serialVersionUID = -0xC4806BEB7B62450L;
  private static final List<UserDefinedTipper<Assignment>> tippers = as
      .list(patternTipper("$X1 = defaults($X1).to($X2)", "lazyInitialize($X1).with(()->$X2)", "lazy evaluation"));

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
