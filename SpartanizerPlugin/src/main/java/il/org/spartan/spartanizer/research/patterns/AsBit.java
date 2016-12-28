package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** @author Ori Marcovitch
 * @since Dec 7, 2016 */
public class AsBit extends NanoPatternTipper<ConditionalExpression> {
  private static Set<UserDefinedTipper<ConditionalExpression>> tippers = new HashSet<UserDefinedTipper<ConditionalExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X == 0 ? 0 : 1", "as.bit($X)", ""));
      add(patternTipper("0 == $X ? 0 : 1", "as.bit($X)", ""));
      add(patternTipper("$X == 0 ? 1 : 0", "bit.not($X)", ""));
      add(patternTipper("0 == $X ? 1 : 0", "bit.not($X)", ""));
      add(patternTipper("($X == 0) ? 0 : 1", "as.bit($X)", ""));
      add(patternTipper("(0 == $X) ? 0 : 1", "as.bit($X)", ""));
      add(patternTipper("($X == 0) ? 1 : 0", "bit.not($X)", ""));
      add(patternTipper("(0 == $X) ? 1 : 0", "bit.not($X)", ""));
    }
  };

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final ConditionalExpression ¢) {
    return firstTipper(tippers, ¢).tip(¢);
  }
}
