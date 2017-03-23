package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** @nano first element in collection, lisp style.
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-20 */
public final class First extends NanoPatternTipper<MethodInvocation> {
  private static final long serialVersionUID = 7012364337198315285L;
  private static final List<UserDefinedTipper<MethodInvocation>> tippers = as.list(patternTipper("$X.get(0)", "first($X)", "lisp: first"));

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
    return "First element in collection";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
