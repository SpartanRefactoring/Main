package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** @nano for(A a : B ) if(X) ++Y;
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-08 */
public class CountIf extends NanoPatternTipper<EnhancedForStatement> {
  private static final long serialVersionUID = 7795065631998358969L;
  private static final String description = "CountIf pattern: conevrt to fluent API";
  private static final List<UserDefinedTipper<EnhancedForStatement>> tippers = as.list(//
      patternTipper("for($T $N1 : $X1) if($X2) ++$N3;", "$N3 += ($X1).stream().filter($N1 -> $X2).count();", description),
      patternTipper("for($T $N1 : $X1) ++$N3;", "$N3 += ($X1).stream().count();", description));

  @Override public boolean canTip(final EnhancedForStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Nullable
  @Override public Tip pattern(final EnhancedForStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @NotNull
  @Override public String description() {
    return "Count elements in collection that satisfy some predicate";
  }

  @NotNull
  @Override public String technicalName() {
    return "CountEInCSatisfyingX";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }

  @NotNull
  @Override public String nanoName() {
    return Aggregate.class.getSimpleName();
  }
}
