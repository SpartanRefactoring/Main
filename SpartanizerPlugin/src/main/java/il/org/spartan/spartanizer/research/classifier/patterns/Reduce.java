package il.org.spartan.spartanizer.research.classifier.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Class to catch some reduce occurrences - deprecated
 * @author Ori Marcovitch */
public class Reduce extends NanoPatternTipper<ForStatement> {
  private static final long serialVersionUID = 0x66AD90147390D909L;
  final Collection<UserDefinedTipper<ForStatement>> tippers = as.list(
      patternTipper("for (int $N0 = $N1; $N0 < $N2; ++$N0)  $N3 = 31 * $N3 + $N4.$N5($N6[$N0]);", "reduce();", "reduce"), //
      patternTipper("for (long $N0 = 0; $N0 < $N1; ++$N0) $N2 += $N3.$N4($N5).$N6();", "reduce();", "reduce"), //
      patternTipper("for (int $N0 = 0; $N0 < $N1; ++$N0)  $N2 += $N3.$N4($N5[($N0 & $N6)], $N7[($N0 & $N6)]);", "reduce();", "reduce"), //
      patternTipper("for (int $N0 = 0; $N0 < $N1; ++$N0)  $N2 += $N3.$N4($N5[($N0 & $N6)], $N7);", "reduce();", "reduce"), //
      patternTipper("for (int $N0=0; $N0 < $N1; ++$N0) for ($N2 $N3 : $N4) $N5+=$N3.$N6();", "reduce();", "reduce") //
  );

  @Override public boolean canTip(final ForStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "Reduce: conevrt to fluent API";
  }

  @Override public Tip pattern(final ForStatement ¢) {
    return firstTip(tippers, ¢);
  }
}
