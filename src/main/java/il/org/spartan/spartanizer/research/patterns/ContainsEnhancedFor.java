package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class ContainsEnhancedFor extends NanoPatternTipper<EnhancedForStatement> {
  Set<UserDefinedTipper<EnhancedForStatement>> tippers = new HashSet<UserDefinedTipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("for (boolean $N0 : $X) if ($N0 == $N1) return true;", "Arrays.asList($X).contains($N1);",
          "Contains pattern: conevrt to fluent API"));
      add(TipperFactory.patternTipper("for (boolean $N0 : $X) if ($N0.equals($N1)) return true;", "Arrays.asList($X).contains($N1);",
          "Contains pattern: conevrt to fluent API"));
      add(TipperFactory.patternTipper("for (boolean $N0 : $X) if ($N1.equals($N0)) return true;", "Arrays.asList($X).contains($N1);",
          "Contains pattern: conevrt to fluent API"));
    }
  };

  @Override public boolean canTip(final EnhancedForStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public String description(@SuppressWarnings("unused") final EnhancedForStatement __) {
    return "Contains pattern: conevrt to fluent API";
  }

  @Override public Tip pattern(final EnhancedForStatement ¢) {
    return firstTip(tippers, ¢);
  }
}
