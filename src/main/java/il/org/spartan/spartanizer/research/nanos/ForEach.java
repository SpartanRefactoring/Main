package il.org.spartan.spartanizer.research.nanos;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** @author Ori Marcovitch
 * @since 2016 */
public class ForEach extends NanoPatternTipper<EnhancedForStatement> {
  private static final List<UserDefinedTipper<EnhancedForStatement>> tippers = new ArrayList<UserDefinedTipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("for($N1 $N2 : $N3) $X;", "$N3.stream().forEach($N2 -> $X);", "ForEach pattern: conevrt to fluent API"));
      add(patternTipper("for($N1 $N2 : $X1) $X2;", "($X1).stream().forEach($N2 -> $X2);", "ForEachThat pattern: conevrt to fluent API"));
    }
  };
  protected static final List<NanoPatternTipper<EnhancedForStatement>> rivals = new ArrayList<NanoPatternTipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(new Select());
      add(new Aggregate());
    }
  };

  @Override public boolean canTip(final EnhancedForStatement ¢) {
    return anyTips(tippers, ¢) && nonTips(rivals, ¢);
  }

  @Override public Tip pattern(final EnhancedForStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String description() {
    return "Iterate a collection and apply a statement for each element";
  }

  @Override public String technicalName() {
    return "ForEachInCApplyS";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
