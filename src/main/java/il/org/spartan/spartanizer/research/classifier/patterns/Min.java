package il.org.spartan.spartanizer.research.classifier.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Min examples
 * @author Ori Marcovitch
 * @since 2016 */
public class Min extends NanoPatternTipper<ForStatement> {
  private static final long serialVersionUID = -4493640368930874861L;
  final Collection<UserDefinedTipper<ForStatement>> tippers = as
      .list(patternTipper("for (int $N0 = 1; $N0 < $N1.$N2; ++$N0)  if ($N1[$N0] < $N3)   $N3 = $N1[$N0];", "min();", "min"));

  @Override public boolean canTip(final ForStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @NotNull @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "ForEach: conevrt to fluent API";
  }

  @Nullable @Override public Tip pattern(final ForStatement ¢) {
    return firstTip(tippers, ¢);
  }
}
