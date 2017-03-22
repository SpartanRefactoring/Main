package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** @nano last element in collection, lisp style.
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-20 */
public final class Last extends NanoPatternTipper<MethodInvocation> {
  private static final long serialVersionUID = -3498399643413388965L;
  private static final List<UserDefinedTipper<MethodInvocation>> tippers = as.list(//
      patternTipper("$X.get($X.size()-1) ", "last($X)", "lisp: last"));

  @Override public boolean canTip(final MethodInvocation ¢) {
    return anyTips(tippers, ¢);
  }

  @Nullable @Override public Tip pattern(final MethodInvocation ¢) {
    return firstTip(tippers, ¢);
  }

  @NotNull @Override public Category category() {
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
