package il.org.spartan.spartanizer.research.classifier.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

import java.util.Collection;

import org.eclipse.jdt.core.dom.ForStatement;

import fluent.ly.as;
import il.org.spartan.spartanizer.research.UserDefinedTipper;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.tipping.Tip;

/** Min examples
 * @author Ori Marcovitch
 * @since 2016 */
public class Min extends NanoPatternTipper<ForStatement> {
  private static final long serialVersionUID = -0x3E5C9E1B727AE5EDL;
  final Collection<UserDefinedTipper<ForStatement>> tippers = as
      .list(patternTipper("for (int $N0 = 1; $N0 < $N1.$N2; ++$N0)  if ($N1[$N0] < $N3)   $N3 = $N1[$N0];", "min();", "min"));

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
