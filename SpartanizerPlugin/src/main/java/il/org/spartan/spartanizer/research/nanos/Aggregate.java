package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.research.nanos.deprecated.*;

/** @author Ori Marcovitch
 * @year 2016 */
public final class Aggregate extends NanoPatternTipper<EnhancedForStatement> {
  private static final NanoPatternTipper<EnhancedForStatement> rival = new Select();
  private static final List<UserDefinedTipper<EnhancedForStatement>> tippers = new ArrayList<UserDefinedTipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("for($T $N1 : $N2) $N3 = $X;", //
          "$N3 = $N2.stream().reduce(($N1,$N3) -> $X).get();", "Go Fluent. Aggregate"));
      add(patternTipper("for($T $N1 : $N2) if($X) $N3 = $N1;", ///
          "$N3 = $N2.stream().reduce(($N1,$N3) -> $X ? $N1 : $N3).get();", "Go Fluent. Reduce"));
      add(patternTipper("for($T $N1 : $N2) $N3 += $X;", //
          "$N3 += $N2.stream().map($N1 -> $X).reduce((x,y) -> x + y).get();", "Go Fluent. Sum"));
      add(patternTipper("for($T $N1 : $X1) $N2 = $X2;", //
          "$N2 = ($X1).stream().reduce(($N1,$N2) -> $X2).get();", "Go Fluent. Aggregate"));
      add(patternTipper("for($T $N1 : $X1) if($X2) $N2 = $N1;", //
          "$N2 = ($X1).stream().reduce(($N1,$N2) -> $X2 ? $N1 : $N2).get();", "Go Fluent. Reduce"));
      add(patternTipper("for($T $N1 : $X1) $N2 += $X2;", //
          "$N2 += ($X1).stream().map($N1 -> $X2).reduce((x,y) -> x + y).get();", "Go Fluent. Sum"));
    }
  };

  @Override public boolean canTip(final EnhancedForStatement x) {
    return anyTips(tippers, x) && rival.cantTip(x);
  }

  @Override public Tip pattern(final EnhancedForStatement x) {
    return firstTip(tippers, x);
  }

  @Override public String description() {
    return "Aggregate a collection into one element";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
