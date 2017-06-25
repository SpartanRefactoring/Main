package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.tipping.*;

/** @nano last element in collection, lisp style.
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-20 */
public final class Last extends NanoPatternTipper<MethodInvocation> {
  private static final long serialVersionUID = -0x308CCFF0BC021EA5L;
  private static final List<UserDefinedTipper<MethodInvocation>> tippers = as.list(//
      patternTipper("$X.get($X.size()-1) ", "last($X)", "lisp: last"));

  @Override public boolean canTip(final MethodInvocation ¢) {
    return anyTips(tippers, ¢);
  }
  @Override public Tip pattern(final MethodInvocation ¢) {
    return firstTip(tippers, ¢);
  }
  @Override public Category category() {
    return Category.Iterative;
  }
  @Override public String description() {
    return "Last element in collection";
  }
  @Override public String example() {
    return firstPattern(tippers);
  }
  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
