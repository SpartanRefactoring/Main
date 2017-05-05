package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** For each element in range (a,b) apply some method
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-12 */
public class ForEachInRange extends NanoPatternTipper<ForStatement> {
  private static final long serialVersionUID = 0x6F5F34CC30F1CACCL;
  private static final List<UserDefinedTipper<ForStatement>> tippers = as
      .list(patternTipper("for(int $N1 = $X1; $N1 < $X2; ++$N1) $X3;", "range.from($X1).to($X2).forEach($N1 -> $X3);", "Go fluent: ForEachInRange"));

  @Override public boolean canTip(final ForStatement ¢) {
    return anyTips(tippers, ¢);
  }
  @Override public Tip pattern(final ForStatement ¢) {
    return firstTip(tippers, ¢);
  }
  @Override public String description() {
    return "Iterate through a range of indexes and apply a statement for each index";
  }
  @Override public String tipperName() {
    return "ForFromTo";
  }
}
