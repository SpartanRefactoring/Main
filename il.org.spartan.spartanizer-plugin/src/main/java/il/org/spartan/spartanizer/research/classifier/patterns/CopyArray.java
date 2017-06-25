package il.org.spartan.spartanizer.research.classifier.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.tipping.*;

/** CopyArray examples
 * @author Ori Marcovitch
 * @since 2016 */
public class CopyArray extends NanoPatternTipper<ForStatement> {
  private static final long serialVersionUID = -0x584D6AC7302164A6L;
  private final Collection<UserDefinedTipper<ForStatement>> tippers = as.list(//
      patternTipper("for (int $N0 = 0; $N0 < $N1; ++$N0)  $N2[$N0] = $N3[$N0];", "copy();", "copy"));

  @Override public boolean canTip(final ForStatement ¢) {
    return anyTips(tippers, ¢);
  }
  @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "Init array: conevrt to fluent API";
  }
  @Override public Tip pattern(final ForStatement ¢) {
    return firstTip(tippers, ¢);
  }
}
