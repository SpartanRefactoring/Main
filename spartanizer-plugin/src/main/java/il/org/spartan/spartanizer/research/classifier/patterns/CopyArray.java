package il.org.spartan.spartanizer.research.classifier.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

import java.util.Collection;

import org.eclipse.jdt.core.dom.ForStatement;

import fluent.ly.as;
import il.org.spartan.spartanizer.research.UserDefinedTipper;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.tipping.Tip;

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
