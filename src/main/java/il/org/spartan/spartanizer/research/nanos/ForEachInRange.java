package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** For each element in range (a,b) apply some method
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-12 */
public class ForEachInRange extends NanoPatternTipper<ForStatement> {
  private static final long serialVersionUID = 8025191112596638412L;
  private static final List<UserDefinedTipper<ForStatement>> tippers = new ArrayList<UserDefinedTipper<ForStatement>>() {
    @SuppressWarnings("hiding") static final long serialVersionUID = 1L;
    {
      add(patternTipper("for(int $N1 = $X1; $N1 < $X2; ++$N1) $X3;", "range.from($X1).to($X2).forEach($N1 -> $X3);", "Go fluent: ForEachInRange"));
    }
  };

  @Override public boolean check(final ForStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final ForStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String description() {
    return "Iterate through a range of indexes and apply a statement for each index";
  }

  @Override public String technicalName() {
    return "ForEachFromL₁ToL₂ApplyS";
  }

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
