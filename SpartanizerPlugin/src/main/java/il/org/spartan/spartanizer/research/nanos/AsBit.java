package il.org.spartan.spartanizer.research.nanos;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** @author Ori Marcovitch
 * @since Dec 7, 2016 */
public class AsBit extends NanoPatternTipper<ConditionalExpression> {
  private static final Set<UserDefinedTipper<ConditionalExpression>> tippers = new HashSet<UserDefinedTipper<ConditionalExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X ? 1 : 0", "as.bit($X)", ""));
      add(patternTipper("$X ? 0 : 1", "as.bit(!($X))", ""));
    }
  };

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final ConditionalExpression ¢) {
    return firstTipper(tippers, ¢).tip(¢);
  }

  @Override public String description() {
    return "Casting a boolean into an int";
  }

  @Override public String technicalName() {
    return "CastXFromBooleanToInt";
  }
}
