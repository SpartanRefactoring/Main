package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

/** @author Ori Marcovitch
 * @year 2016 */
public final class Aggregate extends NanoPatternTipper<EnhancedForStatement> {
  private static Set<UserDefinedTipper<EnhancedForStatement>> tippers = new HashSet<UserDefinedTipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("for($T $N1 : $X1) $N2 = $X2;", " $N2 = $X1.stream().reduce(($N1,$N2) -> $X2).get();", "Go Fluent. Aggregate"));
      add(patternTipper("for($T $N1 : $X1) if($X2) $N2 = $N1;", " $N2 = $X1.stream().reduce(($N1,$N2) -> $X2 ? $N1 : $N2).get();",
          "Go Fluent. Reduce"));
    }
  };

  @Override public boolean canTip(final EnhancedForStatement x) {
    return anyTips(tippers, x);
  }

  @Override public Tip pattern(final EnhancedForStatement x) {
    return firstTip(tippers, x);
  }

  @Override public String description(final EnhancedForStatement x) {
    return firstTipper(tippers, x).description(x);
  }
}
