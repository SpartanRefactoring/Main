package il.org.spartan.spartanizer.research.classifier.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class MaxEnhanced extends NanoPatternTipper<ForStatement> {
  private static final long serialVersionUID = -7684512700914547413L;
  final Collection<UserDefinedTipper<ForStatement>> tippers = new HashSet<UserDefinedTipper<ForStatement>>() {
    @SuppressWarnings("hiding") static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("for (int $N0 = 1; $N0 < $N1.$N2; ++$N0)  if ($N1[$N0] > $N3)   $N3 = $N1[$N0];", "max();", "max"));
    }
  };

  @Override public boolean canTip(final ForStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "ForEach: conevrt to fluent API";
  }

  @Override public Tip pattern(final ForStatement ¢) {
    return firstTip(tippers, ¢);
  }
}
