package il.org.spartan.spartanizer.research.classifier.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

import java.util.Collection;

import org.eclipse.jdt.core.dom.ForStatement;

import fluent.ly.as;
import il.org.spartan.spartanizer.research.UserDefinedTipper;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.tipping.Tip;

/** Contains examples
 * @author Ori Marcovitch
 * @since 2016 */
public class Classifieric {
  public static class Contains extends NanoPatternTipper<ForStatement> {
    private static final long serialVersionUID = 0x47018200A57C4822L;
    final Collection<UserDefinedTipper<ForStatement>> tippers = as.list(//
        patternTipper("for (int $N0 = $N1; $N0 < $N2; ++$N0)  if ($N3[$N0] == $N4)    return $N0;", "contains();", "contains"));

    @Override public boolean canTip(final ForStatement ¢) {
      return anyTips(tippers, ¢);
    }
    @Override public Tip pattern(final ForStatement ¢) {
      return firstTip(tippers, ¢);
    }
  }
}
