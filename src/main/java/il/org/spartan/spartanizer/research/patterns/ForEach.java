package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** @author Ori Marcovitch
 * @since 2016 */
public class ForEach extends NanoPatternTipper<EnhancedForStatement> {
  private static final List<UserDefinedTipper<EnhancedForStatement>> tippers = new ArrayList<UserDefinedTipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("for($N1 $N2 : $N3) if($X1) $X2;", "$N3.stream().filter($N2 -> $X1).forEach($N2 -> $X2);",
          "ForEachThat pattern: conevrt to fluent API"));
      add(patternTipper("for($N1 $N2 : $N3) $X;", "$N3.stream().forEach($N2 -> $X);", "ForEach pattern: conevrt to fluent API"));
      add(patternTipper("for($N1 $N2 : $X1) if($X2) $X3;", "($X1).stream().filter($N2 -> $X2).forEach($N2 -> $X3);",
          "ForEachThat pattern: conevrt to fluent API"));
      add(patternTipper("for($N1 $N2 : $X1) $X2;", "($X1).stream().forEach($N2 -> $X2);", "ForEachThat pattern: conevrt to fluent API"));
    }
  };

  @Override public boolean canTip(final EnhancedForStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final EnhancedForStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String description() {
    return "Iterate a collection and apply a statement for each element";
  }
}
