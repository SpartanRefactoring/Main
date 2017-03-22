package il.org.spartan.spartanizer.research.classifier.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Array initialization examples
 * @author Ori Marcovitch
 * @since 2016 */
public class InitArray extends NanoPatternTipper<ForStatement> {
  private static final long serialVersionUID = -6565220361424484292L;
  final Collection<UserDefinedTipper<ForStatement>> tippers = as
      .list(patternTipper("for (int $N0 = 0; $N0 < $N1; ++$N0)  $N2[$N0] = null;", "init();", "Init array: conevrt to fluent API"));

  @Override public boolean canTip(final ForStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @NotNull @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "Init array: conevrt to fluent API";
  }

  @Nullable @Override public Fragment pattern(@NotNull final ForStatement ¢) {
    return firstTip(tippers, ¢);
  }
}
