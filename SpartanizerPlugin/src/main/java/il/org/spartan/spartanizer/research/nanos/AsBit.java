package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** x true ? 1 : 0 <br>
 * This is actually a casting from boolean to int
 * @author Ori Marcovitch
 * @since Dec 7, 2016 */
public class AsBit extends NanoPatternTipper<ConditionalExpression> {
  private static final long serialVersionUID = -0x531A0B9DA02923FAL;
  private static final List<UserDefinedTipper<ConditionalExpression>> tippers = as.list(//
      patternTipper("$X ? 1 : 0", "as.bit($X)", ""), //
      patternTipper("$X ? 0 : 1", "as.bit(!($X))", "")//
  );

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

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }

  @Override public Category category() {
    return Category.Functional;
  }
}
