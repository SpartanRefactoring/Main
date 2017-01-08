package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** @author Ori Marcovitch
 * @since Dec 17, 2016 */
public class Select extends NanoPatternTipper<EnhancedForStatement> {
  private static final List<UserDefinedTipper<EnhancedForStatement>> tippers = new ArrayList<UserDefinedTipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("for($N1 $N2 : $X1) if($X2) $N3.add($N4);", //
          "$N3.addAll($X1.stream().filter($N2 -> $X2).collect(Collectors.toList()));", //
          "Go Fluent: filter pattern"));
      add(patternTipper("for($N1 $N2 : $X1) if($X2) $N3.add($X3);", //
          "$N3.addAll($X1.stream().filter($N2 -> $X2).map($N2 -> $X3).collect(Collectors.toList()));", //
          "Go Fluent: filter pattern"));
    }
  };

  @Override public boolean canTip(final EnhancedForStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final EnhancedForStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public Category category() {
    return Category.Collection;
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
