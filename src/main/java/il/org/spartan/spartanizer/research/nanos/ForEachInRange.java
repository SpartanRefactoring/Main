package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** For each element in range (a,b) apply some method
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-12 */
public class ForEachInRange extends NanoPatternTipper<ForStatement> {
  private static final List<UserDefinedTipper<ForStatement>> tippers = new ArrayList<UserDefinedTipper<ForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("for(int $N1 = $X1; $N1 < $X2; ++$N1) $X3;", "range.from($X1).to($X2).forEach($N1 -> $X3);", "Go fluent: ForEachInRange"));
    }
  };

  @Override public boolean canTip(final ForStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Nullable
  @Override public Tip pattern(final ForStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @NotNull
  @Override public String description() {
    return "Iterate through a range of indexes and apply a statement for each index";
  }

  @NotNull
  @Override public String technicalName() {
    return "ForEachFromL₁ToL₂ApplyS";
  }

  @NotNull
  @Override public Category category() {
    return Category.Iterative;
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
