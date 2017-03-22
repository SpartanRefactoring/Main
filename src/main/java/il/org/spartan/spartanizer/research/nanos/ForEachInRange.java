package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** For each element in range (a,b) apply some method
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-12 */
public class ForEachInRange extends NanoPatternTipper<ForStatement> {
  private static final long serialVersionUID = 8025191112596638412L;
  private static final List<UserDefinedTipper<ForStatement>> tippers = as
      .list(patternTipper("for(int $N1 = $X1; $N1 < $X2; ++$N1) $X3;", "range.from($X1).to($X2).forEach($N1 -> $X3);", "Go fluent: ForEachInRange"));

  @Override public boolean canTip(final ForStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Nullable @Override public Fragment pattern(final ForStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @NotNull @Override public String description() {
    return "Iterate through a range of indexes and apply a statement for each index";
  }

  @NotNull @Override public String nanoName() {
    return "ForFromTo";
  }
}
