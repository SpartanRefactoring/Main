package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** @nano for(A a : B ) if(X) ++Y;
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-08 */
public class CountIf extends NanoPatternTipper<EnhancedForStatement> {
  private static final long serialVersionUID = 0x6C2DA2E46C1E35B9L;
  private static final String description = "CountIf pattern: conevrt to fluent API";
  private static final List<UserDefinedTipper<EnhancedForStatement>> tippers = as.list(//
      patternTipper("for($T $N1 : $X1) if($X2) ++$N3;", "$N3 += ($X1).stream().filter($N1 -> $X2).count();", description),
      patternTipper("for($T $N1 : $X1) ++$N3;", "$N3 += ($X1).stream().count();", description));

  @Override public boolean canTip(final EnhancedForStatement ¢) {
    return anyTips(tippers, ¢);
  }
  @Override public Tip pattern(final EnhancedForStatement ¢) {
    return firstTip(tippers, ¢);
  }
  @Override public String description() {
    return "Count elements in collection that satisfy some predicate";
  }
  @Override public String tipperName() {
    return Aggregate.class.getSimpleName();
  }
}
