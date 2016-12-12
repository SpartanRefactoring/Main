package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @since Dec 7, 2016 */
public class TernaryBitOperation extends NanoPatternTipper<ConditionalExpression> {
  private static Set<UserDefinedTipper<ConditionalExpression>> tippers = new HashSet<UserDefinedTipper<ConditionalExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("$X == 0 ? 0 : 1", "Bit.of($X)", ""));
      add(TipperFactory.patternTipper("0 == $X ? 0 : 1", "Bit.of($X)", ""));
      add(TipperFactory.patternTipper("$X == 0 ? 1 : 0", "Bit.not($X)", ""));
      add(TipperFactory.patternTipper("0 == $X ? 1 : 0", "Bit.not($X)", ""));
      add(TipperFactory.patternTipper("($X == 0) ? 0 : 1", "Bit.of($X)", ""));
      add(TipperFactory.patternTipper("(0 == $X) ? 0 : 1", "Bit.of($X)", ""));
      add(TipperFactory.patternTipper("($X == 0) ? 1 : 0", "Bit.not($X)", ""));
      add(TipperFactory.patternTipper("(0 == $X) ? 1 : 0", "Bit.not($X)", ""));
    }
  };

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final ConditionalExpression ¢) {
    return firstTipper(tippers, ¢).tip(¢);
  }

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return null;
  }
}
