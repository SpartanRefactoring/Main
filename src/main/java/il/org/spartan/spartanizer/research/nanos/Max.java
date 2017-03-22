package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Max between two expressions
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-12 */
public final class Max extends NanoPatternTipper<ConditionalExpression> {
  private static final long serialVersionUID = -5168213016271470822L;
  private static final Collection<UserDefinedTipper<ConditionalExpression>> tippers = as.list(
      patternTipper("$X1 > $X2 ? $X1 : $X2", "max($X1,$X2)", "max"), patternTipper("$X2 < $X1 ? $X1 : $X2", "max($X1,$X2)", "max"),
      patternTipper("$X1 >= $X2 ? $X1 : $X2", "max($X1,$X2)", "max"), patternTipper("$X2 <= $X1 ? $X1 : $X2", "max($X1,$X2)", "max"));

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return anyTips(tippers, ¢);
  }

  @Nullable @Override public Tip pattern(final ConditionalExpression ¢) {
    return firstTip(tippers, ¢);
  }

  @NotNull @Override public Category category() {
    return Category.Safety;
  }

  @Override public String description() {
    return "Max between two expressions";
  }
}
