package il.org.spartan.spartanizer.research.classifier.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class Classifieric {
  public static class Contains extends NanoPatternTipper<ForStatement> {
    final Set<UserDefinedTipper<ForStatement>> tippers = new HashSet<UserDefinedTipper<ForStatement>>() {
      static final long serialVersionUID = 1L;
      {
        add(TipperFactory.patternTipper("for (int $N0 = $N1; $N0 < $N2; ++$N0)  if ($N3[$N0] == $N4)    return $N0;", "contains();", "contains"));
      }
    };

    @Override public boolean canTip(final ForStatement ¢) {
      return anyTips(tippers, ¢);
    }

    @Override public Tip pattern(final ForStatement ¢) {
      return firstTip(tippers, ¢);
    }
  }
}
