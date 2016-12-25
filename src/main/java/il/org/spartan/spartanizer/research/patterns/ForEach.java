package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class ForEach extends NanoPatternTipper<EnhancedForStatement> {
  List<UserDefinedTipper<EnhancedForStatement>> tippers = new ArrayList<UserDefinedTipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("for($N1 $N2 : $X1 ? $X2 : $X3) if($X4) $X5;",
          "($X1 ? $X2 : $X3).stream().filter($N2 -> $X4).forEach($N2 -> $X5);", "ForEachThat pattern: conevrt to fluent API"));
      add(TipperFactory.patternTipper("for($N1 $N2 : $X1) if($X2) $X3;", "$X1.stream().filter($N2 -> $X2).forEach($N2 -> $X3);",
          "ForEachThat pattern: conevrt to fluent API"));
      add(TipperFactory.patternTipper("for($N1 $N2 : $X1) $X2;", "$X1.stream().forEach($N2 -> $X2);", "ForEach pattern: conevrt to fluent API"));
    }
  };

  @Override public boolean canTip(final EnhancedForStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public String description(@SuppressWarnings("unused") final EnhancedForStatement __) {
    return "ForEach pattern: conevrt to fluent API";
  }

  @Override public Tip pattern(final EnhancedForStatement ¢) {
    return firstTip(tippers, ¢);
  }
}
