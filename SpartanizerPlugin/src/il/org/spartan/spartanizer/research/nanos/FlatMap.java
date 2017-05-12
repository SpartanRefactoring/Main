package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Flatten a collection of collecions into one collection, optionally mapping
 * elements
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-12 */
public class FlatMap extends NanoPatternTipper<EnhancedForStatement> {
  private static final long serialVersionUID = 0x334EEEB7CB69EEEBL;
  private static final Collection<UserDefinedTipper<EnhancedForStatement>> tippers = as.list(
      patternTipper("for($T $N1 : $X1) $N2.addAll($N1);", "$N2.addAll(($X1).stream().flatMap($N1 -> $N1));",
          "FlatMap pattern: conevrt to fluent API"),
      patternTipper("for($T $N1 : $X1) $N2.addAll($X2);", "$N2.addAll(($X1).stream().flatMap($N1 -> $X2));",
          "FlatMap pattern: conevrt to fluent API"),
      patternTipper("for($T1 $N1 : $X1) for($T2 $N3 : $N1) $N2.add($N3);", "$N2.addAll(($X1).stream().flatMap($N1 -> $N1));",
          "FlatMap pattern: conevrt to fluent API"),
      patternTipper("for($T1 $N1 : $X1) for($T2 $N3 : $N1) $N2.add($N3);", "$N2.addAll(($X1).stream().flatMap($N1 -> $X2));",
          "FlatMap pattern: conevrt to fluent API"));

  @Override public boolean canTip(final EnhancedForStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final EnhancedForStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String description() {
    return "Flatten a collection of collecions into one collection, optionally mapping";
  }
}
